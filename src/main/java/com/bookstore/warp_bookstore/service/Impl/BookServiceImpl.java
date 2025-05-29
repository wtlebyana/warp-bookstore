package com.bookstore.warp_bookstore.service.Impl;

import com.bookstore.warp_bookstore.exception.ResourceNotFoundException;
import com.bookstore.warp_bookstore.model.Book;
import com.bookstore.warp_bookstore.repository.BookRepository;
import com.bookstore.warp_bookstore.service.BookService;
import com.bookstore.warp_bookstore.util.IsbnGenerator;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {

        @Autowired
        private BookRepository bookRepository;


        public Book saveBook(Book book) {
            boolean bookExists = bookRepository.existsByTitleAndAuthor(book.getTitle(), book.getAuthor());
            if (bookExists) {
                throw new IllegalStateException("A book with the same title and author already exists.");
            }

            // Generate a unique ISBN that doesn't already exist in the repository
            String isbn;
            do {
                isbn = IsbnGenerator.generateIsbn();
            } while (bookRepository.existsByIsbn(isbn));

            book.setIsbn(isbn);
            return bookRepository.save(book);
        }

        public Book updateBook(Long id, Book updatedBook) {
            Book book = bookRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

            book.setTitle(updatedBook.getTitle());
            book.setAuthor(updatedBook.getAuthor());
            return bookRepository.save(book);
        }
        public void deleteBook(Long id) {
            if (bookRepository.existsById(id)) {
                bookRepository.deleteById(id);
            }
        }

        public Book findBookById(Long id) {
            return bookRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        }
        public List<Book> findAllBooks() {
            return bookRepository.findAll();
        }
}


