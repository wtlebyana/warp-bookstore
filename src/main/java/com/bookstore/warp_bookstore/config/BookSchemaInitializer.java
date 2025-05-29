package com.bookstore.warp_bookstore.config;

import com.bookstore.warp_bookstore.dto.BookRequest;
import com.bookstore.warp_bookstore.dto.BookResponse;
import com.bookstore.warp_bookstore.service.BookService;

import org.springframework.boot.CommandLineRunner;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class BookSchemaInitializer {

    @Bean
    CommandLineRunner initBookSchemaConfig(BookService bookService) {
        return args -> {
            BookRequest bookRequest = new BookRequest();
            bookRequest.setTitle("Initial Start Up Book");
            bookRequest.setAuthor("MR T");

            try {
                BookResponse saved = bookService.saveBook(bookRequest);
            } catch (Exception e) {
                log.error("Unexpected error inserting book on startup", e);
            }
        };
    }
}
