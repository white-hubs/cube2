package com.guhecloud.cube.base.alarm.server.flow.interfaces;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.alarm.server.flow.application.AlarmRecordService;
import com.guhecloud.cube.base.alarm.server.flow.application.AlarmRecordTargetService;
import com.guhecloud.cube.base.alarm.server.flow.application.AlarmStateEventOperateService;
import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.AlarmRecordCommandService;
import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.AssignAlarmRecordCommand;
import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.BatchHandlerAlarmRecordCommand;
import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.DynamicAssignAlarmRecordCommand;
import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.HandlerAlarmRecordCommand;
import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.RemoveAlarmRecordCommand;
import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.SettingAlarmBusinessSnapshotCommand;
import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.SuspendAlarmRecordCommand;
import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.TransferAlarmRecordCommand;
import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.UrgingAlarmRecordCommand;
import com.guhecloud.cube.base.alarm.server.flow.application.querygateway.AlarmRecordHandlerUserQueryService;
import com.guhecloud.cube.base.alarm.server.flow.application.querygateway.AlarmRecordOperatorLogQueryService;
import com.guhecloud.cube.base.alarm.server.flow.application.querygateway.AlarmRecordQueryService;
import com.guhecloud.cube.base.alarm.server.flow.application.querygateway.query.AlarmRecordPageNumberQueryParam;
import com.guhecloud.cube.base.alarm.server.flow.application.querygateway.query.AlarmRecordQueryParam;
import com.guhecloud.cube.base.alarm.server.flow.application.querygateway.query.AlarmRecordStandardQueryParam;
import com.guhecloud.cube.base.alarm.server.flow.domain.aggregates.AlarmRecord;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmHandlerTimeoutStatusEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmStatusEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.DateUnitEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.factory.AlarmRecordFactory;
import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmRecordHandlerUserPO;
import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmRecordOperatorLogPO;
import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmRecordPO;
import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmRecordTargetPO;
import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmStateEventOperatePO;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.repository.AlarmRecordRepository;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmRecordExportVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmRecordOperatorLogVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmRecordPageVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmRecordTargetVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmRecordVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmStateEventOperateVO;
import com.guhecloud.cube.base.alarm.server.sat.entity.SatAlarmBusinessUrl;
import com.guhecloud.cube.base.alarm.server.sat.entity.SatAlarmTypeStatusOperator;
import com.guhecloud.cube.base.alarm.server.sat.service.SatAlarmBusinessUrlService;
import com.guhecloud.cube.base.alarm.server.sat.service.SatAlarmTypeStatusOperatorService;
import com.guhecloud.cube.base.alarm.server.state.AlarmStateContext;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Requester;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.permission.annotation.DataPermissionSwitch;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmRecordController {

    private final AlarmRecordRepository alarmRecordRepository;

    private final AlarmRecordQueryService alarmRecordQueryService;

    private final AlarmRecordService alarmRecordService;

    private final AlarmRecordTargetService alarmRecordTargetService;

    private final AlarmRecordOperatorLogQueryService alarmRecordOperatorLogQueryService;

    private final AlarmStateEventOperateService alarmStateEventOperateService;

    private final AlarmRecordHandlerUserQueryService alarmRecordHandlerUserQueryService;

    private final SatAlarmBusinessUrlService satAlarmBusinessUrlService;

    private final SatAlarmTypeStatusOperatorService satAlarmTypeStatusOperatorService;

    private final AlarmRecordCommandService alarmRecordCommandService;

    private final FileClient fileClient;

    private final AlarmStateContext alarmStateContext;

    @PreAuthorize("hasScope('alarm_manage')")
    @PostMapping("/alarm-standard-records")
    public List<AlarmRecordPageVO> getStandardAlarmRecords(@Valid @RequestBody AlarmRecordStandardQueryParam query) {
        List<AlarmRecordPageVO> alarmRecordPageVOS = this.alarmRecordQueryService.getStandardAlarmRecords(query);
        if (CollectionUtils.isEmpty(alarmRecordPageVOS)) {
            return Collections.emptyList();
        }
        Set<Long> recordIds = alarmRecordPageVOS.stream().map(AlarmRecordPageVO::getId).collect(Collectors.toSet());
        Map<Long, List<AlarmRecordHandlerUserPO>> alarmRecordHandlerUserPOMap = this.alarmRecordHandlerUserQueryService
                .getUsersByAlarmRecordIds(recordIds).stream()
                .collect(Collectors.groupingBy(AlarmRecordHandlerUserPO::getAlarmRecordId));
        alarmRecordPageVOS.forEach(alarmRecordPageVO -> {
            if (Objects.nonNull(alarmRecordPageVO.getAlarmHandlerResult())) {
                alarmRecordPageVO.setAlarmHandlerResultName(alarmRecordPageVO.getAlarmHandlerResult().getDescription());
            }
            List<AlarmRecordHandlerUserPO> userPOS = alarmRecordHandlerUserPOMap.get(alarmRecordPageVO.getId());
            if (!CollectionUtils.isEmpty(userPOS)) {
                alarmRecordPageVO.setAlarmHandlerName(userPOS.stream().map(AlarmRecordHandlerUserPO::getUserName)
                                                              .collect(Collectors.joining(",")));
            }
            alarmRecordPageVO.setAlarmStatusName(alarmRecordPageVO.getAlarmStatus().getDescription());
            //设置报警超时状态
            setAlarmPageTimeoutStatus(alarmRecordPageVO);
            if (Objects.nonNull(alarmRecordPageVO.getTimeoutProcessTimes()) && 0 < alarmRecordPageVO.getTimeoutProcessTimes()) {
                alarmRecordPageVO.setTimeoutProcessTimesName(alarmRecordPageVO.getTimeoutProcessTimes() + "" +
                                                             DateUnitEnum.valueOf(alarmRecordPageVO.getTimeoutProcessTimesUnit())
                                                                     .getDescription());
            }
        });
        return alarmRecordPageVOS;
    }

    /**
     * 报警记录分页列表
     *
     * @param param
     *
     * @return
     */
    @DataPermissionSwitch(dataModel = "alarmCenter:alarmRecordHandlerUser")
    @GetMapping("/alarm-records")
    public Pagination<AlarmRecordPageVO> getPage(@Valid AlarmRecordQueryParam param) {
        Page<AlarmRecordPageVO> page = this.alarmRecordQueryService.getPage(param);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }
        List<String> businessTypes = page.getRecords().stream().map(alarmRecordPageVO -> alarmRecordPageVO.getBusinessType())
                .collect(Collectors.toList());
        //跳转配置
        Map<String, SatAlarmBusinessUrl> satAlarmBusinessUrlsMaps = this.satAlarmBusinessUrlService
                .listByBusinessTypes(businessTypes, SessionContextHolder.getRequester().getTerminal())
                .stream()
                .collect(Collectors
                                 .toMap(satAlarmBusinessUrl -> satAlarmBusinessUrl.getBusinessType() + ":" +
                                                               satAlarmBusinessUrl
                                                                       .getApplicationCode(), satAlarmBusinessUrl -> satAlarmBusinessUrl, (o, n) -> n));
        Set<Long> recordIds = page.getRecords().stream().map(AlarmRecordPageVO::getId).collect(Collectors.toSet());
        Map<Long, List<AlarmRecordHandlerUserPO>> alarmRecordHandlerUserPOMap = this.alarmRecordHandlerUserQueryService
                .getUsersByAlarmRecordIds(recordIds).stream()
                .collect(Collectors.groupingBy(AlarmRecordHandlerUserPO::getAlarmRecordId));
        //状态可操作项map
        Map<String, List<SatAlarmTypeStatusOperator>> statusOperatorMap = this.satAlarmTypeStatusOperatorService.list().stream()
                .collect(Collectors
                                 .groupingBy(statusOperator ->
                                                     statusOperator
                                                             .getAlarmTypeCode() +
                                                     "_" +
                                                     statusOperator
                                                             .getStatus()
                                                             .name()));
        page.getRecords().forEach(alarmRecordPageVO -> {
            //待接收状态不显示处理人信息
            if (AlarmStatusEnum.ALARM_UN_CONFIRMED == alarmRecordPageVO.getAlarmStatus()) {
                alarmRecordPageVO.setAlarmHandlerName(null);
            } else {
                List<AlarmRecordHandlerUserPO> userPOS = alarmRecordHandlerUserPOMap.get(alarmRecordPageVO.getId());
                if (!CollectionUtils.isEmpty(userPOS)) {
                    alarmRecordPageVO.setAlarmHandlerName(userPOS.stream().map(AlarmRecordHandlerUserPO::getUserName)
                                                                  .collect(Collectors.joining(",")));
                }
            }
            if (Objects.nonNull(alarmRecordPageVO.getAlarmHandlerResult())) {
                alarmRecordPageVO.setAlarmHandlerResultName(alarmRecordPageVO.getAlarmHandlerResult().getDescription());
            }
            SatAlarmBusinessUrl satAlarmBusinessUrl = satAlarmBusinessUrlsMaps.get(
                    alarmRecordPageVO.getBusinessType() + ":" + alarmRecordPageVO.getApplicationCode());
            if (Objects.nonNull(satAlarmBusinessUrl)) {
                alarmRecordPageVO.setUrl(satAlarmBusinessUrl.getUrl());
            }
            alarmRecordPageVO.setAlarmStatusName(alarmRecordPageVO.getAlarmStatus().getDescription());
            //设置报警超时状态
            setAlarmPageTimeoutStatus(alarmRecordPageVO);
            if (Objects.nonNull(alarmRecordPageVO.getTimeoutProcessTimes()) && 0 < alarmRecordPageVO.getTimeoutProcessTimes()) {
                alarmRecordPageVO.setTimeoutProcessTimesName(alarmRecordPageVO.getTimeoutProcessTimes() + "" +
                                                             DateUnitEnum.valueOf(alarmRecordPageVO.getTimeoutProcessTimesUnit())
                                                                     .getDescription());
            }
            //状态可执行的操作
            if (statusOperatorMap.containsKey(
                    alarmRecordPageVO.getAlarmTypeCode() + "_" + alarmRecordPageVO.getAlarmStatus().name())) {
                List<SatAlarmTypeStatusOperator> satAlarmTypeStatusOperators = statusOperatorMap.get(
                        alarmRecordPageVO.getAlarmTypeCode() + "_" + alarmRecordPageVO.getAlarmStatus().name());
                alarmRecordPageVO.setAlarmOperator(satAlarmTypeStatusOperators.stream()
                                                           .map(SatAlarmTypeStatusOperator::getOperator)
                                                           .collect(Collectors.toList()));
            }
        });
        return PagingUtils.convert(page);
    }

    /**
     * 通用报警记录分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-records/common-page")
    public Pagination<AlarmRecordPageVO> getCommonPage(@Valid AlarmRecordQueryParam param) {
        return this.getPage(param);
    }

    @PostMapping("/alarm-records/page")
    public Pagination<AlarmRecordPageVO> page(@RequestBody @Valid AlarmRecordQueryParam param) {
        return this.getPage(param);
    }

    /**
     * 报警记录分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-records/queryPageByTenant")
    public Pagination<AlarmRecordPageVO> queryPageByTenant(@Valid AlarmRecordQueryParam param) {
        SessionContextHolder.setRequester(new Requester(SessionContextHolder.getRequester().getUri(), SessionContextHolder
                .getRequester().getHost(), SessionContextHolder.getRequester().getDomain(), SessionContextHolder.getRequester()
                                                                .getAuthorization(), param
                                                                .getTenant(), SessionContextHolder
                                                                .getRequester().getTerminal(), SessionContextHolder.getRequester()
                                                                .getVersion(), SessionContextHolder
                                                                .getRequester().getSignature(), SessionContextHolder
                                                                .getRequester().getPermission()));
        Pagination<AlarmRecordPageVO> pagination = this.getPage(param);
        SessionContextHolder.clearSessionContext();
        return pagination;
    }

    /**
     * 报警记录导出
     *
     * @param param
     *
     * @throws IOException
     */
    @PostMapping("/alarm-records/export")
    public void exportAlarmList(@Valid @RequestBody AlarmRecordQueryParam param) throws IOException {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attr.getResponse();
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码
        String excelName = URLEncoder.encode(
                StringUtils.isNotBlank(param.getFileName()) ? param.getFileName() : "报警记录", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + excelName + ExcelTypeEnum.XLSX.getValue());
        List<AlarmRecordExportVO> alarmRecordExportVOS = this.alarmRecordQueryService.getExportList(param);
        EasyExcel.write(response.getOutputStream(), AlarmRecordExportVO.class).sheet("报警记录")
                .doWrite(alarmRecordExportVOS);
    }

    /**
     * 指挥调度报警记录列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-record/un-handler/list")
    public Pagination<AlarmRecordPageVO> getUnHandlerList(@Valid AlarmRecordQueryParam param) {
        Page<AlarmRecordPageVO> alarmRecordPageVOPage = this.alarmRecordQueryService.getUnHandlerList(param);
        List<AlarmRecordPageVO> alarmRecordPageVOS = alarmRecordPageVOPage.getRecords();
        if (CollectionUtils.isEmpty(alarmRecordPageVOS)) {
            return Pagination.empty();
        }
        Set<Long> recordIds = alarmRecordPageVOS.stream().map(AlarmRecordPageVO::getId).collect(Collectors.toSet());
        Map<Long, List<AlarmRecordHandlerUserPO>> alarmRecordHandlerUserPOMap = this.alarmRecordHandlerUserQueryService
                .getUsersByAlarmRecordIds(recordIds).stream()
                .collect(Collectors.groupingBy(AlarmRecordHandlerUserPO::getAlarmRecordId));
        //状态可操作项map
        Map<String, List<SatAlarmTypeStatusOperator>> statusOperatorMap = this.satAlarmTypeStatusOperatorService.list().stream()
                .collect(Collectors
                                 .groupingBy(statusOperator ->
                                                     statusOperator
                                                             .getAlarmTypeCode() +
                                                     "_" +
                                                     statusOperator
                                                             .getStatus()
                                                             .name()));
        alarmRecordPageVOS.forEach(alarmRecordPageVO -> {
            //待接收状态不显示处理人信息
            if (AlarmStatusEnum.ALARM_UN_CONFIRMED == alarmRecordPageVO.getAlarmStatus()) {
                alarmRecordPageVO.setAlarmHandlerName(null);
            } else {
                List<AlarmRecordHandlerUserPO> userPOS = alarmRecordHandlerUserPOMap.get(alarmRecordPageVO.getId());
                if (!CollectionUtils.isEmpty(userPOS)) {
                    alarmRecordPageVO.setAlarmHandlerName(userPOS.stream().map(AlarmRecordHandlerUserPO::getUserName)
                                                                  .collect(Collectors.joining(",")));
                }
            }
            //设置报警超时状态
            setAlarmPageTimeoutStatus(alarmRecordPageVO);
            //状态可执行的操作
            if (statusOperatorMap.containsKey(
                    alarmRecordPageVO.getAlarmTypeCode() + "_" + alarmRecordPageVO.getAlarmStatus().name())) {
                List<SatAlarmTypeStatusOperator> satAlarmTypeStatusOperators = statusOperatorMap.get(
                        alarmRecordPageVO.getAlarmTypeCode() + "_" + alarmRecordPageVO.getAlarmStatus().name());
                alarmRecordPageVO.setAlarmOperator(satAlarmTypeStatusOperators.stream()
                                                           .map(SatAlarmTypeStatusOperator::getOperator)
                                                           .collect(Collectors.toList()));
            }
        });
        return PagingUtils.convert(alarmRecordPageVOPage, alarmRecordPageVOS);
    }

    /**
     * 获取未处理报警记录页数
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-record/un-handler/page-number")
    public Long getUnHandlerPageNumber(@Valid AlarmRecordPageNumberQueryParam param) {
        return this.alarmRecordQueryService.getUnHandlerPageNumber(param);
    }

    /**
     * 报警记录详情
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/alarm-record/{id}")
    public AlarmRecordVO getAlarmRecord(@PathVariable("id") @Min(value = 1, message = "报警记录主键不能小于1") Long id) {
        AlarmRecordPO alarmRecordPO = this.alarmRecordQueryService.getById(id);
        if (Objects.isNull(alarmRecordPO)) {
            alarmRecordPO = this.alarmRecordQueryService.getByBusinessKey(id.toString());
        }
        Assert.isNotFound(alarmRecordPO, "报警记录不存在或已删除");
        AlarmRecordVO alarmRecordVO = getAlarmRecordVO(alarmRecordPO);
        alarmRecordVO.setAlarmStatusName(alarmRecordVO.getAlarmStatus().getDescription());
        alarmRecordVO.setHandlerFileVOS(this.fileClient.getByBusinessKey(alarmRecordVO.getId()));
        //待接收状态不显示处理人信息
        setHandlerAndNotifyUsers(alarmRecordVO);
        if (Objects.nonNull(alarmRecordVO.getTimeoutProcessTimes()) && 0 < alarmRecordVO.getTimeoutProcessTimes()) {
            alarmRecordVO.setTimeoutProcessTimesName(alarmRecordVO.getTimeoutProcessTimes() + "" +
                                                     DateUnitEnum.valueOf(alarmRecordVO.getTimeoutProcessTimesUnit())
                                                             .getDescription());
        }
        extend(alarmRecordPO, alarmRecordVO);
        return alarmRecordQueryService.getDetailed(alarmRecordVO);
    }

    private void extend(AlarmRecordPO alarmRecordPO, AlarmRecordVO alarmRecordVO) {
        //报警记录可执行的操作
        List<SatAlarmTypeStatusOperator> satAlarmTypeStatusOperators = this.satAlarmTypeStatusOperatorService.list(Wrappers
                                                                                                                           .<SatAlarmTypeStatusOperator>lambdaQuery()
                                                                                                                           .eq(SatAlarmTypeStatusOperator::getAlarmTypeCode, alarmRecordPO
                                                                                                                                   .getAlarmTypeCode())
                                                                                                                           .eq(SatAlarmTypeStatusOperator::getStatus, alarmRecordPO
                                                                                                                                   .getAlarmStatus()));
        if (CollectionUtils.isEmpty(satAlarmTypeStatusOperators)) {
            alarmRecordVO.setAlarmOperator(satAlarmTypeStatusOperators.stream().map(SatAlarmTypeStatusOperator::getOperator)
                                                   .collect(Collectors.toList()));
        }
    }

    /**
     * 报警记录详情
     *
     * @param businessKey
     *
     * @return
     */
    @GetMapping("/alarm-record/by-business/{businessKey}")
    public AlarmRecordVO getAlarmRecordByBusinessKey(@PathVariable("businessKey") @NotNull String businessKey) {
        AlarmRecordPO alarmRecordPO = this.alarmRecordQueryService.getByBusinessKey(businessKey);
        Assert.isNotFound(alarmRecordPO, "报警记录不存在或已删除");
        AlarmRecordVO alarmRecordVO = getAlarmRecordVO(alarmRecordPO);
        if (Objects.nonNull(alarmRecordVO.getTimeoutProcessTimes()) && 0 < alarmRecordVO.getTimeoutProcessTimes()) {
            alarmRecordVO.setTimeoutProcessTimesName(alarmRecordVO.getTimeoutProcessTimes() + "" +
                                                     DateUnitEnum.valueOf(alarmRecordVO.getTimeoutProcessTimesUnit())
                                                             .getDescription());
        }
        alarmRecordVO.setAlarmStatusName(alarmRecordVO.getAlarmStatus().getDescription());
        alarmRecordVO.setHandlerFileVOS(this.fileClient.getByBusinessKey(alarmRecordVO.getId()));
        //待接收状态不显示处理人信息
        setHandlerAndNotifyUsers(alarmRecordVO);
        //报警记录可执行的操作
        extend(alarmRecordPO, alarmRecordVO);
        return alarmRecordQueryService.getDetailed(alarmRecordVO);
    }

    /**
     * 报警确认
     *
     * @param id
     */
    @PutMapping("/alarm-record/confirmed/{id}")
    public void confirmedAlarmRecord(@PathVariable("id") @Min(value = 1, message = "报警记录主键不能小于1") Long id) {
        AlarmRecord alarmRecord = this.alarmRecordRepository.detail(id);
        Assert.isNotFound(alarmRecord, "报警记录不存在");
        this.alarmStateContext.confirmedAlarmRecord(alarmRecord);
    }

    /**
     * 报警挂起
     *
     * @param id
     * @param param
     */
    @PutMapping("/alarm-record/suspend/{id}")
    public void suspendAlarmRecord(@PathVariable("id") @Min(value = 1, message = "报警记录主键不能小于1") Long id,
                                   @Valid @RequestBody SuspendAlarmRecordCommand param
                                  ) {
        AlarmRecord alarmRecord = this.alarmRecordRepository.detail(id);
        Assert.isNotFound(alarmRecord, "报警记录不存在");
        this.alarmStateContext.suspendAlarmRecord(alarmRecord, param);
    }

    /**
     * 报警指派
     *
     * @param id
     * @param param
     */
    @PutMapping("/alarm-record/assign/{id}")
    public void assignAlarmRecord(@PathVariable("id") @Min(value = 1, message = "报警记录主键不能小于1") Long id,
                                  @Valid @RequestBody AssignAlarmRecordCommand param
                                 ) {
        AlarmRecord alarmRecord = this.alarmRecordRepository.detail(id);
        Assert.isNotFound(alarmRecord, "报警记录不存在");
        this.alarmStateContext.assignAlarmRecord(alarmRecord, param);
    }

    /**
     * 报警指派
     *
     * @param id
     * @param param
     */
    @PutMapping("/alarm-record/dynamic/assign/{id}")
    public void dynamicAssignAlarmRecord(@PathVariable("id") @Min(value = 1, message = "报警记录主键不能小于1") Long id,
                                         @Valid @RequestBody DynamicAssignAlarmRecordCommand param
                                        ) {
        AlarmRecord alarmRecord = this.alarmRecordRepository.detail(id);
        Assert.isNotFound(alarmRecord, "报警记录不存在");
        this.alarmStateContext.dynamicAssignAlarmRecord(alarmRecord, param);
    }

    /**
     * 报警移交
     *
     * @param param
     *
     * @return
     */
    @PutMapping("/alarm-record/transfer/{id}")
    public void handlerTransferAlarmRecord(@PathVariable("id") @Min(value = 1, message = "报警记录主键不能小于1") Long id,
                                           @Valid @RequestBody TransferAlarmRecordCommand param
                                          ) {
        AlarmRecord alarmRecord = this.alarmRecordRepository.detail(id);
        Assert.isNotFound(alarmRecord, "报警记录不存在");
        this.alarmStateContext.transferAlarmRecord(alarmRecord, param);
    }

    /**
     * 报警批量移交
     *
     * @param params
     */
    @PostMapping("/alarm-record/assigns")
    public void handlerTransfersAlarmRecord(@Valid @RequestBody List<TransferAlarmRecordCommand> params) {
        this.alarmStateContext.transfersAlarmRecord(params);
    }

    /**
     * 报警处理
     *
     * @param id
     * @param param
     */
    @PutMapping("/alarm-record/handler/{id}")
    public void handlerAlarmRecord(@PathVariable("id") @Min(value = 1, message = "报警记录主键不能小于1") Long id,
                                   @RequestBody HandlerAlarmRecordCommand param
                                  ) {
        AlarmRecord alarmRecord = this.alarmRecordRepository.detail(id);
        Assert.isNotFound(alarmRecord, "报警记录不存在");
        param.setHandlerUser(AlarmRecordFactory.buildAlarmHandlerUser(SecurityContextHolder.getPrincipal().getId()));
        this.alarmStateContext.handlerAlarmRecord(alarmRecord, param);
    }

    /**
     * 报警批量处理
     *
     * @param param
     */
    @PutMapping("/alarm-records/batch-handler")
    public void batchHandlerAlarmRecords(@Valid @RequestBody BatchHandlerAlarmRecordCommand param) {
        param.setHandlerUser(AlarmRecordFactory.buildAlarmHandlerUser(SecurityContextHolder.getPrincipal().getId()));
        this.alarmStateContext.batchHandlerAlarmRecords(param);
    }

    /**
     * 报警一键处理
     */
    @PutMapping("/alarm-records/one-click-handler")
    public void oneClickHandlerAlarmRecords() {
        this.alarmRecordService.oneClickHandlerAlarmRecords();
    }

    /**
     * 获取报警通知人员列表
     *
     * @param businessKey
     *
     * @return
     */
    @GetMapping("/alarm-record/targets/{businessKey}")
    public List<AlarmRecordTargetVO> listAlarmRecordTargets(@PathVariable("businessKey") String businessKey) {
        AlarmRecordPO alarmRecordPO = this.alarmRecordQueryService.getByBusinessKey(businessKey);
        Assert.isNotFound(alarmRecordPO, "报警记录不存在或已删除");
        List<AlarmRecordTargetPO> alarmRecordTargetPOS = this.alarmRecordTargetService.listByAlarmRecordId(alarmRecordPO.getId());
        if (CollectionUtils.isEmpty(alarmRecordTargetPOS)) {
            return Collections.emptyList();
        }
        return ObjectUtils.copying(alarmRecordTargetPOS, AlarmRecordTargetVO.class);
    }

    /**
     * 获取报警流程日志列表
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/alarm-record/operatorLogs/{id}")
    public List<AlarmRecordOperatorLogVO> listAlarmRecordOperatorLogs(
            @PathVariable("id") @Min(value = 1, message = "报警记录主键不能小于1") Long id
                                                                     ) {
        AlarmRecordPO alarmRecordPO = this.alarmRecordQueryService.getById(id);
        if (Objects.isNull(alarmRecordPO)) {
            alarmRecordPO = this.alarmRecordQueryService.getByBusinessKey(id.toString());
        }
        Assert.isNotFound(alarmRecordPO, "报警记录不存在或已删除");
        List<AlarmRecordOperatorLogPO> alarmRecordOperatorLogPOS = this.alarmRecordOperatorLogQueryService
                .listAlarmRecordOperatorLogByRecordId(alarmRecordPO.getId());
        if (CollectionUtils.isEmpty(alarmRecordOperatorLogPOS)) {
            return Collections.emptyList();
        }
        return ObjectUtils.copying(alarmRecordOperatorLogPOS, AlarmRecordOperatorLogVO.class);
    }

    /**
     * 报警状态事件操作列表
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/alarm-record/state-event-operates/{id}")
    public List<AlarmStateEventOperateVO> listAlarmRecordStateEventOperates(
            @PathVariable("id") @Min(value = 1, message = "报警记录主键不能小于1") Long id
                                                                           ) {
        List<AlarmStateEventOperatePO> alarmStateEventOperatePOS = this.alarmStateEventOperateService.listByAlarmRecordId(id);
        List<AlarmStateEventOperateVO> alarmStateEventOperateVOS = new ArrayList<>(alarmStateEventOperatePOS.size());
        alarmStateEventOperatePOS.stream().forEach(alarmStateEventOperate -> {
            AlarmStateEventOperateVO alarmStateEventOperateVO = AlarmStateEventOperateVO
                    .builder()
                    .id(alarmStateEventOperate.getId())
                    .alarmRecordId(alarmStateEventOperate.getAlarmRecordId())
                    .alarmStatus(alarmStateEventOperate.getAlarmStatus())
                    .alarmStatusEvent(alarmStateEventOperate.getAlarmStatusEvent())
                    .operationParam(JsonUtils.deserialize(alarmStateEventOperate.getOperationParam(), Object.class))
                    .createTime(alarmStateEventOperate.getCreateTime())
                    .build();
            alarmStateEventOperateVOS.add(alarmStateEventOperateVO);
        });
        return alarmStateEventOperateVOS;
    }

    /**
     * 设置报警业务快照信息
     *
     * @param snapshot
     */
    @PutMapping("/alarm-record/setting/business-snapshot")
    public void setAlarmBusinessSnapshot(@RequestBody SettingAlarmBusinessSnapshotCommand snapshot) {
        this.alarmRecordCommandService.settingAlarmBusinessSnapshot(snapshot);
    }

    /**
     * 报警催办
     *
     * @param urgingAlarmRecordCommand 请求参数
     */
    @PutMapping("/alarm-record/urging")
    public void urgingAlarmRecord(@RequestBody UrgingAlarmRecordCommand urgingAlarmRecordCommand) {
        this.alarmRecordCommandService.urgingAlarmRecord(urgingAlarmRecordCommand);
    }

    /**
     * 报警记录删除
     *
     * @param command 主键集合
     */
    @PostMapping("/alarm-record/batch-deleted")
    public void removeAlarmRecord(@RequestBody RemoveAlarmRecordCommand command) {
        this.alarmRecordCommandService.removeAlarmRecord(command);
    }

    private AlarmRecordVO getAlarmRecordVO(AlarmRecordPO alarmRecordPO) {
        AlarmRecordVO alarmRecordVO = ObjectUtils.copying(alarmRecordPO, AlarmRecordVO.class);
        if (Objects.nonNull(alarmRecordVO.getAlarmHandlerResult())) {
            alarmRecordVO.setAlarmHandlerResultName(alarmRecordVO.getAlarmHandlerResult().getDescription());
        }
        //跳转配置
        SatAlarmBusinessUrl satAlarmBusinessUrl = this.satAlarmBusinessUrlService
                .getSatAlarmBusinessUrl(alarmRecordPO.getBusinessType(), alarmRecordPO.getApplicationCode(), SessionContextHolder
                        .getRequester().getTerminal());
        if (Objects.nonNull(satAlarmBusinessUrl)) {
            alarmRecordVO.setUrl(satAlarmBusinessUrl.getUrl());
        }
        setAlarmTimeoutStatus(alarmRecordPO, alarmRecordVO);
        List<AlarmRecordHandlerUserPO> alarmRecordHandlerUserPOS = this.alarmRecordHandlerUserQueryService
                .getUsersByAlarmRecordId(alarmRecordVO.getId());
        if (!CollectionUtils.isEmpty(alarmRecordHandlerUserPOS)) {
            alarmRecordVO.setAlarmHandlerName(alarmRecordHandlerUserPOS.stream()
                                                      .map(AlarmRecordHandlerUserPO::getUserName)
                                                      .collect(Collectors.joining(",")));
        }
        return alarmRecordVO;
    }

    private void setHandlerAndNotifyUsers(AlarmRecordVO alarmRecordVO) {
        //待接收状态不显示处理人信息
        if (AlarmStatusEnum.ALARM_UN_CONFIRMED == alarmRecordVO.getAlarmStatus()) {
            alarmRecordVO.setAlarmHandlerName(null);
        }
        //查询报警通知人信息
        List<AlarmRecordTargetPO> alarmRecordTargetPOS = this.alarmRecordTargetService.listByAlarmRecordId(alarmRecordVO.getId());
        if (!CollectionUtils.isEmpty(alarmRecordTargetPOS)) {
            alarmRecordVO.setAlarmNotifyUsername(alarmRecordTargetPOS.stream().map(AlarmRecordTargetPO::getUserName)
                                                         .collect(Collectors.joining(",")));
            alarmRecordVO.setAlarmNotifyUsers(ObjectUtils.copying(alarmRecordTargetPOS, AlarmRecordTargetVO.class));
        }
    }

    private void setAlarmTimeoutStatus(AlarmRecordPO alarmRecordPO, AlarmRecordVO alarmRecordVO) {
        alarmRecordVO.setAlarmHandlerTimeoutStatus(AlarmHandlerTimeoutStatusEnum.NORMAL);
        alarmRecordVO.setAlarmHandlerTimeoutStatusName(AlarmHandlerTimeoutStatusEnum.NORMAL.getDescription());
        if (Objects.nonNull(alarmRecordPO.getTimeoutEquipped()) && alarmRecordPO.getTimeoutEquipped()
            && Objects.nonNull(alarmRecordPO.getTimeoutProcessTime())) {
            //超时处理
            boolean handlerTimeout = AlarmStatusEnum.ALARM_HANDLER == alarmRecordVO.getAlarmStatus() &&
                                     alarmRecordVO.getTimeoutProcessTime()
                                             .isBefore(alarmRecordVO.getAlarmHandlerTime());
            //未处理超时
            boolean unHandlerTimeOut = AlarmStatusEnum.ALARM_HANDLER != alarmRecordVO.getAlarmStatus() &&
                                       alarmRecordVO.getTimeoutProcessTime().isBefore(DateUtils.now());
            if (handlerTimeout || unHandlerTimeOut) {
                alarmRecordVO.setAlarmHandlerTimeoutStatus(AlarmHandlerTimeoutStatusEnum.TIMED_OUT);
                alarmRecordVO.setAlarmHandlerTimeoutStatusName(AlarmHandlerTimeoutStatusEnum.TIMED_OUT.getDescription());
            }
        }
    }

    private void setAlarmPageTimeoutStatus(AlarmRecordPageVO alarmRecordPageVO) {
        alarmRecordPageVO.setAlarmHandlerTimeoutStatus(AlarmHandlerTimeoutStatusEnum.NORMAL);
        alarmRecordPageVO.setAlarmHandlerTimeoutStatusName(AlarmHandlerTimeoutStatusEnum.NORMAL.getDescription());
        if (Objects.nonNull(alarmRecordPageVO.getTimeoutEquipped()) && alarmRecordPageVO.getTimeoutEquipped()
            && Objects.nonNull(alarmRecordPageVO.getTimeoutProcessTime())) {
            //超时处理
            boolean handlerTimeout = AlarmStatusEnum.ALARM_HANDLER == alarmRecordPageVO.getAlarmStatus() &&
                                     alarmRecordPageVO.getTimeoutProcessTime()
                                             .isBefore(alarmRecordPageVO.getAlarmHandlerTime());
            //未处理超时
            boolean unHandlerTimeOut = AlarmStatusEnum.ALARM_HANDLER != alarmRecordPageVO.getAlarmStatus() &&
                                       alarmRecordPageVO.getTimeoutProcessTime()
                                               .isBefore(DateUtils.now());
            if (handlerTimeout || unHandlerTimeOut) {
                alarmRecordPageVO.setAlarmHandlerTimeoutStatus(AlarmHandlerTimeoutStatusEnum.TIMED_OUT);
                alarmRecordPageVO.setAlarmHandlerTimeoutStatusName(AlarmHandlerTimeoutStatusEnum.TIMED_OUT.getDescription());
            }
        }
    }
}
