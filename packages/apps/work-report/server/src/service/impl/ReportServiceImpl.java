package com.guhecloud.cube.srv.gen.reportmanagement.server.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.flow.client.FlowClient;
import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.base.flow.client.ProcessInstanceClient;
import com.guhecloud.cube.base.flow.client.dto.ProcessInstanceDTO;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.reportmanagement.server.dao.ReportMapper;
import com.guhecloud.cube.srv.gen.reportmanagement.server.entity.Report;
import com.guhecloud.cube.srv.gen.reportmanagement.server.entity.ReportType;
import com.guhecloud.cube.srv.gen.reportmanagement.server.param.ReportAddOrUpdateParam;
import com.guhecloud.cube.srv.gen.reportmanagement.server.param.ReportBatchDeleteParam;
import com.guhecloud.cube.srv.gen.reportmanagement.server.param.ReportQueryParam;
import com.guhecloud.cube.srv.gen.reportmanagement.server.param.ReportQueryProcessParam;
import com.guhecloud.cube.srv.gen.reportmanagement.server.param.StatisticsParam;
import com.guhecloud.cube.srv.gen.reportmanagement.server.service.ReportService;
import com.guhecloud.cube.srv.gen.reportmanagement.server.service.ReportTypeService;
import com.guhecloud.cube.srv.gen.reportmanagement.server.vo.ReportFieldVo;
import com.guhecloud.cube.srv.gen.reportmanagement.server.vo.ReportStatistics;
import com.guhecloud.cube.srv.gen.reportmanagement.server.vo.ReportStatisticsVo;
import com.guhecloud.cube.srv.gen.reportmanagement.server.vo.ReportTypeNum;
import com.guhecloud.cube.srv.gen.reportmanagement.server.vo.ReportVo;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import instance.event.ProcessStartedMessage;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import util.CustBeanUtil;
import vo.FormFieldVO;

import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ReportServiceImpl extends ServiceImpl<ReportMapper, Report> implements ReportService {

    private final ReportMapper reportMapper;
    private final ReportTypeService reportTypeService;
    private final UserClient userClient;
    private final FileRefClient fileRefClient;
    private final ProcessInstanceClient processInstanceClient;
    private final FlowClient flowClient;
    String startTime = "";
    String endTime = "";

    @Override
    public void addReport(ReportAddOrUpdateParam param) {
        long id = IdWorker.getId();
        Report copying = ObjectUtils.copying(param, Report.class);
        copying.setId(id);
        copying.setStatus(FlowResult.ACTIVE);
        copying.setStartTime(LocalDateTime.now());
        copying.setReportBy(String.valueOf(SecurityContextHolder.getPrincipal().getId()));
        //关联附件
        if (Objects.nonNull(param.getAppendixId())) {
            this.fileRefClient.save(id, param.getAppendixId());
        }
        this.save(copying);
    }

    @Override
    public void cancelReport(Long id) {
        Report report = reportMapper.selectById(id);
        report.setStatus(FlowResult.UNDO);
        reportMapper.updateById(report);
    }

    @Override
    public Pagination<ProcessInstanceDTO> getReportPage(ReportQueryProcessParam param) {
        //获取流引擎中的数据
        if (StringUtils.isBlank(param.getApplicationCode())) {
            param.setApplicationCode("WORK_REPORT");
        }
        if (Objects.nonNull(param.getReportTypeId()) || Objects.nonNull(param.getStartUserId())) {
            if (Objects.nonNull(param.getReportTypeId()) && 0L == param.getReportTypeId()) {
                param.setReportTypeId(null);
                param.setIsNoType(true);
            }
            List<Report> reportList =
                    this.lambdaQuery()
                            .select(Report::getProcessInsId)
                            .eq(Objects.nonNull(param.getReportTypeId()), Report::getTypeId, param.getReportTypeId())
                            .eq(Objects.nonNull(param.getStartUserId()), Report::getReportBy, param.getStartUserId())
                            .isNull(param.getIsNoType(), Report::getTypeId)
                            .list();
            if (CollectionUtils.isNotEmpty(reportList)) {
                String processInsIds = reportList.stream().map(Report::getProcessInsId)
                        .collect(Collectors.joining(","));
                param.setProcessInstanceIds(processInsIds);
            }
        }
        Pagination<ProcessInstanceDTO> processInstanceDTOPagination = processInstanceClient.allPage(param);
        if (CollectionUtils.isEmpty(processInstanceDTOPagination.getRecords())) {
            return new Pagination<>();
        }
        Set<String> processInsIds = new HashSet<>();
        for (ProcessInstanceDTO record : processInstanceDTOPagination.getRecords()) {
            processInsIds.add(record.getProcessInstanceId());
        }
        //不存在业务表的进行同步
        List<Report> existReport = this.lambdaQuery()
                .select(Report::getProcessInsId)
                .in(Report::getProcessInsId, processInsIds).list();
        if (CollectionUtils.isNotEmpty(existReport)) {
            processInsIds = processInsIds.stream().filter(
                            t -> !existReport.stream()
                                    .map(Report::getProcessInsId).collect(Collectors.toList())
                                    .contains(t))
                    .collect(Collectors.toSet());
        }
        if (CollectionUtils.isNotEmpty(processInsIds)) {
            Set<String> finalProcessInsIds = processInsIds;
            List<Report> needSaveReport = new ArrayList<>();
            processInstanceDTOPagination.getRecords().stream().filter(t ->
                                                                              finalProcessInsIds.contains(t.getProcessInstanceId()))
                    .forEach(process -> {

                        ProcessStartedMessage formInfo = processInstanceClient.getProcessInstanceFormInfo(process.getProcessInstanceId());
                        ReportAddOrUpdateParam saveReport = this.synFlowData(formInfo);
                        Report report = ObjectUtils.copying(saveReport, Report.class);
                        report.setId(IdWorker.getId());
                        if (StringUtils.isBlank(report.getReportBy())) {
                            report.setReportBy(Objects.nonNull(process.getStartUserId())
                                               ? String.valueOf(process.getStartUserId())
                                               : "");
                        }
                        report.setStatus(FlowResult.valueOf(process.getStatus()));
                        if (Objects.nonNull(process.getStartUserInfo())) {
                            report.setDepartmentId(process.getStartUserInfo().getOrganizationId().toString());
                        }
                        needSaveReport.add(report);
                    });
            this.saveBatch(needSaveReport);
        }
        return processInstanceDTOPagination;
    }

    @Override
    public ReportStatisticsVo getReportStatisticsInfo(StatisticsParam param) {
        String startTime = null;
        var date = LocalDateTime.now();
        var endTime = DateUtils.format(LocalDateTime.now());
        if (param != null) {
            if (param.getTimeFlag().equals("THIS_MONTH")) {
                startTime = DateUtils.beginOfMonth(date).toString();
            } else if (param.getTimeFlag().equals("THIS_WEEK")) {
                startTime = DateUtils.beginOfWeek(date).toString();
            } else if (param.getTimeFlag().equals("TODAY")) {
                startTime = DateUtils.beginOfDay(date).toString();
            } else if (param.getTimeFlag().equals("THIS_YEAR")) {
                startTime = DateUtils.beginOfYear(date).toString();
            }
        }
        return reportMapper.selectReportStatisticsInfo(param, startTime, endTime);
    }

    @Override
    public ReportVo getReportDetails(Long id) {
        Report report = reportMapper.selectById(id);
        ReportVo copying = ObjectUtils.copying(report, ReportVo.class);
        ReportType reportType = reportTypeService.getById(report.getTypeId());
        copying.setType(reportType.getName());
        return copying;
    }

    @Override
    public Pagination<ReportVo> getReportPending(ReportQueryParam param) {
        Page<Report> page = this.page(new Page<>(), Wrappers.<Report>lambdaQuery()
                .eq(Report::getReportPname, SecurityContextHolder.getPrincipal().getId())
                .eq(Report::getStatus, FlowResult.ACTIVE)
                .between(Objects.nonNull(param.getPeriod()), Report::getCreateTime, startTime, endTime)
                .orderByDesc(Report::getCreateTime));
        UserDTO user = userClient.getUser(SecurityContextHolder.getPrincipal().getId());
        if (!page.getRecords().isEmpty()) {
            page.getRecords().stream().forEach(r -> {
                r.setReportBy(user.getName());
            });
        }
        Pagination<ReportVo> convert = PagingUtils.convert(page, ReportVo.class);
        return convert;
    }

    @Override
    public Pagination<ReportVo> getReportInitiate(ReportQueryParam param) {
        Page<Report> page = this.page(new Page<>(), Wrappers.<Report>lambdaQuery()
                .eq(Report::getReportBy, SecurityContextHolder.getPrincipal().getId())
                .between(Objects.nonNull(param.getPeriod()), Report::getCreateTime, startTime, endTime)
                .orderByDesc(Report::getCreateTime));
        UserDTO user = userClient.getUser(SecurityContextHolder.getPrincipal().getId());
        if (!page.getRecords().isEmpty()) {
            page.getRecords().stream().forEach(r -> {
                r.setReportBy(user.getName());
            });
        }
        Pagination<ReportVo> convert = PagingUtils.convert(page, ReportVo.class);
        return convert;
    }

    @Override
    public ReportAddOrUpdateParam synFlowData(ProcessStartedMessage message) {
        Map<String, Object> processVariables = message.getProcessVariables();
        ReportAddOrUpdateParam param = new ReportAddOrUpdateParam();
        param.setProcessInsId(message.getProcessInstanceId());
        JSONObject data;
        try {
            data = JSONObject.parseObject(message.getData());
        } catch (Exception ex) {
            log.info("工作汇报流程配置有误，请检查流程配置ProcessInstanceId:{},message.data:{}", message.getProcessInstanceId(), message.getData());
            data = new JSONObject();
        }
        List<FormFieldVO> formField = CustBeanUtil.getFormField(ReportFieldVo.class);
        Map<String, Field> paramMap = Arrays.stream(param.getClass().getDeclaredFields())
                .collect(Collectors.toMap(Field::getName, Function.identity()));
        for (FormFieldVO formFieldVO : formField) {
            Object object = data.getObject(formFieldVO.getId(), Object.class);
            Field field = paramMap.get(formFieldVO.getId());
            if (Objects.isNull(object) || Objects.isNull(field)) {
                continue;
            }
            field.setAccessible(true);
            try {
                String info = "";
                if (object instanceof String) {
                    info = (String) object;
                } else if (object instanceof JSONArray) {
                    Object[] array = ((JSONArray) object).toArray();
                    StringBuilder arrayStr = new StringBuilder();
                    for (Object o : array) {
                        if (StringUtils.isNotBlank(arrayStr.toString())) {
                            arrayStr.append(",");
                        }
                        arrayStr.append(o);
                    }
                    info = arrayStr.toString();
                }
                if (field.getType().equals(Long.class)) {
                    field.set(param, StringUtils.isNotBlank(info) ? Long.parseLong(info) : 0L);
                } else {
                    field.set(param, info);
                }

            } catch (Exception e) {
                log.error("set param fail because ： {}", e);
                throw new RuntimeException(e.getMessage());
            }
        }
        if (StringUtils.isBlank(param.getReportName()) &&
            Objects.nonNull(processVariables.get("internal_taskName"))) {
            param.setReportName((String) processVariables.get("internal_taskName"));
        }
        if (StringUtils.isBlank(param.getReportBy())) {
            param.setReportBy(Objects.nonNull(message.getStartUserId())
                              ? String.valueOf(message.getStartUserId())
                              : "");
        }
        param.setStartTime(message.getStartTime());
        return param;
    }

    @Override
    public Pagination<ReportStatistics> reportStatistics(ReportQueryParam param) {
        Set<Long> reportByIdList = new HashSet<>();
        if (StringUtils.isNotBlank(param.getUserName())) {
            List<UserDTO> userDTOS = userClient.listUsers(param.getUserName());
            if (CollectionUtils.isEmpty(userDTOS)) {
                return new Pagination<>();
            } else {
                reportByIdList.addAll(userDTOS.stream().map(UserDTO::getId).collect(Collectors.toList()));
            }
        }
        if (Objects.nonNull(param.getStartUserId())) {
            reportByIdList.add(param.getStartUserId());
        }
        if (StringUtils.isNotBlank(param.getDepartmentId())) {
            reportByIdList.addAll(userClient.listUserByOrgAndPost(Collections.singletonList(Long.valueOf(param.getDepartmentId())), new ArrayList<>()));
        }
        if (CollectionUtils.isNotEmpty(reportByIdList)) {
            param.setReportByList(new ArrayList<>(reportByIdList));
        }
        //获取分页参数信息
        Page<ReportStatistics> reportStatisticsPage = reportMapper.selectReportStatisticsPage(PagingUtils.convert(param), param);
        if (Objects.isNull(reportStatisticsPage) || CollectionUtils.isEmpty(reportStatisticsPage.getRecords())) {
            return new Pagination<>();
        }
        List<String> userIds = reportStatisticsPage.getRecords().stream().map(ReportStatistics::getUserId).collect(Collectors.toList());
        //获取表中所有提交人
        List<Long> allUserId = this.lambdaQuery().select(Report::getReportBy).list()
                .stream().map(t -> Long.parseLong(t.getReportBy())).collect(Collectors.toList());
        param.setReportByList(allUserId);
        List<ReportStatistics> reportStatistics = reportMapper.selectReportStatistics(param);

        setDepartmentData(param, reportStatistics);

        reportStatisticsPage.setRecords(reportStatistics.stream().filter(t -> userIds.contains(t.getUserId()))
                                                .collect(Collectors.toList()));

        return PagingUtils.convert(reportStatisticsPage);
    }

    private void setDepartmentData(ReportQueryParam param, List<ReportStatistics> reportStatistics) {

        Map<String, Long> userNumMap = new HashMap<>();
        for (ReportStatistics record : reportStatistics) {
            if (CollectionUtils.isNotEmpty(record.getReportTypeNumList())) {
                Long aLong = record.getReportTypeNumList().stream().map(ReportTypeNum::getTypeNum).reduce(Long::sum).get();
                userNumMap.merge(record.getUserId(), aLong, Long::sum);
            } else {
                userNumMap.merge(record.getUserId(), 0L, Long::sum);
            }
        }
        List<UserDTO> userDTOS = userClient.listUserByIds(userNumMap.keySet().stream().map(Long::valueOf).collect(Collectors.toList()));
        Map<Long, Long> departmentNumMap = new HashMap<>();
        Map<String, UserDTO> userDepatmentMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(userDTOS)) {
            Set<Long> departmentIds = new HashSet<>();
            for (UserDTO userDTO : userDTOS) {
                userDepatmentMap.put(userDTO.getId().toString(), userDTO);
                if (Objects.nonNull(userDTO.getOrganization()) && Objects.nonNull(userDTO.getOrganization().getId())) {
                    departmentIds.add(userDTO.getOrganization().getId());
                }
            }
            List<Long> userIdList = userClient.listUserByOrgAndPost(new ArrayList<>(departmentIds), new ArrayList<>());
            param.setReportByList(userIdList);
            List<ReportStatistics> reportStatisticsGroupByDepartment = reportMapper.selectReportStatisticsGroupByDepartment(param);
            //部门是否变更
            for (ReportStatistics statistics : reportStatisticsGroupByDepartment) {
                if (userDepatmentMap.containsKey(statistics.getUserId())) {
                    UserDTO userDTO = userDepatmentMap.getOrDefault(statistics.getUserId(), new UserDTO());
                    if (Objects.nonNull(userDTO.getOrganization())) {
                        departmentNumMap.merge(userDTO.getOrganization().getId(), statistics.getUserNum(), Long::sum);
                    }
                }
            }
        }
        for (ReportStatistics record : reportStatistics) {
            if (Objects.nonNull(record.getUserId())) {
                UserDTO userDTO = userDepatmentMap.getOrDefault(record.getUserId(), new UserDTO());
                record.setUserName(userDTO.getName());
                record.setUserNum(userNumMap.get(record.getUserId()));
                if (Objects.nonNull(userDTO.getOrganization())) {
                    record.setOrganizationId(userDTO.getOrganization().getId());
                    record.setDepartmentName(userDTO.getOrganization().getName());
                    record.setDepartmentNum(departmentNumMap.get(userDTO.getOrganization().getId()));
                }
            }
            for (ReportTypeNum reportTypeNum : record.getReportTypeNumList()) {
                if (StringUtils.isBlank(reportTypeNum.getTypeName())) {
                    reportTypeNum.setTypeName("其他");
                    reportTypeNum.setTypeId(0L);
                }
            }

        }
    }

    @Override
    public void reportStatisticsExport(ReportQueryParam param) {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attr.getResponse();
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        try {
            // 这里URLEncoder.encode可以防止中文乱码
            String excelName = URLEncoder.encode("工作汇报统计导出", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + excelName + ExcelTypeEnum.XLSX.getValue());
            //头数据
            List<List<String>> headerList = new ArrayList<>();
            //行数据
            List<List<Object>> data = new ArrayList<>();
            Arrays.stream(ReportStatistics.class.getDeclaredFields())
                    .forEach(t -> {
                        ExcelProperty annotation = t.getAnnotation(ExcelProperty.class);
                        if (Objects.nonNull(annotation)) {
                            headerList.add(Arrays.asList(annotation.value()));
                        }
                    });
            Set<Long> userIdList = new HashSet<>();
            if (StringUtils.isNotBlank(param.getUserName())) {
                List<UserDTO> userDTOS = userClient.listUsers(param.getUserName());
                if (CollectionUtils.isEmpty(userDTOS)) {
                    //根据用户查不到数据返回空模板
                    EasyExcel.write(response.getOutputStream()).sheet("工作汇报统计")
                            .head(headerList)
                            .doWrite(data);
                    return;
                } else {
                    userIdList.addAll(userDTOS.stream().map(UserDTO::getId).collect(Collectors.toSet()));
                }
            }
            if (StringUtils.isNotBlank(param.getDepartmentId())) {
                userIdList.addAll(userClient.listEnableUserByOrgAndPost(Collections.singletonList(Long.valueOf(param.getDepartmentId())), new ArrayList<>()));
            }
            //勾选的用户数据
            if (StringUtils.isNotBlank(param.getSelectReportBys())) {
                userIdList.addAll(Arrays.stream(param.getSelectReportBys().split(","))
                                          .map(Long::valueOf)
                                          .collect(Collectors.toList()));
            }
            userIdList.removeIf(Objects::isNull);
            param.setReportByList(new ArrayList<>(userIdList));
            List<ReportStatistics> reportStatistics = this.reportMapper.selectReportStatistics(param);
            if (CollectionUtils.isNotEmpty(reportStatistics)) {
                this.setDepartmentData(param, reportStatistics);
                List<ReportStatistics> sortedList = reportStatistics.stream()
                        .filter(t -> CollectionUtils.isNotEmpty(t.getReportTypeNumList()))
                        .sorted(Comparator.comparing(t -> t.getReportTypeNumList().size()))
                        .collect(Collectors.toList());
                List<String> typeNameList = sortedList.stream()
                        .map(ReportStatistics::getReportTypeNumList)
                        .flatMap(Collection::stream)
                        .map(ReportTypeNum::getTypeName)
                        .distinct().collect(Collectors.toList());

                typeNameList.forEach(t -> {
                    headerList.add(Collections.singletonList(t + "汇报数"));
                });

                for (ReportStatistics reportStatistic : reportStatistics) {
                    List<Object> objectList = new ArrayList<>();
                    Arrays.stream(reportStatistic.getClass().getDeclaredFields())
                            .forEach(t -> {
                                if (Objects.nonNull(t.getAnnotation(ExcelProperty.class))) {
                                    try {
                                        t.setAccessible(true);
                                        objectList.add(Objects.nonNull(t.get(reportStatistic)) ? t.get(reportStatistic) : "");
                                    } catch (IllegalAccessException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                                if ("reportTypeNumList".equals(t.getName()) &&
                                    CollectionUtils.isNotEmpty(reportStatistic.getReportTypeNumList())) {
                                    Map<String, Long> stringLongMap = reportStatistic.getReportTypeNumList()
                                            .stream().collect(Collectors.toMap(ReportTypeNum::getTypeName, ReportTypeNum::getTypeNum));
                                    for (String typeName : typeNameList) {
                                        objectList.add(stringLongMap.getOrDefault(typeName, 0L));
                                    }

                                }
                            });
                    data.add(objectList);
                }
            }
            EasyExcel.write(response.getOutputStream()).sheet("工作汇报统计")
                    .head(headerList)
                    .doWrite(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reportBatchDelete(ReportBatchDeleteParam param) {
        if (CollectionUtils.isEmpty(param.getProcessInstanceIds())) {
            return;
        }
        List<Report> list = this.lambdaQuery().in(Report::getProcessInsId, param.getProcessInstanceIds()).list();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        DatabaseContextHolder.softDelete(Report.class, list);
        flowClient.deleteFormFlow(param.getProcessInstanceIds());
    }
}
