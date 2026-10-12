package com.guhecloud.cube.srv.gen.safety.management.report.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.ChartSingleSeriesRenderData;
import com.deepoove.poi.data.Charts;
import com.deepoove.poi.plugin.table.LoopRowTableRenderPolicy;
import com.guhecloud.cube.base.alarm.client.AlarmStatisticsClient;
import com.guhecloud.cube.base.alarm.client.param.AlarmStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.client.vo.AlarmRecordReportVO;
import com.guhecloud.cube.base.alarm.client.vo.AlarmRecordReportVO1;
import com.guhecloud.cube.base.device.center.metadata.vo.DeviceBaseInfoAndNumberVO;
import com.guhecloud.cube.base.device.center.metadata.vo.DeviceBaseInfoSafetyReportVO;
import com.guhecloud.cube.base.device.center.metadata.vo.DeviceFireSafetyReportVO;
import com.guhecloud.cube.base.device.center.std.statistics.DeviceBaseStatisticsClient;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.param.FileModifyParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.emergency.client.EmergencyFileClient;
import com.guhecloud.cube.biz.emergency.client.EmergencyRuleClient;
import com.guhecloud.cube.biz.emergency.client.param.EmergencyRuleApplicationQuery;
import com.guhecloud.cube.biz.emergency.client.param.EmergencyRuleSafetyReportVO;
import com.guhecloud.cube.biz.emergency.client.vo.EmergencyFileEventTypeVO;
import com.guhecloud.cube.biz.emergency.client.vo.EmergencyFileSafetyReportVO;
import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.TenantClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.TenantDTO;
import com.guhecloud.cube.property.management.client.property.param.property.RiskSourceDeviceQueryParam;
import com.guhecloud.cube.property.management.client.property.vo.property.RiskSourceDeviceVO;
import com.guhecloud.cube.property.management.client.risk.source.client.RiskSourceLedgerClient;
import com.guhecloud.cube.srv.gen.loose.works.client.ProjectScheduleClient;
import com.guhecloud.cube.srv.gen.loose.works.vo.ProjectDataForReportVO;
import com.guhecloud.cube.srv.gen.maintain.client.HiddenDangerClient;
import com.guhecloud.cube.srv.gen.maintain.client.InspectionClient;
import com.guhecloud.cube.srv.gen.maintain.client.PatrolLocationClient;
import com.guhecloud.cube.srv.gen.maintain.vo.ElectronLocationSafetyReportVO;
import com.guhecloud.cube.srv.gen.maintain.vo.ElectronTaskSafetyReportVO;
import com.guhecloud.cube.srv.gen.maintain.vo.HiddenDangerHandleVO;
import com.guhecloud.cube.srv.gen.maintain.vo.HiddenDangerReportVO;
import com.guhecloud.cube.srv.gen.maintain.vo.InspectionSafetyReportVO;
import com.guhecloud.cube.srv.gen.maintain.vo.SafetyReportResultVO;
import com.guhecloud.cube.srv.gen.safety.education.client.CultivateTaskClient;
import com.guhecloud.cube.srv.gen.safety.education.client.query.SafetyEducationStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.education.client.vo.SafetyEducationTaskChainVO;
import com.guhecloud.cube.srv.gen.safety.education.client.vo.SafetyEducationThemeRatioVO;
import com.guhecloud.cube.srv.gen.safety.management.report.dao.AlarmSafetyReportMapper;
import com.guhecloud.cube.srv.gen.safety.management.report.entity.AlarmSafetyReport;
import com.guhecloud.cube.srv.gen.safety.management.report.enumeration.SafetyReportEnum;
import com.guhecloud.cube.srv.gen.safety.management.report.param.AlarmSafetyReportParam;
import com.guhecloud.cube.srv.gen.safety.management.report.service.AlarmSafetyReportService;
import com.guhecloud.cube.srv.gen.safety.management.report.vo.AlarmEventVO;
import com.guhecloud.cube.srv.gen.safety.management.report.vo.AlarmRecordResponseVO;
import com.guhecloud.cube.srv.gen.safety.management.report.vo.AlarmRecordVO;
import com.guhecloud.cube.srv.gen.safety.management.report.vo.AlarmSafetyReportVO;
import com.guhecloud.cube.srv.gen.safety.management.report.vo.InspectionDepartmentVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.BeanUtils;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.MathUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.SpringFileUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static com.guhecloud.cube.srv.gen.safety.management.report.constant.SafetyReportConstant.ALARM_HANDLER_SORT;
import static com.guhecloud.cube.srv.gen.safety.management.report.constant.SafetyReportConstant.ALARM_OUT_OF_DAY_SORT;
import static com.guhecloud.cube.srv.gen.safety.management.report.constant.SafetyReportConstant.ALARM_OUT_OF_DAY_SORT_USER;
import static com.guhecloud.cube.srv.gen.safety.management.report.constant.SafetyReportConstant.ALARM_RESPONSE_SORT;
import static com.guhecloud.cube.srv.gen.safety.management.report.constant.SafetyReportConstant.ALARM_RESPONSE_SORT_USER;
import static com.guhecloud.cube.srv.gen.safety.management.report.constant.SafetyReportConstant.APPLICATION;
import static com.guhecloud.cube.srv.gen.safety.management.report.constant.SafetyReportConstant.ELECTRIC_MANAGE;
import static com.guhecloud.cube.srv.gen.safety.management.report.constant.SafetyReportConstant.ERROR_MESSAGE;
import static com.guhecloud.cube.srv.gen.safety.management.report.constant.SafetyReportConstant.FIRE_CONTROL;
import static com.guhecloud.cube.srv.gen.safety.management.report.constant.SafetyReportConstant.GAS_MANAGEMENT;
import static com.guhecloud.cube.srv.gen.safety.management.report.constant.SafetyReportConstant.LEVEL;
import static com.guhecloud.cube.srv.gen.safety.management.report.constant.SafetyReportConstant.NORMAL_MESSAGE;
import static com.guhecloud.cube.srv.gen.safety.management.report.constant.SafetyReportConstant.NO_DATA_TEMP;
import static com.guhecloud.cube.srv.gen.safety.management.report.constant.SafetyReportConstant.ONE_LEVEL;
import static com.guhecloud.cube.srv.gen.safety.management.report.constant.SafetyReportConstant.RISK_ERROR_MESSAGE;
import static com.guhecloud.cube.srv.gen.safety.management.report.constant.SafetyReportConstant.ROTATING_MANAGE;
import static com.guhecloud.cube.srv.gen.safety.management.report.constant.SafetyReportConstant.SECURITY_MONITORING;
import static com.guhecloud.cube.srv.gen.safety.management.report.constant.SafetyReportConstant.TWO_LEVEL;
import static com.guhecloud.cube.srv.gen.safety.management.report.constant.SafetyReportConstant.UN_ENABLE_MESSAGE;
import static com.guhecloud.cube.srv.gen.safety.management.report.constant.SafetyReportConstant.getAlarmEventList;
import static com.guhecloud.cube.srv.gen.safety.management.report.constant.SafetyReportConstant.getApplicationCodeList;
import static com.guhecloud.cube.srv.gen.safety.management.report.constant.SafetyReportConstant.getDeviceNameEnumList;
import static com.guhecloud.cube.srv.gen.safety.management.report.constant.SafetyReportConstant.getDeviceNameList;
import static com.guhecloud.cube.srv.gen.safety.management.report.constant.SafetyReportConstant.getElectricAlarmEventList;
import static com.guhecloud.cube.srv.gen.safety.management.report.constant.SafetyReportConstant.getFireAlarmEventList;
import static com.guhecloud.cube.srv.gen.safety.management.report.constant.SafetyReportConstant.getGasAlarmEventList;
import static com.guhecloud.cube.srv.gen.safety.management.report.constant.SafetyReportConstant.getRoatingAlarmEventList;
import static com.guhecloud.cube.srv.gen.safety.management.report.constant.SafetyReportConstant.getSecurityAlarmEventList;

/**
 * @Author: lijie
 * @CreateTime: 2024-04-10  11:40
 * @Description:
 * @Version: 1.0
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmSafetyReportServiceImpl extends ServiceImpl<AlarmSafetyReportMapper, AlarmSafetyReport> implements AlarmSafetyReportService {

    public static final String APPLICATION_CONSTANTS = "builtin.app::";
    public static final String TEMPLATE_NAME = "security_report_template.docx";
    private static final CopyOnWriteArrayList<String> tableParam = new CopyOnWriteArrayList<>();

    private final TenantClient tenantClient;
    private final FileClient fileClient;
    private final CultivateTaskClient cultivateTaskClient;
    private final DeviceBaseStatisticsClient deviceBaseStatisticsClient;
    private final AlarmStatisticsClient alarmStatisticsClient;
    private final HiddenDangerClient hiddenDangerClient;
    private final ProjectScheduleClient projectScheduleClient;
    private final InspectionClient inspectionClient;
    private final PatrolLocationClient patrolLocationClient;
    private final EmergencyFileClient emergencyFileClient;
    private final EmergencyRuleClient emergencyRuleClient;
    private final RiskSourceLedgerClient riskSourceLedgerClient;
    private final OrganizationClient organizationClient;

    @Override
    public Pagination<AlarmSafetyReportVO> securityReportPage(AlarmSafetyReportParam param) {
        Page<AlarmSafetyReport> page = new Page<>(param.getPageNumber(), param.getPageSize());
        page = this.baseMapper.selectPage(page, new LambdaQueryWrapper<AlarmSafetyReport>()
                .like(ObjectUtils.isNotEmpty(param.getKeyword()), AlarmSafetyReport::getName, param.getKeyword())
                .orderByDesc(AlarmSafetyReport::getCreateTime));
        return PagingUtils.convert(page, AlarmSafetyReportVO.class);
    }

    /**
     * 生成月报
     */
    @Override
    public void addSafetyReport(LocalDateTime reportDate, String param) {
        String fileName = "security_report_template.docx";
        List<TenantDTO> tenants = tenantClient.listTenants(param);
        tenants.forEach(ex -> {
            SessionContextHolder.requestTenant(ex.getCode());
            AlarmSafetyReport safetyReport = new AlarmSafetyReport();
            String date = null;
            if (StringUtils.isNotBlank(param)) {
                var offset = DateUtils.offset(reportDate, ChronoUnit.MONTHS, -1);
                date = DateUtils.format(offset, "yyyy年MM月");
            } else {
                date = DateUtils.format(reportDate, "yyyy年MM月");
            }
            safetyReport.setName(ex.getName() + date + "安全报告");
            // 填充word、
            XWPFTemplate xwpfTemplate = null;
            try {
                xwpfTemplate = this.addSafetyReportWord(ex, param);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("生成报告结果失败:{}", e);
            } finally {
                tableParam.clear();
            }

            if (ObjectUtils.isNotEmpty(xwpfTemplate)) {
                // 上传报告到服务器
                Long fileId = this.uploadWord(xwpfTemplate, fileName, ex);
                if (ObjectUtils.isNotEmpty(fileId)) {
                    safetyReport.setFileId(fileId);
                    Boolean bool = this.save(safetyReport);
                    log.info("{}：生成报告结果:{}", ex.getName(), bool);
                } else {
                    log.error("上传文件报错");
                }
            } else {

            }
        });
    }

    /**
     * 根据模板生成安全报告word
     */
    private XWPFTemplate addSafetyReportWord(TenantDTO tenantDTO, String jobParam) {

        //        一、总述
        //        1.1
        //基础数据
        var now = DateUtils.parse(DateUtils.format(LocalDateTime.now(), DateUtils.DEFAULT_DATETIME_FORMATTER));
        Period period;
        int month = now.getMonthValue();
        if (StringUtils.isNotBlank(jobParam)) {
            var offset = DateUtils.offset(now, ChronoUnit.MONTHS, -1);
            period = new Period(DateUtils.beginOfMonth(offset), DateUtils.endOfMonth(offset));
            month = month - 1;
        } else {
            period = new Period(DateUtils.beginOfMonth(now), now);
        }
        List<String> applicationList = getApplicationCodeList();
        //动态字段
        Map<String, Object> dataMap = new HashMap<>();
        // 设置市场信息
        dataMap.put("marketName", tenantDTO.getName() + now.getYear() + "年" + month + "月");
        dataMap.put("year", now.getYear());
        dataMap.put("month", month);
        dataMap.put("runningDays", DateUtils.between(Optional.ofNullable(tenantDTO.getCreateTime()).orElse(now), now, ChronoUnit.DAYS));

        //整体情况说明
        //1、设备运行情况
        this.buildEquipmentOperationStatus(applicationList, period, dataMap);

        //2、报警时间统计
        //参数组装
        AlarmStatisticsQueryParam param = new AlarmStatisticsQueryParam();
        //        param.setApplicationCodeList(applicationList);
        param.setPeriod(period);
        //当前
        AlarmRecordReportVO1 alarmRecordByApplication = alarmStatisticsClient.getAlarmRecordByApplication(param);
        List<AlarmRecordReportVO> alarmRecordReportVOS = alarmRecordByApplication.getAlarmRecordReportVOS();

        Map<String, AlarmRecordVO> groupByLevelNow = groupByParamType(alarmRecordReportVOS, LEVEL);
        Map<String, AlarmRecordVO> groupByApplicationNow = groupByParamType(alarmRecordReportVOS, APPLICATION);
        //根据部门分组排序
        List<AlarmRecordReportVO> departmentAlarmRecordReportVOS = alarmRecordByApplication.getDepartmentAlarmRecordReportVOS();
        Map<String, List<AlarmRecordReportVO>> departmentAlarmMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(departmentAlarmRecordReportVOS)) {
            //部门名称
            List<Long> collect = departmentAlarmRecordReportVOS.stream().filter(t -> StringUtils.isBlank(t.getDepartmentName()))
                    .map(t -> Long.parseLong(t.getDepartmentId())).distinct().collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(collect)) {
                Map<Long, String> departmentMap = organizationClient.getOrganizationsByIds(collect)
                        .stream().collect(Collectors.toMap(OrganizationDTO::getId, OrganizationDTO::getName, (k1, k2) -> k1));
                for (AlarmRecordReportVO recordReportVO : departmentAlarmRecordReportVOS) {
                    if (StringUtils.isNotBlank(recordReportVO.getDepartmentId())) {
                        Long aLong = Long.valueOf(recordReportVO.getDepartmentId());
                        if (departmentMap.containsKey(aLong)) {
                            recordReportVO.setDepartmentName(departmentMap.get(aLong));
                        }
                    }
                }
            }
            departmentAlarmMap = departmentAlarmRecordReportVOS.stream()
                    .filter(t -> StringUtils.equalsAny(t.getAlarmLevelCode(), ONE_LEVEL, TWO_LEVEL))
                    .collect(Collectors.groupingBy(AlarmRecordReportVO::getAlarmLevelCode));
        }

        //环比
        List<AlarmRecordReportVO> ringRecordReportVOS = alarmRecordByApplication.getRingRecordReportVOS();
        Long ringRecordTotalNum = 0L;
        if (!CollectionUtils.isEmpty(ringRecordReportVOS)) {
            ringRecordTotalNum = ringRecordReportVOS.stream().map(AlarmRecordReportVO::getAlarmNum)
                    .reduce(Long::sum).get();
        }
        Map<String, AlarmRecordVO> groupByLevelRing = groupByParamType(ringRecordReportVOS, LEVEL);
        Map<String, AlarmRecordVO> groupByApplicationRing = groupByParamType(ringRecordReportVOS, APPLICATION);

        //同比
        List<AlarmRecordReportVO> sameRecordReportVOS = alarmRecordByApplication.getSameRecordReportVOS();
        Long sameRecordTotalNum = 0L;
        if (!CollectionUtils.isEmpty(sameRecordReportVOS)) {
            sameRecordTotalNum = sameRecordReportVOS.stream().map(AlarmRecordReportVO::getAlarmNum)
                    .reduce(Long::sum).get();
        }
        Map<String, AlarmRecordVO> groupByLevelSame = groupByParamType(sameRecordReportVOS, LEVEL);
        Map<String, AlarmRecordVO> groupByApplicationSame = groupByParamType(sameRecordReportVOS, APPLICATION);
        //数据填充
        buildAlarmEvent(alarmRecordReportVOS, dataMap, ringRecordTotalNum, sameRecordTotalNum);

        //三、重大安全报警
        //1、安全报警情况 2、报警响应情况 3、报警处理情况
        //一级
        buildLevelData(groupByLevelNow, groupByLevelRing, dataMap, groupByLevelSame, ONE_LEVEL, departmentAlarmMap.getOrDefault(ONE_LEVEL, new ArrayList<>()));
        //二级
        buildLevelData(groupByLevelNow, groupByLevelRing, dataMap, groupByLevelSame, TWO_LEVEL, departmentAlarmMap.getOrDefault(TWO_LEVEL, new ArrayList<>()));
        //各应用安全管理情况
        //获取报警启用情况
        EmergencyRuleApplicationQuery ruleApplicationQuery = new EmergencyRuleApplicationQuery();
        ruleApplicationQuery.setAlarmEventList(getAlarmEventList());
        List<EmergencyRuleSafetyReportVO> emergencyRuleByAlarmAndApplication = emergencyRuleClient.getEmergencyRuleByAlarmAndApplication(ruleApplicationQuery);
        Map<String, List<EmergencyRuleSafetyReportVO>> emergencyRuleMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(emergencyRuleByAlarmAndApplication)) {
            emergencyRuleMap = emergencyRuleByAlarmAndApplication.stream()
                    .filter(t -> StringUtils.isNotBlank(t.getName()))
                    .collect(Collectors.groupingBy(EmergencyRuleSafetyReportVO::getName));
        }
        //根据用户分组数据：再次处理根据应用分组获取每个应用下的用户报警记录
        List<AlarmRecordReportVO> userAlarmRecordReportVOS = alarmRecordByApplication.getUserAlarmRecordReportVOS();
        Map<String, List<AlarmRecordReportVO>> userAlarmMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(userAlarmRecordReportVOS)) {
            userAlarmMap = userAlarmRecordReportVOS.stream()
                    .collect(Collectors.groupingBy(AlarmRecordReportVO::getApplicationCode));
        }
        buildApplicationAlarmData(groupByApplicationNow, groupByApplicationRing, groupByApplicationSame, dataMap, emergencyRuleMap, ELECTRIC_MANAGE, userAlarmMap);
        buildApplicationAlarmData(groupByApplicationNow, groupByApplicationRing, groupByApplicationSame, dataMap, emergencyRuleMap, FIRE_CONTROL, userAlarmMap);
        buildApplicationAlarmData(groupByApplicationNow, groupByApplicationRing, groupByApplicationSame, dataMap, emergencyRuleMap, ROTATING_MANAGE, userAlarmMap);
        buildApplicationAlarmData(groupByApplicationNow, groupByApplicationRing, groupByApplicationSame, dataMap, emergencyRuleMap, SECURITY_MONITORING, userAlarmMap);
        buildApplicationAlarmData(groupByApplicationNow, groupByApplicationRing, groupByApplicationSame, dataMap, emergencyRuleMap, GAS_MANAGEMENT, userAlarmMap);

        //6、隐患整改情况
        buildHiddenDangerData(period, dataMap);
        //7、风险源建档情况
        buildRiskSourceData(period, dataMap);

        //8、小散工程管理情况
        buildProject(period, dataMap);
        //9、安全巡检情况
        this.buildInspectAnalysisData(period, dataMap);
        //10、安全巡更情况
        this.buildPatrolLocationData(period, dataMap);
        //11、安全制度建立情况
        buildEmergencyFileData(period, dataMap);
        log.info("安全制度建立情况");
        //12、安全培训情况
        buildSafetyEducationData(period, dataMap);
        log.info("安全培训情况");
        //五、整体情况排名
        buildALllAlarmRecordData(alarmRecordByApplication, dataMap);
        XWPFTemplate template = null;
        try {
            LoopRowTableRenderPolicy policy = new LoopRowTableRenderPolicy();
            Configure config = Configure.builder()
                    .bind(policy, tableParam.toArray(new String[0]))
                    .build();
            //确定根目录
            ClassPathResource classPathResource = new ClassPathResource("static/" + TEMPLATE_NAME);
            InputStream in = classPathResource.getInputStream();
            template = XWPFTemplate.compile(in, config).render(dataMap);
            log.info("模板数据填充完成");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("模板填充数据失败{}", e);
        } finally {
            tableParam.clear();
        }
        return template;
    }

    private static void buildALllAlarmRecordData(AlarmRecordReportVO1 alarmRecordByApplication, Map<String, Object> dataMap) {

        Long alarmTotal = (Long) dataMap.getOrDefault("alarmTotal", 0L);
        Map<String, List<AlarmRecordReportVO>> allGroupByDeppartMentMap = alarmRecordByApplication.getDepartmentAlarmRecordReportVOS()
                .stream()
                .collect(Collectors.groupingBy(AlarmRecordReportVO::getDepartmentId));
        List<AlarmRecordResponseVO> allAlarmRecordResponse = getAlarmRecordResponseVOS(allGroupByDeppartMentMap, alarmTotal);

        allAlarmRecordResponse = allAlarmRecordResponse.stream().sorted(Comparator
                                                                                .comparing(AlarmRecordResponseVO::getDepAlarmNum)
                                                                                .reversed()).collect(Collectors.toList());
        for (int i = 0; i < allAlarmRecordResponse.size(); i++) {
            allAlarmRecordResponse.get(i).setIndex(i + 1);
        }
        dataMap.put("allAlarmRecordDepartment", allAlarmRecordResponse);
        tableParam.add("allAlarmRecordDepartment");

        //        人员分组
        Map<String, List<AlarmRecordReportVO>> allGroupByUserMap = alarmRecordByApplication.getUserAlarmRecordReportVOS()
                .stream().collect(Collectors.groupingBy(AlarmRecordReportVO::getUserId));
        List<AlarmRecordResponseVO> allAlarmRecordResponseByUser = getAlarmRecordResponseVOS(allGroupByUserMap, alarmTotal);

        allAlarmRecordResponseByUser = allAlarmRecordResponseByUser.stream()
                .sorted(Comparator.comparing(AlarmRecordResponseVO::getUserAlarmNum)
                                .reversed())
                .collect(Collectors.toList());
        for (int i = 0; i < allAlarmRecordResponseByUser.size(); i++) {
            AlarmRecordResponseVO alarmRecordResponseVO = allAlarmRecordResponseByUser.get(i);
            alarmRecordResponseVO.setIndex(i + 1);
        }
        if (allAlarmRecordResponseByUser.size() > 10) {
            allAlarmRecordResponseByUser = allAlarmRecordResponseByUser.subList(0, 10);
        }
        dataMap.put("allAlarmRecordByUser", allAlarmRecordResponseByUser);
        tableParam.add("allAlarmRecordByUser");
    }

    private static List<AlarmRecordResponseVO> getAlarmRecordResponseVOS(Map<String, List<AlarmRecordReportVO>> allGroupByDeppartMentMap,
                                                                         Long alarmTotal
                                                                        ) {
        List<AlarmRecordResponseVO> allAlarmRecordResponse = new ArrayList<>();
        for (Map.Entry<String, List<AlarmRecordReportVO>> entry : allGroupByDeppartMentMap.entrySet()) {
            //部门告警数
            Long depAlarmNum = 0L;
            //响应数
            Long respondedNum = 0L;
            //处理数
            Long alarmHandlerNum = 0L;
            //超时数
            Long timeoutNum = 0L;
            //跨天处理数
            Long outDayHandlerNum = 0L;
            for (AlarmRecordReportVO alarmRecordReportVO : entry.getValue()) {
                depAlarmNum = depAlarmNum + (alarmRecordReportVO.getAlarmNum() != null ? alarmRecordReportVO.getAlarmNum() : 0L);
                respondedNum = respondedNum + (alarmRecordReportVO.getRespondedNum() != null ? alarmRecordReportVO.getRespondedNum() : 0L);
                alarmHandlerNum = alarmHandlerNum +
                                  (alarmRecordReportVO.getAlarmHandlerNum() != null ? alarmRecordReportVO.getAlarmHandlerNum() : 0L);
                timeoutNum = timeoutNum + (alarmRecordReportVO.getTimeoutNum() != null ? alarmRecordReportVO.getTimeoutNum() : 0L);
                outDayHandlerNum = outDayHandlerNum +
                                   (alarmRecordReportVO.getOutDayHandlerNum() != null ? alarmRecordReportVO.getOutDayHandlerNum() : 0L);
            }
            allAlarmRecordResponse.add(
                    AlarmRecordResponseVO.builder()
                            .departmentName(entry.getValue().get(0).getDepartmentName())
                            .userName(entry.getValue().get(0).getUserName())
                            .departmentNum(entry.getValue().get(0).getDepartmentNum())
                            .levelAlarmNum(alarmTotal)
                            .DepAlarmNum(depAlarmNum)
                            .userAlarmNum(depAlarmNum)
                            .respondedNum(respondedNum)
                            .alarmHandlerNum(alarmHandlerNum)
                            .timeoutNum(timeoutNum)
                            .outDayHandlerNum(outDayHandlerNum)
                            .respondRatio(MathUtils.simplePercentageFormat(MathUtils.calculateProportionPercentage(respondedNum,
                                                                                                                   depAlarmNum)))
                            .respondRatioNum(MathUtils.calculateProportionPercentage(respondedNum,
                                                                                     depAlarmNum))
                            .alarmHandlerRatio(MathUtils.simplePercentageFormat(MathUtils.calculateProportionPercentage(alarmHandlerNum,
                                                                                                                        depAlarmNum)))
                            .alarmHandlerRatioNum(MathUtils.calculateProportionPercentage(alarmHandlerNum,
                                                                                          depAlarmNum))
                            .timeoutRatio(MathUtils.simplePercentageFormat(MathUtils.calculateProportionPercentage(timeoutNum, depAlarmNum)))
                            .timeoutRatioNum(MathUtils.calculateProportionPercentage(timeoutNum, depAlarmNum))
                            .outDayHandlerRatio(MathUtils.simplePercentageFormat(MathUtils.calculateProportionPercentage(outDayHandlerNum,
                                                                                                                         alarmHandlerNum)))
                            .outDayHandlerRatioNum(MathUtils.calculateProportionPercentage(outDayHandlerNum,
                                                                                           alarmHandlerNum))
                            .build()
                                      );
        }
        return allAlarmRecordResponse;
    }

    private void buildSafetyEducationData(Period period, Map<String, Object> dataMap) {
        SafetyEducationStatisticsQuery statisticsQuery = new SafetyEducationStatisticsQuery();
        statisticsQuery.setType("THIS_MONTH");
        statisticsQuery.setPeriod(period);
        SafetyEducationTaskChainVO safetyEducationTaskChainRatio = cultivateTaskClient.getSafetyEducationTaskChainRatio(statisticsQuery);
        if (ObjectUtils.isNotNull(safetyEducationTaskChainRatio)) {
            dataMap.put("safetyTotalNum", safetyEducationTaskChainRatio.getTotalNum());
            //环比
            this.upOrDown(dataMap, "safetyChainRatio", safetyEducationTaskChainRatio.getChainRatio());
            //同比
            this.upOrDown(dataMap, "safetySameRatio", safetyEducationTaskChainRatio.getSameRatio());
            //参与人数
            dataMap.put("safetyParticipants", safetyEducationTaskChainRatio.getParticipants());
        }
        List<SafetyEducationThemeRatioVO> safetyEducationThemes = cultivateTaskClient.getSafetyEducationThemes(statisticsQuery);
        if (!CollectionUtils.isEmpty(safetyEducationThemes)) {
            List<String> trainNameList = new ArrayList<>();
            Long totalNum = (long) (safetyEducationThemes.stream().map(SafetyEducationThemeRatioVO::getTotalNum).reduce(Integer::sum)
                    .get());
            for (SafetyEducationThemeRatioVO safetyEducationTheme : safetyEducationThemes) {
                String stringBuilder = safetyEducationTheme.getName() +
                                       "  " +
                                       MathUtils.simplePercentageFormat(MathUtils.calculateProportionPercentage((
                                                                                                                        Objects.nonNull(safetyEducationTheme.getTotalNum())
                                                                                                                        ? safetyEducationTheme.getTotalNum()
                                                                                                                        : 0), totalNum));
                safetyEducationTheme.getRatio();
                trainNameList.add(stringBuilder);
            }
            List<Integer> totalNumList = safetyEducationThemes.stream().map(SafetyEducationThemeRatioVO::getTotalNum)
                    .collect(Collectors.toList());

            //饼状图
            ChartSingleSeriesRenderData safetyChart = Charts
                    .ofSingleSeries("培训主题占比", trainNameList.toArray(new String[0]))
                    .series("任务数", totalNumList.toArray(new Integer[0]))
                    .create();
            dataMap.put("safetyChart", safetyChart);
        }
    }

    private void buildEmergencyFileData(Period period, Map<String, Object> dataMap) {
        EmergencyFileSafetyReportVO emergencyFileForSafetyReport = emergencyFileClient.getEmergencyFileForSafetyReport(period);
        if (Objects.nonNull(emergencyFileForSafetyReport)) {
            dataMap.put("emergencyFileTotal", emergencyFileForSafetyReport.getTotalNum());
            List<EmergencyFileEventTypeVO> emergencyFileEventTypeList = emergencyFileForSafetyReport.getEmergencyFileEventTypeList();
            List<String> eventTypeCodeList = new ArrayList<>();
            List<Long> eventTypeNumList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(emergencyFileEventTypeList)) {
                Long totalNum = emergencyFileEventTypeList.stream().map(EmergencyFileEventTypeVO::getTotalNum).reduce(Long::sum).get();
                for (EmergencyFileEventTypeVO emergencyFileEventTypeVO : emergencyFileEventTypeList) {
                    String stringBuilder = emergencyFileEventTypeVO.getEmergencyEventTypeName() +
                                           "  " +
                                           MathUtils.simplePercentageFormat(MathUtils.calculateProportionPercentage(emergencyFileEventTypeVO.getTotalNum(), totalNum));
                    eventTypeCodeList.add(stringBuilder);
                }
                eventTypeNumList = emergencyFileEventTypeList.stream()
                        .map(EmergencyFileEventTypeVO::getTotalNum).collect(Collectors.toList());
                //饼状图 预案制度类型占比
                ChartSingleSeriesRenderData emergencyFileChart = Charts
                        .ofSingleSeries("预案制度类型占比", eventTypeCodeList.toArray(new String[0]))
                        .series("预案文件数", eventTypeNumList.toArray(new Long[0]))
                        .create();
                dataMap.put("emergencyFileChart", emergencyFileChart);
            }
            //表格 未建立预案制度的事件
            dataMap.put("emergencyFileList", emergencyFileForSafetyReport.getNoSetEmergencyFileList());
            tableParam.add("emergencyFileList");
        }
    }

    private void buildProject(Period period, Map<String, Object> dataMap) {
        ProjectDataForReportVO projectDataForReportVO = projectScheduleClient.projectDataForReport(period);
        if (Objects.nonNull(projectDataForReportVO)) {
            dataMap.put("projectTotalNum", projectDataForReportVO.getNowTotal());
            //环比
            this.upOrDown(dataMap, "projectTotalRingRatio", MathUtils.calculateRingRatioPercentage(projectDataForReportVO.getNowTotal(),
                                                                                                   projectDataForReportVO.getRingTotal()));
            //同比
            this.upOrDown(dataMap, "projectTotalSameRatio", MathUtils.calculateRingRatioPercentage(projectDataForReportVO.getNowTotal(),
                                                                                                   projectDataForReportVO.getSameTotal()));
            //异常
            dataMap.put("abnormalProjectTotal", projectDataForReportVO.getAbnormalProjectReportVOS().size());
            //正常
            dataMap.put("normalProjectTotal",
                        projectDataForReportVO.getNowTotal() - projectDataForReportVO.getAbnormalProjectReportVOS().size());
            //异常率
            dataMap.put("abnormalProjectRatio", MathUtils.simplePercentageFormat(MathUtils.calculateProportionPercentage(projectDataForReportVO
                                                                                                                                 .getAbnormalProjectReportVOS()
                                                                                                                                 .size(), projectDataForReportVO.getNowTotal())));
            //异常列表
            dataMap.put("abnormalProjectLists", projectDataForReportVO.getAbnormalProjectReportVOS());
            tableParam.add("abnormalProjectLists");
        }
    }

    private void buildRiskSourceData(Period period, Map<String, Object> dataMap) {
        List<String> deviceNameList = getDeviceNameList();
        RiskSourceDeviceQueryParam deviceQueryParam = new RiskSourceDeviceQueryParam();
        deviceQueryParam.setDeviceNameList(deviceNameList);
        List<RiskSourceDeviceVO> riskSourceDeviceNum = riskSourceLedgerClient.getRiskSourceDeviceNum(deviceQueryParam);
        Map<String, Long> riskSourceDeviceMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(riskSourceDeviceNum)) {
            riskSourceDeviceMap = riskSourceDeviceNum.stream()
                    .filter(t -> StringUtils.isNotBlank(t.getDeviceCode()) && Objects.nonNull(t.getDeviceNum()))
                    .collect(Collectors.toMap(RiskSourceDeviceVO::getDeviceCode, RiskSourceDeviceVO::getDeviceNum, (k1, k2) -> k1));
        }
        List<SafetyReportEnum> deviceNameEnumList = getDeviceNameEnumList();

        for (SafetyReportEnum safetyReportEnum : deviceNameEnumList) {
            String deviceName = safetyReportEnum.getDescription();
            Long totalNum = riskSourceDeviceMap.getOrDefault(safetyReportEnum.name(), 0L);
            dataMap.put(safetyReportEnum.getCode() + "Num", totalNum);
            if (totalNum == 0L) {
                dataMap.put(safetyReportEnum.getCode(), String.format(RISK_ERROR_MESSAGE, deviceName));
            }

        }
    }

    private void buildHiddenDangerData(Period period, Map<String, Object> dataMap) {
        HiddenDangerHandleVO hiddenDangerHandleVO = hiddenDangerClient.hiddenDangerForSafetyReport(period);
        //总数
        dataMap.put("hiddenDangerTotal", hiddenDangerHandleVO.getNum());
        //环比
        this.upOrDown(dataMap, "hiddenDangerRingRatio", MathUtils.calculateRingRatioPercentage(hiddenDangerHandleVO.getNum(),
                                                                                               hiddenDangerHandleVO.getRingNum()));
        //同比
        this.upOrDown(dataMap, "hiddenDangerSameRatio", MathUtils.calculateRingRatioPercentage(hiddenDangerHandleVO.getNum(),
                                                                                               hiddenDangerHandleVO.getSameNum()));
        Map<String, Long> groupByLevelMap = new HashMap<>();
        Long finishNum = 0L;
        if (!CollectionUtils.isEmpty(hiddenDangerHandleVO.getHiddenDangerReportVOLis())) {
            List<HiddenDangerReportVO> hiddenDangerReportVOLis = hiddenDangerHandleVO.getHiddenDangerReportVOLis();
            groupByLevelMap = hiddenDangerReportVOLis.stream()
                    .collect(Collectors.groupingBy(HiddenDangerReportVO::getLevelName,
                                                   Collectors.summingLong(HiddenDangerReportVO::getNum)));
            dataMap.put("serious", groupByLevelMap.getOrDefault("严重", 0L));
            dataMap.put("medium", groupByLevelMap.getOrDefault("中等", 0L));
            dataMap.put("lower", groupByLevelMap.getOrDefault("低等", 0L));
            //处理总数
            finishNum = hiddenDangerReportVOLis.stream().map(HiddenDangerReportVO::getFinishNum).reduce(Long::sum).get();
            dataMap.put("dangerFinishNum", finishNum);
            //处理率
            dataMap.put("dangerFinishRatio", MathUtils.simplePercentageFormat(MathUtils.calculateProportionPercentage(finishNum, hiddenDangerHandleVO.getNum())));
            //饼状图
            Map<String, Long> groupByTypeMap = hiddenDangerReportVOLis.stream()
                    .collect(Collectors.groupingBy(HiddenDangerReportVO::getTypeName,
                                                   Collectors.summingLong(HiddenDangerReportVO::getNum)));

            List<String> keys = new ArrayList<>();
            Long totalNum = groupByTypeMap.values().stream().reduce(Long::sum).get();
            for (Map.Entry<String, Long> entry : groupByTypeMap.entrySet()) {
                String stringBuilder = entry.getKey() +
                                       "  " +
                                       MathUtils.simplePercentageFormat(MathUtils.calculateProportionPercentage(entry.getValue(), totalNum));
                keys.add(stringBuilder);
            }
            ChartSingleSeriesRenderData dangerChart = Charts
                    .ofSingleSeries("隐患类型占比", keys.toArray(new String[0]))
                    .series("隐患数", groupByTypeMap.values().toArray(new Long[0]))
                    .create();
            dataMap.put("dangerChart", dangerChart);
        }
        dataMap.put("serious", groupByLevelMap.getOrDefault("严重", 0L));
        dataMap.put("medium", groupByLevelMap.getOrDefault("中等", 0L));
        dataMap.put("lower", groupByLevelMap.getOrDefault("低等", 0L));
        dataMap.put("dangerFinishNum", finishNum);
        //处理率
        dataMap.put("dangerFinishRatio", MathUtils.simplePercentageFormat(MathUtils.calculateProportionPercentage(finishNum, hiddenDangerHandleVO.getNum())));
    }

    private void buildEquipmentOperationStatus(List<String> applicationList, Period period, Map<String, Object> dataMap) {
        List<String> tagList = applicationList.stream().map(t -> APPLICATION_CONSTANTS + t).collect(Collectors.toList());
        DeviceBaseInfoSafetyReportVO deviceVO = deviceBaseStatisticsClient.getDeviceNumberGroupOnStatusByTagCode(tagList, period);
        dataMap.put("eqTotal", deviceVO.getTotalNuM());
        Map<String, DeviceBaseInfoAndNumberVO> applicationTypeMap = new HashMap<>(deviceVO.getDeviceBaseInfoAndNumberVOS().size());
        if (CollectionUtils.isNotEmpty(deviceVO.getDeviceBaseInfoAndNumberVOS())) {
            List<String> existEquipmentCode = new ArrayList<>();
            for (DeviceBaseInfoAndNumberVO nowNumber : deviceVO.getDeviceBaseInfoAndNumberVOS()) {
                applicationTypeMap.put(nowNumber.getTagCode(), nowNumber);
                //设备分类统计
                buildEquipmentData(dataMap, nowNumber);
                existEquipmentCode.add(nowNumber.getTagCode());
            }
            List<String> collect = applicationList.stream().filter(t -> !existEquipmentCode.contains(t)).collect(Collectors.toList());
            for (String applicationCode : collect) {
                DeviceBaseInfoAndNumberVO deviceBaseInfoAndNumberVO = new DeviceBaseInfoAndNumberVO();
                deviceBaseInfoAndNumberVO.setTagCode(applicationCode);
                buildEquipmentData(dataMap, deviceBaseInfoAndNumberVO);
            }
        } else {
            for (String applicationCode : applicationList) {
                DeviceBaseInfoAndNumberVO deviceBaseInfoAndNumberVO = new DeviceBaseInfoAndNumberVO();
                deviceBaseInfoAndNumberVO.setTagCode(applicationCode);
                buildEquipmentData(dataMap, deviceBaseInfoAndNumberVO);
            }
        }
        //消防安全饼状图
        Long fireTotalNum = 0L;
        if (CollectionUtils.isNotEmpty(deviceVO.getDeviceFireSafetyReportVOS())) {
            StringBuilder fireDeviceNum = new StringBuilder();
            fireTotalNum = deviceVO.getDeviceFireSafetyReportVOS().stream()
                    .map(DeviceFireSafetyReportVO::getDeviceNum).reduce(Long::sum).get();
            List<Long> deviceNumList = new ArrayList<>();
            List<String> deviceTypeNameList = new ArrayList<>();
            for (DeviceFireSafetyReportVO deviceFireSafetyReportVO : deviceVO.getDeviceFireSafetyReportVOS()) {
                if (StringUtils.isBlank(fireDeviceNum.toString())) {
                    fireDeviceNum.append(", 其中");
                } else {
                    fireDeviceNum.append("、");
                }
                fireDeviceNum.append(deviceFireSafetyReportVO.getCategoryName());
                fireDeviceNum.append(deviceFireSafetyReportVO.getDeviceNum()).append("个");
                deviceNumList.add(deviceFireSafetyReportVO.getDeviceNum());
                deviceTypeNameList.add(deviceFireSafetyReportVO.getCategoryName() + "  " +
                                       MathUtils.simplePercentageFormat(MathUtils.calculateProportionPercentage(deviceFireSafetyReportVO.getDeviceNum(), fireTotalNum)));
            }

            dataMap.put("fireDeviceTypeNum", fireDeviceNum.toString());
            ChartSingleSeriesRenderData fireDeviceTyperChart = Charts
                    .ofSingleSeries("设备子分类占比", deviceTypeNameList.toArray(new String[0]))
                    .series("设备数", deviceNumList.toArray(new Long[0]))
                    .create();
            dataMap.put("fireDeviceTypeChart", fireDeviceTyperChart);
        }
        dataMap.put("fireDeviceTypeTotalNum", fireTotalNum);

    }

    private static void buildEquipmentData(Map<String, Object> dataMap, DeviceBaseInfoAndNumberVO nowNumber) {
        switch (nowNumber.getTagCode()) {
            case FIRE_CONTROL:
                buildApplicationDeviceData(nowNumber, dataMap, "fire");

                break;
            case ELECTRIC_MANAGE:
                buildApplicationDeviceData(nowNumber, dataMap, "electric");
                break;
            case ROTATING_MANAGE:
                buildApplicationDeviceData(nowNumber, dataMap, "rotating");
                break;
            case SECURITY_MONITORING:
                buildApplicationDeviceData(nowNumber, dataMap, "security");
                break;
            case GAS_MANAGEMENT:
                buildApplicationDeviceData(nowNumber, dataMap, "gas");
                break;
            default:
        }
    }

    /**
     * 整体设备运行情况数据赋值，以及不同子应用的设备数据赋值
     *
     * @param nowNumber
     * @param dataMap
     * @param prefix    子应用功能前缀
     */
    private static void buildApplicationDeviceData(DeviceBaseInfoAndNumberVO nowNumber, Map<String, Object> dataMap, String prefix) {
        Long totalNum = (long) (Objects.nonNull(nowNumber.getTotalNumber()) ? nowNumber.getTotalNumber() : 0);
        Long Normal = (long) (Objects.nonNull(nowNumber.getNormalNumber()) ? nowNumber.getNormalNumber() : 0);
        Long Offline = (long) (Objects.nonNull(nowNumber.getOfflineNumber()) ? nowNumber.getOfflineNumber() : 0);
        dataMap.put(prefix, totalNum);
        dataMap.put(prefix + "Normal", Normal);
        dataMap.put(prefix + "Offline", Offline);
        //饼状图
        ChartSingleSeriesRenderData chart = Charts
                .ofSingleSeries("设备状态占比", new String[]{
                        "正常" + "  " + MathUtils.simplePercentageFormat(MathUtils.calculateProportionPercentage(Normal, totalNum)),
                        "离线" + "  " + MathUtils.simplePercentageFormat(MathUtils.calculateProportionPercentage(Offline, totalNum))
                })
                .series("设备数", new Integer[]{ Math.toIntExact(Normal), Math.toIntExact(Offline) })
                .create();
        dataMap.put(prefix + "Chart", chart);
    }

    private void buildAlarmEvent(List<AlarmRecordReportVO> alarmRecordReportVOS, Map<String, Object> dataMap, Long ringRecordTotalNum,
                                 Long sameRecordTotalNum
                                ) {
        Long alarmTotalNum = 0L;
        //真实报警数
        Long alarmActualNum = 0L;
        //已响应报警数
        Long respondedNum = 0L;
        //已处理报警数
        Long alarmHandler = 0L;
        //超时的报警数
        Long timeoutNum = 0L;

        //报警事件分组
        Map<String, List<AlarmRecordReportVO>> alarmRecordEventMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(alarmRecordReportVOS)) {
            for (AlarmRecordReportVO alarmRecordReportVO : alarmRecordReportVOS) {
                alarmTotalNum = alarmTotalNum + alarmRecordReportVO.getAlarmNum();
                alarmActualNum = alarmActualNum + alarmRecordReportVO.getAlarmActualNum();
                respondedNum = respondedNum + alarmRecordReportVO.getRespondedNum();
                alarmHandler = alarmHandler + alarmRecordReportVO.getAlarmHandlerNum();
                timeoutNum = timeoutNum + alarmRecordReportVO.getTimeoutNum();
                if (alarmRecordEventMap.containsKey(alarmRecordReportVO.getAlarmEventName())) {
                    alarmRecordEventMap.get(alarmRecordReportVO.getAlarmEventName()).add(alarmRecordReportVO);
                } else {
                    List<AlarmRecordReportVO> alarmRecordReportVOS1 = new ArrayList<>(alarmRecordReportVOS.size());
                    alarmRecordReportVOS1.add(alarmRecordReportVO);
                    alarmRecordEventMap.put(alarmRecordReportVO.getAlarmEventName(), alarmRecordReportVOS1);
                }
            }
        }
        //安全报警总数
        dataMap.put("alarmTotal", alarmTotalNum);
        //环比
        this.upOrDown(dataMap, "alarmTotalRingRatio", MathUtils.calculateRingRatioPercentage(alarmTotalNum, ringRecordTotalNum));
        //同比
        this.upOrDown(dataMap, "alarmTotalSameRatio", MathUtils.calculateRingRatioPercentage(alarmTotalNum, sameRecordTotalNum));
        //正确数和正确率
        dataMap.put("alarmActualNum", alarmActualNum);
        dataMap.put("alarmActualRatio", MathUtils.simplePercentageFormat(MathUtils.calculateProportionPercentage(alarmActualNum, alarmHandler)));
        //响应数和响应率
        dataMap.put("respondedNum", respondedNum);
        dataMap.put("respondedRatio", MathUtils.simplePercentageFormat(MathUtils.calculateProportionPercentage(respondedNum, alarmTotalNum)));
        //处理数和处理率
        dataMap.put("alarmHandler", alarmHandler);
        dataMap.put("alarmHandlerRatio", MathUtils.simplePercentageFormat(MathUtils.calculateProportionPercentage(alarmHandler, alarmTotalNum)));
        //超市数和超时率
        dataMap.put("timeoutNum", timeoutNum);
        dataMap.put("timeoutRatio", MathUtils.simplePercentageFormat(MathUtils.calculateProportionPercentage(timeoutNum, alarmTotalNum)));
        //报警事件排名
        List<AlarmEventVO> alarmEventVOS = new ArrayList<>();
        for (Map.Entry<String, List<AlarmRecordReportVO>> entry : alarmRecordEventMap.entrySet()) {
            AlarmEventVO alarmEventVO = new AlarmEventVO();
            alarmEventVO.setAlarmEventName(entry.getKey());
            //报警数
            Long excelAlarmNum = 0L;
            //真实报警数
            Long excelAlarmActual = 0L;
            //已处理报警数
            Long excelAlarmHandler = 0L;
            for (AlarmRecordReportVO alarmRecordReportVO : entry.getValue()) {
                excelAlarmNum = excelAlarmNum + alarmRecordReportVO.getAlarmNum();
                excelAlarmActual = excelAlarmActual + alarmRecordReportVO.getAlarmActualNum();
                excelAlarmHandler = excelAlarmHandler + alarmRecordReportVO.getAlarmHandlerNum();
            }
            alarmEventVO.setAlarmNum(excelAlarmNum);
            alarmEventVO.setAlarmProportion(MathUtils.simplePercentageFormat(MathUtils.calculateProportionPercentage(excelAlarmNum, alarmTotalNum)));
            alarmEventVO.setProcessed(excelAlarmHandler);
            alarmEventVO.setProcessedRate(MathUtils.simplePercentageFormat(MathUtils.calculateProportionPercentage(excelAlarmHandler, excelAlarmNum)));
            alarmEventVO.setAccuracy(MathUtils.simplePercentageFormat(MathUtils.calculateProportionPercentage(excelAlarmActual, excelAlarmHandler)));
            alarmEventVOS.add(alarmEventVO);
        }
        //报警事件排名表格
        alarmEventVOS = alarmEventVOS.stream()
                .sorted(Comparator.comparing(AlarmEventVO::getAlarmNum).reversed())
                .collect(Collectors.toList());
        for (int i = 0; i < alarmEventVOS.size(); i++) {
            alarmEventVOS.get(i).setIndex(i + 1);
        }
        dataMap.put("alarmLists", alarmEventVOS);
        tableParam.add("alarmLists");
    }

    private void buildPatrolLocationData(Period period, Map<String, Object> dataMap) {
        ElectronTaskSafetyReportVO electronTaskForSafetyReport = patrolLocationClient.getElectronTaskForSafetyReport(period);
        Long betweenDay = DateUtils.between(period.getBegin(), period.getEnd(), ChronoUnit.DAYS);
        if (Objects.nonNull(electronTaskForSafetyReport)) {
            dataMap.put("patrolTotalNum", electronTaskForSafetyReport.getNum());
            //巡更频率
            dataMap.put("patrolTotalRate", electronTaskForSafetyReport.getNum() / betweenDay);
            //环比
            this.upOrDown(dataMap, "patrolTotalRingRatio", MathUtils.calculateRingRatioPercentage(electronTaskForSafetyReport.getNum(),
                                                                                                  electronTaskForSafetyReport.getRingNum()));
            //同比
            this.upOrDown(dataMap, "patrolTotalSameRatio", MathUtils.calculateRingRatioPercentage(electronTaskForSafetyReport.getNum(),
                                                                                                  electronTaskForSafetyReport.getSameNum()));

            List<ElectronLocationSafetyReportVO> electronLocationSafetyReportVOS = electronTaskForSafetyReport.getElectronLocationSafetyReportVOS();
            Long patrolAbnormalTotal = 0L;
            Long patrolNormalTotal = 0L;
            if (CollectionUtils.isNotEmpty(electronLocationSafetyReportVOS)) {
                patrolAbnormalTotal = electronLocationSafetyReportVOS.stream()
                        .map(ElectronLocationSafetyReportVO::getAbnormalNum).reduce(Long::sum).get();
                patrolNormalTotal = electronLocationSafetyReportVOS.stream()
                        .map(ElectronLocationSafetyReportVO::getNormalNum).reduce(Long::sum).get();
            }

            //异常
            dataMap.put("patrolAbnormalTotal", patrolAbnormalTotal);
            //正常
            dataMap.put("patrolNormalTotal", patrolNormalTotal);
            //异常率
            dataMap.put("patrolAbnormalRatio", MathUtils.simplePercentageFormat(MathUtils.calculateProportionPercentage(patrolAbnormalTotal, electronTaskForSafetyReport.getNum())));
            //开展列表
            Map<Long, List<ElectronLocationSafetyReportVO>> groupByDepartmentMap = electronLocationSafetyReportVOS.stream()
                    .collect(Collectors.groupingBy(ElectronLocationSafetyReportVO::getUserDepId));
            List<InspectionDepartmentVO> patrolDepartmentVOS = new ArrayList<>();
            for (Map.Entry<Long, List<ElectronLocationSafetyReportVO>> entry : groupByDepartmentMap.entrySet()) {

                Long patrolNum = 0L;
                Long abnormalNum = 0L;
                for (ElectronLocationSafetyReportVO safetyReportVO : entry.getValue()) {
                    patrolNum = patrolNum + safetyReportVO.getTotalNum();
                    abnormalNum = abnormalNum + safetyReportVO.getAbnormalNum();
                }
                patrolDepartmentVOS.add(InspectionDepartmentVO.builder()
                                                .num(patrolNum)
                                                .organizationName(entry.getValue().get(0).getUserDepName())
                                                .abnormalNum(abnormalNum)
                                                .abnormalRate(MathUtils.simplePercentageFormat(MathUtils.calculateProportionPercentage(abnormalNum, patrolNum)))
                                                .patrolRate((patrolNum / betweenDay) + "次/天")
                                                .build());
            }
            patrolDepartmentVOS = patrolDepartmentVOS.stream().sorted(Comparator.comparing(InspectionDepartmentVO::getNum).reversed())
                    .collect(Collectors.toList());
            for (int i = 0; i < patrolDepartmentVOS.size(); i++) {
                patrolDepartmentVOS.get(i).setIndex(i + 1);
            }
            dataMap.put("patrolDepartmentLists", patrolDepartmentVOS);
            tableParam.add("patrolDepartmentLists");
        }
    }

    private void buildInspectAnalysisData(Period period, Map<String, Object> dataMap) {
        SafetyReportResultVO safetyReportResultVO = inspectionClient.inspectAnalysisSafetyReport(period);
        if (Objects.nonNull(safetyReportResultVO)) {
            dataMap.put("inspectTotalNum", safetyReportResultVO.getNum());
            //环比
            this.upOrDown(dataMap, "inspectTotalRingRatio", MathUtils.calculateRingRatioPercentage(safetyReportResultVO.getNum(),
                                                                                                   safetyReportResultVO.getRingNum()));
            //同比
            this.upOrDown(dataMap, "inspectTotalSameRatio", MathUtils.calculateRingRatioPercentage(safetyReportResultVO.getNum(),
                                                                                                   safetyReportResultVO.getSameNum()));

            List<InspectionSafetyReportVO> inspectionSafetyReportVOLis = safetyReportResultVO.getInspectionSafetyReportVOLis();
            Long abnormalInspectTotal = 0L;
            Long normalInspectTotal = 0L;
            if (CollectionUtils.isNotEmpty(inspectionSafetyReportVOLis)) {
                abnormalInspectTotal = inspectionSafetyReportVOLis.stream()
                        .map(InspectionSafetyReportVO::getAbnormalNum).reduce(Long::sum).get();
                normalInspectTotal = inspectionSafetyReportVOLis.stream()
                        .map(InspectionSafetyReportVO::getNormalNum).reduce(Long::sum).get();
            }

            //异常
            dataMap.put("inspectAbnormalTotal", abnormalInspectTotal);
            //正常
            dataMap.put("inspectNormalTotal", normalInspectTotal);
            //异常率
            dataMap.put("inspectAbnormalRatio", MathUtils.simplePercentageFormat(MathUtils.calculateProportionPercentage(abnormalInspectTotal, safetyReportResultVO.getNum())));
            //开展列表
            Map<String, List<InspectionSafetyReportVO>> groupByDepartmentMap = inspectionSafetyReportVOLis.stream()
                    .collect(Collectors.groupingBy(InspectionSafetyReportVO::getOrganizationName));
            List<InspectionDepartmentVO> inspectionDepartmentVOS = new ArrayList<>();
            for (Map.Entry<String, List<InspectionSafetyReportVO>> entry : groupByDepartmentMap.entrySet()) {

                Long inspectionNum = 0L;
                Long abnormalNum = 0L;
                for (InspectionSafetyReportVO inspectionSafetyReportVO : entry.getValue()) {
                    inspectionNum = inspectionNum + inspectionSafetyReportVO.getNum();
                    abnormalNum = abnormalNum + inspectionSafetyReportVO.getAbnormalNum();
                }
                inspectionDepartmentVOS.add(InspectionDepartmentVO.builder()
                                                    .organizationName(entry.getKey())
                                                    .num(inspectionNum)
                                                    .abnormalNum(abnormalNum)
                                                    .abnormalRate(MathUtils.simplePercentageFormat(MathUtils.calculateProportionPercentage(abnormalNum, inspectionNum)))
                                                    .build());
            }
            inspectionDepartmentVOS = inspectionDepartmentVOS.stream()
                    .sorted(Comparator.comparing(InspectionDepartmentVO::getNum).reversed())
                    .collect(Collectors.toList());
            for (int i = 0; i < inspectionDepartmentVOS.size(); i++) {
                inspectionDepartmentVOS.get(i).setIndex(i + 1);
            }
            dataMap.put("inspectDepartmentLists", inspectionDepartmentVOS);
            tableParam.add("inspectDepartmentLists");
            //饼状图
            Map<String, Long> groupByCategoryMap = inspectionSafetyReportVOLis.stream()
                    .collect(Collectors.groupingBy(InspectionSafetyReportVO::getTargetCategory,
                                                   Collectors.summingLong(InspectionSafetyReportVO::getNum)));
            if (MapUtils.isNotEmpty(groupByCategoryMap)) {
                List<String> keys = new ArrayList<>();
                Long totalNum = groupByCategoryMap.values().stream().reduce(Long::sum).get();
                for (Map.Entry<String, Long> entry : groupByCategoryMap.entrySet()) {
                    String stringBuilder = entry.getKey() +
                                           "  " +
                                           MathUtils.simplePercentageFormat(MathUtils.calculateProportionPercentage(entry.getValue(), totalNum));
                    keys.add(stringBuilder);
                }
                ChartSingleSeriesRenderData inspectionChart = Charts
                        .ofSingleSeries("巡检类型占比", keys.toArray(new String[0]))
                        .series("巡检数", groupByCategoryMap.values().toArray(new Long[0]))
                        .create();
                dataMap.put("inspectionChart", inspectionChart);
            }
        }
    }

    private void buildApplicationAlarmData(Map<String, AlarmRecordVO> groupByApplicationNow,
                                           Map<String, AlarmRecordVO> groupByApplicationRing,
                                           Map<String, AlarmRecordVO> groupByApplicationSame, Map<String, Object> dataMap, Map<String,
                    List<EmergencyRuleSafetyReportVO>> emergencyRuleNameMap, String applicationType,
                                           Map<String, List<AlarmRecordReportVO>> userAlarmMap
                                          ) {
        AlarmRecordVO electricManageVO = groupByApplicationNow.getOrDefault(applicationType,
                                                                            AlarmRecordVO.builder().alarmTotalNum(0L)
                                                                                    .alarmActualNum(0L)
                                                                                    .respondedNum(0L)
                                                                                    .alarmHandler(0L)
                                                                                    .timeoutNum(0L)
                                                                                    .outDayHandlerNum(0L)
                                                                                    .build());
        AlarmRecordVO electricManageRing = groupByApplicationRing.getOrDefault(applicationType, AlarmRecordVO.builder().alarmTotalNum(0L)
                .build());
        AlarmRecordVO electricManageSame = groupByApplicationSame.getOrDefault(applicationType, AlarmRecordVO.builder().alarmTotalNum(0L)
                .build());

        String prefix = "";
        String chartTitle = "";
        List<SafetyReportEnum> alarmEventList = new ArrayList<>();
        switch (applicationType) {
            case FIRE_CONTROL:
                prefix = "fire";
                chartTitle = "消防";
                alarmEventList = getFireAlarmEventList();
                break;
            case ELECTRIC_MANAGE:
                prefix = "elect";
                chartTitle = "电气";
                alarmEventList = getElectricAlarmEventList();
                break;
            case ROTATING_MANAGE:
                prefix = "rotating";
                chartTitle = "机房";
                alarmEventList = getRoatingAlarmEventList();
                break;
            case SECURITY_MONITORING:
                prefix = "security";
                chartTitle = "经营秩序";
                alarmEventList = getSecurityAlarmEventList();
                break;
            case GAS_MANAGEMENT:
                prefix = "gas";
                chartTitle = "用气";
                alarmEventList = getGasAlarmEventList();
                break;
            default:
        }
        //报警机制启用情况
        for (SafetyReportEnum safetyReportEnum : alarmEventList) {
            String message = null;
            String description = safetyReportEnum.getDescription();
            //存在
            if (emergencyRuleNameMap.containsKey(description)) {
                //存在一个启用的就算建立
                Optional<EmergencyRuleSafetyReportVO> first = emergencyRuleNameMap
                        .get(description).stream().filter(EmergencyRuleSafetyReportVO::getEnabled).findFirst();
                if (first.isPresent()) {
                    message = String.format(NORMAL_MESSAGE, description, description);
                } else {
                    message = String.format(UN_ENABLE_MESSAGE, description, description);
                }
            } else {
                message = String.format(ERROR_MESSAGE, description, description);
            }
            dataMap.put(safetyReportEnum.getCode(), message);
        }

        List<AlarmRecordResponseVO> alarmRecordResponseVOList = getAlarmRecordResponseVOS(userAlarmMap.get(applicationType), electricManageVO);
        List<AlarmRecordResponseVO> orderByResponse = alarmRecordResponseVOList
                .stream().sorted(Comparator.comparing(AlarmRecordResponseVO::getRespondRatioNum).reversed())
                .collect(Collectors.toList());
        buildSortedData(dataMap, orderByResponse, prefix + "ResponseUser");
        List<AlarmRecordResponseVO> orderByHandler = alarmRecordResponseVOList
                .stream().sorted(Comparator.comparing(AlarmRecordResponseVO::getAlarmHandlerRatioNum).reversed())
                .collect(Collectors.toList());
        buildSortedData(dataMap, orderByHandler, prefix + "HandlerUser");

        List<AlarmRecordResponseVO> orderByOutDayHandler = alarmRecordResponseVOList
                .stream().sorted(Comparator.comparing(AlarmRecordResponseVO::getOutDayHandlerRatioNum).reversed())
                .collect(Collectors.toList());
        buildSortedData(dataMap, orderByOutDayHandler, prefix + "OutDayUser");
        dataMap.put(prefix + "AlarmTotal", electricManageVO.getAlarmTotalNum());
        //环比
        this.upOrDown(dataMap, prefix + "AlarmTotalRingRatio", MathUtils.calculateRingRatioPercentage(electricManageVO.getAlarmTotalNum(),
                                                                                                      electricManageRing.getAlarmTotalNum()));
        //同比
        this.upOrDown(dataMap, prefix + "AlarmTotalSameRatio", MathUtils.calculateRingRatioPercentage(electricManageVO.getAlarmTotalNum(),
                                                                                                      electricManageSame.getAlarmTotalNum()));
        //正确数和正确率
        dataMap.put(prefix + "AlarmActual", electricManageVO.getAlarmActualNum());
        dataMap.put(prefix +
                    "AlarmActualRatio", MathUtils.simplePercentageFormat(MathUtils.calculateProportionPercentage(electricManageVO.getAlarmActualNum(), electricManageVO.getAlarmHandler())));
        //响应数和响应率
        dataMap.put(prefix + "RespondedNum", electricManageVO.getRespondedNum());
        dataMap.put(prefix +
                    "RespondedRatio", MathUtils.simplePercentageFormat(MathUtils.calculateProportionPercentage(electricManageVO.getRespondedNum(), electricManageVO.getAlarmTotalNum())));
        //处理数和处理率
        dataMap.put(prefix + "AlarmHandler", electricManageVO.getAlarmHandler());
        dataMap.put(prefix +
                    "AlarmHandlerRatio", MathUtils.simplePercentageFormat(MathUtils.calculateProportionPercentage(electricManageVO.getAlarmHandler(), electricManageVO.getAlarmTotalNum())));
        //超时数和超时率
        dataMap.put(prefix + "TimeoutNum", electricManageVO.getTimeoutNum());
        dataMap.put(prefix +
                    "TimeoutRatio", MathUtils.simplePercentageFormat(MathUtils.calculateProportionPercentage(electricManageVO.getTimeoutNum(), electricManageVO.getAlarmTotalNum())));
        //饼状图
        Map<String, List<AlarmRecordReportVO>> electricManageEventMap = electricManageVO.getAlarmRecordEventMap();
        if (MapUtils.isNotEmpty(electricManageEventMap)) {
            List<Long> size = new ArrayList<>();
            Long totalNum = electricManageEventMap.values().stream()
                    .flatMap(Collection::stream).map(AlarmRecordReportVO::getAlarmNum).reduce(Long::sum)
                    .get();
            List<String> keys = new ArrayList<>();
            for (Map.Entry<String, List<AlarmRecordReportVO>> entry : electricManageEventMap.entrySet()) {
                Long aLong = entry.getValue().stream().map(AlarmRecordReportVO::getAlarmNum).reduce(Long::sum).get();
                size.add(aLong);
                String stringBuilder = entry.getKey() +
                                       "  " +
                                       MathUtils.simplePercentageFormat(MathUtils.calculateProportionPercentage(aLong, totalNum));
                keys.add(stringBuilder);
            }
            ChartSingleSeriesRenderData safetyChart = Charts
                    .ofSingleSeries(chartTitle + "报警事件占比", keys.toArray(new String[0]))
                    .series("报警数", size.toArray(new Long[0]))
                    .create();
            dataMap.put(prefix + "AlarmChart", safetyChart);
        }

    }

    private void buildLevelData(Map<String, AlarmRecordVO> groupByLevelNow,
                                Map<String, AlarmRecordVO> groupByLevelRing, Map<String, Object> dataMap,
                                Map<String, AlarmRecordVO> groupByLevelSame,
                                String level, List<AlarmRecordReportVO> departmentAlarmList
                               ) {
        String prefix = level.split("_")[0];
        AlarmRecordVO oneLevelRecord = groupByLevelNow.getOrDefault(level,
                                                                    AlarmRecordVO.builder().alarmTotalNum(0L)
                                                                            .alarmActualNum(0L)
                                                                            .respondedNum(0L)
                                                                            .alarmHandler(0L)
                                                                            .outDayHandlerNum(0L)
                                                                            .timeoutNum(0L).build());
        AlarmRecordVO ringOneLevelRecord = groupByLevelRing.getOrDefault(level,
                                                                         AlarmRecordVO.builder().alarmTotalNum(0L)
                                                                                 .alarmActualNum(0L)
                                                                                 .respondedNum(0L)
                                                                                 .alarmHandler(0L)
                                                                                 .timeoutNum(0L)
                                                                                 .build());
        dataMap.put(prefix + "LevelTotal", oneLevelRecord.getAlarmTotalNum());
        //环比
        this.upOrDown(dataMap, prefix + "LevelRing", MathUtils.calculateRingRatioPercentage(oneLevelRecord.getAlarmTotalNum(),
                                                                                            ringOneLevelRecord.getAlarmTotalNum()
                                                                                           ));
        AlarmRecordVO sameOneLevelRecord = groupByLevelSame.getOrDefault(level,
                                                                         AlarmRecordVO.builder().alarmTotalNum(0L)
                                                                                 .alarmActualNum(0L)
                                                                                 .respondedNum(0L)
                                                                                 .alarmHandler(0L)
                                                                                 .timeoutNum(0L)
                                                                                 .build());
        //同比
        this.upOrDown(dataMap, prefix + "LevelSame", MathUtils.calculateRingRatioPercentage(oneLevelRecord.getAlarmTotalNum(),
                                                                                            sameOneLevelRecord.getAlarmTotalNum()
                                                                                           ));
        dataMap.put(prefix + "LevelActual", oneLevelRecord.getAlarmActualNum());
        //真实报警率
        dataMap.put(prefix +
                    "LevelActualRatio", MathUtils.simplePercentageFormat(MathUtils.calculateProportionPercentage(oneLevelRecord.getAlarmActualNum(),
                                                                                                                 oneLevelRecord.getAlarmHandler())));
        //饼状图 报警事件占比
        Map<String, List<AlarmRecordReportVO>> alarmRecordEventMap = oneLevelRecord.getAlarmRecordEventMap();
        if (MapUtils.isNotEmpty(alarmRecordEventMap)) {
            List<Long> size = new ArrayList<>();
            Long totalNum = alarmRecordEventMap.values().stream()
                    .flatMap(Collection::stream).map(AlarmRecordReportVO::getAlarmNum).reduce(Long::sum)
                    .get();
            List<String> keys = new ArrayList<>();
            for (Map.Entry<String, List<AlarmRecordReportVO>> entry : alarmRecordEventMap.entrySet()) {
                Long aLong = entry.getValue().stream().map(AlarmRecordReportVO::getAlarmNum).reduce(Long::sum).get();
                size.add(aLong);
                String stringBuilder = entry.getKey() +
                                       "  " +
                                       MathUtils.simplePercentageFormat(MathUtils.calculateProportionPercentage(aLong, totalNum));
                keys.add(stringBuilder);
            }
            ChartSingleSeriesRenderData safetyChart = Charts
                    .ofSingleSeries("报警事件占比", keys.toArray(new String[0]))
                    .series("报警数", size.toArray(new Long[0]))
                    .create();
            dataMap.put(prefix + "AlarmChart", safetyChart);
        }
        //2、报警响应情况
        //响应数和响应率
        dataMap.put(prefix + "RespondedNum", oneLevelRecord.getRespondedNum());
        dataMap.put(prefix +
                    "RespondedRatio", MathUtils.simplePercentageFormat(MathUtils.calculateProportionPercentage(oneLevelRecord.getRespondedNum(),
                                                                                                               oneLevelRecord.getAlarmTotalNum())));

        //计算部门下报警比率
        List<AlarmRecordResponseVO> alarmRecordResponseVOList = getAlarmRecordResponseVOS(departmentAlarmList, oneLevelRecord);

        //响应率排名
        List<AlarmRecordResponseVO> orderByResponse = alarmRecordResponseVOList
                .stream().sorted(Comparator.comparing(AlarmRecordResponseVO::getRespondRatioNum, Comparator.reverseOrder())
                                         .thenComparing(AlarmRecordResponseVO::getDepAlarmNum, Comparator.reverseOrder()))
                .collect(Collectors.toList());
        buildSortedData(dataMap, orderByResponse, prefix + "ResponseDep");
        //3、报警处理情况
        //处理数和处理率
        dataMap.put(prefix + "AlarmHandler", oneLevelRecord.getAlarmHandler());
        dataMap.put(prefix +
                    "AlarmHandlerRatio", MathUtils.simplePercentageFormat(MathUtils.calculateProportionPercentage(oneLevelRecord.getAlarmHandler(), oneLevelRecord.getAlarmTotalNum())));
        //超时数和超时率
        dataMap.put(prefix + "TimeoutNum", oneLevelRecord.getTimeoutNum());
        dataMap.put(prefix +
                    "TimeoutRatio", MathUtils.simplePercentageFormat(MathUtils.calculateProportionPercentage(oneLevelRecord.getTimeoutNum(), oneLevelRecord.getAlarmTotalNum())));
        dataMap.put(prefix + "LevelActual", oneLevelRecord.getAlarmActualNum());
        //跨天处理数
        dataMap.put(prefix + "OutDayHandler", oneLevelRecord.getOutDayHandlerNum());
        //处理率排序
        List<AlarmRecordResponseVO> orderByHandler = alarmRecordResponseVOList
                .stream().sorted(Comparator.comparing(AlarmRecordResponseVO::getAlarmHandlerRatioNum, Comparator.reverseOrder())
                                         .thenComparing(AlarmRecordResponseVO::getDepAlarmNum, Comparator.reverseOrder()))
                .collect(Collectors.toList());
        buildSortedData(dataMap, orderByHandler, prefix + "HandlerDep");

        List<AlarmRecordResponseVO> orderByOutDayHandler = alarmRecordResponseVOList
                .stream().sorted(Comparator.comparing(AlarmRecordResponseVO::getOutDayHandlerRatioNum).reversed())
                .collect(Collectors.toList());
        buildSortedData(dataMap, orderByOutDayHandler, prefix + "OutDayDep");
    }

    /**
     * 根据分组后的数据计算报警数
     *
     * @param alarmRecordDepartmentList
     * @param oneLevelRecord
     *
     * @return
     */
    private static List<AlarmRecordResponseVO> getAlarmRecordResponseVOS(List<AlarmRecordReportVO> alarmRecordDepartmentList,
                                                                         AlarmRecordVO oneLevelRecord
                                                                        ) {
        List<AlarmRecordResponseVO> alarmRecordResponseVOList = new ArrayList<>();
        if (CollectionUtils.isEmpty(alarmRecordDepartmentList)) {
            return alarmRecordResponseVOList;
        }
        for (AlarmRecordReportVO alarmRecordReportVO : alarmRecordDepartmentList) {

            //部门告警数
            Long totalAlarmNum = Objects.nonNull(alarmRecordReportVO) ? alarmRecordReportVO.getAlarmNum() : 0L;
            //响应数
            Long respondedNum = Objects.nonNull(alarmRecordReportVO) ? alarmRecordReportVO.getRespondedNum() : 0L;
            //处理数
            Long alarmHandlerNum = Objects.nonNull(alarmRecordReportVO) ? alarmRecordReportVO.getAlarmHandlerNum() : 0L;
            //超时数
            Long timeoutNum = Objects.nonNull(alarmRecordReportVO) ? alarmRecordReportVO.getTimeoutNum() : 0L;
            //跨天处理数
            Long outDayHandlerNum = Objects.nonNull(alarmRecordReportVO) ? alarmRecordReportVO.getOutDayHandlerNum() : 0L;

            alarmRecordResponseVOList.add(
                    AlarmRecordResponseVO.builder()
                            .departmentName(alarmRecordReportVO.getDepartmentName())
                            .userName(alarmRecordReportVO.getUserName())
                            .levelAlarmNum(oneLevelRecord.getAlarmTotalNum())
                            .DepAlarmNum(totalAlarmNum)
                            .departmentNum(alarmRecordReportVO.getDepartmentNum())
                            .userAlarmNum(totalAlarmNum)
                            .respondedNum(respondedNum)
                            .alarmHandlerNum(alarmHandlerNum)
                            .timeoutNum(timeoutNum)
                            .outDayHandlerNum(outDayHandlerNum)
                            .respondRatio(MathUtils.simplePercentageFormat(MathUtils.calculateProportionPercentage(respondedNum,
                                                                                                                   totalAlarmNum)))
                            .respondRatioNum(MathUtils.calculateProportionPercentage(respondedNum,
                                                                                     totalAlarmNum))
                            .alarmHandlerRatio(MathUtils.simplePercentageFormat(MathUtils.calculateProportionPercentage(alarmHandlerNum,
                                                                                                                        totalAlarmNum)))
                            .alarmHandlerRatioNum(MathUtils.calculateProportionPercentage(alarmHandlerNum,
                                                                                          totalAlarmNum))
                            .timeoutRatio(MathUtils.simplePercentageFormat(MathUtils.calculateProportionPercentage(timeoutNum, totalAlarmNum)))
                            .timeoutRatioNum(MathUtils.calculateProportionPercentage(timeoutNum, totalAlarmNum))
                            .outDayHandlerRatio(MathUtils.simplePercentageFormat(MathUtils.calculateProportionPercentage(outDayHandlerNum,
                                                                                                                         alarmHandlerNum)))
                            .outDayHandlerRatioNum(MathUtils.calculateProportionPercentage(outDayHandlerNum,
                                                                                           alarmHandlerNum))
                            .build()
                                         );
        }
        return alarmRecordResponseVOList;
    }

    /**
     * prefix:
     * oneResponseTopDep
     * oneResponseTopDepRatio
     * oneResponseEndDep
     * oneResponseEndDepRatio
     * oneResponseTopThreeList
     * oneResponseEndThreeList
     *
     * @param dataMap
     * @param orderByDataNoIndex
     * @param prefix
     */
    private static void buildSortedData(Map<String, Object> dataMap, List<AlarmRecordResponseVO> orderByDataNoIndex, String prefix) {
        List<AlarmRecordResponseVO> topThreeResponseList = new ArrayList<>(3);
        List<AlarmRecordResponseVO> endThreeResponseList = new ArrayList<>(3);
        if (CollectionUtils.isNotEmpty(orderByDataNoIndex)) {
            //copy加赋值index
            List<AlarmRecordResponseVO> orderByData = new ArrayList<>();
            for (int i = 0; i < orderByDataNoIndex.size(); i++) {
                AlarmRecordResponseVO alarmRecordResponseVO = AlarmRecordResponseVO.builder().build();
                BeanUtils.copyProperties(orderByDataNoIndex.get(i), alarmRecordResponseVO);
                alarmRecordResponseVO.setIndex(i + 1);
                orderByData.add(alarmRecordResponseVO);
            }
            if (orderByData.size() >= 3) {
                //前三
                topThreeResponseList = orderByData.subList(0, 3);
                //后三
                endThreeResponseList = orderByData.subList(orderByData.size() - 3, orderByData.size());
            } else {
                topThreeResponseList = orderByData;
                endThreeResponseList = orderByData;
            }
            //反排序处理
            if (CollectionUtils.isNotEmpty(endThreeResponseList)) {
                List<AlarmRecordResponseVO> sortEndThree = new ArrayList<>();
                for (int i = 0; i < endThreeResponseList.size(); i++) {
                    AlarmRecordResponseVO alarmRecordResponseVO = AlarmRecordResponseVO.builder().build();
                    BeanUtils.copyProperties(endThreeResponseList.get(i), alarmRecordResponseVO);
                    sortEndThree.add(alarmRecordResponseVO);
                }
                Collections.reverse(sortEndThree);
                for (int i = 0; i < sortEndThree.size(); i++) {
                    sortEndThree.get(i).setIndex(i + 1);
                }
                endThreeResponseList = sortEndThree;
            }
            List<String> paramName = new ArrayList<>(3);
            List<String> paramRatio = new ArrayList<>(3);
            for (int i = 0; i < topThreeResponseList.size(); i++) {
                AlarmRecordResponseVO alarmRecordResponseVO = topThreeResponseList.get(i);
                if (prefix.contains("User")) {
                    paramName.add(alarmRecordResponseVO.getUserName());
                } else if (prefix.contains("Dep")) {
                    paramName.add(alarmRecordResponseVO.getDepartmentName());
                }

                if (prefix.contains("OutDay")) {
                    paramRatio.add(alarmRecordResponseVO.getOutDayHandlerRatio());
                } else if (prefix.contains("Response")) {
                    paramRatio.add(alarmRecordResponseVO.getRespondRatio());
                } else if (prefix.contains("Handler")) {
                    paramRatio.add(alarmRecordResponseVO.getAlarmHandlerRatio());
                }
            }
            StringBuilder sortName = new StringBuilder();
            for (String name : paramName) {
                if (StringUtils.isNotBlank(sortName.toString())) {
                    sortName.append("、");
                }
                sortName.append(name);
            }
            StringBuilder sortRatio = new StringBuilder();
            for (String ratio : paramRatio) {
                if (StringUtils.isNotBlank(sortRatio.toString())) {
                    sortRatio.append("、");
                }
                sortRatio.append(ratio);
            }
            if (prefix.contains("OutDay")) {
                dataMap.put(prefix + "Top", String.format(
                        prefix.contains("User") ? ALARM_OUT_OF_DAY_SORT_USER : ALARM_OUT_OF_DAY_SORT, sortName, sortRatio));
            } else if (prefix.contains("Response")) {
                dataMap.put(prefix + "Top", String.format(
                        prefix.contains("User") ? ALARM_RESPONSE_SORT_USER : ALARM_RESPONSE_SORT, sortName, sortRatio));
            } else if (prefix.contains("Handler")) {
                dataMap.put(prefix + "Top", String.format(ALARM_HANDLER_SORT, sortName, sortRatio));
            }
        } else {
            if (prefix.contains("OutDay")) {
            } else if (prefix.contains("Response")) {
                dataMap.put(prefix + "Top", NO_DATA_TEMP);
            } else if (prefix.contains("Handler")) {
                dataMap.put(prefix + "Top", NO_DATA_TEMP);
            }
        }
        if (!prefix.contains("OutDay")) {
            dataMap.put(prefix + "TopThreeList", topThreeResponseList);
            tableParam.add(prefix + "TopThreeList");
        }
    }

    /**
     * 根据指定条件分组
     *
     * @param alarmRecordReportVOS
     * @param groupType
     *
     * @return
     */
    private static Map<String, AlarmRecordVO> groupByParamType(List<AlarmRecordReportVO> alarmRecordReportVOS, String groupType) {
        Map<String, AlarmRecordVO> alarmRecordLevelMap = new HashMap<>();
        Map<String, List<AlarmRecordReportVO>> groupByType = new HashMap<>();
        if (!CollectionUtils.isEmpty(alarmRecordReportVOS)) {
            if (LEVEL.equals(groupType)) {
                groupByType = alarmRecordReportVOS
                        .stream().filter(t -> StringUtils.equalsAny(t.getAlarmLevelCode(), ONE_LEVEL, TWO_LEVEL))
                        .collect(Collectors.groupingBy(AlarmRecordReportVO::getAlarmLevelCode));
            } else if (APPLICATION.equals(groupType)) {
                groupByType = alarmRecordReportVOS
                        .stream()
                        .collect(Collectors.groupingBy(AlarmRecordReportVO::getApplicationCode));
            }
            for (Map.Entry<String, List<AlarmRecordReportVO>> entry : groupByType.entrySet()) {
                //报警数
                Long total = 0L;
                //真实报警数
                Long alarmActualNum = 0L;
                //已响应报警数
                Long respondedNum = 0L;
                //已处理报警数
                Long alarmHandler = 0L;
                //超时的报警数
                Long timeoutNum = 0L;
                //跨天处理数
                Long outDayHandlerNum = 0L;

                //报警事件分组
                Map<String, List<AlarmRecordReportVO>> alarmRecordEventMap = new HashMap<>();
                //按部门分组
                Map<String, List<AlarmRecordReportVO>> alarmRecordDepartmentMap = new HashMap<>();
                //按用户分组
                Map<String, List<AlarmRecordReportVO>> alarmRecordUserMap = new HashMap<>();
                for (AlarmRecordReportVO alarmRecordReportVO : entry.getValue()) {
                    total = total + alarmRecordReportVO.getAlarmNum();
                    alarmActualNum = alarmActualNum +
                                     (alarmRecordReportVO.getAlarmActualNum() != null ? alarmRecordReportVO.getAlarmActualNum() : 0L);
                    respondedNum =
                            respondedNum + (alarmRecordReportVO.getRespondedNum() != null ? alarmRecordReportVO.getRespondedNum() : 0L);
                    ;
                    alarmHandler = alarmHandler +
                                   (alarmRecordReportVO.getAlarmHandlerNum() != null ? alarmRecordReportVO.getAlarmHandlerNum() : 0L);
                    ;
                    timeoutNum = timeoutNum + (alarmRecordReportVO.getTimeoutNum() != null ? alarmRecordReportVO.getTimeoutNum() : 0L);
                    ;
                    outDayHandlerNum = outDayHandlerNum +
                                       (alarmRecordReportVO.getOutDayHandlerNum() != null ? alarmRecordReportVO.getOutDayHandlerNum() : 0L);
                    ;
                    if (alarmRecordEventMap.containsKey(alarmRecordReportVO.getAlarmEventName())) {
                        alarmRecordEventMap.get(alarmRecordReportVO.getAlarmEventName()).add(alarmRecordReportVO);
                    } else {
                        List<AlarmRecordReportVO> alarmRecordReportVOS1 = new ArrayList<>(alarmRecordReportVOS.size());
                        alarmRecordReportVOS1.add(alarmRecordReportVO);
                        alarmRecordEventMap.put(alarmRecordReportVO.getAlarmEventName(), alarmRecordReportVOS1);
                    }
                    if (alarmRecordDepartmentMap.containsKey(alarmRecordReportVO.getDepartmentName())) {
                        alarmRecordDepartmentMap.get(alarmRecordReportVO.getDepartmentName()).add(alarmRecordReportVO);
                    } else {
                        List<AlarmRecordReportVO> alarmRecordReportVOS1 = new ArrayList<>(alarmRecordReportVOS.size());
                        alarmRecordReportVOS1.add(alarmRecordReportVO);
                        alarmRecordDepartmentMap.put(alarmRecordReportVO.getDepartmentName(), alarmRecordReportVOS1);
                    }
                    if (alarmRecordUserMap.containsKey(alarmRecordReportVO.getUserName())) {
                        alarmRecordUserMap.get(alarmRecordReportVO.getUserName()).add(alarmRecordReportVO);
                    } else {
                        List<AlarmRecordReportVO> alarmRecordReportVOS1 = new ArrayList<>(alarmRecordReportVOS.size());
                        alarmRecordReportVOS1.add(alarmRecordReportVO);
                        alarmRecordUserMap.put(alarmRecordReportVO.getUserName(), alarmRecordReportVOS1);
                    }

                }
                AlarmRecordVO build = AlarmRecordVO.builder()
                        .alarmTotalNum(total)
                        .alarmActualNum(alarmActualNum)
                        .respondedNum(respondedNum)
                        .alarmHandler(alarmHandler)
                        .timeoutNum(timeoutNum)
                        .outDayHandlerNum(outDayHandlerNum)
                        .alarmRecordEventMap(alarmRecordEventMap)
                        .alarmRecordDepartmentMap(alarmRecordDepartmentMap)
                        .alarmRecordUserMap(alarmRecordUserMap)
                        .build();
                alarmRecordLevelMap.put(entry.getKey(), build);
            }

        }
        return alarmRecordLevelMap;
    }

    /**
     * 增长和下降判断
     *
     * @param dataMap
     * @param objectType
     * @param ratio
     */
    private void upOrDown(Map<String, Object> dataMap, String objectType, BigDecimal ratio) {

        if (BigDecimal.ZERO.compareTo(ratio) > 0) {
            dataMap.put(objectType, "下降" + ratio.abs() + "%");
        } else {
            dataMap.put(objectType, "增长" + ratio.abs() + "%");
        }
    }

    /**
     * 上传word到服务器
     */
    private Long uploadWord(XWPFTemplate template, String fileName, TenantDTO tenantDTO) {
        var multipartFile = this.fileByteConvertMultipartFile(template, fileName);
        FileVO fileVO = null;
        if (ObjectUtils.isNotEmpty(multipartFile)) {
            var fileModifyParam = new FileModifyParam();
            fileModifyParam.setFile(multipartFile);
            fileModifyParam.setBusinessKey("alarm-word");
            fileModifyParam.setTenant(tenantDTO.getCode());
            try {
                fileVO = fileClient.upload(fileModifyParam);
            } catch (Exception e) {
                log.error("{}：上传文件失败", tenantDTO.getName(), e);
            }
        }
        return Optional.ofNullable(fileVO).orElse(new FileVO()).getId();
    }

    /**
     * xwpfTemplate 转 MultipartFile
     */
    private MultipartFile fileByteConvertMultipartFile(XWPFTemplate xwpfTemplate, String fileName) {
        try (var out = new ByteArrayOutputStream()) {
            xwpfTemplate.write(out);
            var bytes = out.toByteArray();
            return SpringFileUtils.fileByteConvertMultipartFile(fileName, bytes);
        } catch (IOException e) {
            log.error("文件字节转CommonsMultipartFile对象失败,失败原因", e);
        }
        return null;
    }
}
