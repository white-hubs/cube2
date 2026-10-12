package com.guhecloud.cube.srv.gen.attendance.management.server.leave.interfaces;

import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.FormFieldVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.tool.FormNumberCreater;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.querygateway.HolidayQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.Holiday;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.entity.LeaveRule;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.application.commandgateway.LeaveFormCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.application.commandgateway.command.SubmitLeaveFormCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.application.querygateway.LeaveTimeConvertQuery;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.domain.domainservice.LeaveFormDomainService;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.domain.domainservice.LeaveTimeDomainService;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.domain.entity.LeaveFormDetail;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.interfaces.dto.LeaveFormDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.interfaces.dto.PreSubmitData;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.interfaces.vo.LeaveTimeVO;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LeaveFormController {
    private final LeaveFormCommandService leaveFormCommandService;
    private final FormNumberCreater formNumberCreater;

    private final HolidayQueryService holidayQueryService;

    /**
     * 预提交
     */
    @PostMapping("/leave/form/pre/submit")
    public Long preSubmit(@RequestBody PreSubmitData preSubmitData) {
        if (StringUtils.isEmpty(preSubmitData.getData())) {
            throw new ServiceException("数据解析错误");
        }
        LeaveFormDTO leaveFormDTO = JSONObject.parseObject(preSubmitData.getData(), LeaveFormDTO.class);
        LeaveFormDetail.LeaveTime leaveTime = leaveFormDTO.getLeaveTime();
        SubmitLeaveFormCommand submitLeaveFormCommand = SubmitLeaveFormCommand.builder()
                .submitTime(LocalDateTime.now())
                .leaveType(leaveFormDTO.getHolidayType())
                .name("请假单")
                .leaveTime(leaveTime)
                .submitUserId(leaveFormDTO.getEmployeeId())
                .location(leaveFormDTO.getLeaveLocation())
                .build();
        Long leaveId = leaveFormCommandService.trySubmit(submitLeaveFormCommand);
        log.info("收到预提交");
        return leaveId;
    }

    /**
     *
     */
    @GetMapping("/leave/form/field")
    public List<FormFieldVO> getAbleFormFields() {
        List<FormFieldVO> result = new ArrayList<>();
        result.add(FormFieldVO.builder().id("employeeId").name("员工id").build());
        result.add(FormFieldVO.builder().id("departmentId").name("部门id").build());
        result.add(FormFieldVO.builder().id("positionId").name("岗位id").build());
        result.add(FormFieldVO.builder().id("holidayType").name("请假类型").build());
        result.add(FormFieldVO.builder().id("leaveTime").name("请假时间").build());
        return result;
    }

    @PostMapping("/leave/form/number")
    public String generateLeaveFormNumber() {
        return formNumberCreater.create("QJ");
    }

    /**
     * 假期时长计算
     *
     * @return 返回换算后的时长
     */
    @PostMapping("/leave/holidayType/duration")
    public LeaveTimeVO getLeaveTimeConvert(@RequestBody LeaveTimeConvertQuery leaveTimeConvertQuery) {
        Holiday holiday = holidayQueryService.getByType(leaveTimeConvertQuery.getHolidayType());
        // 计算时长
        Float hour = LeaveFormDomainService.computeLeaveTime(
                SecurityContextHolder.getPrincipal().getId(),
                LeaveTimeDomainService.getLeaveTime(leaveTimeConvertQuery.getLeaveTimeStr()),
                holiday.getLeaveRule()
                                                            );
        LeaveRule.MinimumLeaveTimeUnit unit = LeaveRule.MinimumLeaveTimeUnit.HOUR;
        if (holiday.getLeaveRule().getMinimumLeaveTimeUnitConvert().getMinimumLeaveTimeUnit() == LeaveRule.MinimumLeaveTimeUnit.DAY
            ||
            holiday.getLeaveRule().getMinimumLeaveTimeUnitConvert().getMinimumLeaveTimeUnit() == LeaveRule.MinimumLeaveTimeUnit.HALF_DAY) {
            // 小数1位
            BigDecimal b = BigDecimal.valueOf(hour / 8);
            unit = LeaveRule.MinimumLeaveTimeUnit.DAY;
            hour = b.setScale(1, RoundingMode.HALF_UP).floatValue();
        }

        return new LeaveTimeVO(hour.toString(), unit.name());
    }
}
