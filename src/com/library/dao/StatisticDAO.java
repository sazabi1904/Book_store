package com.library.dao;

import com.library.model.BookStatistic;
import com.library.model.BorrowedBook;
import java.util.ArrayList;
import java.util.List;

public class StatisticDAO {

    public List<BookStatistic> getTopBorrowedBooks() {
        // TODO: Lấy dữ liệu thống kê từ DB
        return new ArrayList<>();
    }

    public List<String> getInventoryStatus() {
        // TODO: Lấy trạng thái kho sách từ DB
        return new ArrayList<>();
    }

    public List<BorrowedBook> getOverdueBooks() {
        // TODO: Lấy danh sách sách quá hạn từ DB
        return new ArrayList<>();
    }
}
