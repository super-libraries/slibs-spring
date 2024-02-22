package cn.slibs.test;

import cn.slibs.base.page.v3.Page;
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
        cn.slibs.base.page.v2.Page<Object> page = PageUtils.toPageV2(null);
        cn.slibs.base.page.v2.Page<Object> page1 = PageUtils.toPageV2(new ArrayList<>());
        Page<String> page2 = PageUtils.toPageV3(datas);
        Page<String> page3 = PageUtils.toPage(datas);
        System.out.println(page);
        System.out.println(page1);
        System.out.println(page2);
        System.out.println(page3);
    }
}
