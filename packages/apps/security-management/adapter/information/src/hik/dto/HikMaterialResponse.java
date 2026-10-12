package com.guhecloud.cube.srv.gen.adapter.information.server.hik.dto;

import lombok.Data;

import java.util.List;

@Data
public class HikMaterialResponse {
    private Integer total;
    /**
     * 素材列表
     */
    private List<HikMaterialDto> list;
}
