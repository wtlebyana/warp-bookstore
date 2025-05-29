package com.bookstore.warp_bookstore.util;

import com.bookstore.warp_bookstore.dto.BookResponse;
import com.bookstore.warp_bookstore.model.Book;

import java.util.List;
import java.util.stream.Collectors;

public class BookMapper {
    public static BookResponse toBookResponse(Book book) {
        if (book == null) {
            return null;
        }
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn()
        );
    }

    public static List<BookResponse> toBookResponseList(List<Book> books) {
        if (books == null) {
            return List.of();
        }
        return books.stream()
                .map(BookMapper::toBookResponse)
                .collect(Collectors.toList());
    }
}
