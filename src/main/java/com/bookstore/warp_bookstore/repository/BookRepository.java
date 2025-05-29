package com.bookstore.warp_bookstore.repository;

import com.bookstore.warp_bookstore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByIsbn(String isbn);
    boolean existsByTitleAndAuthor(String title, String author);

}
