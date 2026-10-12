package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.application.commandgateway;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.StyleSet;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.repository.AttClockInRecordsRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.mapper.AttApproveLogMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.mapper.AttClockInRecordsMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttApproveLogPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttClockInRecordsPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param.WorkOmitParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result.WorkOmitDetailsResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result.WorkOmitResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttClassesRecordPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.ClassesStatusTriggerEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.repairclock.commandgateway.command.ClockRepairCommand;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @Author: lijie
 * @CreateTime: 2023-11-13  10:31
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttWorkOmitApplication {
    private final AttApproveLogMapper approveLogMapper;
    private final AttClockInRecordsRepository clockInRecordsRepository;
    private final UserClient userClient;
    private final AttClockInRecordsMapper clockInRecordsMapper;

    /**
     * 补卡统计
     */
    public Pagination<WorkOmitResult> getWorkOmitByPage(WorkOmitParam param) {
        Page<WorkOmitResult> page = new Page<>(param.getPageNumber(), param.getPageSize());
        page = approveLogMapper.getWorkOmitByPage(page, param);
        page.getRecords().forEach(ex -> {
            UserDTO userDTO = userClient.getUser(ex.getUserId());
            ex.setUserName(Optional.ofNullable(userDTO).orElse(new UserDTO()).getName());
            if (ObjectUtils.isNotEmpty(userDTO)) {
                ex.setOrgName(Optional.ofNullable(userDTO.getOrganization()).orElse(new UserDTO.Organization()).getName());
            }
        });
        return PagingUtils.convert(page);
    }

    public Pagination<WorkOmitDetailsResult> getWorkOmitDetailsByPage(WorkOmitParam param) {
        Page<AttApproveLogPO> page = new Page<>(param.getPageNumber(), param.getPageSize());
        page = approveLogMapper.selectPage(page, new LambdaQueryWrapper<AttApproveLogPO>()
                .eq(AttApproveLogPO::getUserId, param.getUserId())
                .eq(AttApproveLogPO::getBusinessType, ClassesStatusTriggerEnum.REPAIR_CLOCK.name())
                .eq(AttApproveLogPO::getStatus, FlowResult.FINISHED.name())
                .ge(AttApproveLogPO::getStartTime, param.getPeriod().getBegin())
                .le(AttApproveLogPO::getEndTime, param.getPeriod().getEnd()));
        UserDTO userDTO = userClient.getUser(param.getUserId());
        List<WorkOmitDetailsResult> list = new ArrayList<>();
        for (AttApproveLogPO ex : page.getRecords()) {
            WorkOmitDetailsResult result = ObjectUtils.copying(ex, WorkOmitDetailsResult.class);
            result.setUserName(userDTO.getName());
            result.setClockInDate(ex.getStartTime());
            AttClassesRecordPO classesRecord = clockInRecordsMapper.getRecordsByClock(ex.getClockInId());
            if (ObjectUtils.isNotEmpty(classesRecord)) {
                result.setStartWorkTime(classesRecord.getStartWorkTime());
                result.setEndWorkTime(classesRecord.getEndWorkTime());
                result.setClassesName(classesRecord.getName());
            }
            list.add(result);
        }
        return PagingUtils.convert(page, list);
    }

    public void getWorkOmitExcel(HttpServletRequest request, HttpServletResponse response, WorkOmitParam param) {
        List<WorkOmitResult> data = approveLogMapper.getWorkOmitExcle(param);
        Assert.isParameterError(CollectionUtils.isEmpty(data), "暂无数据，无需导出");
        data.forEach(ex -> {
            UserDTO userDTO = userClient.getUser(ex.getUserId());
            ex.setUserName(Optional.ofNullable(userDTO).orElse(new UserDTO()).getName());
            if (ObjectUtils.isNotEmpty(userDTO)) {
                ex.setOrgName(Optional.ofNullable(userDTO.getOrganization()).orElse(new UserDTO.Organization()).getName());
            }
        });
        //导出excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.writeCellValue(0, 0, "员工姓名");
        writer.writeCellValue(1, 0, "部门");
        writer.writeCellValue(2, 0, "补卡次数");
        for (int y = 0; y < data.size(); y++) {
            WorkOmitResult result = data.get(y);
            writer.writeCellValue(0, y + 1, result.getUserName());
            writer.writeCellValue(1, y + 1, result.getOrgName());
            writer.writeCellValue(2, y + 1, result.getNum());
        }
        StyleSet ss = writer.getStyleSet();
        ss.setWrapText();
        writer.setStyleSet(ss);
        writer.setOnlyAlias(true);
        writer.setColumnWidth(-1, 25);
        String fileName = "补卡台账";
        ExcelUtils.downloadExcel(request, response, fileName, writer);
    }

    public void getWorkOmitDetailsExcel(HttpServletRequest request, HttpServletResponse response, WorkOmitParam param) {
        List<AttApproveLogPO> data = approveLogMapper.selectList(new LambdaQueryWrapper<AttApproveLogPO>()
                                                                         .eq(AttApproveLogPO::getUserId, param.getUserId())
                                                                         .eq(AttApproveLogPO::getBusinessType, ClassesStatusTriggerEnum.REPAIR_CLOCK.name())
                                                                         .eq(AttApproveLogPO::getStatus, FlowResult.FINISHED.name())
                                                                         .ge(AttApproveLogPO::getStartTime, param.getPeriod().getBegin())
                                                                         .le(AttApproveLogPO::getEndTime, param.getPeriod().getEnd()));
        Assert.isParameterError(CollectionUtils.isEmpty(data), "暂无数据，无需导出");
        //导出excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.writeCellValue(0, 0, "员工姓名");
        writer.writeCellValue(1, 0, "补卡申请时间");
        writer.writeCellValue(2, 0, "补卡处理时间");
        writer.writeCellValue(3, 0, "补卡日期");
        writer.writeCellValue(4, 0, "补卡班次");
        UserDTO userDTO = userClient.getUser(param.getUserId());
        for (int y = 0; y < data.size(); y++) {
            AttApproveLogPO result = data.get(y);
            AttClassesRecordPO classesRecord = clockInRecordsMapper.getRecordsByClock(result.getClockInId());
            writer.writeCellValue(0, y + 1, userDTO.getName());
            writer.writeCellValue(1, y + 1, DateUtils.format(result.getCreateTime()));
            writer.writeCellValue(2, y + 1, DateUtils.format(result.getUpdateTime()));
            writer.writeCellValue(3, y + 1, DateUtils.formatDate(result.getStartTime()));
            if (ObjectUtils.isNotEmpty(classesRecord)) {
                writer.writeCellValue(4, y + 1, classesRecord.getStartWorkTime() + "-" + classesRecord.getEndWorkTime());
            }
        }
        StyleSet ss = writer.getStyleSet();
        ss.setWrapText();
        writer.setStyleSet(ss);
        writer.setOnlyAlias(true);
        writer.setColumnWidth(-1, 25);
        String fileName = "补卡台账-详情";
        ExcelUtils.downloadExcel(request, response, fileName, writer);
    }

    /**
     * 补卡审批数据处理
     */
    public void getApproveLog(AttApproveLogPO approveLog, String data) {
        ClockRepairCommand command = JsonUtils.deserialize(data, ClockRepairCommand.class);
        if (ObjectUtils.isNull(command)) {
            return;
        }
        AttClockInRecordsPO clock = clockInRecordsRepository.getById(command.getClassesId());
        if (ObjectUtils.isNotNull(clock)) {
            approveLog.setClockInId(clock.getId());
            approveLog.setSceneCode(clock.getState());
        }
        approveLog.setSceneName(ClassesStatusTriggerEnum.REPAIR_CLOCK.getDesc());
        approveLog.setStartTime(DateUtils.parse(command.getDate()));
        approveLog.setEndTime(DateUtils.parse(command.getDate()));
        approveLog.setUserId(command.getApplicantId());
        approveLog.setBusinessType(ClassesStatusTriggerEnum.REPAIR_CLOCK.name());

    }
}
