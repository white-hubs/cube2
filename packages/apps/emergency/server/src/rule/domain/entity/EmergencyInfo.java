package com.guhecloud.cube.biz.emergency.server.rule.domain.entity;

import lombok.Getter;

/**
 * 预案信息
 *
 * @author lanchun
 * @date 2022/9/3 11:16
 */
@Getter
public class EmergencyInfo {

    /**
     * 名称
     */
    private String name;

    /**
     * 预案级别主键
     */
    private Long alarmLevelId;

    /**
     * 备注信息
     */
    private String remark;

    public EmergencyInfo(String name, Long alarmLevelId, String remark) {
        this.name = name;
        this.alarmLevelId = alarmLevelId;
        this.remark = remark;
    }

    public void changeRemark(String remark) {
        this.remark = remark;
    }
}
