package com.guhecloud.cube.biz.emergency.client.vo;

import com.guhecloud.apps.linkage.metadata.dto.LinkageActionDTO;
import lombok.Data;

import java.util.List;

@Data
public class EmergencyRuleDetailVO {

    private Long id;

    private String name;

    private Long alarmLevelId;

    private String alarmLevelName;

    private Long emergencyEventTypeId;

    private String emergencyEventTypeCode;

    private String emergencyEventTypeName;

    private Long emergencyTypeId;

    private String emergencyTypeName;

    private Boolean enabled;

    private Integer repeatTime;

    private String repeatTimeUnit;

    private Integer estimatedHandlerTime;

    private String estimatedHandlerUnit;

    private String applicationCode;

    private String applicationName;

    private Boolean notifyCommandDispatch;

    private Boolean turnWarningSound;

    private Integer tenant;

    /**
     * 预案规则条件组。
     */
    private List<EmergencyRuleConditionGroupVO> emergencyRuleConditionGroups;

    /**
     * 预案规则条件设备列表。
     */
    private List<EmergencyRuleConditionDeviceCollectionVO> emergencyRuleConditionDevices;

    /**
     * 预案规则动作列表
     */
    private List<LinkageActionDTO> emergencyActions;
}
