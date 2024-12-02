import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// Enum representing the status of a book
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

// Class representing a Book
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

// Generic LinkedList Node
class LinkedListNode<T> {
    T data;
    LinkedListNode<T> next;

    LinkedListNode(T data) {
        this.data = data;
        this.next = null;
    }
}

// Class representing the Library
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

// Main Application Class with CLI
class LibraryApp {
    private static Library library = new Library();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Check if a file path is provided as a command-line argument
        if (args.length > 0) {
            String filePath = args[0];
            loadBooksFromFile(filePath);
        } else {
            System.out.println("No file path provided. You can load books using the menu options.");
        }

        boolean exit = false;

        while (!exit) {
            displayMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    loadBooksFromFileInteractive();
                    break;
                case 2:
                    displayAllBooks();
                    break;
                case 3:
                    searchBookByTitle();
                    break;
                case 4:
                    getBooksByAuthor();
                    break;
                case 5:
                    addNewBook();
                    break;
                case 6:
                    deleteBook();
                    break;
                case 7:
                    sortBooksByYear();
                    break;
                case 8:
                    exit = true;
                    System.out.println("Exiting the Library Application. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option (1-8).");
            }
        }

        scanner.close();
    }

    // Display the interactive menu
    private static void displayMenu() {
        System.out.println("\n=== Library Menu ===");
        System.out.println("1. Load books from file");
        System.out.println("2. Display all books");
        System.out.println("3. Search book by title");
        System.out.println("4. Get books by author");
        System.out.println("5. Add a new book");
        System.out.println("6. Delete a book");
        System.out.println("7. Sort books by year");
        System.out.println("8. Exit");
        System.out.print("Enter your choice (1-8): ");
    }

    // Get user's menu choice
    private static int getUserChoice() {
        int choice = -1;
        try {
            choice = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            // Invalid input; choice remains -1
        }
        return choice;
    }

    // Interactive method to load books from a file
    private static void loadBooksFromFileInteractive() {
        System.out.print("Enter the path to the books file (e.g., books.txt): ");
        String filePath = scanner.nextLine().trim();
        loadBooksFromFile(filePath);
    }

    // Method to load books from a specified file
    private static void loadBooksFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 0;
            int booksLoaded = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",", -1);
                if (parts.length != 4) {
                    System.out.println("Invalid format in line " + lineNumber + ": " + line);
                    continue; // Skip invalid lines
                }

                String author = parts[0].trim();
                String title = parts[1].trim();
                int year;
                try {
                    year = Integer.parseInt(parts[2].trim());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid year in line " + lineNumber + ": " + parts[2]);
                    continue; // Skip lines with invalid year
                }

                Status status;
                try {
                    status = Status.valueOf(parts[3].trim().toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid status in line " + lineNumber + ": " + parts[3]);
                    continue; // Skip lines with invalid status
                }

                Book book = new Book(author, title, year, status);
                library.addBook(book);
                booksLoaded++;
            }
            System.out.println(booksLoaded + " books loaded successfully from " + filePath + ".");
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
    }

    // Display all books in the library
    private static void displayAllBooks() {
        System.out.println("\nBooks in the library:");
        library.sortBooksByYear();
        library.displayBooks();
    }

    // Search for a book by its title
    private static void searchBookByTitle() {
        System.out.print("Enter the title of the book to search: ");
        String title = scanner.nextLine().trim();
        Book foundBook = library.searchBookByTitle(title);
        System.out.println(foundBook != null ? foundBook : "Book not found.");
    }

    // Get all books by a specific author
    private static void getBooksByAuthor() {
        System.out.print("Enter the author's name: ");
        String author = scanner.nextLine().trim();
        List<Book> authorBooks = library.getBooksByAuthor(author);
        if (!authorBooks.isEmpty()) {
            System.out.println("\nBooks by '" + author + "':");
            authorBooks.forEach(System.out::println);
        } else {
            System.out.println("No books found by " + author + ".");
        }
    }

    // Add a new book to the library
    private static void addNewBook() {
        System.out.print("Enter author name: ");
        String author = scanner.nextLine().trim();

        System.out.print("Enter book title: ");
        String title = scanner.nextLine().trim();

        int year = -1;
        while (year < 0) {
            System.out.print("Enter year of publication: ");
            try {
                year = Integer.parseInt(scanner.nextLine().trim());
                if (year < 0) {
                    System.out.println("Year cannot be negative. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid year. Please enter a valid integer.");
            }
        }

        Status status = null;
        while (status == null) {
            System.out.print("Enter status (BANNED, BORROWED, EXIT): ");
            String statusInput = scanner.nextLine().trim().toUpperCase();
            try {
                status = Status.valueOf(statusInput);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid status. Please enter one of: BANNED, BORROWED, EXIT.");
            }
        }

        Book newBook = new Book(author, title, year, status);
        library.addBook(newBook);
        System.out.println("Book added successfully: " + newBook);
    }

    // Delete a book from the library
    private static void deleteBook() {
        System.out.print("Enter the title of the book to delete: ");
        String title = scanner.nextLine().trim();
        boolean success = library.deleteBook(title);
        System.out.println(success ? "Book deleted successfully." : "Book not found. Deletion failed.");
    }

    // Sort books by their year of publication
    private static void sortBooksByYear() {
        library.sortBooksByYear();
        System.out.println("Books have been sorted by year of publication.");
    }
}
