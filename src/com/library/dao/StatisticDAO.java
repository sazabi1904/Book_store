package com.library.dao;

import com.library.model.BookStatistic;
import com.library.model.BorrowedBook;
import java.util.ArrayList;
import java.util.List;

public class StatisticDAO {

    public List<BookStatistic> getTopBorrowedBooks() {
        return new ArrayList<>();
    }

    public List<String> getInventoryStatus() {
        return new ArrayList<>();
    }

    public List<BorrowedBook> getOverdueBooks() {
        return new ArrayList<>();
    }
}
