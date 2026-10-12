package com.guhecloud.cube.biz.adapter.park.server.application.dahua.model;

import lombok.Data;

import java.util.List;

@Data
public class PageModel {
    private Integer currentPage;
    private Integer pageSize;
    private String totalPage;
    private String totalRows;
    private List<Object> pageData;
}
