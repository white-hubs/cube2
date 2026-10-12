package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DisinfectionNotificationTypeEnum {

    GROUP("小组"),
    GROUP_ROLE("小组+角色"),
    DEPT("部门"),
    ROLE("角色"),
    POST("岗位");
    private String desc;

}
