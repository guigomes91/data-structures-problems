# Nível 4 - 20 minutos

Contexto: O time de risco do Nubank precisa de relatórios analíticos sobre o comportamento de transações para detectar padrões suspeitos e consolidar dados financeiros.

## Requisitos:

- getAccountSummary(String accountId) - retorna um record AccountSummary contendo:

saldo atual
total transferido para fora (soma de todas as transferências COMPLETED como origem, todos os dias)
total recebido (soma de todas as transferências COMPLETED como destino, todos os dias)
quantidade de transações FAILED
limite diário atual
limite diário restante hoje


- getTopSenders(int limit) - retorna as limit contas que mais transferiram em valor total (COMPLETED), do maior para o menor, como Map<String, BigDecimal>
- detectSuspiciousAccounts() - retorna lista de accountId que atendem a pelo menos um dos critérios:
 mais de 3 transações FAILED no dia de hoje
 transferência única COMPLETED acima de 80% do limite diário da conta
 saldo atual negativo (pode ocorrer via bug ou ajuste manual)

## Casos de borda:

- getTopSenders(0) ou negativo - retorna lista vazia
- Conta sem nenhuma transação - getAccountSummary deve funcionar com zeros
- detectSuspiciousAccounts sem nenhuma conta suspeita - retorna lista vazia

Foque em getAccountSummary e getTopSenders primeiro - detectSuspiciousAccounts é o mais complexo. 