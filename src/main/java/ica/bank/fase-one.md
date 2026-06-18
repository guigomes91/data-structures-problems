# Nível 1 - 20 minutos

Contexto: Você vai construir o núcleo de um sistema de processamento de transações instantâneas (TransactionProcessor), 
responsável por gerenciar saldos de contas e registrar transferências.

## Requisitos:
Crie a classe TransactionProcessor com os seguintes métodos:

- createAccount(String accountId, BigDecimal initialBalance) - cria uma conta com saldo inicial
- getBalance(String accountId) - retorna o saldo atual da conta
- transfer(String fromAccountId, String toAccountId, BigDecimal amount) - transfere valor entre contas

## Casos de borda a considerar:

- Transferência com valor maior que o saldo disponível
- Transferência envolvendo conta que não existe (origem ou destino)
- Transferência com valor zero ou negativo
- Transferência de uma conta para ela mesma

Dica de design: use BigDecimal para valores monetários, nunca double - é o padrão em sistemas financeiros e provavelmente vai ser 
cobrado se você usar double (problemas de arredondamento são um red flag clássico em entrevistas de fintech).