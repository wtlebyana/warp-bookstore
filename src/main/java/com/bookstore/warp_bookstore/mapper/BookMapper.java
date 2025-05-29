package com.bookstore.warp_bookstore.mapper;

import com.bookstore.warp_bookstore.dto.BookResponse;
import com.bookstore.warp_bookstore.model.Book;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {
    BookResponse toBookResponse(Book book);
    List<BookResponse> toBookResponseList(List<Book> books);
}