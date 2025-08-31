package cn.slibs.test;

import cn.slibs.spring.http.RestHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;


public class RestHelperTest {
    @Test
    void testRestHelper() {
        RestTemplate restTemplate = new RestTemplate();

        RestHelper restHelper = RestHelper.build(restTemplate);
        String html = restHelper.getForString("https://www.baidu.com");
        Document document = Jsoup.parse(html);
        System.out.println(document);

    }
}
