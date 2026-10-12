package com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.server.impl;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.StyleSet;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttApproveLogPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.dao.WorkOvertimeLedgerMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.entity.WorkOvertimeLedger;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.param.WorkOvertimeLedgerQueryParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.param.WorkOvertimeSituationParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.result.WorkOvertimeLedgerDetailPageResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.result.WorkOvertimeLedgerPageResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.server.WorkOvertimeLedgerServer;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.WorkOvertimeCompensateEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.WorkOvertimeTypeEnum;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.cloud.util.ExcelUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @Author: lijie
 * @CreateTime: 2023-12-14  18:24
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorkOvertimeLedgerServerImpl extends ServiceImpl<WorkOvertimeLedgerMapper, WorkOvertimeLedger> implements WorkOvertimeLedgerServer {
    private final WorkOvertimeLedgerMapper workOvertimeLedgerMapper;
    private final UserClient userClient;
    private final OrganizationClient organizationClient;

    @Override
    public Pagination<WorkOvertimeLedgerPageResult> getWorkOvertimeLedgerPage(WorkOvertimeLedgerQueryParam param) {
        Page<WorkOvertimeLedgerPageResult> page = new Page<>(param.getPageNumber(), param.getPageSize());
        page = workOvertimeLedgerMapper.getLedgerByPage(page, param);
        List<WorkOvertimeLedgerPageResult> results = ObjectUtils.copying(page.getRecords(), WorkOvertimeLedgerPageResult.class);
        for (WorkOvertimeLedgerPageResult ex : results) {
            if (ObjectUtils.isNotEmpty(ex.getOrgId())) {
                OrganizationDTO organization = organizationClient.getOrganizationsById(ex.getOrgId());
                ex.setOrgName(Optional.ofNullable(organization).orElse(new OrganizationDTO()).getName());
            }
        }
        Pagination<WorkOvertimeLedgerPageResult> pagination = PagingUtils.convert(page, results);
        return pagination;
    }

    @Override
    public void getWorkOvertimeLedgerExcel(HttpServletRequest request, HttpServletResponse response, WorkOvertimeLedgerQueryParam param) {
        // 导出excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.merge(0, 0, 0, 7, "加班统计", true);
        writer.writeCellValue(0, 1, "员工姓名");
        writer.writeCellValue(1, 1, "部门");
        writer.writeCellValue(2, 1, "工作日加班（小时）");
        writer.writeCellValue(3, 1, "工作日计加班费的时长（小时）");
        writer.writeCellValue(4, 1, "休息日加班（小时）");
        writer.writeCellValue(5, 1, "休息日计加班费的时长（小时）");
        writer.writeCellValue(6, 1, "节假日加班（小时）");
        writer.writeCellValue(7, 1, "节假日计加班费的时长（小时）");
        List<WorkOvertimeLedgerPageResult> data = workOvertimeLedgerMapper.getLedgerByAll(param);
        Assert.isParameterError(CollectionUtils.isEmpty(data), "暂无数据，无需导出");
        int i = 2;
        for (int y = 0; y < data.size(); y++) {
            WorkOvertimeLedgerPageResult ex = data.get(y);
            String organizationName = "";
            if (ObjectUtils.isNotEmpty(ex.getOrgId())) {
                OrganizationDTO organization = organizationClient.getOrganizationsById(ex.getOrgId());
                organizationName = Optional.ofNullable(organization).orElse(new OrganizationDTO()).getName();
            }
            writer.writeCellValue(0, y + i, ex.getUserName());
            writer.writeCellValue(1, y + i, organizationName);
            writer.writeCellValue(2, y + i, ex.getWorkDuration());
            writer.writeCellValue(3, y + i, ex.getWorkPayDuration());
            writer.writeCellValue(4, y + i, ex.getRestDuration());
            writer.writeCellValue(5, y + i, ex.getRestPatDuration());
            writer.writeCellValue(6, y + i, ex.getFestDuration());
            writer.writeCellValue(7, y + i, ex.getFestOatDuration());
        }
        StyleSet ss = writer.getStyleSet();
        ss.setWrapText();
        writer.setStyleSet(ss);
        writer.setOnlyAlias(true);
        writer.setColumnWidth(-1, 25);
        String fileName = "打卡台账";
        ExcelUtils.downloadExcel(request, response, fileName, writer);
    }

    @Override
    public Pagination<WorkOvertimeLedgerDetailPageResult> getWorkOvertimeLedgerDetailsPage(WorkOvertimeLedgerQueryParam param) {
        Page<WorkOvertimeLedger> page = new Page<>(param.getPageNumber(), param.getPageSize());
        page = this.baseMapper.selectPage(page, new LambdaQueryWrapper<WorkOvertimeLedger>()
                .between(WorkOvertimeLedger::getStartTime, param.getPeriod().getBegin(), param.getPeriod().getEnd())
                .eq(WorkOvertimeLedger::getUserId, param.getUserId())
                .eq(WorkOvertimeLedger::getWorkType, param.getWorkType()));
        List<WorkOvertimeLedgerDetailPageResult> resultList = new ArrayList<>();
        for (WorkOvertimeLedger ex : page.getRecords()) {
            WorkOvertimeLedgerDetailPageResult result = ObjectUtils.copying(ex, WorkOvertimeLedgerDetailPageResult.class);
            if (WorkOvertimeTypeEnum.WORK.name().equals(result.getWorkType())) {
                result.setActualDuration(Optional.ofNullable(ex.getWorkDuration()).orElse(BigDecimal.ZERO));
            } else if (WorkOvertimeTypeEnum.REST.name().equals(result.getWorkType())) {
                result.setActualDuration(Optional.ofNullable(ex.getRestDuration()).orElse(BigDecimal.ZERO));
            } else if (WorkOvertimeTypeEnum.FESTIVAL.name().equals(result.getWorkType())) {
                result.setActualDuration(Optional.ofNullable(ex.getFestDuration()).orElse(BigDecimal.ZERO));
            }
            resultList.add(result);
        }

        return PagingUtils.convert(page, resultList);
    }

    @Override
    public void getWorkOvertimeLedgerDetailsExcel(HttpServletRequest request, HttpServletResponse response,
                                                  WorkOvertimeLedgerQueryParam param
                                                 ) {
        //导出excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.writeCellValue(0, 0, "员工姓名");
        writer.writeCellValue(1, 0, "加班类型");
        writer.writeCellValue(2, 0, "加班开始时间");
        writer.writeCellValue(3, 0, "加班结束时间");
        writer.writeCellValue(4, 0, "加班时长（小时）");
        List<WorkOvertimeLedger> data = this.baseMapper.selectList(new LambdaQueryWrapper<WorkOvertimeLedger>()
                                                                           .between(WorkOvertimeLedger::getStartTime, param.getPeriod()
                                                                                   .getBegin(), param.getPeriod().getEnd())
                                                                           .eq(WorkOvertimeLedger::getUserId, param.getUserId())
                                                                           .eq(WorkOvertimeLedger::getWorkType, param.getWorkType()));
        Assert.isParameterError(CollectionUtils.isEmpty(data), "暂无数据，无需导出");
        for (int y = 0; y < data.size(); y++) {
            WorkOvertimeLedger ex = data.get(y);
            UserDTO user = userClient.getUser(ex.getUserId());
            writer.writeCellValue(0, y + 1, Optional.ofNullable(user).orElse(new UserDTO()).getName());
            writer.writeCellValue(1, y + 1, WorkOvertimeTypeEnum.valueOf(ex.getWorkType()).getValue());
            writer.writeCellValue(2, y + 1, DateUtils.format(ex.getStartTime()));
            writer.writeCellValue(3, y + 1, DateUtils.format(ex.getEndTime()));
            BigDecimal actualDuration = BigDecimal.ZERO;
            if (WorkOvertimeTypeEnum.WORK.name().equals(ex.getWorkType())) {
                actualDuration = Optional.ofNullable(ex.getWorkDuration()).orElse(BigDecimal.ZERO);
            } else if (WorkOvertimeTypeEnum.REST.name().equals(ex.getWorkType())) {
                actualDuration = Optional.ofNullable(ex.getRestDuration()).orElse(BigDecimal.ZERO);
            } else if (WorkOvertimeTypeEnum.FESTIVAL.name().equals(ex.getWorkType())) {
                actualDuration = Optional.ofNullable(ex.getFestDuration()).orElse(BigDecimal.ZERO);
            }
            writer.writeCellValue(4, y + 1, actualDuration);
        }
        StyleSet ss = writer.getStyleSet();
        ss.setWrapText();
        writer.setStyleSet(ss);
        writer.setOnlyAlias(true);
        writer.setColumnWidth(-1, 25);
        String fileName = "打卡台账详情";
        ExcelUtils.downloadExcel(request, response, fileName, writer);
    }

    @Override
    public Boolean addWorkOvertime(WorkOvertimeSituationParam command, List<AttApproveLogPO> attApproveLog) {
        List<WorkOvertimeLedger> ledgerList = new ArrayList<>();
        attApproveLog.forEach(ex -> {
            log.info("开始写入台账");
            command.getOvertimeDetails().forEach(detailsParam -> {
                if (ObjectUtils.isEmpty(ex.getSubDataId()) || ex.getSubDataId().equals(detailsParam.getSubDataId())) {
                    long num = DateUtils.between(ex.getStartTime(), ex.getEndTime(), ChronoUnit.DAYS, true);
                    for (int i = 0; i <= num; i++) {
                        LocalDateTime startTime =
                                i > 0 ? DateUtils.beginOfDay(DateUtils.offsetDay(ex.getStartTime(), i)) : ex.getStartTime();
                        LocalDateTime endTime =
                                DateUtils.isSameDay(startTime, ex.getEndTime()) ? ex.getEndTime() : DateUtils.endOfDay(startTime);
                        ledgerList.add(this.getWorkOvertimeLedger(startTime, endTime, command, ex, detailsParam.getCompensate()));
                    }
                }
            });
        });
        return this.saveBatch(ledgerList);
    }

    public WorkOvertimeLedger getWorkOvertimeLedger(LocalDateTime startTime, LocalDateTime endTime, WorkOvertimeSituationParam command,
                                                    AttApproveLogPO attApproveLog, String compensate
                                                   ) {
        WorkOvertimeLedger ledger = new WorkOvertimeLedger();
        ledger.setUserId(attApproveLog.getUserId());
        UserDTO userDTO = userClient.getUser(ledger.getUserId());
        ledger.setUserName(Optional.ofNullable(userDTO).orElse(new UserDTO()).getName());
        if (userDTO != null) {
            ledger.setOrgId(Optional.ofNullable(userDTO.getOrganization()).orElse(new UserDTO.Organization()).getId());
        }
        ledger.setWorkDate(startTime);
        ledger.setStartTime(startTime);
        ledger.setEndTime(endTime);
        ledger.setWorkType(attApproveLog.getSceneCode());
        ledger.setApplyTime(command.getApplyTime());
        ledger.setApplyDoneTime(command.getApplyDoneTime());
        BigDecimal duration = WorkOvertimeCompensateEnum.FEE.name().equals(compensate) ? BigDecimal.ZERO
                                                                                       : new BigDecimal(attApproveLog.getActualDuration());
        BigDecimal payDuration =
                WorkOvertimeCompensateEnum.FEE.name().equals(compensate) ? new BigDecimal(attApproveLog.getActualDuration())
                                                                         : BigDecimal.ZERO;
        if (WorkOvertimeTypeEnum.WORK.name().equals(attApproveLog.getSceneCode())) {
            ledger.setWorkDuration(duration);
            ledger.setWorkPayDuration(payDuration);
        } else if (WorkOvertimeTypeEnum.REST.name().equals(attApproveLog.getSceneCode())) {
            ledger.setRestDuration(duration);
            ledger.setRestPatDuration(payDuration);
        } else if (WorkOvertimeTypeEnum.FESTIVAL.name().equals(attApproveLog.getSceneCode())) {
            ledger.setFestDuration(duration);
            ledger.setFestOatDuration(payDuration);
        }
        return ledger;
    }
}
