package com.bookstore.warp_bookstore.service;

import com.bookstore.warp_bookstore.dto.BookRequest;
import com.bookstore.warp_bookstore.dto.BookResponse;

import java.util.List;

public interface BookService {
    BookResponse saveBook(BookRequest bookRequest);
    BookResponse updateBook(Long id, BookRequest updatedBookRequest);
    BookResponse findBookById(Long id);
    List<BookResponse> findAllBooks();
    void deleteBook(Long id);
}
