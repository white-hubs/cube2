package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.param.LineLossAlarmRulePageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.param.LineLossAlarmRuleParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.service.LineLossAlarmRuleService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.vo.LineLossAlarmRulePageVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.vo.LineLossAlarmRuleVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 线损报警规则控制器
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LineLossAlarmRuleController {
    private final LineLossAlarmRuleService lineLossAlarmRuleService;

    /**
     * 分页查询
     *
     * @param param 查询参数
     *
     * @return 分页列表
     */
    /**
     * 分页查询报警规则列表
     *
     * @param param 查询参数
     *
     * @return 分页列表
     */
    @GetMapping("/lineLoss/alarm/rule/pageList")
    public Pagination<LineLossAlarmRulePageVO> pageList(LineLossAlarmRulePageParam param) {
        return lineLossAlarmRuleService.pageList(param);
    }

    /**
     * 根据 ID 查询详情
     *
     * @param id 规则 ID
     *
     * @return 规则详情
     */
    @GetMapping("/lineLoss/alarm/rule/detail")
    public LineLossAlarmRuleVO detail(@RequestParam Long id) {
        return lineLossAlarmRuleService.detail(id);
    }

    /**
     * 新增规则
     *
     * @param param 规则参数
     *
     * @return 规则 ID
     */
    @PostMapping("/lineLoss/alarm/rule/add")
    public Long addRule(@RequestBody @Valid LineLossAlarmRuleParam param) {
        return lineLossAlarmRuleService.addRule(param);
    }

    /**
     * 修改规则
     *
     * @param param 规则参数
     *
     * @return 是否成功
     */
    @PostMapping("/lineLoss/alarm/rule/edit")
    public Boolean updateRule(@RequestBody @Valid LineLossAlarmRuleParam param) {
        return lineLossAlarmRuleService.updateRule(param);
    }

    /**
     * 删除规则
     *
     * @param id 规则 ID
     *
     * @return 是否成功
     */
    @DeleteMapping("/lineLoss/alarm/rule/delete")
    public Boolean deleteRule(@RequestParam Long id) {
        return lineLossAlarmRuleService.deleteRule(id);
    }

    /**
     * 启用/禁用规则（切换状态）
     *
     * @param param 启用/禁用参数
     *
     * @return 是否成功
     */
    @PostMapping("/lineLoss/alarm/rule/enableRule")
    public Boolean enableRule(@RequestBody LineLossAlarmRulePageParam param) {
        return lineLossAlarmRuleService.enableRule(param.getId(), param.getEnabled());
    }
}
