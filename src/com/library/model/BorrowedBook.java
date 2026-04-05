package com.library.model;

public class BorrowedBook {
    private String readerName;
    private String bookTitle;
    private String returnDate;
    private String status;

    public BorrowedBook(String readerName, String bookTitle, String returnDate, String status) {
        this.readerName = readerName;
        this.bookTitle = bookTitle;
        this.returnDate = returnDate;
        this.status = status;
    }

    public String getReaderName() {
        return readerName;
    }

    public void setReaderName(String readerName) {
        this.readerName = readerName;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
