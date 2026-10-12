package com.guhecloud.platform.database.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.util.ObjectUtils;

import java.util.List;

/**
 * 分页工具类
 *
 * @author Woody
 * @date 2021/7/8
 */
public final class PagingUtils {
    private PagingUtils() {
    }

    /**
     * 将分页请求参数转换成MyBatis Plus分页参数
     *
     * @param paging 分页请求参数
     * @param <T>    目标对象类型泛型
     *
     * @return MyBatis Plus分页参数
     */
    public static <T> Page<T> convert(Paging paging) {
        return paging == null ? null : new Page<>(paging.getPageNumber(), paging.getPageSize());
    }

    /**
     * 将MyBatis Plus分页参数转换成分页返回参数
     *
     * @param page MyBatis Plus分页参数
     * @param <T>  目标对象类型泛型
     *
     * @return 分页返回参数
     */
    public static <T> Pagination<T> convert(Page<T> page) {
        return convert(page, (List<T>) ObjectUtils.ifNull(page, Page::getRecords));
    }

    /**
     * 将MyBatis Plus分页参数转换成分页返回参数
     *
     * @param page MyBatis Plus分页参数
     * @param type 目标类型
     * @param <T>  目标对象类型泛型
     *
     * @return 分页返回参数
     */
    public static <T> Pagination<T> convert(Page<?> page, Class<T> type) {
        return convert(page, ObjectUtils.copying(ObjectUtils.ifNull(page, Page::getRecords), type));
    }

    /**
     * 将MyBatis Plus分页参数转换成分页返回参数
     *
     * @param page    MyBatis Plus分页参数
     * @param records 分页数据列表
     * @param <T>     目标对象类型泛型
     *
     * @return 分页返回参数
     */
    public static <T> Pagination<T> convert(Page<?> page, List<T> records) {
        return page == null ? null : Pagination.<T>builder().total(page.getTotal()).pages(page.getPages())
                .records(records).build();
    }

    /**
     * 分页记录转换
     *
     * @param page    原始分页信息
     * @param records 分页数据列表
     * @param <T>     目标对象类型泛型
     *
     * @return 分页返回参数
     */
    public static <T> Pagination<T> convert(Pagination<?> page, List<T> records) {
        return page == null ? null : Pagination.<T>builder().total(page.getTotal()).pages(page.getPages())
                .records(records).build();
    }
}
