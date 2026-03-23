/* Design a digital library system to manage different types of library items. */
package practice;

abstract class Library{
    String title;
    String author;
    int itemid,price;
    boolean isavailbe;

    Library(String title, String author, int itemid, int price){
        this.title = title;
        this.author = author;
        this.itemid = itemid;
        this.price = price;
        this.isavailbe = true;
    }

    abstract void displayDetails();

    void borrowItem(){
        if(isavailbe){
            isavailbe = false;
            System.out.println("You have borrowed: " + title);
        }
        else{
            System.out.println("Sorry, " + title + " is currently unavailable.");
        }
    }

    void returnItem(){
        if(!isavailbe){
            isavailbe = true;
            System.out.println("You have returned: " + title);
        }
        else{
            System.out.println(title + " was not borrowed.");
        }
    }
}

class Book extends Library{
    String author;

    Book(String title, String author, int itemid, int price) {
        super(title, author, itemid, price);
        this.author = author;
    }

    @Override
    void displayDetails() {
        System.out.println("Book Title: " + title);
        System.out.println("Author: " + author);
        System.out.println("Item ID: " + itemid);
        System.out.println("Price: " + price);
        System.out.println("Available: " + isavailbe);
    }

    boolean isAvailable(){
        return isavailbe;
    }
}

class EBook extends Library{
    String format;
    EBook(String title,String author,int itemid,int price,String format){
        super(title, author, itemid, price);
        this.format = format;
    }

    @Override
    void displayDetails() {
        System.out.println("E-Book Title: " + title);
        System.out.println("Author: " + author);
        System.out.println("Item ID: " + itemid);
        System.out.println("Price: " + price);
        System.out.println("Format: " + format);
        System.out.println("Available: " + isavailbe);
    }

    boolean isAvailable(){
        return isavailbe;
    }
}

public class LibraryManagementSystem {
    public static void main(String[] args) {
        Book book1 = new Book("The Great Gatsby", "F. Scott Fitzgerald", 101, 10);
        EBook ebook1 = new EBook("1984", "George Orwell", 102, 5, "PDF");

        book1.displayDetails();
        System.out.println();
        ebook1.displayDetails();

        System.out.println("\nBorrowing the book...");
        book1.borrowItem();
        System.out.println("Is the book available? " + book1.isAvailable());

        System.out.println("\nReturning the book...");
        book1.returnItem();
        System.out.println("Is the book available? " + book1.isAvailable());
    }
}