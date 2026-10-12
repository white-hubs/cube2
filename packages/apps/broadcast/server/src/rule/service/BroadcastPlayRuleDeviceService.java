package com.guhecloud.cube.biz.subsystem.broadcast.server.rule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.subsystem.broadcast.server.rule.po.BroadcastPlayRuleDevicePO;

import java.util.List;

public interface BroadcastPlayRuleDeviceService extends IService<BroadcastPlayRuleDevicePO> {

    List<BroadcastPlayRuleDevicePO> getListByRuleId(Long ruleId);

    void removeByRuleId(Long ruleId);
}
