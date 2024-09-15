package cn.slibs.test;

import cn.slibs.spring.http.HttpHeadersBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author GG
 * @version 1.0
 * @date 2024/9/15 15:30
 */
public class HttpHeadersBuilderTest {
    @Test
    void testHttpHeadersCreate() {
        HttpHeaders httpHeaders = HttpHeadersBuilder.create().add("Content-Type", "application/json", "Authorization", "Bearer token", "key1", "aaa", "key2", "bbb").build();
        assertEquals("[Content-Type:\"application/json\", Authorization:\"Bearer token\", key1:\"aaa\", key2:\"bbb\"]", httpHeaders.toString());

        httpHeaders = HttpHeadersBuilder.create()
                .add("Content-Type", "application/json", "Authorization", "Bearer token")
                .add("key1", "aaa", "key2", "bbb")
                .build();
        assertEquals("[Content-Type:\"application/json\", Authorization:\"Bearer token\", key1:\"aaa\", key2:\"bbb\"]", httpHeaders.toString());

        try {
            httpHeaders = HttpHeadersBuilder.create()
                    .add("Content-Type", "application/json", "Authorization", "Bearer token")
                    .add("key1", "aaa", "key2")
                    .build();
            System.out.println(httpHeaders);
        } catch (Exception e) {
            assertEquals("The parameters length must be even. ", e.getMessage());
        }
    }


}
