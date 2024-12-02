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

class LinkedListNode<T> {
    T data;
    LinkedListNode<T> next;

    LinkedListNode(T data) {
        this.data = data;
        this.next = null;
    }
}

class Library {
    private LinkedListNode<Book> head;

    public void addBook(Book book) {
        LinkedListNode<Book> newNode = new LinkedListNode<>(book);
        if (head == null) {
            head = newNode;
        } else {
            LinkedListNode<Book> temp = head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = newNode;
        }
    }

    public void displayBooks() {
        if (head == null) {
            System.out.println("The library has no books.");
            return;
        }
        LinkedListNode<Book> temp = head;
        while (temp != null) {
            System.out.println(temp.data);
            temp = temp.next;
        }
    }

    public Book searchBookByTitle(String title) {
        LinkedListNode<Book> temp = head;
        while (temp != null) {
            if (temp.data.getTitle().equalsIgnoreCase(title)) {
                return temp.data;
            }
            temp = temp.next;
        }
        return null;
    }

    public List<Book> getBooksByAuthor(String author) {
        List<Book> books = new ArrayList<>();
        LinkedListNode<Book> temp = head;
        while (temp != null) {
            if (temp.data.getAuthor().equalsIgnoreCase(author)) {
                books.add(temp.data);
            }
            temp = temp.next;
        }
        return books;
    }

    public boolean deleteBook(String title) {
        if (head == null) return false;

        if (head.data.getTitle().equalsIgnoreCase(title)) {
            head = head.next;
            return true;
        }

        LinkedListNode<Book> temp = head;
        while (temp.next != null && !temp.next.data.getTitle().equalsIgnoreCase(title)) {
            temp = temp.next;
        }

        if (temp.next != null) {
            temp.next = temp.next.next;
            return true;
        }

        return false;
    }

    public void sortBooksByYear() {
        if (head == null || head.next == null) return;

        boolean swapped;
        do {
            swapped = false;
            LinkedListNode<Book> current = head;
            LinkedListNode<Book> prev = null;
            LinkedListNode<Book> next = head.next;

            while (next != null) {
                if (current.data.getYearOfPublish() > next.data.getYearOfPublish()) {
                    swapped = true;

                    if (prev != null) {
                        LinkedListNode<Book> temp = next.next;
                        prev.next = next;
                        next.next = current;
                        current.next = temp;
                    } else {
                        LinkedListNode<Book> temp = next.next;
                        head = next;
                        next.next = current;
                        current.next = temp;
                    }

                    prev = next;
                    next = current.next;
                } else {
                    prev = current;
                    current = next;
                    next = next.next;
                }
            }
        } while (swapped);
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

        // Deleting a book
        System.out.println("\nDeleting 'The Great Gatsby': " + (library.deleteBook("The Great Gatsby") ? "Success" : "Failed"));

        // Sorting books by year
        System.out.println("\nBooks sorted by year:");
        library.sortBooksByYear();
        library.displayBooks();
    }
}
