package com.library.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.library.entity.Book;
import lombok.Getter;

import java.util.List;

@Getter
public class BorrowBooksReqDto {

    @JsonProperty("id")
    private int id;

    @JsonProperty("book_list")
    private List<Integer> bookList;

}
