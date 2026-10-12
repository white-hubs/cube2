package com.guhecloud.cube.biz.linkage.client;

import com.guhecloud.apps.linkage.metadata.dto.LinkageActionBeforeDTO;
import com.guhecloud.apps.linkage.metadata.dto.LinkageActionDTO;
import com.guhecloud.cube.biz.linkage.client.param.LinkageModifyParam;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Set;

@FeignClient(name = "linkage")
public interface LinkageClient {

    @PostMapping("/linkage")
    Long saveRuleLinkage(@Valid @RequestBody LinkageModifyParam param);

    @PutMapping("/linkage")
    void updateLinkage(@Valid @RequestBody LinkageModifyParam param);

    @DeleteMapping("/linkage/remove/by-rule/{ruleIds}")
    void removeByRuleIds(@PathVariable("ruleIds") Set<Long> ruleIds);

    /**
     * 获取规则动作列表
     *
     * @param ruleId 规则id
     *
     * @return 动作列表
     */
    @GetMapping("/linkage-rule/{ruleId}/actions")
    List<LinkageActionDTO> getLinkageActions(@PathVariable("ruleId") Long ruleId);

    /**
     * 获取规则动作前置配置
     *
     * @param ruleId 规则id
     *
     * @return 动作前置配置列表
     */
    @GetMapping("/linkage-rule/{ruleId}/action-before/list")
    List<LinkageActionBeforeDTO> getLinkageActionBeforeList(@PathVariable("ruleId") Long ruleId);
}
