package com.library.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BookDto {
	
	@JsonProperty("title")
	private String title;
	
	@JsonProperty("author")
	private String author;
	
	@JsonProperty("code")
	private String code;
	
	@JsonProperty("total_cnt")
	private int totalCnt;
	
	@JsonProperty("available_cnt")
	private int avaiableCnt;

}
