package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.querygateway;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.dto.AttAskLeaveFromDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces.param.AttCalcLeaveDurationParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.repository.AttApproveLogRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.AttConstants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.AttHolidayDomain;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.enums.AttAskLeaveCalcWayEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.mapper.AttHolidayBalanceMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.mapper.AttHolidayRuleMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.po.AttHolidayBalancePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.po.AttHolidayRulePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.application.querygateway.AttSchedulingQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.ClassesStatusTriggerEnum;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ResourceConflictException;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 请假申请查询服务
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttAskLeaveQueryService {

    private final AttHolidayRuleMapper attHolidayRuleMapper;
    private final AttHolidayBalanceMapper attHolidayBalanceMapper;
    private final AttSchedulingQueryService attSchedulingQueryService;
    private final AttApproveLogRepository approveLogRepository;

    /**
     * 请假校验
     *
     * @return 是否通过
     */
    public AttHolidayRulePO askLeaveCheck(AttAskLeaveFromDTO param) {
        if (param == null) {
            throw new ServiceException("参数不能为空！");
        }
        param.checkNull();
        AttHolidayRulePO holidayRule = attHolidayRuleMapper.selectById(param.getHolidayId());
        Assert.isNotFound(holidayRule, "假期类型不存在！");
        // 是否有此假期
        AttHolidayBalancePO holidayBalance = attHolidayBalanceMapper.selectOne(Wrappers.<AttHolidayBalancePO>lambdaQuery()
                                                                                       .eq(AttHolidayBalancePO::getStaffId, param.getApplicantId())
                                                                                       .eq(AttHolidayBalancePO::getHolidayRuleId, param.getHolidayId())
                                                                                       .last("limit 1")
                                                                              );
        Assert.isNotAcceptable(holidayBalance == null, "无此假期类型！");
        // 最小请假单位
        Assert.isOutOfRange(
                param.getLeaveNumber() < holidayRule.getMinAskUnit(), "时长不足，小于最小请假单位" + holidayRule.getMinAskUnit() + "小时");
        // 校验是否已经请过该类型的假期
        List<String> statusList = new ArrayList<>();
        statusList.add(FlowResult.ACTIVE.name());
        statusList.add(FlowResult.PASS.name());
        statusList.add("PROGRESS");
        Period period = approveLogRepository.existApproveLog(param.getApplicantId(), param.getLeaveStartTime(), param.getLeaveEndTime(), ClassesStatusTriggerEnum.LEAVE.name(), String.valueOf(holidayRule.getId()), statusList);
        if (period != null) {
            throw new ResourceConflictException(
                    "该时段已有审批,请确认!(" + holidayRule.getName() + ":" + DateUtils.format(period.getBegin()) + "-" +
                    DateUtils.format(period.getEnd()) + ")");
        }

        // 不限额直接返回
        if (Boolean.TRUE.equals(holidayRule.getNotLimit())) {
            return holidayRule;
        }
        // 余额是否足够扣减
        Assert.isOutOfRange(holidayBalance.getAvailableBalance() <
                            AttHolidayDomain.unitAndNumToLong(param.getLeaveNumber(), AttConstants.HOUR, holidayRule.getDayOfHour()), "余额不足");
        return holidayRule;
    }

    /**
     * 请假时间计算
     *
     * @param param 请假入参
     *
     * @return 请假时间
     */
    public String calcAskLeaveDuration(AttCalcLeaveDurationParam param) {
        Assert.isParameterError(param.getPeriod().getEnd().isBefore(param.getPeriod().getBegin()), "开始时间必须小于结束时间！");
        AttHolidayRulePO holidayRule = attHolidayRuleMapper.selectById(param.getHolidayRuleId());
        Assert.isNotFound(holidayRule, "假期类型不存在，请确认是否被删除！");
        long duration = AttConstants.zeroLong;

        // 按工作日计算
        if (AttAskLeaveCalcWayEnum.WORKING_DAY.name().equals(holidayRule.getCalculationWay())) {
            duration = attSchedulingQueryService.getStaffWorkTime(param.getStaffId(), param.getPeriod());
        } else {
            LocalDateTime startTime = DateUtils.newLocalDateTime(param.getPeriod().getBegin());
            LocalDateTime dayEndTime = DateUtils.endOfDay(param.getPeriod().getBegin());
            do {
                LocalDateTime endTime =
                        param.getPeriod().getEnd().isAfter(dayEndTime) ? dayEndTime
                                                                       : DateUtils.newLocalDateTime(param.getPeriod().getEnd());
                int dayOfHour = holidayRule.getDayOfHour() == null ? AttConstants.dayOfHour : holidayRule.getDayOfHour();
                long dayLong = Math.min((long) dayOfHour * 60 * 60 * 1000, DateUtils.betweenMs(startTime, endTime));
                duration += dayLong;
                dayEndTime = DateUtils.offsetDay(dayEndTime, AttConstants.oneInt);
                startTime = DateUtils.beginOfDay(dayEndTime);
            } while (param.getPeriod().getEnd().isAfter(startTime));
        }

        return AttHolidayDomain.longToUnit(duration, AttConstants.HOUR, holidayRule.getDayOfHour(), false);
    }

}
