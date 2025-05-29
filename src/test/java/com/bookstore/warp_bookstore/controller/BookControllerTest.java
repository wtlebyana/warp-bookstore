package com.bookstore.warp_bookstore.controller;

import com.bookstore.warp_bookstore.model.Book;
import com.bookstore.warp_bookstore.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookService bookService;
    @Autowired
    private ObjectMapper objectMapper;
    private Book book;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("WARP JAVA ENGINEER ASSESSMENT");
        book.setAuthor("WILLIAM");
        book.setIsbn("9781234567897");
    }

    @Test
    void testCreateBook() throws Exception {
        Mockito.when(bookService.saveBook(any(Book.class))).thenReturn(book);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.book.title").value(book.getTitle()))
                .andExpect(jsonPath("$.book.author").value(book.getAuthor()))
                .andExpect(jsonPath("$.book.isbn").value(book.getIsbn()));
    }


    @Test
    void testUpdateBook() throws Exception {
        Mockito.when(bookService.updateBook(eq(1L), any(Book.class))).thenReturn(book);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/updateBook/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.book.author").value(book.getAuthor()))
                .andExpect(jsonPath("$.book.title").value(book.getTitle()));
    }


    @Test
    void testGetBookById() throws Exception {
        Mockito.when(bookService.findBookById(1L)).thenReturn(book);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/findBookById/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn").value(book.getIsbn()))
                .andExpect(jsonPath("$.title").value(book.getTitle()));
    }

    @Test
    void testGetAllBooks() throws Exception {
        List<Book> books = Arrays.asList(book, book);
        Mockito.when(bookService.findAllBooks()).thenReturn(books);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books/findAllBooks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Books retrieved successfully"))
                .andExpect(jsonPath("$.books", hasSize(2)))
                .andExpect(jsonPath("$.books[0].title").value(book.getTitle()))
                .andExpect(jsonPath("$.books[1].author").value(book.getAuthor()));
    }

    @Test
    void testDeleteBook() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/deleteBookById/1"))
                .andExpect(status().isNoContent());

        //Verify that delete was called once
        Mockito.verify(bookService, Mockito.times(1)).deleteBook(1L);
    }
}
