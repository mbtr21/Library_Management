import java.util.*;

enum Status {
    BANNED(1), BORROWED(2), EXIT(3);

    private final int code;

    Status(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return this.name() + "(" + code + ")";
    }
}

class Book {
    private String author, title;
    private int yearOfPublish;
    private Status status;

    public Book(String author, String title, int yearOfPublish, Status status) {
        this.author = author;
        this.title = title;
        this.yearOfPublish = yearOfPublish;
        this.status = status;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public int getYearOfPublish() {
        return yearOfPublish;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Book{author='" + author + "', title='" + title + "', year=" + yearOfPublish + ", status=" + status + '}';
    }
}

class Library {
    private List<Book> books = new ArrayList<>();

    public void addBook(Book book) {
        books.add(book);
    }

    public void displayBooks() {
        if (books.isEmpty()) {
            System.out.println("The library has no books.");
        } else {
            books.forEach(book -> System.out.println(book));
        }
    }

    public Book searchBookByTitle(String title) {
        return books.stream().filter(book -> book.getTitle().equalsIgnoreCase(title)).findFirst().orElse(null);
    }

    public List<Book> getBooksByAuthor(String author) {
        return books.stream().filter(book -> book.getAuthor().equalsIgnoreCase(author)).toList();
    }

    public boolean deleteBook(String title) {
        return books.removeIf(book -> book.getTitle().equalsIgnoreCase(title));
    }

    public void sortBooksByYear() {
        books.sort(Comparator.comparingInt(Book::getYearOfPublish));
    }
}

class LibraryApp {
    public static void main(String[] args) {
        Library library = new Library();

        // Adding books
        library.addBook(new Book("J.K. Rowling", "Harry Potter", 1997, Status.BORROWED));
        library.addBook(new Book("George Orwell", "1984", 1949, Status.BANNED));
        library.addBook(new Book("J.R.R. Tolkien", "The Hobbit", 1937, Status.EXIT));
        library.addBook(new Book("J.R.R. Tolkien", "The Lord of the Rings", 1954, Status.BORROWED));
        library.addBook(new Book("F. Scott Fitzgerald", "The Great Gatsby", 1925, Status.EXIT));
        library.addBook(new Book("George Orwell", "Animal Farm", 1945, Status.EXIT));

        // Displaying all books
        System.out.println("Books in the library:");
        library.sortBooksByYear();
        library.displayBooks();

        // Searching for a book by title
        System.out.println("\nSearching for '1984':");
        Book foundBook = library.searchBookByTitle("1984");
        System.out.println(foundBook != null ? foundBook : "Book not found.");

        // Getting books by J.R.R. Tolkien
        System.out.println("\nBooks by 'J.R.R. Tolkien':");
        List<Book> tolkienBooks = library.getBooksByAuthor("J.R.R. Tolkien");
        if (!tolkienBooks.isEmpty()) {
            tolkienBooks.forEach(System.out::println);
        } else {
            System.out.println("No books found by J.R.R. Tolkien.");
        }

        // Getting books by George Orwell
        System.out.println("\nBooks by 'George Orwell':");
        List<Book> orwellBooks = library.getBooksByAuthor("George Orwell");
        if (!orwellBooks.isEmpty()) {
            orwellBooks.forEach(System.out::println);
        } else {
            System.out.println("No books found by George Orwell.");
        }

        // Getting books by an unknown author
        System.out.println("\nBooks by 'Unknown Author':");
        List<Book> unknownBooks = library.getBooksByAuthor("Unknown Author");
        if (!unknownBooks.isEmpty()) {
            unknownBooks.forEach(System.out::println);
        } else {
            System.out.println("No books found by Unknown Author.");
        }

        // Deleting a book
        System.out.println("\nDeleting 'The Great Gatsby': " + (library.deleteBook("The Great Gatsby") ? "Success" : "Failed"));

        // Sorting books by year
        System.out.println("\nBooks sorted by year:");
        library.sortBooksByYear();
        library.displayBooks();
    }
}


