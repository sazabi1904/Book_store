package com.library.model;

public class BookStatistic {
    private String title;
    private int borrowCount;

    public BookStatistic(String title, int borrowCount) {
        this.title = title;
        this.borrowCount = borrowCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getBorrowCount() {
        return borrowCount;
    }

    public void setBorrowCount(int borrowCount) {
        this.borrowCount = borrowCount;
    }
}
