package com.bookstore.warp_bookstore.service;

import com.bookstore.warp_bookstore.model.Book;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BookService {
    public Book saveBook(Book book);
    public Book updateBook(Long id, Book updatedBook);
    public Book findBookById(Long id);
    public List<Book> findAllBooks();
    public void deleteBook(Long id);

}
