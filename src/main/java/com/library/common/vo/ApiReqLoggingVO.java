package com.library.common.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.common.filter.ApiRequestWrapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class ApiReqLoggingVO {

	@JsonProperty("method")
	private String method;

	@JsonProperty("uri")
	private String uri;

	@JsonRawValue
	@JsonProperty("body")
	private Object body;

	@JsonRawValue
	@JsonProperty("param")
	private String param;

	@JsonProperty("ip")
	private String ip;


	public static ApiReqLoggingVO of(ApiRequestWrapper request, JsonNode requestBody) {
		return ApiReqLoggingVO.builder()
				.method(request.getMethod())
				.uri(request.getRequestURI())
				.body(requestBody.isEmpty() ? null : requestBody)
				.param(getReqQueryString(request))
				.ip(request.getRemoteAddr())
				.build();
	}

	private static String getReqQueryString(HttpServletRequest request) {
		String result = null;
		Map<String, String[]> paramMap = request.getParameterMap();

		if (!paramMap.isEmpty()) {
			try {
				result = new ObjectMapper().writeValueAsString(paramMap);
			} catch (JsonProcessingException e) {
				return null;
			}
		}
		return result;
	}
}
