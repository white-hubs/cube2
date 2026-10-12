package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.controller;

import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.entity.MedicalRuleNotifyConfig;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.param.MedicalRuleNotifyConfigParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.rocketmq.message.MedicalRuleNotifyConfigUpdateMessage;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.service.MedicalRuleNotifyConfigService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.vo.MedicalRuleNotifyConfigVO;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * 规则通知配置
 *
 * @author lanchun
 * @date 2024/9/14 14:35
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MedicalRuleNotifyConfigController {

    private final MedicalRuleNotifyConfigService medicalRuleNotifyConfigService;

    /**
     * 超限提醒配置保存
     *
     * @param param
     */
    @PostMapping("/rule/notify/config")
    public void save(@Valid @RequestBody MedicalRuleNotifyConfigParam param) {
        Boolean updated = this.medicalRuleNotifyConfigService.save(param);
        if (updated) {
            RocketMQContextHolder.send(MedicalRuleNotifyConfigUpdateMessage.TOPIC, MedicalRuleNotifyConfigUpdateMessage
                    .builder()
                    .timestamp(System.currentTimeMillis())
                    .build());
        }
    }

    /**
     * 超限提醒配置详情
     *
     * @return
     */
    @GetMapping("/rule/notify/config")
    public MedicalRuleNotifyConfigVO getMedicalRuleNotifyConfig() {
        MedicalRuleNotifyConfig medicalRuleNotifyConfig = this.medicalRuleNotifyConfigService.getMedicalRuleNotifyConfig();
        return ObjectUtils.copying(medicalRuleNotifyConfig, MedicalRuleNotifyConfigVO.class);
    }
}
