package com.bookstore.warp_bookstore.controller;


import com.bookstore.warp_bookstore.dto.BookRequest;
import com.bookstore.warp_bookstore.dto.BookResponse;
import com.bookstore.warp_bookstore.exception.ResourceNotFoundException;
import com.bookstore.warp_bookstore.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1")
public class BookController {

    @Autowired
    private BookService bookService;

    @Operation(summary = "Save a new book")
    @PostMapping("/save")
    public ResponseEntity<?> save(@Valid @RequestBody BookRequest bookRequest) {
        try {
            BookResponse savedBook = bookService.saveBook(bookRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    Map.of(
                            "message", "Book saved successfully",
                            "book", savedBook
                    )
            );
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "An unexpected error occurred"));
        }
    }

    @Operation(summary = "Update Book by id")
    @PutMapping("/updateBook/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @Valid @RequestBody BookRequest bookRequest) {
        try {
            BookResponse updatedBook = bookService.updateBook(id, bookRequest);
            return ResponseEntity.ok(
                    Map.of(
                            "message", "Book updated successfully",
                            "book", updatedBook
                    )
            );
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "An unexpected error occurred"));
        }
    }

    @Operation(summary = "find Book by Id")
    @GetMapping("/findBookById/{id}")
    public ResponseEntity<?> findBookById(@PathVariable Long id) {
        try {
            BookResponse book = bookService.findBookById(id);
            return ResponseEntity.ok(
                    Map.of(
                            "message", "Book retrieved successfully",
                            "book", book
                    )
            );
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    @Operation(summary = "find All Books")
    @GetMapping("/books/findAllBooks")
    public ResponseEntity<?> findAllBooks() {
        List<BookResponse> books = bookService.findAllBooks();
        return ResponseEntity.ok(
                Map.of(
                        "message", "Books retrieved successfully",
                        "books", books
                )
        );
    }

    @Operation(summary = "Delete book by Id")
    @DeleteMapping("/deleteBookById/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok(
                Map.of("bookId", id, "message", "Book deleted successfully")
        );
    }

}
