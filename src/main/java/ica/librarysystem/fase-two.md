## Nível 2 - 20-25 minutos
Contexto: Agora a biblioteca precisa rastrear quem pegou cada livro emprestado e manter um histórico de transações.

# Requisitos do Nível 2:

- borrowBook(String isbn, String memberId) - agora recebe também o ID do membro. Registra o empréstimo associando o livro ao membro
- returnBook(String isbn, String memberId) - registra a devolução, removendo a associação
- getBorrowedBooks(String memberId) - retorna a lista de ISBNs atualmente emprestados para esse membro
- getTransactionHistory(String isbn) - retorna o histórico de transações (BORROW/RETURN) daquele livro, em ordem cronológica

# Casos de borda a considerar:

- O que acontece se um membro tentar devolver um livro que ele não pegou emprestado?
- O que acontece se o mesmo membro tentar pegar o mesmo livro emprestado duas vezes (sem devolver)?

Pode começar - você vai precisar mudar a assinatura dos métodos do Nível 1 (borrowBook/returnBook), então pense em como isso afeta quem já chama esses métodos.