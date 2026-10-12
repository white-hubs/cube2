package com.guhecloud.cube.srv.gen.device.supervise.server.analysis.interfaces.vo;

import lombok.Data;

import java.util.Objects;

/**
 * @author pengjian
 * @date 2022/8/5 11:42
 * @description
 */
@Data
public class DeviceInstanceHealthAlarmRulePreviewVO {
    private Long id;
    private Long deviceId;
    private String name;
    private String productCategoryName;
    private String levelName;
    private Long levelId;
    private String alarmTypeName;
    private Long alarmTypeId;
    private Integer thresholdValue;
    private Boolean enable;
    private Boolean configuredAlarmRule;

    public void setId(Long id) {
        this.id = id;
        this.deviceId = id;
    }

    public Boolean getConfiguredAlarmRule() {
        if (Objects.nonNull(this.thresholdValue)) {
            return true;
        } else {
            return false;
        }
    }
}
