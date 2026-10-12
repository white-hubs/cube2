package com.guhecloud.cube.biz.linkage.server.linkage.controller;

import com.guhecloud.apps.linkage.metadata.dto.LinkageActionBeforeDTO;
import com.guhecloud.apps.linkage.metadata.dto.LinkageActionDTO;
import com.guhecloud.cube.biz.linkage.server.linkage.param.LinkageModifyParam;
import com.guhecloud.cube.biz.linkage.server.linkage.service.LinkageActionBeforeService;
import com.guhecloud.cube.biz.linkage.server.linkage.service.LinkageActionService;
import com.guhecloud.cube.biz.linkage.server.linkage.service.LinkageService;
import com.guhecloud.cube.biz.linkage.server.rule.base.entity.RuleLinkage;
import com.guhecloud.cube.biz.linkage.server.rule.base.service.RuleLinkageService;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LinkageController {

    private final LinkageService linkageService;

    private final RuleLinkageService ruleLinkageService;

    private final LinkageActionService linkageActionService;

    private final LinkageActionBeforeService linkageActionBeforeService;

    @PostMapping("/linkage")
    public Long saveLinkage(@Valid @RequestBody LinkageModifyParam param) {
        return this.linkageService.saveRuleLinkage(param);
    }

    @PutMapping("/linkage")
    public void updateLinkage(@Valid @RequestBody LinkageModifyParam param) {
        this.linkageService.updateRuleLinkage(param);
    }

    /**
     * 获取规则动作列表
     *
     * @param ruleId
     *
     * @return
     */
    @GetMapping("/linkage-rule/{ruleId}/actions")
    public List<LinkageActionDTO> getLinkageActions(@PathVariable("ruleId") Long ruleId) {
        var ruleLinkageList = this.ruleLinkageService.listByRuleId(ruleId);
        //联动主键
        var linkageIds = ruleLinkageList.stream().map(RuleLinkage::getLinkageId).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(linkageIds)) {
            return Collections.emptyList();
        }
        //查询动作
        var linkageActions = this.linkageActionService.listByLinkageIds(linkageIds);
        if (CollectionUtils.isEmpty(linkageActions)) {
            return Collections.emptyList();
        }
        var linkageActionDTOS = new ArrayList<LinkageActionDTO>(linkageActions.size());
        linkageActions.forEach(linkageAction -> {
            var linkageActionDTO = ObjectUtils.copying(linkageAction, LinkageActionDTO.class);
            if (!StringUtils.isEmpty(linkageAction.getActionConfig())) {
                linkageActionDTO.setActionConfig(JsonUtils.deserialize(linkageAction.getActionConfig(), Object.class));
            }
            linkageActionDTOS.add(linkageActionDTO);
        });
        return linkageActionDTOS;
    }

    /**
     * 获取规则动作前置配置
     *
     * @param ruleId 规则id
     *
     * @return
     */
    @GetMapping("/linkage-rule/{ruleId}/action-before/list")
    public List<LinkageActionBeforeDTO> getLinkageActionBeforeList(@PathVariable("ruleId") Long ruleId) {
        var ruleLinkageList = this.ruleLinkageService.listByRuleId(ruleId);
        var linkageIds = ruleLinkageList.stream().map(RuleLinkage::getLinkageId).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(linkageIds)) {
            return Collections.emptyList();
        }
        var linkageActionBeforeList = this.linkageActionBeforeService.listByLinkageIds(linkageIds);
        if (CollectionUtils.isEmpty(linkageActionBeforeList)) {
            return Collections.emptyList();
        }
        var linkageActionBeforeDTOS = new ArrayList<LinkageActionBeforeDTO>(linkageActionBeforeList.size());
        linkageActionBeforeList.forEach(linkageActionBefore -> {
            var linkageActionBeforeDTO = new LinkageActionBeforeDTO();
            linkageActionBeforeDTO.setId(linkageActionBefore.getId());
            linkageActionBeforeDTO.setLinkageId(linkageActionBefore.getLinkageId());
            linkageActionBeforeDTO.setActionBeforeType(linkageActionBefore.getActionBeforeType());
            linkageActionBeforeDTO.setEnabled(linkageActionBefore.getEnabled());
            if (!StringUtils.isEmpty(linkageActionBefore.getActionBeforeConfig())) {
                linkageActionBeforeDTO.setActionBeforeConfig(JsonUtils.deserialize(linkageActionBefore.getActionBeforeConfig(), Object.class));
            }
            linkageActionBeforeDTOS.add(linkageActionBeforeDTO);
        });
        return linkageActionBeforeDTOS;
    }

    @DeleteMapping("/linkage/remove/by-rule/{ruleIds}")
    public void removeByRuleIds(@PathVariable("ruleIds") Set<Long> ruleIds) {
        this.linkageService.removeByRuleIds(ruleIds);
    }
}
