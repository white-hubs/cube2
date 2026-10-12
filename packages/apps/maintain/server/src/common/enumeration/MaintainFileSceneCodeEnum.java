package com.guhecloud.cube.srv.gen.maintain.server.common.enumeration;

import lombok.Getter;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/1/22 11:15
 * @Description: maintain文件服务场景编码定义
 */
@Getter
public enum MaintainFileSceneCodeEnum {
    ELECT_PATROL_SUBMIT("ELECT_PATROL:SUBMIT_RESULT", "电子巡更巡更结果提交"),
    INSPECTION_RECORD("INSPECTION_RECORD", "巡检记录"),
    FAULT_ORDER_CASE("FAULT_ORDER:CASE", "故障案例"),
    OUTSOURCE_REPAIR_CONFIRM("OUTSOURCE_REPAIR:CONFIRM", "委外维修确认"),
    OUTSOURCE_REPAIR_RESULT("OUTSOURCE_REPAIR:RESULT", "委外维修结果"),
    OUTSOURCE_REPAIR_ASSESSMENT("OUTSOURCE_REPAIR:ASSESSMENT", "委外维修评价"),
    OUTSOURCE_REPAIR_SUBMIT("OUTSOURCE_REPAIR:SUBMIT", "委外维修提交"),

    ;
    private final String code;
    private final String desc;

    MaintainFileSceneCodeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
