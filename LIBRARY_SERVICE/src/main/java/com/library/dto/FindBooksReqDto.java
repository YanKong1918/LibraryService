package com.library.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class FindBooksReqDto {
	
	@JsonProperty("keyword")
	private String keyword;
	
	@JsonProperty("page_no")
	private int pageNo = 0;
	
}
