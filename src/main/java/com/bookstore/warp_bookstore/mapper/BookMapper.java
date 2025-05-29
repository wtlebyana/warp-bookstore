package com.bookstore.warp_bookstore.mapper;

import com.bookstore.warp_bookstore.dto.BookResponse;
import com.bookstore.warp_bookstore.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    BookResponse toBookResponse(Book book);

    List<BookResponse> toBookResponseList(List<Book> books);
}
