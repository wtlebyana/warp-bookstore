package com.bookstore.warp_bookstore.service;

import com.bookstore.warp_bookstore.dto.BookResponse;
import com.bookstore.warp_bookstore.exception.ResourceNotFoundException;
import com.bookstore.warp_bookstore.mapper.BookMapper;
import com.bookstore.warp_bookstore.model.Book;
import com.bookstore.warp_bookstore.dto.BookRequest;
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
    @Mock
    private BookMapper bookMapper;

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
        BookRequest bookRequest = new BookRequest();
        bookRequest.setTitle("WARP JAVA ENGINEER ");
        bookRequest.setAuthor("LEBYANA");

        when(bookRepository.existsByIsbn(anyString())).thenReturn(false);

        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> {
            Book bookArg = invocation.getArgument(0);
            bookArg.setId(1L);
            bookArg.setIsbn("9781234567897");
            return bookArg;
        });

        when(bookMapper.toBookResponse(any(Book.class))).thenAnswer(invocation -> {
            Book bookArg = invocation.getArgument(0);
            return new BookResponse(
                    bookArg.getId(),
                    bookArg.getTitle(),
                    bookArg.getAuthor(),
                    bookArg.getIsbn()
            );
        });

        BookResponse savedBook = bookService.saveBook(bookRequest);

        assertNotNull(savedBook);
        assertNotNull(savedBook.getIsbn());
        assertEquals("WARP JAVA ENGINEER ", savedBook.getTitle());
        assertEquals("LEBYANA", savedBook.getAuthor());
        assertEquals(1L, savedBook.getId());

        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void testUpdateBook() {

        BookRequest updateRequest = new BookRequest();
        updateRequest.setTitle("WARP DEVELOPMENT HIRE ASSESSMENT");
        updateRequest.setAuthor("THABANG");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenAnswer(
                invocation -> invocation.getArgument(0));
        when(bookMapper.toBookResponse(any(Book.class))).thenAnswer(invocation -> {
            Book b = invocation.getArgument(0);
            return new BookResponse(
                    b.getId(),
                    b.getTitle(),
                    b.getAuthor(),
                    b.getIsbn()
            );
        });

        BookResponse result = bookService.updateBook(1L, updateRequest);

        assertEquals("WARP DEVELOPMENT HIRE ASSESSMENT", result.getTitle());
        assertEquals("THABANG", result.getAuthor());
        assertEquals(1L, result.getId());
        assertEquals("9780306406157", result.getIsbn());

        verify(bookRepository).save(any(Book.class));
    }



    @Test
    void testUpdateBook_NotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        BookRequest updateRequest = new BookRequest();
        updateRequest.setTitle("LEBYANA TWO");
        updateRequest.setAuthor("LEBYANA THREE");

        assertThrows(ResourceNotFoundException.class, () ->
                bookService.updateBook(1L, updateRequest)
        );
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

        Book book = new Book(1L,
                "WARP JAVA ENGINEER ASSESSMENT",
                "WILLIAM",
                "9780306406157");

        BookResponse bookResponse = new BookResponse(1L,
                "WARP JAVA ENGINEER ASSESSMENT",
                "WILLIAM",
                "9780306406157");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.toBookResponse(book)).thenReturn(bookResponse);

        BookResponse found = bookService.findBookById(1L);

        assertEquals(1L, found.getId());
        assertEquals("WARP JAVA ENGINEER ASSESSMENT", found.getTitle());
        assertEquals("WILLIAM", found.getAuthor());
        assertEquals("9780306406157", found.getIsbn());
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
        when(bookMapper.toBookResponse(any(Book.class))).thenAnswer(invocation -> {
            Book book = invocation.getArgument(0);
            return new BookResponse(
                    book.getId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getIsbn()
            );
        });

        List<BookResponse> allBooks = bookService.findAllBooks();

        assertNotNull(allBooks);
        assertEquals(2, allBooks.size());
        assertThat(allBooks).hasSize(2);
        assertThat(allBooks.get(0).getTitle()).isEqualTo(book.getTitle());
        assertThat(allBooks.get(1).getTitle()).isEqualTo(book.getTitle());

        verify(bookRepository).findAll();
    }

}
