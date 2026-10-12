package com.guhecloud.cube.property.management.client.property.vo.assembly;

import lombok.Data;

@Data
public class PropertyCheckTaskAdminPageVO {
    private Long id;
    private String name;
    private String remark;
    private Integer members;
    /**
     * 资产管理员
     */
    private Long adminId;
    private String adminName;
}
