package com.guhecloud.cube.property.management.client.property.param.assembly;

import lombok.Data;

import java.util.List;

@Data
public class PropertyCheckTaskAdminParam {
    /**
     * 部门id
     */
    private Long orgId;
    /**
     * 用户id
     */
    private List<Long> userIds;
}
