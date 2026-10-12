package com.guhecloud.cube.biz.subsystem.broadcast.server.rule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.subsystem.broadcast.server.rule.po.BroadcastPlayRuleTemplatePO;

import java.util.List;

public interface BroadcastPlayRuleTemplateService extends IService<BroadcastPlayRuleTemplatePO> {

    List<BroadcastPlayRuleTemplatePO> getListByRuleId(Long ruleId);

    void removeByRuleId(Long ruleId);
}
