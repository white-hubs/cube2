package com.guhecloud.cube.base.iot.product.model.v3.util;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.database.util.PagingUtils;

/**
 * 实体构建工具类
 * 使用 Builder 模式简化实体对象的创建
 */
public class PageAdapter {

    /**
     * 转换为标准分页对象
     *
     * @param page 原始分页对象
     * @param <T>  目标对象类型泛型
     *
     * @return 标准分页对象
     */
    public static <T> Pagination<T> toStandard(IPage<T> page) {
        if (page instanceof Page<T> stdPage) {
            return PagingUtils.convert(stdPage);
        }

        throw new UnsupportedOperationException("不支持的分页类型");
    }

}
