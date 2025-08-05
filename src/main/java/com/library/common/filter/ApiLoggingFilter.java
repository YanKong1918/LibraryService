package com.library.common.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.common.vo.ApiReqLoggingVO;
import com.library.common.vo.ApiResLoggingVO;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
public class ApiLoggingFilter implements Filter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        // 1. HttpServletRequest, HttpServletResponse 로 캐스팅
        ApiRequestWrapper reqWrapper = new ApiRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper resWrapper = new ContentCachingResponseWrapper((HttpServletResponse) response);

        // 2. 요청 ID 생성 및 MDC 에 추가
        String uuid = UUID.randomUUID().toString();
        MDC.put("request_id", uuid);

        long start = System.currentTimeMillis();

        try {
            // 3. 요청 정보 로깅
            try {
                JsonNode requestBody = objectMapper.readTree(reqWrapper.getReader());
                requestInfo(reqWrapper, requestBody);
            } catch (Exception e) {
                log.error("Failed to log Reqeust Info : {}", e.getMessage(), e);
            }

            // 4. 필터 체인 실행
            chain.doFilter(reqWrapper, resWrapper);
        } finally {
            // 5. 요청 종료
            long end = System.currentTimeMillis();

            // 6. 응답 정보 로깅
            try {
                responseInfo(end - start, reqWrapper, resWrapper);
            } catch (Exception e) {
                log.error("Failed to log Response Info : {}", e.getMessage(), e);
            }

            // 7. 클라이언트 응답 본문 복사 => 반드시 수행해야 응답 데이터 전달 가능
            resWrapper.copyBodyToResponse();
            // 8. MDC 클리어
            MDC.clear();
        }
    }

    private void requestInfo(ApiRequestWrapper requestWrapper, JsonNode requestBody) throws IOException {
        log.info("===> [API-REQUEST]  [{}] : {}", MDC.get("request_id"),
                objectMapper.writeValueAsString(ApiReqLoggingVO.of(requestWrapper, requestBody)));
    }

    private void responseInfo(long executionTime, ApiRequestWrapper requestWrapper, ContentCachingResponseWrapper responseWrapper) throws IOException {
        log.info("<=== [API-RESPONSE] [{}] : {}", MDC.get("request_id"),
                objectMapper.writeValueAsString(ApiResLoggingVO.of(executionTime, requestWrapper, responseWrapper)));
    }

}
