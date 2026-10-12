package com.guhecloud.cube.base.cloud.qr.code.sdk.model;

import lombok.Data;

import java.util.List;

/**
 * 分页结果
 *
 * @author system
 */
@Data
public class PageResult<T> {

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 当前页码
     */
    private Integer pageNum;

    /**
     * 每页大小
     */
    private Integer pageSize;

    /**
     * 数据列表
     */
    private List<T> records;

    /**
     * 总页数
     */
    public Integer getTotalPages() {
        if (total == null || pageSize == null || pageSize == 0) {
            return 0;
        }
        return (int) ((total + pageSize - 1) / pageSize);
    }
}
