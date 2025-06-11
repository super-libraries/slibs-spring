package cn.slibs.spring.http;

import lombok.Getter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate 工具
 *
 * @since 0.0.1
 */
@Getter
public class RestHelper {
    private final RestTemplate restTemplate;

    RestHelper(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public static RestHelper build(RestTemplate restTemplate) {
        return new RestHelper(restTemplate);
    }

    public <REQ, RES> RES postForEntity(String url, REQ req, HttpHeaders headers, Class<RES> responseType) {
        HttpEntity<REQ> httpEntity = new HttpEntity<>(req, headers);
        return restTemplate.postForEntity(url, httpEntity, responseType).getBody();
    }

    public <REQ, RES> RES postForEntity(String url, REQ req, Class<RES> responseType) {
        HttpHeaders headers = HttpHeadersBuilder.getHttpHeadersWithJsonType();
        return postForEntity(url, req, headers, responseType);
    }

    public <REQ> String postForString(String url, REQ req) {
        return postForEntity(url, req, String.class);
    }

    public <REQ, RES> RES getWithBody(String url, REQ req, HttpHeaders headers, Class<RES> responseType) {
        HttpEntity<REQ> httpEntity = new HttpEntity<>(req, headers);
        return restTemplate.exchange(url, HttpMethod.GET, httpEntity, responseType).getBody();
    }

    public <REQ, RES> RES getWithBody(String url, REQ req, Class<RES> responseType) {
        HttpHeaders headers = HttpHeadersBuilder.getHttpHeadersWithJsonType();
        HttpEntity<REQ> httpEntity = new HttpEntity<>(req, headers);
        return restTemplate.exchange(url, HttpMethod.GET, httpEntity, responseType).getBody();
    }

    public <RES> RES getForEntity(String url, HttpHeaders headers, Class<RES> responseType) {
        HttpEntity<Object> httpEntity = new HttpEntity<>(null, headers);
        return restTemplate.exchange(url, HttpMethod.GET, httpEntity, responseType).getBody();
    }

    public <RES> RES getForEntity(String url, Class<RES> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        return getForEntity(url, headers, responseType);
    }

    public String getForString(String url) {
        return getForEntity(url, new HttpHeaders(), String.class);
    }

    public <REQ, RES> RES exchange(String url, HttpMethod method, REQ req, HttpHeaders headers, Class<RES> responseType) {
        HttpEntity<REQ> httpEntity = new HttpEntity<>(req, headers);
        return restTemplate.exchange(url, method, httpEntity, responseType).getBody();
    }

    public <REQ, RES> RES exchange(String url, HttpMethod method, REQ req, Class<RES> responseType) {
        HttpHeaders headers = HttpHeadersBuilder.getHttpHeadersWithJsonType();
        return exchange(url, method, req, headers, responseType);
    }

    /*
     * ===============================
     * *****    返回带泛型的实体    *****
     * ===============================
     */
    public <REQ, RES> RES postForEntity(String url, REQ req, HttpHeaders headers, ParameterizedTypeReference<RES> responseType) {
        HttpEntity<REQ> httpEntity = new HttpEntity<>(req, headers);
        return restTemplate.exchange(url, HttpMethod.POST, httpEntity, responseType).getBody();
    }

    public <REQ, RES> RES postForEntity(String url, REQ req, ParameterizedTypeReference<RES> responseType) {
        HttpHeaders headers = HttpHeadersBuilder.getHttpHeadersWithJsonType();
        return postForEntity(url, req, headers, responseType);
    }

    public <REQ, RES> RES getWithBody(String url, REQ req, HttpHeaders headers, ParameterizedTypeReference<RES> responseType) {
        HttpEntity<REQ> httpEntity = new HttpEntity<>(req, headers);
        return restTemplate.exchange(url, HttpMethod.GET, httpEntity, responseType).getBody();
    }

    public <REQ, RES> RES getWithBody(String url, REQ req, ParameterizedTypeReference<RES> responseType) {
        HttpHeaders headers = HttpHeadersBuilder.getHttpHeadersWithJsonType();
        HttpEntity<REQ> httpEntity = new HttpEntity<>(req, headers);
        return restTemplate.exchange(url, HttpMethod.GET, httpEntity, responseType).getBody();
    }

    public <RES> RES getForEntity(String url, HttpHeaders headers, ParameterizedTypeReference<RES> responseType) {
        HttpEntity<Object> httpEntity = new HttpEntity<>(null, headers);
        return restTemplate.exchange(url, HttpMethod.GET, httpEntity, responseType).getBody();
    }

    public <RES> RES getForEntity(String url, ParameterizedTypeReference<RES> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        return getForEntity(url, headers, responseType);
    }

    public <REQ, RES> RES exchange(String url, HttpMethod method, REQ req, HttpHeaders headers, ParameterizedTypeReference<RES> responseType) {
        HttpEntity<REQ> httpEntity = new HttpEntity<>(req, headers);
        return restTemplate.exchange(url, method, httpEntity, responseType).getBody();
    }

    public <REQ, RES> RES exchange(String url, HttpMethod method, REQ req, ParameterizedTypeReference<RES> responseType) {
        HttpHeaders headers = HttpHeadersBuilder.getHttpHeadersWithJsonType();
        return exchange(url, method, req, headers, responseType);
    }

    /*==================================================
     ******************   静态方法调用  ******************
     ==================================================*/
    public static <REQ, RES> RES postForEntity(RestTemplate restTemplate, String url, REQ req, HttpHeaders headers, Class<RES> responseType) {
        HttpEntity<REQ> httpEntity = new HttpEntity<>(req, headers);
        return restTemplate.postForEntity(url, httpEntity, responseType).getBody();
    }

    public static <REQ, RES> RES postForEntity(RestTemplate restTemplate, String url, REQ req, Class<RES> responseType) {
        HttpHeaders headers = HttpHeadersBuilder.getHttpHeadersWithJsonType();
        return postForEntity(restTemplate, url, req, headers, responseType);
    }

    public static <REQ> String postForString(RestTemplate restTemplate, String url, REQ req) {
        return postForEntity(restTemplate, url, req, String.class);
    }

    public static <REQ, RES> RES getWithBody(RestTemplate restTemplate, String url, REQ req, HttpHeaders headers, Class<RES> responseType) {
        HttpEntity<REQ> httpEntity = new HttpEntity<>(req, headers);
        return restTemplate.exchange(url, HttpMethod.GET, httpEntity, responseType).getBody();
    }

    public static <REQ, RES> RES getWithBody(RestTemplate restTemplate, String url, REQ req, Class<RES> responseType) {
        HttpHeaders headers = HttpHeadersBuilder.getHttpHeadersWithJsonType();
        HttpEntity<REQ> httpEntity = new HttpEntity<>(req, headers);
        return restTemplate.exchange(url, HttpMethod.GET, httpEntity, responseType).getBody();
    }

    public static <RES> RES getForEntity(RestTemplate restTemplate, String url, HttpHeaders headers, Class<RES> responseType) {
        HttpEntity<Object> httpEntity = new HttpEntity<>(null, headers);
        return restTemplate.exchange(url, HttpMethod.GET, httpEntity, responseType).getBody();
    }

    public static <RES> RES getForEntity(RestTemplate restTemplate, String url, Class<RES> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        return getForEntity(restTemplate, url, headers, responseType);
    }

    public static String getForString(RestTemplate restTemplate, String url) {
        return getForEntity(restTemplate, url, new HttpHeaders(), String.class);
    }

    public static <REQ, RES> RES exchange(RestTemplate restTemplate, String url, HttpMethod method, REQ req, HttpHeaders headers, Class<RES> responseType) {
        HttpEntity<REQ> httpEntity = new HttpEntity<>(req, headers);
        return restTemplate.exchange(url, method, httpEntity, responseType).getBody();
    }

    public static <REQ, RES> RES exchange(RestTemplate restTemplate, String url, HttpMethod method, REQ req, Class<RES> responseType) {
        HttpHeaders headers = HttpHeadersBuilder.getHttpHeadersWithJsonType();
        return exchange(restTemplate, url, method, req, headers, responseType);
    }

    /*
     * ===============================
     * *****    返回带泛型的实体    *****
     * ===============================
     */
    public static <REQ, RES> RES postForEntity(RestTemplate restTemplate, String url, REQ req, HttpHeaders headers, ParameterizedTypeReference<RES> responseType) {
        HttpEntity<REQ> httpEntity = new HttpEntity<>(req, headers);
        return restTemplate.exchange(url, HttpMethod.POST, httpEntity, responseType).getBody();
    }

    public static <REQ, RES> RES postForEntity(RestTemplate restTemplate, String url, REQ req, ParameterizedTypeReference<RES> responseType) {
        HttpHeaders headers = HttpHeadersBuilder.getHttpHeadersWithJsonType();
        return postForEntity(restTemplate, url, req, headers, responseType);
    }

    public static <REQ, RES> RES getWithBody(RestTemplate restTemplate, String url, REQ req, HttpHeaders headers, ParameterizedTypeReference<RES> responseType) {
        HttpEntity<REQ> httpEntity = new HttpEntity<>(req, headers);
        return restTemplate.exchange(url, HttpMethod.GET, httpEntity, responseType).getBody();
    }

    public static <REQ, RES> RES getWithBody(RestTemplate restTemplate, String url, REQ req, ParameterizedTypeReference<RES> responseType) {
        HttpHeaders headers = HttpHeadersBuilder.getHttpHeadersWithJsonType();
        HttpEntity<REQ> httpEntity = new HttpEntity<>(req, headers);
        return restTemplate.exchange(url, HttpMethod.GET, httpEntity, responseType).getBody();
    }

    public static <RES> RES getForEntity(RestTemplate restTemplate, String url, HttpHeaders headers, ParameterizedTypeReference<RES> responseType) {
        HttpEntity<Object> httpEntity = new HttpEntity<>(null, headers);
        return restTemplate.exchange(url, HttpMethod.GET, httpEntity, responseType).getBody();
    }

    public static <RES> RES getForEntity(RestTemplate restTemplate, String url, ParameterizedTypeReference<RES> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        return getForEntity(restTemplate, url, headers, responseType);
    }

    public static <REQ, RES> RES exchange(RestTemplate restTemplate, String url, HttpMethod method, REQ req, HttpHeaders headers, ParameterizedTypeReference<RES> responseType) {
        HttpEntity<REQ> httpEntity = new HttpEntity<>(req, headers);
        return restTemplate.exchange(url, method, httpEntity, responseType).getBody();
    }

    public static <REQ, RES> RES exchange(RestTemplate restTemplate, String url, HttpMethod method, REQ req, ParameterizedTypeReference<RES> responseType) {
        HttpHeaders headers = HttpHeadersBuilder.getHttpHeadersWithJsonType();
        return exchange(restTemplate, url, method, req, headers, responseType);
    }


}
