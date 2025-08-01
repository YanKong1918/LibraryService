package com.library.common.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.library.config.ApiRequestWrapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Getter
@Builder
@AllArgsConstructor
public class ApiResLoggingVO {
	
	@JsonProperty("execution_time")
	private String executionTime;

	@JsonProperty("status")
	private int status;
	
	@JsonRawValue
	@JsonProperty("body")
	private String body;

	public static ApiResLoggingVO of(long executionTime, ApiRequestWrapper request, ContentCachingResponseWrapper response) {
		String body = null;
		try {
			byte[] content = response.getContentAsByteArray();
			if(content.length > 0) {
				body = new String(content, response.getCharacterEncoding());
			}
		} catch (Exception e) {
			body = "fail to return response";
		}
		
		return ApiResLoggingVO.builder()
				.executionTime(executionTime / 1000.0 + "s")
				.status(response.getStatus())
				.body(body)
				.build();
	}
}
