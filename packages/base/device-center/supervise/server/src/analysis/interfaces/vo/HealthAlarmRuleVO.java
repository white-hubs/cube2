package com.guhecloud.cube.srv.gen.device.supervise.server.analysis.interfaces.vo;

import com.guhecloud.apps.linkage.metadata.dto.LinkageActionDTO;
import lombok.Data;

import java.util.List;

/**
 * @author pengjian
 * @date 2022/8/5 11:24
 * @description
 */
@Data
public class HealthAlarmRuleVO {
    private Long id;
    private String name;
    private Integer thresholdValue;
    private Boolean enable;
    private Long levelId;
    private String levelName;
    private Long triggeredRuleId;
    private String alarmTypeName;
    private Long alarmTypeId;
    private List<LinkageActionDTO> ruleBasicDTOS;
}
