package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.application.commandgateway.AttClockRuleCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.application.querygateway.AttClockRuleQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param.AttClockRulesPageParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param.AttClockRulesParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result.AttClockRuleDetailResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result.AttClockRuleListResult;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 打卡规则入口
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttClockRuleController {
    private final AttClockRuleCommandService attClockRuleCommandService;
    private final AttClockRuleQueryService attClockRuleQueryService;

    /**
     * 新增打卡规则
     *
     * @param param 打卡规则
     *
     * @return id
     */
    @PostMapping("/att/clock/rule/add")
    public Long addAttClockRule(@RequestBody @Valid AttClockRulesParam param) {
        return attClockRuleCommandService.addAttClockRules(param);
    }

    /**
     * 编辑打卡规则
     *
     * @param param 打卡规则
     *
     * @return id
     */
    @PostMapping("/att/clock/rule/edit")
    public Long editAttClockRule(@RequestBody @Valid AttClockRulesParam param) {
        return attClockRuleCommandService.editAttClockRules(param);
    }

    /**
     * 删除打卡规则
     *
     * @param id 主键
     *
     * @return id
     */
    @DeleteMapping("/att/clock/rule/remove")
    public Long removeAttClockRule(Long id) {
        return attClockRuleCommandService.removeAttClockRules(id);
    }

    /**
     * 分页查询打卡规则
     *
     * @param param 条件
     *
     * @return 列表
     */
    @GetMapping("/att/clock/rule/page")
    public Pagination<AttClockRuleListResult> pageAttClockRule(AttClockRulesPageParam param) {
        return attClockRuleQueryService.pageAttClockRules(param);
    }

    /**
     * 详情
     *
     * @param id 主键
     *
     * @return 规则详情
     */
    @GetMapping("/att/clock/rule/detail")
    public AttClockRuleDetailResult infoClockRule(Long id) {
        return attClockRuleQueryService.infoClockRule(id);
    }

}
