package com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.server.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.flow.client.FlowClient;
import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.base.flow.client.param.FormDataParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.repository.AttApproveLogRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.repository.AttClockInRecordsRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.mapper.AttApproveLogMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttApproveLogPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.domain.repository.AttGroupRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttGroupPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.param.WorkOvertimeRuleQueryParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.param.WorkOvertimeSituationParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.param.query.WorkOvertimePeriodQueryParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.result.WorkOvertimeRuleVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.application.querygateway.AttSchedulingQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.po.AttSchedulingPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.ClassesStatusTriggerEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.FormFieldVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.WorkOvertimeRuleDetailMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.WorkOvertimeRuleMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.WorkOvertimeCompensateEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.WorkOvertimeCompensateMethodEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.WorkOvertimeCountDurationEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.WorkOvertimeCountUnitsEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.WorkOvertimeTypeEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.WorkOvertimeUpperLimitCycleEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.interfaces.vo.WorkOvertimeRuleDetailVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.WorkOvertimeRuleDetailPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.WorkOvertimeRulePO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ResourceConflictException;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.entity.BasicEntity;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import instance.command.SubmitInitFormCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author bq
 * @date 2022/11/11
 * @description 加班规则查询
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorkOvertimeRuleQueryService extends ServiceImpl<WorkOvertimeRuleMapper, WorkOvertimeRulePO> {
    private final FlowClient flowClient;
    private final AttGroupRepository groupRepository;
    private final WorkOvertimeRuleMapper workOvertimeRuleMapper;
    private final AttApproveLogMapper attApproveLogMapper;
    private final AttSchedulingQueryService attSchedulingQueryService;
    private final WorkOvertimeRuleDetailMapper ruleDetailMapper;
    private final AttClockInRecordsRepository clockInRecordsRepository;
    private final AttApproveLogRepository approveLogRepository;
    private final AttApproveLogMapper approveLogMapper;

    public WorkOvertimeRuleVO getById(Long id) {
        WorkOvertimeRulePO rulePO = workOvertimeRuleMapper.selectById(id);
        WorkOvertimeRuleVO ruleVO = ObjectUtils.copying(rulePO, WorkOvertimeRuleVO.class);
        this.extend(ruleVO);
        return ruleVO;
    }

    public List<WorkOvertimeRuleVO> getList(WorkOvertimeRuleQueryParam param) {
        List<WorkOvertimeRulePO> classesPOS = this.getQueryChainWrapper(param).list();
        return ObjectUtils.copying(classesPOS, WorkOvertimeRuleVO.class);
    }

    public Pagination<WorkOvertimeRuleVO> getPage(WorkOvertimeRuleQueryParam param) {
        Page<WorkOvertimeRulePO> page = this.getQueryChainWrapper(param)
                .page(PagingUtils.convert(param));
        Pagination<WorkOvertimeRuleVO> pagination = PagingUtils.convert(page, WorkOvertimeRuleVO.class);
        pagination.getRecords().parallelStream().forEach(this::extend);
        return pagination;
    }

    private LambdaQueryChainWrapper<WorkOvertimeRulePO> getQueryChainWrapper(WorkOvertimeRuleQueryParam param) {
        return this.lambdaQuery()
                .like(StringUtils.isNotEmpty(param.getKeyword()), WorkOvertimeRulePO::getName, param.getKeyword())
                .orderByDesc(BasicEntity::getUpdateTime)
                ;
    }

    public void extend(WorkOvertimeRuleVO ruleVO) {
        if (null == ruleVO) {
            return;
        }
        // 查询细则
        List<WorkOvertimeRuleDetailPO> ruleDetailPOS = ruleDetailMapper.selectList(Wrappers.<WorkOvertimeRuleDetailPO>lambdaQuery()
                                                                                           .eq(WorkOvertimeRuleDetailPO::getRuleId, ruleVO.getId()));
        ruleVO.setRuleDetails(ObjectUtils.copying(ruleDetailPOS, WorkOvertimeRuleDetailVO.class));
        ruleVO.setContent(this.extendContent(ruleDetailPOS));
    }

    public List<String> extendContent(List<WorkOvertimeRuleDetailPO> ruleDetailPOS) {
        // 规则内容
        List<String> content = new ArrayList<>();
        WorkOvertimeRuleDetailPO workDetailPO = ruleDetailPOS.parallelStream()
                .filter(r -> WorkOvertimeTypeEnum.WORK.name().equals(r.getType())).findAny().orElse(new WorkOvertimeRuleDetailPO());
        if (StringUtils.isNotEmpty(workDetailPO.getMethod())) {
            String work = "工作日加班：" + WorkOvertimeCountDurationEnum.valueOf(workDetailPO.getMethod()).getValue();
            content.add(work);
        }
        WorkOvertimeRuleDetailPO restDetailPO = ruleDetailPOS.parallelStream()
                .filter(r -> WorkOvertimeTypeEnum.REST.name().equals(r.getType())).findAny().orElse(new WorkOvertimeRuleDetailPO());
        if (StringUtils.isNotEmpty(restDetailPO.getMethod())) {
            String work = "休息日加班：" + WorkOvertimeCountDurationEnum.valueOf(restDetailPO.getMethod()).getValue();
            content.add(work);
        }
        WorkOvertimeRuleDetailPO festivalDetailPO = ruleDetailPOS.parallelStream()
                .filter(r -> WorkOvertimeTypeEnum.FESTIVAL.name().equals(r.getType())).findAny().orElse(new WorkOvertimeRuleDetailPO());
        if (StringUtils.isNotEmpty(festivalDetailPO.getMethod())) {
            String work = "节假日加班：" + WorkOvertimeCountDurationEnum.valueOf(festivalDetailPO.getMethod()).getValue();
            content.add(work);
        }
        return content;
    }

    public String getWorkOvertimeDuration(SubmitInitFormCommand formCommand) {
        FormDataParam data = new FormDataParam(formCommand.getFormDefinitionId(), formCommand.getData());
        String fromString = flowClient.formParse(data);
        WorkOvertimeSituationParam.flowVerification(fromString);
        WorkOvertimeSituationParam fomrData = WorkOvertimeSituationParam.getWorkOvertimeSituationParam(fromString);
        fomrData.getOvertimeDetails().forEach(ex -> {
            Assert.isParameterError(
                    ObjectUtils.isEmpty(ex.getWorkDuration()) || ex.getWorkDuration().compareTo(BigDecimal.ZERO) < 0, "加班时长不能为空");
            WorkOvertimePeriodQueryParam queryParam = new WorkOvertimePeriodQueryParam();
            String[] time = ex.getWorkPeriod().substring(1, ex.getWorkPeriod().length() - 1).split(",");
            queryParam.setPeriod(new Period(DateUtils.parse(time[0].substring(1,
                                                                              time[0].length() - 1)), DateUtils.parse(time[1].substring(1,
                                                                                                                                        time[1].length() -
                                                                                                                                        1))));
            queryParam.setWorkOvertimeType(ex.getWorkType());
            // 检查该时段是否已提过加班
            List<String> statusList = new ArrayList<>();
            statusList.add(FlowResult.ACTIVE.name());
            statusList.add(FlowResult.PASS.name());
            statusList.add("PROGRESS");
            Period existPeriod = approveLogRepository.existApproveLog(SecurityContextHolder.getPrincipal().getId(), queryParam.getPeriod()
                    .getBegin(), queryParam.getPeriod().getEnd(), ClassesStatusTriggerEnum.OVERTIME.name(), null, statusList);
            if (existPeriod != null) {
                throw new ResourceConflictException("该时段已有审批(" + DateUtils.format(existPeriod.getBegin()) + "-" +
                                                    DateUtils.format(existPeriod.getEnd()) + ")");
            }
            this.getWorkOvertimeDuration(queryParam);
        });
        String processInstanceId = flowClient.submitFirst(formCommand);
        Assert.isParameterError(StringUtils.isBlank(processInstanceId), "发起加班申请失败,流程发起异常");
        return processInstanceId;
    }

    public FormFieldVO getWorkOvertimeDuration(WorkOvertimePeriodQueryParam query) {
        //获取用户的考勤规则
        Long userId = SecurityContextHolder.getPrincipal().getId();
        AttGroupPO attGroup = groupRepository.getGroupByStaffId(userId);
        Assert.isParameterError(Objects.isNull(attGroup), "你当前未在考勤组,无法提交加班申请");
        WorkOvertimeRulePO rule = workOvertimeRuleMapper.selectById(attGroup.getOvertimeRuleId());
        WorkOvertimeRuleDetailPO ruleDetail = ruleDetailMapper.selectOne(new LambdaQueryWrapper<WorkOvertimeRuleDetailPO>()
                                                                                 .eq(WorkOvertimeRuleDetailPO::getRuleId, rule.getId())
                                                                                 .eq(WorkOvertimeRuleDetailPO::getType, query.getWorkOvertimeType())
                                                                                 .last("limit 1"));
        Assert.isParameterError(Objects.isNull(ruleDetail), "当前考勤组未配置加班规则，无法计算时长");
        Assert.isParameterError("ADVANCE".equals(ruleDetail.getWorkCondition()) &&
                                DateUtils.compare(query.getPeriod().getBegin(), LocalDateTime.now()) < 0, "只能在加班前发起审批");
        Assert.isParameterError(!DateUtils.isSameDay(query.getPeriod().getBegin(), query.getPeriod().getEnd()), "加班时间暂不支持跨天");
        List<AttSchedulingPO> scheduling = attSchedulingQueryService.getPeriodScheduling(userId, DateUtils.offsetDay(query.getPeriod()
                                                                                                                             .getBegin(), -1), query
                                                                                                 .getPeriod().getEnd());
        // 加班申请时间距离开始上班时间相差多少分钟
        long classMinute = 0L;
        for (AttSchedulingPO ex : scheduling) {
            if (ObjectUtils.isNotEmpty(ex.getClassesId())) {
                // 加班开始时间小于上班时间+加班结束时间
                boolean boo = DateUtils.isIn(query.getPeriod()
                                                     .getBegin(), DateUtils.offsetMinute(ex.getClassesRealStartTime(), 1), DateUtils.offsetMinute(ex.getClassesRealEndTime(), -1));
                Assert.isParameterError(boo, "加班时间内有班次，请检查加班时间");
                boo = DateUtils.isIn(query.getPeriod()
                                             .getEnd(), DateUtils.offsetMinute(ex.getClassesRealStartTime(), 1), DateUtils.offsetMinute(ex.getClassesRealEndTime(), -1));
                Assert.isParameterError(boo, "加班时间内有班次，请检查加班时间");
                boo = DateUtils.isIn(DateUtils.offsetMinute(ex.getClassesRealStartTime(), 1), query.getPeriod().getBegin(), query
                        .getPeriod()
                        .getEnd());
                Assert.isParameterError(boo, "加班时间内有班次，请检查加班时间");
                boo = DateUtils.isIn(DateUtils.offsetMinute(ex.getClassesRealEndTime(), -1), query.getPeriod().getBegin(), query.getPeriod()
                        .getEnd());
                Assert.isParameterError(boo, "加班时间内有班次，请检查加班时间");
                long minute = DateUtils.between(query.getPeriod().getBegin(), ex.getClassesRealEndTime(), ChronoUnit.MINUTES, true);
                if (ObjectUtils.isNotEmpty(ruleDetail.getAfterTime()) && ruleDetail.getAfterTime() > 0 && minute >= 0) {
                    if (minute < ruleDetail.getAfterTime()) {
                        classMinute = ruleDetail.getAfterTime() - minute;
                    }
                }
            }
        }
        Period period = query.getPeriod();
        // 根据打卡时间计算时长
        if (WorkOvertimeCountDurationEnum.CLOCK.name().equals(ruleDetail.getMethod())) {
            period = clockInRecordsRepository.getAttClockInByDate(query.getPeriod());
            Assert.isParameterError(Objects.isNull(period), "未找到打卡记录，无法提交加班申请");
        }
        long between = DateUtils.between(period.getBegin(), period.getEnd(), ChronoUnit.HOURS);
        long minute = DateUtils.between(period.getBegin(), period.getEnd(), ChronoUnit.MINUTES, true);
        if (ObjectUtils.isNotEmpty(ruleDetail.getAfterTime()) && ruleDetail.getAfterTime() > 0) {
            minute = minute - classMinute;
            between = minute / 60;
        }
        BigDecimal bigDecimal = new BigDecimal(between);
        if (ObjectUtils.isEmpty(ruleDetail.getUnits())) {
            ruleDetail.setUnits(WorkOvertimeCountUnitsEnum.HALF_HOUR.name());
        }
        if (WorkOvertimeCountUnitsEnum.HALF_HOUR.name().equals(ruleDetail.getUnits())) {
            if (minute % 60 >= 30) {
                bigDecimal = bigDecimal.add(new BigDecimal("0.5"));
            }
        }
        if (ObjectUtils.isNotEmpty(rule.getUpperLimitDuration())) {
            if (ObjectUtils.isEmpty(rule.getUpperLimitCycle())) {
                rule.setUpperLimitCycle(WorkOvertimeUpperLimitCycleEnum.EVERY_MONTH.name());
            }
            LocalDateTime startTime = LocalDateTime.now();
            LocalDateTime endTime = LocalDateTime.now();
            // 已经加班时间
            if (WorkOvertimeUpperLimitCycleEnum.EVERY_DAY.name().equals(rule.getUpperLimitCycle())) {
                startTime = DateUtils.beginOfDay(query.getPeriod().getBegin());
                endTime = DateUtils.endOfDay(query.getPeriod().getBegin());
            } else if (WorkOvertimeUpperLimitCycleEnum.EVERY_WEEK.name().equals(rule.getUpperLimitCycle())) {
                startTime = DateUtils.beginOfWeek(query.getPeriod().getBegin());
                endTime = DateUtils.endOfWeek(query.getPeriod().getBegin());
            } else if (WorkOvertimeUpperLimitCycleEnum.EVERY_MONTH.name().equals(rule.getUpperLimitCycle())) {
                startTime = DateUtils.beginOfMonth(query.getPeriod().getBegin());
                endTime = DateUtils.endOfMonth(query.getPeriod().getBegin());
            }
            Integer approveLog = attApproveLogMapper.getActualDuration(userId, startTime, endTime);
            if (ObjectUtils.isNotEmpty(rule.getUpperLimitDuration())) {
                Assert.isOutOfRange(
                        new BigDecimal(rule.getUpperLimitDuration()).compareTo(new BigDecimal(Optional.ofNullable(approveLog).orElse(0)
                                                                                                      .toString()).add(bigDecimal)) <
                        0, "已超过累计加班时长");
            }
        }
        Assert.isOutOfRange(bigDecimal.compareTo(BigDecimal.ZERO) < 1, "加班时长不能小于0");
        return FormFieldVO.builder().name(String.valueOf(bigDecimal)).build();
    }

    public List<FormFieldVO> getWorkOvertimeDuration(String processInstanceId, String workOvertimeType) {
        List<FormFieldVO> result = new ArrayList<>();
        AttApproveLogPO attApproveLog = null;
        if (ObjectUtils.isNotEmpty(processInstanceId)) {
            attApproveLog = approveLogMapper.selectOne(new LambdaQueryWrapper<AttApproveLogPO>()
                                                               .eq(AttApproveLogPO::getProcessInstanceId, processInstanceId)
                                                               .last("limit 1"));
        }
        // 获取用户的考勤规则
        if (ObjectUtils.isNotEmpty(attApproveLog) && ObjectUtils.isNotEmpty(attApproveLog.getProportion())) {
            WorkOvertimeRuleDetailPO ruleDetail = JsonUtils.deserialize(attApproveLog.getProportion(), WorkOvertimeRuleDetailPO.class);
            WorkOvertimeCompensateEnum typeEnum = WorkOvertimeCompensateEnum.getWorkOvertimeCompensateEnum(ruleDetail.getCompensate());
            if (ObjectUtils.isNotEmpty(typeEnum)) {
                FormFieldVO fieldVO = FormFieldVO.builder().id(typeEnum.name()).name(typeEnum.getValue()).build();
                result.add(fieldVO);
                return result;
            }
        }
        return this.getWorkOvertimeDuration(ObjectUtils.isNotEmpty(attApproveLog) ? attApproveLog.getUserId()
                                                                                  : SecurityContextHolder.getPrincipal()
                                                    .getId(), workOvertimeType);
    }

    private List<FormFieldVO> getWorkOvertimeDuration(Long userId, String workOvertimeType) {
        List<FormFieldVO> result = new ArrayList<>();
        //获取用户的考勤规则
        AttGroupPO attGroup = groupRepository.getGroupByStaffId(userId);
        Assert.isParameterError(Objects.isNull(attGroup), "还未加入考勤组，无法提交加班");
        WorkOvertimeRuleDetailPO rule = ruleDetailMapper.selectOne(new LambdaQueryWrapper<WorkOvertimeRuleDetailPO>()
                                                                           .eq(WorkOvertimeRuleDetailPO::getType, workOvertimeType)
                                                                           .eq(WorkOvertimeRuleDetailPO::getRuleId, attGroup.getOvertimeRuleId())
                                                                           .last("limit 1"));
        if (ObjectUtils.isNotEmpty(rule) && WorkOvertimeCompensateMethodEnum.SYSTEM.name().equals(rule.getCompensationMethod())) {
            WorkOvertimeCompensateEnum typeEnum = WorkOvertimeCompensateEnum.getWorkOvertimeCompensateEnum(rule.getCompensate());
            FormFieldVO fieldVO = FormFieldVO.builder().id(typeEnum.name()).name(typeEnum.getValue()).build();
            result.add(fieldVO);
            return result;
        }
        WorkOvertimeCompensateEnum[] values = WorkOvertimeCompensateEnum.values();
        for (WorkOvertimeCompensateEnum typeEnum : values) {
            FormFieldVO fieldVO = FormFieldVO.builder().id(typeEnum.name()).name(typeEnum.getValue()).build();
            result.add(fieldVO);
        }
        return result;
    }
}
