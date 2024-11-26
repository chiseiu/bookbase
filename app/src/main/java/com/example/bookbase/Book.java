package com.example.bookbase;

public class Book {
    protected String title;
    protected String author;
    protected String publisher;
    protected int price;
    protected String isbn;

    public Book() {
        this.title = "Unknown Title";
        this.author = "Unknown Author";
        this.publisher = "Unknown Publisher";
        this.price = 0;
        this.isbn = "Unknown ISBN";
    }

    // コンストラクタ
    public Book(String title, String author, String publisher, int price, String isbn) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.price = price;
        this.isbn = isbn;
    }

    // getter メソッド
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }


    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    // equals メソッド - ISBN での一意性を判定
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Book book = (Book) obj;
        return isbn.equals(book.isbn);
    }

    // hashCode メソッド
    @Override
    public int hashCode() {
        return isbn.hashCode();
    }

    // toString メソッド（オプション） - デバッグ用
    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publisher='" + publisher + '\'' +
                ", price=" + price +
                ", isbn='" + isbn + '\'' +
                '}';
    }

}
