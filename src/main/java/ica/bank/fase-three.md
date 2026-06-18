# Nível 3 - 25 minutos

Contexto: O Banco Central impõe limites diários de transação por conta (regra do PIX real). 
Cada conta tem um limite configurável, e o sistema precisa rastrear o total transferido no dia.

## Requisitos:

- setDailyLimit(String accountId, BigDecimal limit) - define o limite diário de transferências (saída) para uma conta. 
Toda conta criada sem limite explícito deve ter limite BigDecimal ilimitado (ou um valor padrão alto, sua escolha - documente)
- transfer(...) agora também deve validar o limite diário: se a soma das transferências já realizadas hoje + o valor 
atual excederem o limite, retornar TransactionReceipt com status FAILED
- getRemainingDailyLimit(String accountId) - retorna quanto ainda pode ser transferido no dia (limite - total já transferido hoje)
Considere que "hoje" significa transações com timestamp na mesma data de LocalDateTime.now() — transações de dias anteriores não contam para o limite

## Casos de borda a considerar:

- Conta sem limite configurado (usa o padrão)
- Transferência que sozinha já excede o limite (mesmo sem nenhuma transação anterior)
- Apenas transações COMPLETED devem contar para o limite — FAILED não consome limite
- setDailyLimit para conta inexistente

Dica de design: pense em onde armazenar o limite - é um atributo da conta (Account) ou um mapa separado (Map<String, BigDecimal>)? 
Como Account é um record imutável, mudar seu construtor vai impactar todo código que já cria Account.