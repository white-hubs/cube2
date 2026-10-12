package com.guhecloud.cube.biz.emergency.server.vo;

import lombok.Data;

@Data
public class EmergencyFileTypeVO {

    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 备注
     */
    private String remark;
}
