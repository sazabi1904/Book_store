package com.library.model;

import java.util.ArrayList;
import java.util.List;

// PHẦN THÊM VÀO: Model để phân loại sách theo thể loại
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
