package com.guhecloud.cube.srv.gen.device.supervise.server.analysis.application.param;

import com.guhecloud.cube.biz.linkage.client.param.LinkageActionModifyParam;
import lombok.Data;

import jakarta.validation.Valid;

import java.util.List;

/**
 * @author pengjian
 * @date 2022/8/5 11:28
 * @description
 */
@Data
public class ConfigAlarmRuleParam {
    private Long id;
    private Integer thresholdValue;
    private Long levelId;
    private String levelName;
    private Long alarmTypeId = 1534825245150576642L;
    private Boolean enable;

    /**
     * 规则联动动作配置
     */
    @Valid
    private List<LinkageActionModifyParam> linkageEmergencyActionModifies;
}
