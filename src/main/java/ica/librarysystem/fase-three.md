# Nível 3 - 25 minutos

Contexto: A biblioteca cresce e agora precisa de um sistema de reservas - quando um livro não tem cópias disponíveis, o membro entra numa fila de espera.

## Requisitos:

- reserveBook(String isbn, String memberId) - se o livro tiver cópias disponíveis, faz o empréstimo direto (chama borrowBook). 
Se não tiver, adiciona o membro a uma fila de espera para aquele ISBN
- cancelReservation(String isbn, String memberId) - remove o membro da fila de espera daquele ISBN
- getWaitlist(String isbn) — retorna a fila de espera do livro, na ordem de chegada
- Quando returnBook(String isbn, String memberId) for chamado e houver fila de espera para aquele ISBN, o primeiro da fila deve ser automaticamente promovido para empréstimo

## Casos de borda a considerar:

- Membro tenta reservar um livro que já está emprestado para ele
- Membro tenta reservar o mesmo livro duas vezes (entrar na fila duas vezes)
- Membro tenta cancelar reserva de um livro que não reservou