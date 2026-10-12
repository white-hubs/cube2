package com.guhecloud.cube.base.device.center.core.controller;

import com.guhecloud.cube.base.device.center.core.model.BizTagRule;
import com.guhecloud.cube.base.device.center.core.param.CreateOrUpdateTagRuleParam;
import com.guhecloud.cube.base.device.center.core.rule.tag.event.TagRuleChangeEvent;
import com.guhecloud.cube.base.device.center.core.service.CenterBizTagRuleService;
import com.guhecloud.cube.base.device.center.metadata.vo.DeviceTagRuleVO;
import com.guhecloud.platform.base.util.ObjectUtils;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
public class CenterDeviceTagRuleController {

    @Resource
    private CenterBizTagRuleService centerBizTagRuleService;

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * 保存标签规则
     *
     * @param param com.guhecloud.cube.base.device.center.core.param.CreateOrUpdateTagRuleParam
     *
     * @return 记录id
     */
    @PostMapping("/tag/rule")
    public Long save(@RequestBody @Valid CreateOrUpdateTagRuleParam param) {
        Long id = this.centerBizTagRuleService.saveOrUpdateTagRule(param);
        applicationEventPublisher.publishEvent(new TagRuleChangeEvent(this, param.getCode()));
        return id;
    }

    /**
     * 获取标签规则
     *
     * @param code 标签编号
     *
     * @return com.guhecloud.cube.base.device.center.metadata.vo.DeviceTagRuleVO
     */
    @GetMapping("/tag/rule/{code}")
    public DeviceTagRuleVO getTagRule(@PathVariable("code") String code) {
        Optional<BizTagRule> bizTagRuleOptional = centerBizTagRuleService.getTagRule(code);
        return bizTagRuleOptional.map(bizTagRule -> ObjectUtils.copying(bizTagRule, DeviceTagRuleVO.class)).orElse(null);
    }
}
