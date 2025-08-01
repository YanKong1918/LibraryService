package com.library.config

import jakarta.servlet.ReadListener
import jakarta.servlet.ServletInputStream
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import org.springframework.util.StreamUtils
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets.UTF_8

class ApiRequestWrapper(request: HttpServletRequest) : HttpServletRequestWrapper(request) {

    // 요청 본문 데이터를 저장할 필드
    private val requestData: String

    // 초기화 블록 -> 생성자에서 requestData를 초기화
    init {
        // 부모 클래스인 HttpServletRequestWrapper의 생성자를 호출한 후,
        // 요청 데이터를 읽어 requestData 필드에 저장.
        requestData = try {
            requestDataByte(request)
        } catch (e: IOException) {
            ""
        }
    }

    @Throws(IOException::class)
    private fun requestDataByte(request: HttpServletRequest): String {
        val inputStream: ServletInputStream = request.inputStream
        // InputStream의 모든 바이트를 읽는다.
        val rawData: ByteArray = StreamUtils.copyToByteArray(inputStream)
        return String(rawData, UTF_8)
    }

    // API 요청 본문을 여러 번 읽을 수 있게 해줌
    override fun getInputStream(): ServletInputStream {

        val inputStream = ByteArrayInputStream(this.requestData.toByteArray(UTF_8))

        return object : ServletInputStream() {
            override fun isFinished(): Boolean {
                return inputStream.available() == 0
            }

            override fun isReady(): Boolean {
                return true
            }

            override fun read(): Int {
                return inputStream.read()
            }

            override fun setReadListener(listener: ReadListener) {
                throw UnsupportedOperationException()
            }
        }
    }

    override fun getReader(): BufferedReader {
        return BufferedReader(InputStreamReader(this.inputStream, UTF_8))
    }

}
