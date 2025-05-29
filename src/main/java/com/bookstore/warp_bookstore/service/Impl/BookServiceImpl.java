package com.bookstore.warp_bookstore.service.Impl;

import com.bookstore.warp_bookstore.dto.BookRequest;
import com.bookstore.warp_bookstore.dto.BookResponse;
import com.bookstore.warp_bookstore.exception.ResourceNotFoundException;
import com.bookstore.warp_bookstore.mapper.BookMapper;
import com.bookstore.warp_bookstore.model.Book;
import com.bookstore.warp_bookstore.repository.BookRepository;
import com.bookstore.warp_bookstore.service.BookService;
import com.bookstore.warp_bookstore.util.IsbnGenerator;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookServiceImpl(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

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

        return bookMapper.toBookResponse(savedBook);
    }

    @Override
    public BookResponse updateBook(Long id, BookRequest updatedBookRequest) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        book.setTitle(updatedBookRequest.getTitle());
        book.setAuthor(updatedBookRequest.getAuthor());

        Book updatedBook = bookRepository.save(book);
        return bookMapper.toBookResponse(updatedBook);    }

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
        return bookMapper.toBookResponse(book);    }

    @Override
    public List<BookResponse> findAllBooks() {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(bookMapper::toBookResponse)
                .collect(Collectors.toList());
    }


}


