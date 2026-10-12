package com.guhecloud.platform.base.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 分页信息
 *
 * @author Woody
 * @date 2021/5/27
 */
@Getter
@ToString
@SuperBuilder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Pagination<T> implements Serializable {
    /**
     * 空分页信息
     */
    private static final Pagination<?> EMPTY_PAGINATION = Pagination.builder().build();

    /**
     * 总数量
     */
    @Builder.Default
    private final Long total = 0L;

    /**
     * 总页数
     */
    @Builder.Default
    private final Long pages = 0L;

    /**
     * 当页数据列表
     */
    @Builder.Default
    private final List<T> records = Collections.emptyList();

    /**
     * 获取空分页信息
     *
     * @param <T> 分页对象类型
     *
     * @return 分页信息
     */
    @SuppressWarnings("unchecked")
    public static <T> Pagination<T> empty() {
        return (Pagination<T>) EMPTY_PAGINATION;
    }
}
