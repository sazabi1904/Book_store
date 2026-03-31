package com.library.model;

public class Book {
    private String bookId;
    private String title;
    private String author;
    private String category;
    private int year;
    private int quantity;

    public Book(String bookId, String title, String author, String category, int year, int quantity) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.category = category;
        this.year = year;
        this.quantity = quantity;
    }

    public String getBookId() { return bookId; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public int getYear() { return year; }
    public int getQuantity() { return quantity; }

    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setCategory(String category) { this.category = category; }
    public void setYear(int year) { this.year = year; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    @Override
    public String toString() {
        return "Mã sách: " + bookId + " | Tên: " + title + " | Tác giả: " + author +
               " | Thể loại: " + category + " | Năm: " + year + " | Số lượng: " + quantity;
    }
}
