package com.guhecloud.cube.biz.emergency.server.time.domain.event;

import com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject.EmergencyRuleConditionDeviceInfo;
import com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject.EmergencyRuleConditionGroupInfo;
import com.guhecloud.cube.biz.emergency.server.time.domain.po.EmergencyRuleTimeJobPO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class IssuanceIotRuleGroupEvent extends ApplicationEvent {

    private EmergencyRuleConditionGroupInfo emergencyRuleConditionGroupInfo;

    private List<EmergencyRuleConditionDeviceInfo> emergencyRuleConditionDeviceInfos;

    private String emergencyRuleName;

    private EmergencyRuleTimeJobPO emergencyRuleTimeJob;

    /**
     * 开启规则or关闭规则
     */
    private String openOrClose;

    private String op = "or";

    public IssuanceIotRuleGroupEvent(Object source,
                                     String emergencyRuleName,
                                     String openOrClose,
                                     EmergencyRuleConditionGroupInfo emergencyRuleConditionGroupInfo,
                                     List<EmergencyRuleConditionDeviceInfo> emergencyRuleConditionDeviceInfos,
                                     EmergencyRuleTimeJobPO emergencyRuleTimeJob
                                    ) {
        super(source);
        this.emergencyRuleName = emergencyRuleName;
        this.openOrClose = openOrClose;
        this.emergencyRuleConditionGroupInfo = emergencyRuleConditionGroupInfo;
        this.emergencyRuleConditionDeviceInfos = emergencyRuleConditionDeviceInfos;
        this.emergencyRuleTimeJob = emergencyRuleTimeJob;
    }
}
