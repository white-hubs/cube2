package com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicealarm.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicealarm.entity.RuleDeviceAlarm;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicealarm.param.RuleDeviceAlarmQueryParam;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicealarm.vo.RuleDeviceAlarmPageVO;

import java.util.List;

public interface RuleDeviceAlarmService extends IService<RuleDeviceAlarm> {

    Page<RuleDeviceAlarmPageVO> page(RuleDeviceAlarmQueryParam param);

    List<RuleDeviceAlarm> listByRuleId(Long ruleId);

    void removeByRuleId(Long ruleId);
}
