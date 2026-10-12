package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.Data;

@Data
public class InspectionAreaVO {

    private Long Id;

    /**
     * 区域编码
     */
    private String Code;

    /**
     * 区域名称
     */
    private String Name;
}
