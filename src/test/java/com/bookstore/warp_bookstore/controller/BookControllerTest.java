package com.bookstore.warp_bookstore.controller;

import com.bookstore.warp_bookstore.dto.BookResponse;
import com.bookstore.warp_bookstore.model.Book;
import com.bookstore.warp_bookstore.dto.BookRequest;
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
        book.setTitle("WARP JAVA ENGINEER ASSESSMENT");
        book.setAuthor("WILLIAM");
    }

    @Test
    void testCreateBook() throws Exception {
        BookRequest bookRequest = new BookRequest();
        bookRequest.setTitle(book.getTitle());
        bookRequest.setAuthor(book.getAuthor());

        BookResponse bookResponse = new BookResponse (
                1L,
                "WARP JAVA ENGINEER ASSESSMENT",
                "WILLIAM",
                "9781234567897");

        Mockito.when(bookService.saveBook(any(BookRequest.class))).thenReturn(bookResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.book.title").value(bookResponse.getTitle()))
                .andExpect(jsonPath("$.book.author").value(bookResponse.getAuthor()))
                .andExpect(jsonPath("$.book.isbn").value(bookResponse.getIsbn()));
    }


    @Test
    void testUpdateBook() throws Exception {
        BookRequest updateRequest = new BookRequest();
        updateRequest.setTitle("JAVA");
        updateRequest.setAuthor("THABANG");

        BookResponse updatedResponse = new BookResponse(1L, updateRequest.getTitle(), updateRequest.getAuthor(), "9781234567897");


        Mockito.when(bookService.updateBook(eq(1L), any(BookRequest.class)))
                .thenReturn(updatedResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/updateBook/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.book.title").value(updateRequest.getTitle()))
                .andExpect(jsonPath("$.book.author").value(updateRequest.getAuthor()));
    }



    @Test
    void testGetBookById() throws Exception {
        BookResponse bookResponse = new BookResponse (
                1L,
                "WARP JAVA ENGINEER ASSESSMENT",
                "WILLIAM",
                "9781234567897");
        Mockito.when(bookService.findBookById(1L)).thenReturn(bookResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/findBookById/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.book.title").value("WARP JAVA ENGINEER ASSESSMENT"))
                .andExpect(jsonPath("$.book.author").value("WILLIAM"))
                .andExpect(jsonPath("$.book.isbn").value("9781234567897"));

    }


    @Test
    void testGetAllBooks() throws Exception {
        BookResponse bookResponse1 = new BookResponse(1L, "Title One", "Author One", "9781234567890");
        BookResponse bookResponse2 = new BookResponse(2L, "Title Two", "Author Two", "9781234567891");

        List<BookResponse> books = Arrays.asList(bookResponse1, bookResponse2);

        Mockito.when(bookService.findAllBooks()).thenReturn(books);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books/findAllBooks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Books retrieved successfully"))
                .andExpect(jsonPath("$.books", hasSize(2)))
                .andExpect(jsonPath("$.books[0].title").value("Title One"))
                .andExpect(jsonPath("$.books[1].author").value("Author Two"));
    }

    @Test
    void testDeleteBook() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/deleteBookById/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookId").value(1))
                .andExpect(jsonPath("$.message").value("Book deleted successfully"));

        Mockito.verify(bookService, Mockito.times(1)).deleteBook(1L);
    }
}
