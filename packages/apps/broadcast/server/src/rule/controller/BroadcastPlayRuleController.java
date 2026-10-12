package com.guhecloud.cube.biz.subsystem.broadcast.server.rule.controller;

import com.guhecloud.cube.biz.subsystem.broadcast.server.rule.param.BroadcastPlayRuleModifyParam;
import com.guhecloud.cube.biz.subsystem.broadcast.server.rule.param.BroadcastPlayRuleQueryParam;
import com.guhecloud.cube.biz.subsystem.broadcast.server.rule.service.BroadcastPlayRuleService;
import com.guhecloud.cube.biz.subsystem.broadcast.server.rule.vo.BroadcastPlayRulePageVO;
import com.guhecloud.cube.biz.subsystem.broadcast.server.rule.vo.BroadcastPlayRuleVO;
import com.guhecloud.platform.base.model.Pagination;
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

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BroadcastPlayRuleController {

    private final BroadcastPlayRuleService broadcastPlayRuleService;

    /**
     * 规则新增
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/broadcast-play-rule")
    public Long save(@Valid @RequestBody BroadcastPlayRuleModifyParam param) {
        return this.broadcastPlayRuleService.save(param);
    }

    /**
     * 规则修改
     *
     * @param id
     * @param param
     */
    @PutMapping("/broadcast-play-rule/{id}")
    public void update(@PathVariable("id") Long id, @Valid @RequestBody BroadcastPlayRuleModifyParam param) {
        this.broadcastPlayRuleService.update(id, param);
    }

    /**
     * 规则启用状态修改
     *
     * @param id
     * @param enabled
     */
    @PutMapping("/broadcast-play-rule/{id}/enabled/{enabled}")
    public void updateEnable(@PathVariable("id") Long id, @PathVariable("enabled") Boolean enabled) {
        this.broadcastPlayRuleService.updateEnable(id, enabled);
    }

    /**
     * 规则分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/broadcast-play-rule/page")
    public Pagination<BroadcastPlayRulePageVO> getRulePage(@Valid BroadcastPlayRuleQueryParam param) {
        return this.broadcastPlayRuleService.getPage(param);
    }

    /**
     * 规则详情
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/broadcast-play-rule/{id}")
    public BroadcastPlayRuleVO getRuleDetail(@PathVariable("id") Long id) {
        return this.broadcastPlayRuleService.getDetail(id);
    }

    /**
     * 规则删除
     *
     * @param id
     */
    @DeleteMapping("/broadcast-play-rule/{id}")
    public void removeRule(@PathVariable("id") Long id) {
        this.broadcastPlayRuleService.removeById(id);
    }
}
