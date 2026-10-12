package com.guhecloud.cube.biz.linkage.server.linkage.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.linkage.server.linkage.dao.LinkageMapper;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.Linkage;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.LinkageAction;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.LinkageActionBefore;
import com.guhecloud.cube.biz.linkage.server.linkage.param.ActionBeforeParam;
import com.guhecloud.cube.biz.linkage.server.linkage.param.LinkageActionModifyParam;
import com.guhecloud.cube.biz.linkage.server.linkage.param.LinkageModifyParam;
import com.guhecloud.cube.biz.linkage.server.linkage.service.LinkageActionBeforeService;
import com.guhecloud.cube.biz.linkage.server.linkage.service.LinkageActionService;
import com.guhecloud.cube.biz.linkage.server.linkage.service.LinkageService;
import com.guhecloud.cube.biz.linkage.server.rule.base.entity.RuleLinkage;
import com.guhecloud.cube.biz.linkage.server.rule.base.service.RuleLinkageService;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LinkageServiceImpl extends ServiceImpl<LinkageMapper, Linkage> implements LinkageService {

    private final LinkageActionService linkageActionService;

    private final LinkageActionBeforeService linkageActionBeforeService;

    private final RuleLinkageService ruleLinkageService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveRuleLinkage(@NonNull String code, @NonNull String name, List<LinkageActionModifyParam> params) {
        var linkage = new Linkage(IdWorker.getId(), code, name, true);
        var linkageActions = new ArrayList<LinkageAction>();
        //联动动作
        params.forEach(param -> {
            var linkageAction = ObjectUtils.copying(param, LinkageAction.class);
            linkageAction.setLinkageId(linkage.getId());
            linkageAction.setActionConfig(JsonUtils.serialize(param.getActionConfig()));
            linkageActions.add(linkageAction);
        });
        this.baseMapper.insert(linkage);
        this.linkageActionService.saveBatch(linkageActions);
        return linkage.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveRuleLinkage(String code, String name, List<LinkageActionModifyParam> params,
                                List<ActionBeforeParam> actionBeforeParams
                               ) {
        var linkageId = this.saveRuleLinkage(code, name, params);
        var linkageActionBeforeList = new ArrayList<LinkageActionBefore>();
        //联动动作
        actionBeforeParams.forEach(param -> {
            var actionBefore = new LinkageActionBefore();
            actionBefore.setLinkageId(linkageId);
            actionBefore.setEnabled(Objects.isNull(param.getEnabled()) ? null : param.getEnabled());
            actionBefore.setActionBeforeType(param.getActionBeforeType());
            actionBefore.setActionBeforeConfig(JsonUtils.serialize(param.getActionBeforeConfig()));
            linkageActionBeforeList.add(actionBefore);
        });
        this.linkageActionBeforeService.saveBatch(linkageActionBeforeList);
        return linkageId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveRuleLinkage(@NonNull LinkageModifyParam param) {
        var linageId = this.saveRuleLinkage(param.getRuleId().toString(),
                                            param.getRuleName(),
                                            param.getLinkageActionModifyParams(), param.getActionBeforeParams());
        ruleLinkageService.save(new RuleLinkage(IdWorker.getId(), param.getRuleId(), linageId));
        return linageId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long updateRuleLinkage(@NonNull LinkageModifyParam param) {
        this.ruleLinkageService.removeByRuleId(param.getRuleId());
        return this.saveRuleLinkage(param);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByRuleIds(@NonNull Set<Long> ruleIds) {
        for (var ruleId : ruleIds) {
            this.ruleLinkageService.removeByRuleId(ruleId);
        }
    }
}
