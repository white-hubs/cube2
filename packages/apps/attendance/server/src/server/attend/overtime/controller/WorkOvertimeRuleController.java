package com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.controller;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.param.WorkOvertimeRuleQueryParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.param.query.WorkOvertimePeriodQueryParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.result.WorkOvertimeRuleVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.server.impl.WorkOvertimeRuleCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.server.impl.WorkOvertimeRuleQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.FormFieldVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.command.SubmitWorkOvertimeRuleCommand;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorkOvertimeRuleController {

    private final WorkOvertimeRuleCommandService commandService;
    private final WorkOvertimeRuleQueryService queryService;

    @GetMapping("/work-overtime-rule/{id}")
    public WorkOvertimeRuleVO getById(@PathVariable("id") Long id) {
        return queryService.getById(id);
    }

    @GetMapping("/work-overtime-rule/list")
    public List<WorkOvertimeRuleVO> list(WorkOvertimeRuleQueryParam param) {
        return queryService.getList(param);
    }

    @GetMapping("/work-overtime-rule/page")
    public Pagination<WorkOvertimeRuleVO> getPage(WorkOvertimeRuleQueryParam param) {
        return queryService.getPage(param);
    }

    @PostMapping("/work-overtime-rule")
    public Long saveOrUpdate(@Valid @RequestBody SubmitWorkOvertimeRuleCommand command) {
        return commandService.saveOrUpdate(command);
    }

    @DeleteMapping("/work-overtime-rule/{id}")
    public void delete(@PathVariable("id") Long id) {
        commandService.delete(id);
    }

    @GetMapping("/work/overtime/duration")
    public FormFieldVO getWorkOvertimeDuration(@Valid WorkOvertimePeriodQueryParam query) {
        return queryService.getWorkOvertimeDuration(query);
    }

    /**
     * 获取加班补偿
     */
    @GetMapping("/work/overtime/compensation")
    public List<FormFieldVO> getWorkOvertimeDuration(String processInstanceId, String workOvertimeType) {
        return queryService.getWorkOvertimeDuration(processInstanceId, workOvertimeType);
    }
}
