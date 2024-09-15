package cn.slibs.spring.http;

import com.iofairy.top.O;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;


import java.util.List;
import java.util.Map;

import static com.iofairy.falcon.misc.Preconditions.*;

/**
 * HttpHeaders 构建器
 *
 * @since 0.1.2
 */
public class HttpHeadersBuilder {

    private HttpHeaders httpHeaders;

    public HttpHeadersBuilder() {
        this.httpHeaders = new HttpHeaders();
    }

    public HttpHeadersBuilder(HttpHeaders HttpHeaders) {
        this.httpHeaders = HttpHeaders;
    }

    public static HttpHeadersBuilder create() {
        return new HttpHeadersBuilder();
    }

    public static HttpHeadersBuilder create(HttpHeaders HttpHeaders) {
        return new HttpHeadersBuilder(HttpHeaders);
    }

    public HttpHeadersBuilder add(String... kvs) {
        checkEmpty(kvs, args("kvs"));
        O.verifyMapKV(true, true, true, kvs);
        for (int i = 0; i < kvs.length; ) {
            httpHeaders.add(kvs[i], kvs[i + 1]);
            i += 2;
        }
        return this;
    }

    public HttpHeadersBuilder addAll(String key, List<? extends String> values) {
        checkEmpty(key, args("key"));
        httpHeaders.addAll(key, values);
        return this;
    }

    public HttpHeadersBuilder addAll(MultiValueMap<String, String> map) {
        checkEmpty(map, args("map"));
        httpHeaders.addAll(map);
        return this;
    }

    public HttpHeadersBuilder set(String headerName, @Nullable String headerValue) {
        checkEmpty(headerName, args("headerName"));
        httpHeaders.set(headerName, headerValue);
        return this;
    }

    public HttpHeadersBuilder setAll(Map<String, String> map) {
        checkEmpty(map, args("map"));
        httpHeaders.setAll(map);
        return this;
    }

    public HttpHeaders build() {
        return httpHeaders;
    }
}
