package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.list;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class AreaInfoVO {
    /**
     * Id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    /**
     * 名称
     */
    private String areaName;
    /**
     * 详情
     */
    private String areaDetail;
}