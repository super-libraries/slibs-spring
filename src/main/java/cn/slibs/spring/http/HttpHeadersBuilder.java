package cn.slibs.spring.http;

import com.iofairy.top.O;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;


import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import static com.iofairy.falcon.misc.Preconditions.*;

/**
 * HttpHeaders 构建器
 *
 * @since 0.1.2
 */
public class HttpHeadersBuilder {

    private final HttpHeaders httpHeaders;

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

    public HttpHeadersBuilder addContentType(String contentType) {
        checkBlank(contentType, args("contentType"));
        httpHeaders.add("Content-Type", contentType);
        return this;
    }

    public HttpHeadersBuilder addJsonContentType() {
        return addContentType("application/json");
    }

    public HttpHeadersBuilder set(String headerName, String headerValue) {
        checkEmpty(headerName, args("headerName"));
        httpHeaders.set(headerName, headerValue);
        return this;
    }

    public HttpHeadersBuilder setAll(Map<String, String> map) {
        checkEmpty(map, args("map"));
        httpHeaders.setAll(map);
        return this;
    }

    public HttpHeadersBuilder setContentType(MediaType mediaType) {
        httpHeaders.setContentType(mediaType);
        return this;
    }

    public HttpHeadersBuilder setAccept(List<MediaType> acceptableMediaTypes) {
        httpHeaders.setAccept(acceptableMediaTypes);
        return this;
    }

    public HttpHeadersBuilder setAcceptCharset(List<Charset> acceptableCharsets) {
        httpHeaders.setAcceptCharset(acceptableCharsets);
        return this;
    }

    public HttpHeaders build() {
        return httpHeaders;
    }


    /**
     * Get HttpHeaders With JsonType
     *
     * @return HttpHeaders
     */
    public static HttpHeaders getHttpHeadersWithJsonType() {
        return HttpHeadersBuilder.create()
                .setContentType(MediaType.parseMediaType("application/json; charset=UTF-8"))
                .add("Accept", MediaType.APPLICATION_JSON.toString())
                .build();
    }

}
