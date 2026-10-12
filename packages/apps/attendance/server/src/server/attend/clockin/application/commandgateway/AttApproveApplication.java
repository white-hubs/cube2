package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.application.commandgateway;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.flow.client.FlowClient;
import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.base.flow.client.ProcessInstanceClient;
import com.guhecloud.cube.base.flow.client.dto.ProcessInstanceDTO;
import com.guhecloud.cube.base.flow.client.param.query.ProcessInstancePageQuery;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.enumeration.ClockInEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.mapper.AttApproveLogMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.mapper.AttClockInRecordsMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttApproveLogPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttClockInRecordsPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param.ApprovalRecordPageParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param.ApprovePageParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param.ApproveVerificationParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.domain.repository.AttGroupRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttGroupPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.ClassesStatusTriggerEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.repairclock.commandgateway.command.ClockRepairCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.SupplementCardRuleMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.SupplementCardRulePO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import feign.Response;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: lijie
 * @CreateTime: 2023-11-07  19:38
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttApproveApplication {
    private final SupplementCardRuleMapper supplementCardRuleMapper;
    private final AttGroupRepository groupRepository;
    private final AttApproveLogMapper attApproveLogMapper;
    private final FlowClient flowClient;
    private final AttClockInRecordsMapper clockInRecordsMapper;
    private final ProcessInstanceClient processInstanceClient;

    public Boolean verification(ApproveVerificationParam param) {
        Long userId = SecurityContextHolder.getPrincipal().getId();
        AttGroupPO attGroup = groupRepository.getGroupByStaffId(userId);
        String biz = flowClient.formParse(param.getFlowData());
        if (ClassesStatusTriggerEnum.REPAIR_CLOCK.name().equals(param.getBusinessType())) {
            return this.checkRepairClockRule(biz, attGroup.getCardRuleIds());
        }
        return false;
    }

    public Pagination<ProcessInstanceDTO> getApproveByUserPage(ApprovePageParam param) {
        Page<AttApproveLogPO> page = new Page<>(param.getPageNumber(), param.getPageSize());
        page = attApproveLogMapper.selectPage(page, new LambdaQueryWrapper<AttApproveLogPO>()
                .eq(AttApproveLogPO::getUserId, param.getUserId())
                .eq(AttApproveLogPO::getBusinessType, param.getBusinessType())
                .eq(ObjectUtils.isNotEmpty(param.getSceneCode()), AttApproveLogPO::getSceneCode, param.getSceneCode())
                .ge(AttApproveLogPO::getStartTime, param.getPeriod().getBegin())
                .le(AttApproveLogPO::getEndTime, param.getPeriod().getEnd()));
        if (ObjectUtils.isEmpty(page.getPages()) || page.getPages() < 1) {
            return Pagination.empty();
        }
        ProcessInstancePageQuery query = new ProcessInstancePageQuery();
        query.setPageNumber(param.getPageNumber());
        query.setPageSize(param.getPageSize());
        query.setBusinessType(param.getBusinessType());
        List<String> processInstanceIds = page.getRecords().stream().map(AttApproveLogPO::getProcessInstanceId)
                .collect(Collectors.toList());
        query.setProcessInstanceIds(String.join(",", processInstanceIds));
        Pagination<ProcessInstanceDTO> data = processInstanceClient.allPage(query);
        return new Pagination<>(page.getTotal(), page.getPages(), data.getRecords());
    }

    public Pagination<ProcessInstanceDTO> approvalRecordPage(ApprovalRecordPageParam param) {
        ProcessInstancePageQuery query = buildApprovalRecordQuery(param);
        if (hasBusinessTimeCondition(param) && CollectionUtils.isEmpty(query.getProcessInstanceIdsList())) {
            return Pagination.empty();
        }
        return processInstanceClient.allPageBatch(query);
    }

    public void approvalRecordExport(ApprovalRecordPageParam param, HttpServletResponse response) {
        ProcessInstancePageQuery query = buildApprovalRecordQuery(param);
        Assert.isNotFound(hasBusinessTimeCondition(param) && CollectionUtils.isEmpty(query.getProcessInstanceIdsList()),
                "没有流程审批记录,无法导出");
        Response flowResponse = processInstanceClient.processInstanceHistoricalExportBatch(query);
        writeFlowExport(flowResponse, response);
    }

    private ProcessInstancePageQuery buildApprovalRecordQuery(ApprovalRecordPageParam param) {
        ProcessInstancePageQuery query = new ProcessInstancePageQuery();
        BeanUtils.copyProperties(param, query);
        if (!hasBusinessTimeCondition(param)) {
            return query;
        }

        LambdaQueryWrapper<AttApproveLogPO> wrapper = new LambdaQueryWrapper<AttApproveLogPO>()
                .select(AttApproveLogPO::getProcessInstanceId)
                .isNotNull(AttApproveLogPO::getProcessInstanceId);
        wrapper.ge(param.getBusinessTime().getBegin() != null, AttApproveLogPO::getStartTime, param.getBusinessTime().getBegin())
                .le(param.getBusinessTime().getEnd() != null, AttApproveLogPO::getStartTime, param.getBusinessTime().getEnd());
        Set<String> processInstanceIds = attApproveLogMapper.selectList(wrapper).stream()
                .map(AttApproveLogPO::getProcessInstanceId)
                .filter(id -> id != null && !id.trim().isEmpty())
                .collect(Collectors.toCollection(LinkedHashSet::new));

        Set<String> conditionProcessInstanceIds = new LinkedHashSet<>();
        if (param.getProcessInstanceIdsList() != null) {
            conditionProcessInstanceIds.addAll(param.getProcessInstanceIdsList());
        }
        if (param.getProcessInstanceIds() != null) {
            conditionProcessInstanceIds.addAll(Arrays.asList(param.getProcessInstanceIds().split(",")));
        }
        conditionProcessInstanceIds.removeIf(id -> id == null || id.trim().isEmpty());
        if (!conditionProcessInstanceIds.isEmpty()) {
            processInstanceIds.retainAll(conditionProcessInstanceIds);
        }

        query.setProcessInstanceIds(null);
        query.setProcessInstanceIdsList(new ArrayList<>(processInstanceIds));
        return query;
    }

    private boolean hasBusinessTimeCondition(ApprovalRecordPageParam param) {
        return param.getBusinessTime() != null
                && (param.getBusinessTime().getBegin() != null || param.getBusinessTime().getEnd() != null);
    }

    private void writeFlowExport(Response flowResponse, HttpServletResponse response) {
        flowResponse.headers().forEach((name, values) -> {
            if ("transfer-encoding".equalsIgnoreCase(name)) {
                return;
            }
            values.forEach(value -> {
                if ("content-type".equalsIgnoreCase(name)) {
                    response.setContentType(value);
                } else {
                    response.addHeader(name, value);
                }
            });
        });
        if (flowResponse.body() == null) {
            return;
        }
        try (InputStream inputStream = flowResponse.body().asInputStream()) {
            inputStream.transferTo(response.getOutputStream());
        } catch (IOException e) {
            log.error("考勤审批记录导出失败", e);
            throw new ServiceException("文件下载失败");
        }
    }

    /**
     * 补卡前置校验
     *
     * @param biz         表单数据json
     * @param cardRuleIds 补卡规则id
     */
    public Boolean checkRepairClockRule(String biz, String cardRuleIds) {
        ClockRepairCommand command = JSONObject.parseObject(biz, ClockRepairCommand.class);
        AttClockInRecordsPO clock = clockInRecordsMapper.selectById(command.getClassesId());
        Assert.isNotFound(clock, "班次不存在");
        Assert.isNotAcceptable(ClockInEnum.NORMAL.name().equals(clock.getState()), "班次正常，无需补卡");
        Long num = attApproveLogMapper.selectCount(new LambdaQueryWrapper<AttApproveLogPO>()
                                                           .le(AttApproveLogPO::getEndTime, DateUtils.endOfMonth(DateUtils.parse(command.getDate())))
                                                           .ge(AttApproveLogPO::getStartTime, DateUtils.beginOfMonth(DateUtils.parse(command.getDate())))
                                                           .eq(AttApproveLogPO::getUserId, SecurityContextHolder.getPrincipal().getId())
                                                           .eq(AttApproveLogPO::getBusinessType, ClassesStatusTriggerEnum.REPAIR_CLOCK.name())
                                                           .in(AttApproveLogPO::getStatus, FlowResult.FINISHED.name(), FlowResult.ACTIVE.name(), "PROGRESS"));
        List<String> ids = Arrays.asList(cardRuleIds.split(","));
        SupplementCardRulePO supplementCardRule = supplementCardRuleMapper.selectOne(new LambdaQueryWrapper<SupplementCardRulePO>()
                                                                                             .in(SupplementCardRulePO::getId, ids)
                                                                                             .like(SupplementCardRulePO::getType, clock.getState())
                                                                                             .last("limit 1"));
        Assert.isParameterError(Objects.isNull(supplementCardRule), "未找到补卡规则，请联系管理员");
        // 是否还有补卡次数
        Assert.isNotAcceptable(num >= supplementCardRule.getTimes(), "本月补卡次数已用尽");
        LocalDateTime endDate = DateUtils.offsetDay(clock.getClockInDate(), supplementCardRule.getDays());
        // 是否在运行的时间内
        Assert.isNotAcceptable(DateUtils.compare(LocalDateTime.now(), endDate) > 0, "已过最晚补卡时间");
        return true;
    }
}
