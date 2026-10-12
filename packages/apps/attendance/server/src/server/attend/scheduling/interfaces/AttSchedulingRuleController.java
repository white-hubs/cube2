package com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.AttConstants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.AttModuleConfigCheck;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.application.commandgateway.AttSchedulingRuleCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.application.querygateway.AttSchedulingRuleQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.domain.valueobject.AttSchedulingWaysEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.param.AttSchedulingRulePageParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.param.AttSchedulingRuleParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.result.AttSchedulingRuleDetailResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.result.AttSchedulingRuleResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.utils.AttLegalHolidayUtils;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.ServiceException;
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

import java.util.List;
import java.util.Map;

/**
 * 排班规则入口
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttSchedulingRuleController {

    private final AttSchedulingRuleCommandService attSchedulingRuleCommandService;
    private final AttSchedulingRuleQueryService attSchedulingRuleQueryService;
    private final AttModuleConfigCheck attModuleConfigCheck;

    /**
     * 新增排班规则
     *
     * @param param 规则入参
     *
     * @return id
     */
    @PostMapping("/att/scheduling/rule/add")
    public Long addSchedulingRule(@RequestBody AttSchedulingRuleParam param) {
        this.checkArrangeModule();
        return attSchedulingRuleCommandService.addSchedulingRule(param);
    }

    /**
     * 编辑排班规则
     *
     * @param param 规则入参
     *
     * @return id
     */
    @PostMapping("/att/scheduling/rule/edit")
    public Long editSchedulingRule(@RequestBody @Valid AttSchedulingRuleParam param) {
        this.checkArrangeModule();
        return attSchedulingRuleCommandService.editSchedulingRule(param);
    }

    /**
     * 删除排班规则
     *
     * @param id 主键
     *
     * @return id
     */
    @DeleteMapping("/att/scheduling/rule/remove")
    public Long removeSchedulingRule(Long id) {
        this.checkArrangeModule();
        return attSchedulingRuleCommandService.removerSchedulingRule(id);
    }

    /**
     * 排班规则分页列表
     *
     * @param param 条件对象
     *
     * @return 分页列表
     */
    @GetMapping("/att/scheduling/rule/page")
    public Pagination<AttSchedulingRuleResult> pageSchedulingRule(AttSchedulingRulePageParam param) {
        this.checkArrangeModule();
        return attSchedulingRuleQueryService.pageSchedulingRule(param);
    }

    /**
     * 排班规则详情
     *
     * @param id 规则id
     *
     * @return 详情
     */
    @GetMapping("/att/scheduling/rule/detail")
    public AttSchedulingRuleDetailResult infoSchedulingRule(Long id) {
        this.checkArrangeModule();
        return attSchedulingRuleQueryService.infoSchedulingRule(id);
    }

    /**
     * 前端获取法定节假日
     *
     * @param year  年 2023
     * @param month 月 9
     *
     * @return 日历
     */
    @GetMapping("/att/scheduling/rule/legal/holidays")
    public Object get(@RequestParam("year") String year, @RequestParam("month") String month) {
        return AttLegalHolidayUtils.getWebMap(year, month);
    }

    /**
     * 获取排班方式枚举
     *
     * @return 枚举内容
     */
    @GetMapping("/att/scheduling/ways/enums")
    public List<Map<String, String>> getSchedulingWaysList() {
        this.checkArrangeModule();
        return AttSchedulingWaysEnum.getList();
    }

    /**
     * 校验是否有排班模块
     */
    private void checkArrangeModule() {
        if (!attModuleConfigCheck.checkModuleHave(AttConstants.arrangeCode)) {
            throw new ServiceException("无此模块");
        }
    }

}
