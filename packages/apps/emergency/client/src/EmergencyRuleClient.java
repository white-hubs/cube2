package com.guhecloud.cube.biz.emergency.client;

import com.guhecloud.cube.biz.emergency.client.param.EmergencyRuleApplicationQuery;
import com.guhecloud.cube.biz.emergency.client.param.EmergencyRuleSafetyReportVO;
import com.guhecloud.cube.biz.emergency.client.vo.EmergencyRuleDetailVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("emergencyManagement")
public interface EmergencyRuleClient {

    /**
     * 根据应用和报警事件获取预案规则
     *
     * @param emergencyRuleApplicationQuery
     *
     * @return
     */
    @PostMapping("/emergency-rule/safety-report")
    List<EmergencyRuleSafetyReportVO> getEmergencyRuleByAlarmAndApplication(
            @RequestBody EmergencyRuleApplicationQuery emergencyRuleApplicationQuery
                                                                           );

    /**
     * 预案规则详情
     *
     * @param emergencyRuleId 预案规则主键
     *
     * @return 预案规则详情
     */
    @GetMapping("/emergency-rule/{emergencyRuleId}")
    EmergencyRuleDetailVO detail(@PathVariable("emergencyRuleId") Long emergencyRuleId);
}
