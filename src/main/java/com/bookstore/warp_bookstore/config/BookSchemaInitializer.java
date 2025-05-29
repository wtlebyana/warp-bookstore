package com.bookstore.warp_bookstore.config;

import com.bookstore.warp_bookstore.model.Book;
import com.bookstore.warp_bookstore.service.BookService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BookSchemaInitializer {

    @Bean
    CommandLineRunner initBookSchemaConfig(BookService bookService) {
        return args -> {
            Book book = new Book();
            book.setTitle("Initial Start Up Book");
            book.setAuthor("MR T");
            try {
                bookService.saveBook(book);
                System.out.println("Book created on start up");
            } catch (Exception e) {
                System.out.println("Book already exists or could not be added");
            }
        };
    }
}
