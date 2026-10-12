package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.dahua.api.vo;

import lombok.Data;

import java.util.List;

/**
 * 分页数据
 */
@Data
public class DaHuaPageVO {
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
    private List<DaHuaDoorEventInfoVO> pageData;
}
