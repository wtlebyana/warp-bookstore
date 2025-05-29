package com.bookstore.warp_bookstore.service.Impl;

import com.bookstore.warp_bookstore.dto.BookResponse;
import com.bookstore.warp_bookstore.exception.ResourceNotFoundException;
import com.bookstore.warp_bookstore.model.Book;
import com.bookstore.warp_bookstore.dto.BookRequest;
import com.bookstore.warp_bookstore.repository.BookRepository;
import com.bookstore.warp_bookstore.service.BookService;
import com.bookstore.warp_bookstore.util.IsbnGenerator;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    @Transactional
    public BookResponse saveBook(BookRequest bookRequest) {
        Book book = new Book();
        book.setTitle(bookRequest.getTitle());
        book.setAuthor(bookRequest.getAuthor());

        String isbn;
        do {
            isbn = IsbnGenerator.generateIsbn();
        } while (bookRepository.existsByIsbn(isbn));

        book.setIsbn(isbn);

        Book savedBook = bookRepository.save(book);

        return new BookResponse(
                savedBook.getId(),
                savedBook.getTitle(),
                savedBook.getAuthor(),
                savedBook.getIsbn()
        );
    }

    @Override
    public BookResponse updateBook(Long id, BookRequest updatedBookRequest) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        book.setTitle(updatedBookRequest.getTitle());
        book.setAuthor(updatedBookRequest.getAuthor());

        Book updatedBook = bookRepository.save(book);
        return mapToBookResponse(updatedBook);
    }

    @Override
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book not found");
        }
        bookRepository.deleteById(id);
    }

    @Override
    public BookResponse findBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        return mapToBookResponse(book);
    }

    @Override
    public List<BookResponse> findAllBooks() {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(this::mapToBookResponse)
                .collect(Collectors.toList());
    }

    private BookResponse mapToBookResponse(Book book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn()
        );
    }

}


