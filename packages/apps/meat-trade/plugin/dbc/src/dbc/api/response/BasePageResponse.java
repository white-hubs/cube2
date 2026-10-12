package com.guhecloud.cube.sn.meattrade.plugin.dbc.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BasePageResponse<T> {

    /**
     * 总数
     */
    private Integer total;

    /**
     * 页码
     */
    private Integer pageNum;

    /**
     * 页大小
     */
    private Integer pageSize;

    /**
     * 大小
     */
    private Integer size;

    /**
     * 开始行
     */
    private Integer startRow;

    /**
     * 结束行
     */
    private Integer endRow;

    /**
     * 总页数
     */
    private Integer pages;

    /**
     * 上一页
     */
    private Integer prePage;

    /**
     * 下一页
     */
    private Integer nextPage;

    /**
     * 是否为第一页
     */
    private Boolean isFirstPage;

    /**
     * 是否为最后一页
     */
    private Boolean isLastPage;

    /**
     * 是否有上一页
     */
    private Boolean hasPreviousPage;

    /**
     * 是否有下一页
     */
    private Boolean hasNextPage;

    private Integer navigatePages;

    private List<Integer> navigatepageNums;

    private Integer navigateFirstPage;

    private Integer navigateLastPage;

    /**
     * 列表
     */
    private List<T> list;
}

