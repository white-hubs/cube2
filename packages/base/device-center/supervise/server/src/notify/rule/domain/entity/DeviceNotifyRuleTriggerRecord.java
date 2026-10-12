package com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DeviceNotifyRuleTriggerRecord {

    private Long id;
    private Long ruleId;
    private Long deviceId;
    private String deviceCode;
    private String productCategoryName;
    private String prodName;
    private String deviceName;
    private String triggerContent;
    private LocalDateTime triggerTime;
    private Boolean readStatus;
}