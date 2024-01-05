package cn.slibs.test;

import cn.slibs.spring.page.v3.Page;
import cn.slibs.spring.utils.PageUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author GG
 * @version 1.0
 * @date 2024/1/5 8:08
 */
public class PageTest {
    @Test
    void testToPage() {
        List<String> datas = new ArrayList<>();
        datas.add("a");
        datas.add("b");
        // Page<Object> pageV2 = PageUtils.toPageV2(null);
        Page<String> pageV3 = PageUtils.toPageV3(datas);
        System.out.println(pageV3);
    }
}
