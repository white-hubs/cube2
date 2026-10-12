package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.commandgateway;

import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.commandgateway.command.ChangeHolidayConfigCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.querygateway.dto.HolidayDetailDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.Holiday;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.entity.LeaveRule;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.event.ChangeExpiryDateEvent;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.repository.HolidayRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.ExpiryDate;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.FixedHolidayGrantQuota;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.HolidayGrantQuota;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.HolidayGrantRule;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.QuotaType;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.TimeOfInductionYearHolidayGrantQuota;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.WorkingYearHolidayGrantQuota;
import com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.vo.HolidayOperation;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.operationservice.HolidayOperationService;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional(rollbackFor = Exception.class)
public class HolidayCommandService {

    private final HolidayRepository holidayRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final HolidayOperationService holidayOperationService;
    private final UserClient userClient;

    /**
     * 修改假期配置规则
     */
    public void changeHolidayConfig(ChangeHolidayConfigCommand changeHolidayConfigCommand) {
        // 修改有效期规则
        Optional<Holiday> optional = holidayRepository.getById(changeHolidayConfigCommand.getId());
        if (optional.isEmpty()) {
            throw new ServiceException("假期类型不存在");
        }
        optional.ifPresent(holiday -> {
            // 修改假期有效期-发出事件
            ChangeExpiryDateEvent changeExpiryDateEvent = null;
            if (Objects.isNull(changeHolidayConfigCommand.getExpiryDateConfig())) {
                changeExpiryDateEvent = holiday.changeExpiryDate(null);
            } else {
                changeExpiryDateEvent = holiday.changeExpiryDate(ExpiryDate.builder()
                                                                         .expiryDateType(changeHolidayConfigCommand.getExpiryDateConfig()
                                                                                                 .getType())
                                                                         .timeUnit(changeHolidayConfigCommand.getExpiryDateConfig()
                                                                                           .getTimeUnit())
                                                                         .value(changeHolidayConfigCommand.getExpiryDateConfig().getValue())
                                                                         .build());

            }

            applicationEventPublisher.publishEvent(changeExpiryDateEvent);
            // 修改发放规则
            if (Objects.nonNull(changeHolidayConfigCommand.getGrantRuleConfig())) {
                HolidayDetailDTO.GrantRuleConfig grantRuleConfig = changeHolidayConfigCommand.getGrantRuleConfig();
                List<HolidayGrantQuota> grantQuotas = new ArrayList<>();
                switch (grantRuleConfig.getQuotaType()) {
                    case "FIXED":
                        grantQuotas.add(new FixedHolidayGrantQuota(
                                8.0f * changeHolidayConfigCommand.getGrantRuleConfig().getFixedQuota(), "FIXED"));
                        break;
                    case "WORKING_YEAR":
                        List<HolidayDetailDTO.QuotaThresholdConfig> workingYear = grantRuleConfig.getWorkingYear();
                        workingYear.forEach(quotaThresholdConfig -> {
                            grantQuotas.add(new WorkingYearHolidayGrantQuota(
                                    8.0f * quotaThresholdConfig.getQuota(), "WORKING_YEAR", quotaThresholdConfig.getThreshold()));
                        });
                        break;
                    case "TIME_OF_INDUCTION":
                        List<HolidayDetailDTO.QuotaThresholdConfig> timeOfInductionYear = grantRuleConfig.getTimeOfInductionYear();
                        timeOfInductionYear.forEach(quotaThresholdConfig -> {
                            grantQuotas.add(new TimeOfInductionYearHolidayGrantQuota(
                                    8.0f * quotaThresholdConfig.getQuota(), "TIME_OF_INDUCTION", quotaThresholdConfig.getThreshold()));
                        });
                        break;
                }
                holiday.changeGrantRule(grantRuleConfig.getGrantWay(), grantRuleConfig.getGrantTime(), grantQuotas);
            } else {
                holiday.setHolidayGrantRule(null);
            }

            // 修改基本信息
            holiday.changeHolidayDetail(changeHolidayConfigCommand.getName());

            // 修改请假规则
            holiday.changeLeaveRule(
                    changeHolidayConfigCommand.getTimeOfInductionLimit(),
                    changeHolidayConfigCommand.getConvertWay(),
                    changeHolidayConfigCommand.getMinimumLeaveTimeUnitConvert()
                                   );

            // 修改适用范围
            holiday.changeEmployees(changeHolidayConfigCommand.getEmployeeIds());
            saveUpdateOperation(holiday);
            holidayRepository.save(holiday);
        });

    }

    /**
     * @Author wql
     * @Description 记录操作日志
     * @Date 11:55 2023/6/14
     **/
    private void saveUpdateOperation(Holiday holiday) {
        Holiday oldHoliday = holidayRepository.getById(holiday.getId()).get();
        try {
            HolidayOperation oldLog = getOperationLog(oldHoliday);
            HolidayOperation newLog = getOperationLog(holiday);
            holidayOperationService.updateOperation(oldLog, newLog);
        } catch (Exception e) {
            log.error("记录假期类型管理操作日志异常,old:{},new:{}", JsonUtils.serialize(oldHoliday), JsonUtils.serialize(holiday));
        }
    }

    /**
     * @Author wql
     * @Description 扁平化操作日志数据
     * @Date 16:36 2023/6/14
     **/
    private HolidayOperation getOperationLog(Holiday holiday) {
        HolidayOperation log = new HolidayOperation();
        if (ObjectUtils.isEmpty(holiday)) {
            return log;
        }
        log.setId(holiday.getId());
        log.setScope(ObjectUtils.isEmpty(holiday.getEmployees()) ? "所有人" : "部分人");
        if (ObjectUtils.isNotEmpty(holiday.getEmployees())) {
            List<String> collect = holiday.getEmployees().stream().map(id -> {
                UserDTO user = null;
                if (ObjectUtils.isNotEmpty(id)) {
                    user = userClient.getUser(id);
                }
                return ObjectUtils.isEmpty(user) ? "" : user.getName();
            }).collect(Collectors.toList());
            log.setScopeStaff(collect);
        }

        //请假规则
        LeaveRule leaveRule = holiday.getLeaveRule();
        if (ObjectUtils.isNotEmpty(leaveRule)) {
            log.setTimeOfInduction(leaveRule.getNewEmployeeLeaveRule().getTimeOfInduction());
            log.setUnit(leaveRule.getMinimumLeaveTimeUnitConvert().getMinimumLeaveTimeUnit().getValue());
            log.setAccounting(leaveRule.getMinimumLeaveTimeUnitConvert().getMinimumLeaveTimeUnit().getValue());
            List<LeaveRule.LeaveTimeUnitConvert> converts = leaveRule.getMinimumLeaveTimeUnitConvert().getLeaveTimeUnitConverts();
            if (ObjectUtils.isNotEmpty(converts)) {
                for (LeaveRule.LeaveTimeUnitConvert convert : converts) {
                    if (LeaveRule.MinimumLeaveTimeUnit.HALF_DAY.name().equals(convert.getType())) {
                        LeaveRule.HalfDayConvert dayHalfConvert = (LeaveRule.HalfDayConvert) convert;
                        log.setForenoon(dayHalfConvert.getAmHour());
                        log.setAfternoon(dayHalfConvert.getPmHour());
                    }
                    if (LeaveRule.MinimumLeaveTimeUnit.DAY.name().equals(convert.getType())) {
                        LeaveRule.DayConvert dayConvert = (LeaveRule.DayConvert) convert;
                        log.setDay(dayConvert.getDayHour());
                    }
                }
            }
        }

        //发放规则
        HolidayGrantRule grantRule = holiday.getHolidayGrantRule();
        if (ObjectUtils.isEmpty(grantRule)) {
            log.setQuota("不限额");
        } else {
            log.setQuota("限额");
            log.setGrantWay(grantRule.getGrantWay().getValue());
            log.setGrantDate(grantRule.getGrantTime().getValue());
            List<HolidayGrantQuota> quotas = grantRule.getHolidayGrantQuotas();
            if (ObjectUtils.isNotEmpty(quotas)) {
                for (HolidayGrantQuota quota : quotas) {
                    QuotaType type = QuotaType.valueOf(quota.getType());
                    switch (type) {
                        case FIXED:
                            log.setQuotaConfig(type.getValue() + (quota.getQuota().intValue() >> 3) + "天");
                            break;
                        case WORKING_YEAR:
                            WorkingYearHolidayGrantQuota workQuota = (WorkingYearHolidayGrantQuota) quota;
                            log.setQuotaConfig(
                                    type.getValue() + ",工龄大于等于" + workQuota.getThreshold() + "年" + workQuota.getQuota() + "天");
                            break;
                        case TIME_OF_INDUCTION:
                            WorkingYearHolidayGrantQuota timeQuota = (WorkingYearHolidayGrantQuota) quota;
                            log.setQuotaConfig(
                                    type.getValue() + ",司龄大于等于" + timeQuota.getThreshold() + "年" + timeQuota.getQuota() + "天");
                            break;
                    }
                }
            }
        }
        //有效期
        ExpiryDate expiryDate = holiday.getExpiryDate();
        if (ObjectUtils.isNotEmpty(expiryDate)) {
            ExpiryDate.ExpiryDateType type = expiryDate.getExpiryDateType();
            switch (type) {
                case ALONG:
                    log.setValidity(type.getValue());
                    break;
                case CALENDAR_YEAR:
                    log.setValidity(type.getValue() + ":" + expiryDate.getValue() + "年");
                    break;
                case FULL:
                    log.setValidity(type.getValue() + ":" + expiryDate.getValue() + expiryDate.getTimeUnit().getValue());
                    break;
            }
        }
        return log;
    }
}
