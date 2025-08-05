package com.library.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class ReturnBooksReqDto {

    @JsonProperty("book_list")
    private List<Integer> bookList;
}
