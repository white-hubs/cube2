package com.guhecloud.cube.base.cloud.qr.code.sdk.model;

import lombok.Data;

/**
 * 分页查询请求
 *
 * @author system
 */
@Data
public class PageQueryRequest {

    /**
     * 页码，从1开始
     */
    private Integer pageNum = 1;

    /**
     * 每页大小
     */
    private Integer pageSize = 10;

    /**
     * 关键字搜索
     */
    private String keyword;
}
