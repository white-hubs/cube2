package com.guhecloud.cube.biz.linkage.server.linkage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.Linkage;
import com.guhecloud.cube.biz.linkage.server.linkage.param.ActionBeforeParam;
import com.guhecloud.cube.biz.linkage.server.linkage.param.LinkageActionModifyParam;
import com.guhecloud.cube.biz.linkage.server.linkage.param.LinkageModifyParam;

import java.util.List;
import java.util.Set;

public interface LinkageService extends IService<Linkage> {

    Long saveRuleLinkage(String code, String name, List<LinkageActionModifyParam> params);

    Long saveRuleLinkage(String code, String name, List<LinkageActionModifyParam> params, List<ActionBeforeParam> actionBeforeParams);

    Long saveRuleLinkage(LinkageModifyParam param);

    Long updateRuleLinkage(LinkageModifyParam param);

    void removeByRuleIds(Set<Long> ruleIds);
}
