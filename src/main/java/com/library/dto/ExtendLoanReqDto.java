package com.library.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class ExtendLoanReqDto {

    @JsonProperty("id")
    private int id;

    @JsonProperty("book_list")
    private List<Integer> bookList;

}
