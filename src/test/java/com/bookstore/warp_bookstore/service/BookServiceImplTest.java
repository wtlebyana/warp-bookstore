package com.bookstore.warp_bookstore.service;

import com.bookstore.warp_bookstore.exception.ResourceNotFoundException;
import com.bookstore.warp_bookstore.model.Book;
import com.bookstore.warp_bookstore.repository.BookRepository;
import com.bookstore.warp_bookstore.service.Impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceImplTest {
    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private BookServiceImpl bookService;
    private Book book;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        book = new Book();
        book.setId(1L);
        book.setTitle("WARP JAVA ENGINEER ASSESSMENT");
        book.setAuthor("WILLIAM");
        book.setIsbn("9780306406157");
    }

    @Test
    void testSaveBook() {
        when(bookRepository.existsByIsbn(anyString())).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenAnswer(i -> i.getArgument(0));
        Book book_one = bookService.saveBook(book);
        assertNotNull(book_one.getIsbn());
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void testUpdateBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        Book updatedBook = new Book();
        when(bookRepository.save(any(Book.class))).thenAnswer(i -> i.getArgument(0));
        updatedBook.setTitle("WARP DEVELOPMENT HIRE ASSESSMENT");
        updatedBook.setAuthor("THABANG");
        Book result = bookService.updateBook(1L, updatedBook);
        assertEquals("WARP DEVELOPMENT HIRE ASSESSMENT", result.getTitle());
        assertEquals("THABANG", result.getAuthor());
    }

    @Test
    void testUpdateBook_NotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        Book update = new Book();
        assertThrows(ResourceNotFoundException.class, () -> bookService.updateBook(1L, update));
    }

    @Test
    void testDeleteBook() {
        when(bookRepository.existsById(1L)).thenReturn(true);
        doNothing().when(bookRepository).deleteById(1L);
        bookService.deleteBook(1L);
        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetBookById() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        Book found = bookService.findBookById(1L);
        assertEquals(book, found);
    }

    @Test
    void testFindBookById_NotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> bookService.findBookById(1L));
    }

    @Test
    void testFindAllBooks() {
        List<Book> books = List.of(book, book);
        when(bookRepository.findAll()).thenReturn(books);
        List<Book> allBooks = bookService.findAllBooks();
        assertThat(allBooks).hasSize(2);
        verify(bookRepository).findAll();
    }
}
