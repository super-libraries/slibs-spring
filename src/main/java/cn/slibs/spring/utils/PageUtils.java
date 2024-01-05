package cn.slibs.spring.utils;

import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 分页工具类
 *
 * @since 0.0.1
 */
public class PageUtils {
    public static <T> cn.slibs.spring.page.v2.Page<T> toPageV2(List<T> data) {
        PageInfo<T> pageInfo = new PageInfo<>(data);
        return new cn.slibs.spring.page.v2.Page<T>(pageInfo.getPageSize(), pageInfo.getPageNum(), pageInfo.getTotal(), pageInfo.getPages(), data);
    }

    public static <T> cn.slibs.spring.page.v3.Page<T> toPageV3(List<T> data) {
        PageInfo<T> pageInfo = new PageInfo<>(data);
        return new cn.slibs.spring.page.v3.Page<T>(pageInfo.getPageSize(), pageInfo.getPageNum(), pageInfo.getTotal(), pageInfo.getPages(), data);
    }

}
