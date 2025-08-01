package com.library.common.filter

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.library.common.domain.ApiReqLoggingVO
import com.library.common.domain.ApiResLoggingVO
import com.library.config.ApiRequestWrapper
import jakarta.servlet.*
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.util.ContentCachingResponseWrapper
import java.io.IOException
import java.util.*

@Component
class ApiLoggingFilter : Filter {

    private val objectMapper = ObjectMapper()
    private val log = LoggerFactory.getLogger(ApiLoggingFilter::class.java)

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {

        // response Content-Type 설정
        response.contentType = MediaType.APPLICATION_JSON_VALUE

        // HttpServletRequest와 HttpServletResponse로 캐스팅
        val reqWrapper = ApiRequestWrapper(request as HttpServletRequest)
        val resWrapper = ContentCachingResponseWrapper(response as HttpServletResponse)

        // 요청 ID 생성 및 MDC에 추가
        val uuid = UUID.randomUUID()
        MDC.put("request_id", uuid.toString())

        // 1. 요청 시작 시간 기록
        val start = System.currentTimeMillis()
        try {
            // 2. 요청 정보 기록
            runCatching {
                requestInfo(reqWrapper, objectMapper.readTree(reqWrapper.reader))
            }.onFailure { e ->
                log.error("Failed to log request info : ${e.message}", e)
            }

            // 필터 체인 실행
            chain.doFilter(reqWrapper, resWrapper)
        } finally {

            // 3. 요청 종료 시간 기록
            val end = System.currentTimeMillis()
            // 4. 응답 정보 기록
            runCatching {
                responseInfo(end - start, reqWrapper, resWrapper)
            }.onFailure { e ->
                log.error("Failed to log response info : ${e.message}", e)
            }
            // 5. 클라이언트 응답 본문 복사
            resWrapper.copyBodyToResponse()
            // MDC 클리어
            MDC.clear()
        }
    }

    private fun requestInfo(requestWrapper: ApiRequestWrapper, requestBody: JsonNode) {
        log.info(
            "==> [API-REQUEST]  [${MDC.get("request_id")}] : ${
                objectMapper.writeValueAsString(ApiReqLoggingVO.of(requestWrapper, requestBody))
            }"
        )
    }

    private fun responseInfo(executionTime: Long, requestWrapper: ApiRequestWrapper, responseWrapper: ContentCachingResponseWrapper) {
        log.info(
            "<== [API-RESPONSE] [${MDC.get("request_id")}] : ${
                objectMapper.writeValueAsString(ApiResLoggingVO.of(executionTime, requestWrapper, responseWrapper))
            }"
        )
    }

}