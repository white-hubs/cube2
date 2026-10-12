package com.guhecloud.cube.biz.emergency.server.param;

import lombok.Data;

/**
 * @author Appet
 * @Description 应急组织管理
 * @date 2024-01-22 10:08:36
 */
@Data
public class EmergencyOrganizationParam {

    //名称
    private String name;
    //父级ID
    private Long parentId;
    //等级
    private Integer level;

}