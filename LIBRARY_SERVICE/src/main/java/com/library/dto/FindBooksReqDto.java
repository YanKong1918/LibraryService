package com.library.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FindBooksReqDto {

    @JsonProperty("keyword")
    private String keyword;

    @JsonProperty("page_no")
    private int pageNo = 0;

}
