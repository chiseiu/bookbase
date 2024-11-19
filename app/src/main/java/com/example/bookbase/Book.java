package com.example.bookbase;

public class Book {
    private String title;
    private String author;
    private String publisher;
    private double price;
    private String isbn;

    // コンストラクタ
    public Book(String title, String author, String publisher, double price, String isbn) {
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

    public String getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }

    public double getPrice() {
        return price;
    }

    public String getIsbn() {
        return isbn;
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
