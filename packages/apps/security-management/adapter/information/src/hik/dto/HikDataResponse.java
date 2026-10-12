package com.guhecloud.cube.srv.gen.adapter.information.server.hik.dto;

import lombok.Data;

import java.util.List;

@Data
public class HikDataResponse {
    private Integer total;
    private Integer pageNo;
    private Integer pageSize;
    private Long sortby;
    private Long order;
    /**
     * 节目列表
     */
    private List<HikProgramResponse> list;
}
