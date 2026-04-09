package com.library.model;

import java.util.ArrayList;
import java.util.List;

public class BookCategoryModel {
    private String categoryName;
    private List<String> bookTitles;

    public BookCategoryModel(String categoryName) {
        this.categoryName = categoryName;
        this.bookTitles = new ArrayList<>();
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<String> getBookTitles() {
        return bookTitles;
    }

    public void addBookTitle(String title) {
        this.bookTitles.add(title);
    }
}
