# Nível 2 - 20-25 minutos

Contexto: O Banco Central exige que toda transação PIX seja registrada com um comprovante (receipt) e um status - algumas transações podem falhar 
por motivos operacionais (não apenas saldo insuficiente) e precisam ser rastreáveis.

## Requisitos:

- Crie um enum TransactionStatus com pelo menos: COMPLETED, FAILED, PENDING
- Crie um record TransactionReceipt contendo: id da transação (gerado automaticamente, pode ser UUID), fromAccountId, toAccountId, amount, status, timestamp, e 
uma descrição/motivo (para casos de falha)
- transfer(...) agora deve retornar um TransactionReceipt em vez de void
- Em caso de falha de validação (saldo insuficiente, conta inexistente, etc.), em vez de lançar exception, retorne um TransactionReceipt com status FAILED e o 
motivo da falha - a transação não deve ser executada, mas deve ser registrada no histórico
- getTransactionHistory(String accountId) - retorna todos os TransactionReceipt (sucesso ou falha) onde a conta participou como origem ou destino, em ordem cronológica

## Casos de borda a considerar:

Como você decide, para cada validação do Nível 1, se ela deve continuar lançando exception (erro de uso da API) ou retornar FAILED (regra de negócio)? 
Pense nisso como uma decisão de design - não existe resposta única, mas você precisa justificar

Dica: essa é uma mudança de paradigma importante - você está migrando de "exception como controle de fluxo de erro de negócio" para "resultado como objeto de retorno". 
Isso é muito comum em sistemas financeiros reais (idempotência, auditoria, conciliação).