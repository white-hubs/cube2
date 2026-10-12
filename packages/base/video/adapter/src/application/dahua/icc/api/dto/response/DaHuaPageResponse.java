package com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.dto.response;

import lombok.Data;

import java.util.List;

/**
 * 分页数据
 */
@Data
public class DaHuaPageResponse<T> {

    /**
     * 当前页码
     */
    private int currentPage;
    /**
     * 总页数
     */
    private int totalPage;
    /**
     * 每页记录数
     */
    private int pageSize;
    /**
     * 总数
     */
    private int totalRows;
    /**
     * 数据列表
     */
    private List<T> pageData;
}
