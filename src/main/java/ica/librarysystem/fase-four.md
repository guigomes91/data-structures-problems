# Nível 4 - 20 minutos
Contexto: A biblioteca agora precisa de relatórios e regras de negócio para gestão do acervo.

## Requisitos:

- getMostBorrowedBooks(int limit) - retorna os limit livros mais emprestados de todos os tempos (por quantidade de operações BORROW no histórico), 
ordenados do mais para o menos emprestado
- getMemberReport(String memberId) - retorna um relatório do membro contendo: total de empréstimos realizados, total de devoluções, 
livros atualmente em posse, posições em filas de espera
- addBook deve lançar exception se o ISBN já existir no catálogo — hoje ele sobrescreve silenciosamente

## Casos de borda:

- getMostBorrowedBooks(0) ou valor negativo - o que retornar?
- Membro sem nenhuma transação em getMemberReport
- limit maior que o total de livros existentes - retorna o que tiver

Esse é o último nível - foque em terminar getMostBorrowedBooks e getMemberReport primeiro, o addBook é o mais simples e pode fechar por último