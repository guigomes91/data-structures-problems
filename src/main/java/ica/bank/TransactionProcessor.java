package ica.bank;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

record Account(String accountId, BigDecimal balance) {}
record TransactionReceipt(String uuid, String fromAccountId, String toAccountId, BigDecimal amount,
                          TransactionStatus status, LocalDateTime timestamp, String descriptionFails) {}
record AccountSummary(BigDecimal saldoAtual, BigDecimal totalTransferido, BigDecimal totalRecebido,
                      int totalFaileds, BigDecimal limiteDiarioAtual, BigDecimal limiteDiarioRestante) {}

enum TransactionStatus {
    COMPLETED, FAILED, PENDING
}

public class TransactionProcessor {

    private static final BigDecimal INITIAL_LIMIT_ACCOUNT = new BigDecimal("15000.00");

    private Map<String, Account> accounts;
    private List<TransactionReceipt> transactionReceipts;
    private Map<String, BigDecimal> limitsAccount;

    public TransactionProcessor() {
        this.accounts = new HashMap<>();
        this.transactionReceipts = new ArrayList<>();
        this.limitsAccount = new HashMap<>();
    }

    public void createAccount(String accountId, BigDecimal initialBalance) {
        if (accounts.containsKey(accountId)) {
            throw new IllegalArgumentException("Conta " + accountId + " já criada");
        }

        limitsAccount.putIfAbsent(accountId, INITIAL_LIMIT_ACCOUNT);
        accounts.putIfAbsent(accountId, new Account(accountId, initialBalance));
    }

    public BigDecimal getBalance(String accountId) {
        if (!accounts.containsKey(accountId)) {
            throw new IllegalArgumentException("Conta não encontrada para consultar saldo!");
        }

        return accounts.get(accountId).balance();
    }

    public TransactionReceipt transfer(String fromAccountId, String toAccountId, BigDecimal amount) {
        final var transactionId = UUID.randomUUID().toString();

        var fromAccount = accounts.get(fromAccountId);
        var failReceipt = accountExists(fromAccount, transactionId, amount, fromAccountId, fromAccountId, toAccountId);
        if (failReceipt != null) return failReceipt;

        var toAccount = accounts.get(toAccountId);
        failReceipt = accountExists(toAccount, transactionId, amount, toAccountId, fromAccountId, toAccountId);
        if (failReceipt != null) return failReceipt;

        if (fromAccountId.equalsIgnoreCase(toAccountId)) {
            var transactionBalanceFails = buildTransactionReceipt(transactionId,
                    fromAccountId,
                    toAccountId,
                    amount,
                    TransactionStatus.FAILED,
                    LocalDateTime.now(),
                    "Transferencia para mesma conta não permitida! TransactionId: " + transactionId);
            this.transactionReceipts.add(transactionBalanceFails);
            return transactionBalanceFails;
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            var transactionBalanceFails = buildTransactionReceipt(transactionId,
                    fromAccountId,
                    toAccountId,
                    amount,
                    TransactionStatus.FAILED,
                    LocalDateTime.now(),
                    "Transferencia com valor zero ou negativo não permitido!! TransactionId: " + transactionId);
            this.transactionReceipts.add(transactionBalanceFails);
            return transactionBalanceFails;
        }

        if (amount.compareTo(fromAccount.balance()) > 0) {
            var transactionBalanceFails = buildTransactionReceipt(transactionId,
                    fromAccountId,
                    toAccountId,
                    amount,
                    TransactionStatus.FAILED,
                    LocalDateTime.now(),
                    "Saldo não disponível para transferencia! TransactionId: " + transactionId);
            this.transactionReceipts.add(transactionBalanceFails);
            return transactionBalanceFails;
        }

        var valueFromAccount = fromAccount.balance().subtract(amount);
        var valueToAccount = toAccount.balance().add(amount);

        var today = LocalDate.now();
        var totalTransferDaily = this.getSumOfDayTransfers(fromAccountId, today);

        var accountLimit = this.limitsAccount.get(fromAccountId);
        if (totalTransferDaily.add(amount).compareTo(accountLimit) > 0) {
            var transactionFails = buildTransactionReceipt(transactionId,
                    fromAccountId,
                    toAccountId,
                    amount,
                    TransactionStatus.FAILED,
                    LocalDateTime.now(),
                    "Sem limite de transferencia para o dia! transactionId: " + transactionId);
            this.transactionReceipts.add(transactionFails);
            return transactionFails;
        }

        accounts.replace(fromAccountId, new Account(fromAccountId, valueFromAccount));
        accounts.replace(toAccountId, new Account(toAccountId, valueToAccount));

        var transactionSucess = buildTransactionReceipt(transactionId,
                fromAccountId,
                toAccountId,
                amount,
                TransactionStatus.COMPLETED,
                LocalDateTime.now(),
                "pix realizado com sucesso para transactionId: " + transactionId);

        this.transactionReceipts.add(transactionSucess);
        return transactionSucess;
    }

    public List<TransactionReceipt> getTransactionHistory(String accountId) {
        return this.transactionReceipts
                .stream()
                .filter(transaction -> transaction.fromAccountId().equalsIgnoreCase(accountId) ||
                        transaction.toAccountId().equalsIgnoreCase(accountId))
                .sorted(Comparator.comparing(TransactionReceipt::timestamp))
                .toList();
    }

    public void setDailyLimit(String accountId, BigDecimal limit) {
        final var account = this.accounts.get(accountId);
        isAccountInvalid(account);

        this.limitsAccount.put(accountId, limit);
    }

    private static void isAccountInvalid(Account account) {
        if (Objects.isNull(account)) {
            throw new IllegalArgumentException("Conta invalida!");
        }
    }

    public BigDecimal getRemainingDailyLimit(String accountId) {
        final var account = this.accounts.get(accountId);
        isAccountInvalid(account);

        final var accountLimit = this.limitsAccount.get(accountId);
        var today = LocalDate.now();
        return accountLimit.subtract(getSumOfDayTransfers(accountId, today));
    }

    public AccountSummary getAccountSummary(String accountId) {
        var account = this.accounts.get(accountId);
        isAccountInvalid(account);

        var saldoAtual = account.balance();

        var totalTransferido = this.transactionReceipts
                .stream()
                .filter(transactionReceipt -> transactionReceipt.fromAccountId().equalsIgnoreCase(accountId) &&
                        transactionReceipt.status().equals(TransactionStatus.COMPLETED))
                .map(TransactionReceipt::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        var totalRecebido = this.transactionReceipts
                .stream()
                .filter(transactionReceipt -> transactionReceipt.toAccountId().equalsIgnoreCase(accountId) &&
                        transactionReceipt.status().equals(TransactionStatus.COMPLETED))
                .map(TransactionReceipt::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        var totalFails = this.transactionReceipts
                .stream()
                .filter(transactionReceipt -> transactionReceipt.fromAccountId().equalsIgnoreCase(accountId) &&
                        transactionReceipt.status().equals(TransactionStatus.FAILED))
                .count();

        var limiteDiarioAtual = this.limitsAccount.get(accountId);
        var limiteDiarioRestante = this.getRemainingDailyLimit(accountId);

        return new AccountSummary(saldoAtual, totalTransferido, totalRecebido, (int) totalFails, limiteDiarioAtual, limiteDiarioRestante);
    }

    public Map<String, BigDecimal> getTopSenders(int limit) {
        if (limit <= 0) {
            return Map.of();
        }

        return this.transactionReceipts.stream()
                        .filter(transactionReceipt ->
                                transactionReceipt.status().equals(TransactionStatus.COMPLETED))
                        .collect(Collectors.groupingBy(
                                TransactionReceipt::fromAccountId,
                                Collectors.reducing(
                                        BigDecimal.ZERO,
                                        TransactionReceipt::amount,
                                        BigDecimal::add
                                )
                        ))
                        .entrySet()
                        .stream()
                        .sorted(Map.Entry.<String, BigDecimal>comparingByValue(
                                Comparator.reverseOrder()))
                        .limit(limit)
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (a, b) -> a,
                                LinkedHashMap::new
                        ));
    }

    private BigDecimal getSumOfDayTransfers(String accountId, LocalDate today) {
        return this.transactionReceipts
                .stream()
                .filter(transactionReceipt -> transactionReceipt.fromAccountId().equalsIgnoreCase(accountId) &&
                        transactionReceipt.status().equals(TransactionStatus.COMPLETED) &&
                        transactionReceipt.timestamp().toLocalDate().equals(today))
                .map(TransactionReceipt::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<String> detectSuspiciousAccounts() {
        List<String> suspiciousAccounts = new ArrayList<>();
        AtomicInteger totalFailed = new AtomicInteger();
        Map<String, Integer> totalFailedMap = new HashMap<>();

        this.transactionReceipts.forEach(transactionReceipt -> {
            if (transactionReceipt.status().equals(TransactionStatus.FAILED)) {
                totalFailedMap.putIfAbsent(transactionReceipt.fromAccountId(), 1);

                if (totalFailedMap.get(transactionReceipt.fromAccountId()) != null) {
                    totalFailed.incrementAndGet();
                }

                if (totalFailed.get() >= 3) {
                    suspiciousAccounts.add(transactionReceipt.fromAccountId());
                }
            }

            var limiteDiario = this.limitsAccount.get(transactionReceipt.fromAccountId());
            if (transactionReceipt.status().equals(TransactionStatus.COMPLETED) &&
                    transactionReceipt.amount().compareTo(limiteDiario.multiply(new BigDecimal("0.80"))) > 0) {
                suspiciousAccounts.add(transactionReceipt.fromAccountId());
            }
        });

        return suspiciousAccounts;
    }

    private TransactionReceipt buildTransactionReceipt(String uuid, String fromAccountId, String toAccountId, BigDecimal amount,
                                                       TransactionStatus status, LocalDateTime timestamp, String description) {
        return new TransactionReceipt(
                uuid, fromAccountId, toAccountId, amount,
                status, timestamp, description
        );
    }

    private TransactionReceipt accountExists(
            Account account, String transactionId, BigDecimal amount, String accountInformed, String fromAccountId, String toAccountId
    ) {
        if (Objects.isNull(account)) {
            var transactionReceipt = buildTransactionReceipt(transactionId,
                    fromAccountId,
                    toAccountId,
                    amount,
                    TransactionStatus.FAILED,
                    LocalDateTime.now(),
                    "Conta " + accountInformed + " informada não existe para a operação! transactionId: " + transactionId);

            this.transactionReceipts.add(transactionReceipt);
            return transactionReceipt;
        }

        return null;
    }
}