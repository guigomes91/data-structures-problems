## Nível 1 — 20 minutos

Contexto: Você vai construir um sistema de gerenciamento de reservas de uma biblioteca (LibrarySystem).

# Requisitos do Nível 1:
Crie a classe LibrarySystem com os seguintes métodos:

- addBook(String isbn, String title, int totalCopies) - adiciona um livro ao catálogo com um número total de cópias disponíveis
- borrowBook(String isbn) - registra o empréstimo de uma cópia do livro. Deve reduzir as cópias disponíveis em 1
- returnBook(String isbn) - registra a devolução de uma cópia. Deve aumentar as cópias disponíveis em 1
- getAvailableCopies(String isbn) - retorna o número de cópias disponíveis

# Casos de borda a considerar:

O que acontece se tentarem pegar um livro emprestado que não existe no catálogo?
O que acontece se tentarem pegar um livro emprestado quando não há cópias disponíveis?
O que acontece se tentarem devolver mais cópias do que o total existente?