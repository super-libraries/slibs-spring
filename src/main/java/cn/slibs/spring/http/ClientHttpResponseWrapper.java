package cn.slibs.spring.http;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * ClientHttpResponse 包装类
 *
 * @since 0.0.1
 */
public class ClientHttpResponseWrapper implements ClientHttpResponse {
    private final ClientHttpResponse clientHttpResponse;
    private byte[] body;

    public ClientHttpResponseWrapper(ClientHttpResponse clientHttpResponse) {
        this.clientHttpResponse = clientHttpResponse;
    }

    @Override
    public HttpStatus getStatusCode() throws IOException {
        return this.clientHttpResponse.getStatusCode();
    }

    @Override
    public int getRawStatusCode() throws IOException {
        return this.clientHttpResponse.getRawStatusCode();
    }

    @Override
    public String getStatusText() throws IOException {
        return this.clientHttpResponse.getStatusText();
    }

    @Override
    public void close() {
        this.clientHttpResponse.close();
    }

    /**
     * 缓存body每次返回一个新的输入流
     *
     * @return 新的输入流
     * @throws IOException IOException
     */
    @Override
    public InputStream getBody() throws IOException {
        if (Objects.isNull(this.body)) {
            this.body = StreamUtils.copyToByteArray(this.clientHttpResponse.getBody());
        }
        return new ByteArrayInputStream(this.body);
    }

    @Override
    public HttpHeaders getHeaders() {
        return this.clientHttpResponse.getHeaders();
    }

}
