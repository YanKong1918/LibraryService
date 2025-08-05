package com.library.common.filter;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.util.StreamUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ApiRequestWrapper extends HttpServletRequestWrapper {

    private final String requestData;

    public ApiRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        // 생성자에서 요청 데이터를 읽어 requestData 필드에 저장.
        // 부모 클래스인 HttpServletRequestWrapper의 생성자를 먼저 호출해야 함.
        this.requestData = new String(StreamUtils.copyToByteArray(request.getInputStream()), StandardCharsets.UTF_8);
    }

    // API 요청 본문을 여러 번 읽을 수 있게 함.
    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(this.requestData.getBytes(StandardCharsets.UTF_8));

        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return inputStream.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public int read() {
                return inputStream.read();
            }

            @Override
            public void setReadListener(ReadListener listener) {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(this.getInputStream(), StandardCharsets.UTF_8));
    }

}
