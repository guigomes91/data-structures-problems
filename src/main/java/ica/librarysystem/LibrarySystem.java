package ica;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

record Book(String isbn, String title, int totalCopies, int available) {}

record History(String isbn, String memberId, LocalDateTime now, String operation) {}

record QueueBook(String isbn, String memberId, LocalDateTime reserveDate) {}

record ReportMember(int borrowCount, int returnCount, int borrowAtualCount, int positionQueue) {}

enum Operation {
    BORROW,
    RETURNED
}

public class LibrarySystem {
    public static final int UNAVAILABLE = 0;
    private final Map<String, Book> catalogs;
    private final Map<String, List<History>> transactions;
    private final List<History> histories;
    private final Map<String, String> memberBooks;
    private final List<QueueBook> queueBookList;

    public LibrarySystem() {
        this.catalogs = new HashMap<>();
        this.histories = new ArrayList<>();
        this.memberBooks = new HashMap<>();
        this.transactions = new HashMap<>();
        this.queueBookList = new ArrayList<>();
    }

    public void addBook(String isbn, String title, int totalCopies) {
        final var book = factoryBook(isbn, title, totalCopies, totalCopies);

        catalogs.put(isbn, book);
    }

    public void borrowBook(String isbn) {
        var book = catalogs.get(isbn);

        verifyBookExists(book);

        if (book.available() <= 0) {
            throw new IllegalArgumentException("Copies unavailable for ISBN: " + isbn);
        }

        int available = book.available() - 1;
        final var borrowBook = factoryBook(isbn, book.title(), book.totalCopies(), available);
        catalogs.replace(isbn, borrowBook);
    }

    public void borrowBook(String isbn, String memberId) {
        memberIsNull(memberId);

        var isbnBorrowed = memberBooks.get(isbn);

        if (isbnBorrowed != null && isbnBorrowed.equalsIgnoreCase(memberId)) {
            throw new IllegalArgumentException("ISBN already borrowed to memberId " + memberId);
        }

        this.borrowBook(isbn);
        this.histories.add(new History(isbn, memberId, LocalDateTime.now(), Operation.BORROW.name()));
        this.memberBooks.put(isbn, memberId);
        transactions.put(memberId, this.histories);
    }

    public void returnBook(String isbn) {
        var book = catalogs.get(isbn);

        verifyBookExists(book);
        int totalReturn = book.available() + 1;

        if (totalReturn > book.totalCopies()) {
            throw new IllegalArgumentException("Return copies is greater than total copies available!");
        }

        var returnedBook = factoryBook(isbn, book.title(), book.totalCopies(), totalReturn);
        catalogs.replace(isbn, returnedBook);
    }

    public void returnBook(String isbn, String memberId) {
        memberIsNull(memberId);

        final var memberIdReturned = memberBooks.get(isbn);
        if (Objects.isNull(memberIdReturned)) {
            throw new IllegalArgumentException("Return not allow to memberId : " + memberId);
        }

        this.returnBook(isbn);
        this.memberBooks.remove(isbn);
        histories.add(new History(isbn, memberId, LocalDateTime.now(), Operation.RETURNED.name()));

        var book = queueBookList
                .stream()
                .filter(queueBook -> queueBook.isbn().equalsIgnoreCase(isbn))
                .min(Comparator.comparing(QueueBook::reserveDate));

        book.ifPresent(queueBook -> {
            this.borrowBook(queueBook.isbn(), queueBook.memberId());
            this.queueBookList.remove(queueBook);
        });
    }

    public List<String> getBorrowedBooks(String memberId) {
        return memberBooks.entrySet().stream()
                .filter(member -> member.getValue().equals(memberId))
                .map(Map.Entry::getKey)
                .toList();
    }

    public List<History> getTransactionHistory(String isbn) {
        return histories.stream()
                .filter(history -> history.isbn().equalsIgnoreCase(isbn))
                .toList();
    }

    public void reserveBook(String isbn, String memberId) {
        var book = catalogs.get(isbn);

        if (book.available() == UNAVAILABLE) {
            queueBookList.add(new QueueBook(isbn, memberId, LocalDateTime.now()));
        } else {
            this.borrowBook(isbn, memberId);
        }
    }

    public void cancelReservation(String isbn, String memberId) {
        queueBookList.removeIf(queueBook -> queueBook.memberId().equalsIgnoreCase(memberId) && queueBook.isbn().equalsIgnoreCase(isbn));
    }

    public List<QueueBook> getWaitlist(String isbn) {
        return queueBookList.stream()
                .filter(queueBook -> queueBook.isbn().equalsIgnoreCase(isbn))
                .sorted(Comparator.comparing(QueueBook::reserveDate))
                .toList();
    }

    private static void memberIsNull(String memberId) {
        if (Objects.isNull(memberId) || memberId.isBlank()) {
            throw new IllegalArgumentException("MemberId must not be null");
        }
    }

    public int getAvailableCopies(String isbn) {
        var book = catalogs.get(isbn);

        verifyBookExists(book);

        return book.available();
    }

    public Map<String, Long> getMostBorrowedBooks(int limit) {
        if (limit <= 0) {
            return Map.of();
        }

        return histories
                .stream()
                .filter(history -> history.operation().equalsIgnoreCase(Operation.BORROW.name()))
                .collect(Collectors.groupingBy(History::isbn, Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(limit)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }

    public ReportMember getMemberReport(String memberId) {
        memberIsNull(memberId);

        final var borrowTotal = getBorrowTotal(Operation.BORROW, memberId);
        final var returnedTotal = getBorrowTotal(Operation.RETURNED, memberId);
        final var borrowAtualCount = getBorrowAtualCount(memberId);

        final var positionQueue = getQueuePosition(memberId);

        return new ReportMember(borrowTotal, returnedTotal, borrowAtualCount, positionQueue);
    }

    public int getQueuePosition(String memberId) {
        for (int i = 0; i < queueBookList.size(); i++) {
            if (queueBookList.get(i).memberId().equals(memberId)) {
                return i + 1;
            }
        }
        return 0;
    }

    private int getBorrowAtualCount(String memberId) {
        var transactionActually = transactions.get(memberId);
        var transactionCount = transactionActually
                .stream()
                .filter(history -> history.operation().equalsIgnoreCase(Operation.BORROW.name()))
                .collect(Collectors.groupingBy(History::memberId, Collectors.counting()));

        return Math.toIntExact(transactionCount.getOrDefault(memberId, 0L));
    }

    private int getBorrowTotal(Operation operation, String memberId) {
        return (int) histories.stream()
                .filter(h -> h.operation().equalsIgnoreCase(operation.name()) &&
                        h.memberId().equalsIgnoreCase(memberId))
                .count();
    }

    private static Book factoryBook(String isbn, String book, int totalCopies, int available) {
        return new Book(isbn, book, totalCopies, available);
    }

    private static void verifyBookExists(Book book) {
        if (Objects.isNull(book)) {
            throw new IllegalArgumentException("ISBN not exist in Catalog!");
        }
    }
}