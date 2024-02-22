package cn.slibs.spring.utils;

import com.github.pagehelper.PageInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页工具类
 *
 * @since 0.0.1
 */
public class PageUtils {
    /**
     * 将数据转为 <b>swagger-v2</b> 的分页类
     *
     * @param data 数据
     * @param <T>  数据类型
     * @return <b>swagger-v2</b> 的分页类
     */
    public static <T> cn.slibs.base.page.v2.Page<T> toPageV2(List<T> data) {
        if (data == null) data = new ArrayList<>();

        PageInfo<T> pageInfo = new PageInfo<>(data);
        return new cn.slibs.base.page.v2.Page<T>(pageInfo.getPageSize(), pageInfo.getPageNum(), pageInfo.getTotal(), pageInfo.getPages(), data);
    }

    /**
     * 将数据转为 <b>swagger-v3</b> 的分页类
     *
     * @param data 数据
     * @param <T>  数据类型
     * @return <b>swagger-v3</b> 的分页类
     */
    public static <T> cn.slibs.base.page.v3.Page<T> toPage(List<T> data) {
        return toPageV3(data);
    }

    /**
     * 将数据转为 <b>swagger-v3</b> 的分页类
     *
     * @param data 数据
     * @param <T>  数据类型
     * @return <b>swagger-v3</b> 的分页类
     */
    public static <T> cn.slibs.base.page.v3.Page<T> toPageV3(List<T> data) {
        if (data == null) data = new ArrayList<>();

        PageInfo<T> pageInfo = new PageInfo<>(data);
        return new cn.slibs.base.page.v3.Page<T>(pageInfo.getPageSize(), pageInfo.getPageNum(), pageInfo.getTotal(), pageInfo.getPages(), data);
    }

}
