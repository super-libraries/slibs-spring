package cn.slibs.spring.http;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate工具类
 *
 * @since 0.0.1
 */
public class RestHttpUtils {

    public static <REQ, RES> RES postForEntity(RestTemplate restTemplate, String url, REQ req, HttpHeaders headers, Class<RES> resClass) {
        HttpEntity<REQ> formEntity = new HttpEntity<>(req, headers);
        return restTemplate.postForEntity(url, formEntity, resClass).getBody();
    }

    public static <REQ, RES> RES postForEntity(RestTemplate restTemplate, String url, REQ req, Class<RES> resClass) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/json; charset=UTF-8"));
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        return postForEntity(restTemplate, url, req, headers, resClass);
    }

    public static <REQ> String postForString(RestTemplate restTemplate, String url, REQ req) {
        return postForEntity(restTemplate, url, req, String.class);
    }

    public static <RES> RES getForEntity(RestTemplate restTemplate, String url, HttpHeaders headers, Class<RES> resClass) {
        HttpEntity<Object> formEntity = new HttpEntity<>(null, headers);
        return restTemplate.exchange(url, HttpMethod.GET, formEntity, resClass).getBody();
    }

    public static <RES> RES getForEntity(RestTemplate restTemplate, String url, Class<RES> resClass) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        return getForEntity(restTemplate, url, headers, resClass);
    }

    public static String getForString(RestTemplate restTemplate, String url) {
        return getForEntity(restTemplate, url, new HttpHeaders(), String.class);
    }

    /*
     * ===============================
     * *****    返回带泛型的实体    *****
     * ===============================
     */
    public static <REQ, RES> RES postForEntity(RestTemplate restTemplate, String url, REQ req, HttpHeaders headers, ParameterizedTypeReference<RES> responseType) {
        HttpEntity<REQ> formEntity = new HttpEntity<>(req, headers);
        return restTemplate.exchange(url, HttpMethod.POST, formEntity, responseType).getBody();
    }

    public static <REQ, RES> RES postForEntity(RestTemplate restTemplate, String url, REQ req, ParameterizedTypeReference<RES> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/json; charset=UTF-8"));
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        return postForEntity(restTemplate, url, req, headers, responseType);
    }

    public static <RES> RES getForEntity(RestTemplate restTemplate, String url, HttpHeaders headers, ParameterizedTypeReference<RES> responseType) {
        HttpEntity<Object> formEntity = new HttpEntity<>(null, headers);
        return restTemplate.exchange(url, HttpMethod.GET, formEntity, responseType).getBody();
    }

    public static <RES> RES getForEntity(RestTemplate restTemplate, String url, ParameterizedTypeReference<RES> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        return getForEntity(restTemplate, url, headers, responseType);
    }

}
