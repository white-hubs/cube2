package com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.application.querygateway;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.style.StyleUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.biz.ouaa.client.TerminalClient;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserTenantDTO;
import com.guhecloud.cube.biz.ouaa.client.param.UserQueryParam;
import com.guhecloud.cube.biz.ouaa.client.param.WorkStatusQueryParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.AttConstants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.application.querygateway.AttSchedulingStaffQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.domain.repository.AttGroupRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.domain.valueobject.AttClassesTypeEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.domain.valueobject.AttGroupRangesEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper.AttClassesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper.AttGroupMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper.AttGroupStaffMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttClassesPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttGroupPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttGroupStaffPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.application.dto.AttClockSchedulingClassesDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.application.dto.AttSchedulingClassesDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.application.dto.AttSchedulingDateDOT;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.application.dto.AttSchedulingRuleSameShiftDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.application.dto.AttSchedulingStoreyDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.domain.valueobject.AttSchedulingWaysEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.domain.valueobject.AttStaffWorkStatusEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.mapper.AttSchedulingMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.mapper.AttSchedulingRuleClassesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.mapper.AttSchedulingRuleMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.po.AttSchedulingPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.po.AttSchedulingRuleClassesPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.po.AttSchedulingRulePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.po.AttSchedulingStaffPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.param.AttSchedulingPageParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.param.AttWorkStaffPageParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.param.AttWorkingStaffParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.result.AttSchedulingHandResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.result.AttSchedulingPageResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.result.AttWorkingResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.result.AttWorkingStaffResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.utils.AttLegalHolidayUtils;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.utils.dto.AttLegalHolidayMapDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.util.ExcelUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.BeanUtils;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 排班查询服务
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttSchedulingQueryService {
    private final AttSchedulingMapper attSchedulingMapper;
    private final AttGroupStaffMapper attGroupStaffMapper;
    private final AttGroupMapper attGroupMapper;
    private final AttSchedulingRuleClassesMapper attSchedulingRuleClassesMapper;
    private final AttGroupRepository attGroupRepository;
    private final AttSchedulingRuleMapper attSchedulingRuleMapper;
    private final AttClassesMapper attClassesMapper;
    private final AttSchedulingStaffQueryService attSchedulingStaffQueryService;
    private final TerminalClient terminalClient;
    private final UserClient userClient;

    /**
     * 排班分页列表
     *
     * @param param 条件入参
     *
     * @return 排班分页列表
     */
    public Pagination<AttSchedulingPageResult> pageAttScheduling(AttSchedulingPageParam param) {
        // 获取分页的人员对象
        Pagination<AttSchedulingStaffPO> schedulingPageResultPagination = attSchedulingStaffQueryService.getSchedulingStaffIdPage(param);
        // 根据id获取排班信息
        List<AttSchedulingPageResult> resultList = schedulingPageResultPagination.getRecords().stream().map(staffResult -> {
            AttSchedulingPageResult result = new AttSchedulingPageResult();
            result.setStaffId(staffResult.getStaffId());
            result.setStaffName(staffResult.getStaffName());
            // 获取人员的考勤组
            AttGroupPO groupPO = attGroupRepository.getGroupByStaffId(staffResult.getStaffId());
            if (groupPO != null) {
                result.setGroupId(groupPO.getId());
                AttSchedulingRulePO rulePO = attSchedulingRuleMapper.selectById(groupPO.getSchedulingRuleId());
                result.setRuleId(rulePO.getId());
                result.setSchedulingWays(rulePO.getWays());
                result.setLegalHolidaysRest(rulePO.getLegalHolidaysRest());
                if (AttSchedulingWaysEnum.FIXED.name().equals(rulePO.getWays())) {
                    // 设置使用规则的排班班次
                    result.setSchedulingRuleClassesList(attSchedulingRuleClassesMapper.selectRuleClassesByRuleId(groupPO.getSchedulingRuleId()));
                }
            }
            // 获取手动排班
            result.setSchedulingHandList(this.getHandSchedulingByStaffId(staffResult.getStaffId(), param.getYearMonth()));
            return result;
        }).collect(Collectors.toList());

        return new Pagination<>(schedulingPageResultPagination.getTotal(), schedulingPageResultPagination.getPages(), resultList);

    }

    /**
     * 导出排班
     *
     * @param param    条件
     * @param request  请假
     * @param response 返回
     */
    public void exportAttScheduling(AttSchedulingPageParam param, HttpServletRequest request, HttpServletResponse response) {
        long start = System.currentTimeMillis();
        String[] yearMonths = param.getYearMonth().split("-");
        int year = Integer.parseInt(yearMonths[0]);
        int month = Integer.parseInt(yearMonths[1]);
        AttLegalHolidayMapDTO holidayDTO = AttLegalHolidayUtils.getMonthLegalHolidayMap(year, month);
        // 获取人员
        List<AttSchedulingStaffPO> staffList = attSchedulingStaffQueryService.getSchedulingStaffIdAll(param);
        // 所有考勤组
        List<AttGroupPO> groupPOList = attGroupMapper.selectList(Wrappers.lambdaQuery());
        Map<Long, AttGroupPO> groupPOMap = new HashMap<>();
        if (!groupPOList.isEmpty()) {
            groupPOMap = groupPOList.stream().collect(Collectors.toMap(AttGroupPO::getId, v -> v));
        }
        // 所有规则
        List<AttSchedulingRulePO> rulePOList = attSchedulingRuleMapper.selectList(Wrappers.lambdaQuery());
        Map<Long, AttSchedulingRulePO> rulePOMap = new HashMap<>();
        if (!rulePOList.isEmpty()) {
            rulePOMap = rulePOList.stream().collect(Collectors.toMap(AttSchedulingRulePO::getId, v -> v));
        }
        // 所有人员考勤组关系
        List<AttGroupStaffPO> groupStaffPOList = attGroupStaffMapper.selectList(Wrappers.lambdaQuery());
        Map<Long, List<AttGroupStaffPO>> staffGroupMap = new HashMap<>();
        if (!groupStaffPOList.isEmpty()) {
            staffGroupMap = groupStaffPOList.stream().collect(Collectors.groupingBy(AttGroupStaffPO::getStaffId));
        }
        AttGroupPO allGroupPO = null;
        List<AttGroupPO> groupAllList = attGroupMapper.selectList(Wrappers.<AttGroupPO>lambdaQuery()
                                                                          .eq(AttGroupPO::getRanges, AttGroupRangesEnum.ALL.name())
                                                                 );
        if (groupAllList != null && !groupAllList.isEmpty()) {
            allGroupPO = groupAllList.getFirst();
        }
        // 循环获取人员的排班
        List<AttSchedulingStoreyDTO> schedulingStoreyDTOList = new ArrayList<>();
        for (AttSchedulingStaffPO schedulingStaffPO : staffList) {
            // 获取考勤组
            AttGroupPO groupPO;
            if (allGroupPO != null) {
                groupPO = allGroupPO;
            } else {
                Long groupId = 0L;
                List<AttGroupStaffPO> groupTempList = staffGroupMap.get(schedulingStaffPO.getStaffId());
                if (groupTempList != null && !groupTempList.isEmpty()) {
                    groupId = groupTempList.getFirst().getGroupId();
                }
                groupPO = groupPOMap.get(groupId);
            }

            AttSchedulingRulePO rulePO = null;
            if (groupPO != null) {
                schedulingStaffPO.setGroupId(groupPO.getId());
                rulePO = rulePOMap.get(groupPO.getSchedulingRuleId());
            }

            AttSchedulingStoreyDTO storeyDTO = new AttSchedulingStoreyDTO();
            storeyDTO.setStaffId(schedulingStaffPO.getStaffId());
            storeyDTO.setStaffName(schedulingStaffPO.getStaffName());
            List<AttSchedulingDateDOT> dateDOTList = this.getScheduling(schedulingStaffPO.getStaffId(), param.getYearMonth(), rulePO, holidayDTO, false);
            storeyDTO.setDateDOTList(dateDOTList);
            schedulingStoreyDTOList.add(storeyDTO);
        }
        Map<String, String> headers = new LinkedHashMap<>();
        List<Map<String, String>> excelDataList = new ArrayList<>();
        // 遍历生成导出的数据
        for (int i = 0; i < schedulingStoreyDTOList.size(); i++) {
            // 循环人
            AttSchedulingStoreyDTO storeyDTO = schedulingStoreyDTOList.get(i);
            Map<String, String> dataMap = new LinkedHashMap<>();
            dataMap.put("staffName", storeyDTO.getStaffName());
            if (i == 0) {
                headers.put("staffName", "员工");
            }
            int dateSize = storeyDTO.getDateDOTList().size();
            for (int j = 0; j < dateSize; j++) {
                // 循环人的日期
                if (i == 0) {
                    headers.put("classes" + j, DateUtils.formatDate(storeyDTO.getDateDOTList().get(j).getDate()));
                }
                AttSchedulingDateDOT dateDOT = storeyDTO.getDateDOTList().get(j);
                boolean lawDateBool = Boolean.TRUE.equals(dateDOT.getLawRest()) || Boolean.TRUE.equals(dateDOT.getLawWork());
                StringBuilder classStr = new StringBuilder();
                if (dateDOT.getClassesDTOList() == null || dateDOT.getClassesDTOList().isEmpty()) {
                    classStr.append("休息");
                } else {
                    for (AttSchedulingClassesDTO classesDTO : dateDOT.getClassesDTOList()) {
                        if (classesDTO.getId() == null) {
                            classStr.append("休息");
                            continue;
                        }
                        classStr.append(classesDTO.getNickName()).append("\r\n");
                    }
                }
                if (lawDateBool) {
                    classStr.append("（法）");
                }
                dataMap.put("classes" + j, classStr.toString());
            }
            excelDataList.add(dataMap);
        }

        //导出Excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.setHeaderAlias(headers);
        writer.setOnlyAlias(true);
        //默认配置
        writer.write(excelDataList, true);
        writer.setColumnWidth(-1, 15);
        //设置单元格自动换行
        CellStyle cellStyle = writer.getCellStyle();
        cellStyle.setWrapText(true);
        String fileName = (StringUtils.isNotBlank(param.getYearMonth()) ? param.getYearMonth() + "_" : "") + "排班";
        log.info("导出排班处理耗时(ms)：" + (System.currentTimeMillis() - start));

        ExcelUtils.downloadExcel(request, response, fileName, writer);

    }

    /**
     * 下载排班模板
     *
     * @param param    条件
     * @param request  请求对象
     * @param response 返回对象
     */
    public void downloadTemplate(AttSchedulingPageParam param, HttpServletRequest request, HttpServletResponse response) {
        Assert.isParameterError(param.getGroupId() == null, "请选择导出的考勤组！");
        AttGroupPO groupPO = attGroupMapper.selectById(param.getGroupId());
        Assert.isNotFound(groupPO, "考勤组不存在,请检查是否被删除!");
        // 获取人员
        List<AttGroupStaffPO> staffList = attGroupStaffMapper.selectList(Wrappers.<AttGroupStaffPO>lambdaQuery()
                                                                                 .eq(AttGroupStaffPO::getGroupId, param.getGroupId())
                                                                        );
        Assert.isNotFound(staffList == null || staffList.isEmpty(), "暂无排班人员！");

        ExcelWriter writer = ExcelUtil.getWriter(true);

        /* 获取班次 */
        List<String> classInfoDataList = new ArrayList<>();
        List<String> classNickNameList = new ArrayList<>();
        // 默认查询全部班次
        LambdaQueryWrapper<AttClassesPO> classWrapper = Wrappers.<AttClassesPO>lambdaQuery().orderByAsc(AttClassesPO::getStartWorkTime);
        /* 如果考勤组有选择使用班次就用使用的班次 */
        if (StringUtils.isNotBlank(groupPO.getGroupClassIds())) {
            String[] groupClassIdArr = groupPO.getGroupClassIds().split(",");
            List<Long> classIdParamList = new ArrayList<>();
            for (String classId : groupClassIdArr) {
                classIdParamList.add(Long.valueOf(classId));
            }
            if (!classIdParamList.isEmpty()) {
                classWrapper = Wrappers.<AttClassesPO>lambdaQuery().in(AttClassesPO::getId, classIdParamList)
                        .orderByAsc(AttClassesPO::getStartWorkTime);
            }
        }
        List<AttClassesPO> classesPOList = attClassesMapper.selectList(classWrapper);
        if (classesPOList != null) {
            for (AttClassesPO classesPO : classesPOList) {
                String tempValue = classesPO.getNickName() + ":" + classesPO.getName() + classesPO.getStartWorkTime() + "-" +
                                   classesPO.getEndWorkTime();
                classInfoDataList.add(tempValue);
                classNickNameList.add(classesPO.getNickName());
            }
        }
        if (classNickNameList.isEmpty()) {
            throw new ServiceException("考勤组无可用班次！");
        }

        /* 处理表头 */
        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("staffName", "员工姓名");
        LocalDateTime printDate = DateUtils.parseDate(param.getYearMonth() + "-01");
        LocalDateTime monthEndDate = DateUtils.endOfMonth(printDate);
        String[] yearMonths = param.getYearMonth().split("-");
        int index = AttConstants.oneInt;
        do {
            String heardString = yearMonths[AttConstants.oneInt] + "-" + String.format("%02d", index) + "\r\n" +
                                 printDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.CHINESE);
            headers.put("staffDay" + index, heardString);
            printDate = DateUtils.offsetDay(printDate, AttConstants.oneInt);
            index++;
        } while (printDate.isBefore(monthEndDate) || printDate.isEqual(monthEndDate));
        int headerSize = headers.size();
        /* 处理导出内容 */
        List<Map<String, String>> excelDataList = new ArrayList<>();
        for (AttGroupStaffPO attGroupStaffPO : staffList) {
            Map<String, String> staffData = new HashMap<>();

            staffData.put("staffName", attGroupStaffPO.getName());
            for (int j = 1; j < index; j++) {
                staffData.put("staffDay" + j, "休息");
            }
            excelDataList.add(staffData);
        }

        // 下拉框范围
        CellRangeAddressList cellRangeAddressList = new CellRangeAddressList(2, excelDataList.size() + 1, 1, headerSize + 1);

        /* 设置样式 */
        Sheet sheet = writer.getSheet();
        // 设置表头背景色
        CellStyle headCellStyle = writer.getHeadCellStyle();
        headCellStyle.setLocked(true);
        StyleUtil.setColor(headCellStyle, IndexedColors.AQUA, FillPatternType.SOLID_FOREGROUND);
        //设置单元格自动换行
        CellStyle cellStyle = writer.getCellStyle();
        cellStyle.setWrapText(true);
        cellStyle.setLocked(false);
        // 设置列宽
        writer.setColumnWidth(-1, 25);
        writer.setRowHeight(-1, 28);

        /* 设置数据 */
        writer.passCurrentRow();
        writer.setHeaderAlias(headers);
        writer.write(excelDataList, true);
        // 设置下拉班次
        this.createClassesSelect(writer, classNickNameList, cellRangeAddressList, param.getYearMonth());
        /* 设置显示的班次 */
        for (int i1 = AttConstants.zeroInt; i1 < classInfoDataList.size(); i1++) {
            writer.writeCellValue(i1, AttConstants.zeroInt, classInfoDataList.get(i1));
        }
        String fileName = (StringUtils.isNotBlank(param.getYearMonth()) ? param.getYearMonth() + "_" : "") + "PB";
        ExcelUtils.downloadExcel(request, response, fileName, writer);
    }

    public void createClassesSelect(ExcelWriter writer, List<String> classesNames, CellRangeAddressList cellRangeAddressList,
                                    String yearMonth
                                   ) {
        String classesNameSheet = "班次列表";
        writer.setSheet(classesNameSheet);
        for (int i = AttConstants.zeroInt; i < classesNames.size(); i++) {
            writer.writeCellValue(AttConstants.zeroInt, i, classesNames.get(i));
        }
        // 添加休息班次
        writer.writeCellValue(AttConstants.zeroInt, classesNames.size(), "休息");
        // 添加月份校验标识
        writer.writeCellValue(1, 0, yearMonth);
        // 添加模板类型标识
        writer.writeCellValue(2, 0, "ARRANGE");
        writer.getSheet().protectSheet("abc123");
        writer.setSheet("sheet1");
        String classesValida = "=" + classesNameSheet + "!$A$1:$A$" + classesNames.size() + AttConstants.oneInt;
        DataValidationHelper helper = new XSSFDataValidationHelper((XSSFSheet) writer.getSheet());
        DataValidationConstraint formulaListConstraint = helper.createFormulaListConstraint(classesValida);
        DataValidation dataValidation = helper.createValidation(formulaListConstraint, cellRangeAddressList);
        //设置只能选下拉菜单里的值不能随便输入
        dataValidation.setSuppressDropDownArrow(true);
        dataValidation.setShowErrorBox(true);
        dataValidation.setEmptyCellAllowed(false);

        writer.addValidationData(dataValidation);
        // 将创建的班次列表sheet隐藏
        writer.getWorkbook().setSheetHidden(AttConstants.oneInt, true);
    }

    /**
     * 获取某人某月的排班
     * 三班倒 或 固定班(综合的手动和规则的排班)
     *
     * @param staffId   人员id
     * @param yearMonth 年月(yyyy-MM)
     *
     * @return 排班集合
     */
    public List<AttSchedulingDateDOT> getScheduling(Long staffId, String yearMonth) {
        if (yearMonth == null) {
            return new ArrayList<>();
        }
        AttGroupPO groupPO = attGroupRepository.getGroupByStaffId(staffId);
        if (groupPO == null) {
            return new ArrayList<>();
        }
        AttSchedulingRulePO rulePO = attSchedulingRuleMapper.selectById(groupPO.getSchedulingRuleId());
        if (rulePO == null) {
            return new ArrayList<>();
        }
        String[] yearMonths = yearMonth.split("-");
        return getScheduling(staffId, yearMonth, rulePO, AttLegalHolidayUtils.getMonthLegalHolidayMap(Integer.parseInt(yearMonths[0]), Integer.parseInt(yearMonths[1])), true);
    }

    /**
     * 获取某人某月的排班
     * 完全日期的排班，
     * 三班倒 或 固定班(综合的手动和规则的排班)
     *
     * @param staffId     员工id
     * @param yearMonth   年月(2023-09)
     * @param rulePO      规则对象
     * @param holidayDTO  法定班休对象
     * @param isClassTime 班次是否需要带日期
     *
     * @return 排班集合
     */
    public List<AttSchedulingDateDOT> getScheduling(Long staffId, String yearMonth, AttSchedulingRulePO rulePO,
                                                    AttLegalHolidayMapDTO holidayDTO, Boolean isClassTime
                                                   ) {
        if (rulePO != null && AttSchedulingWaysEnum.FIXED.name().equals(rulePO.getWays())) {
            return this.getFixedScheduling(staffId, yearMonth, rulePO, holidayDTO, isClassTime);
        }
        return this.getShiftsScheduling(staffId, yearMonth, isClassTime);
    }

    /**
     * 获取某月固定班的排班
     * 完全日期的排班，综合规则和手动的排班
     *
     * @param staffId     员工id
     * @param yearMonth   年月（2023-09）
     * @param rulePO      规则对象
     * @param holidayDTO  法定班休对象
     * @param isClassTime 班次是否需要带日期
     *
     * @return 排班集合
     */
    public List<AttSchedulingDateDOT> getFixedScheduling(Long staffId, String yearMonth, AttSchedulingRulePO rulePO,
                                                         AttLegalHolidayMapDTO holidayDTO, Boolean isClassTime
                                                        ) {
        List<AttSchedulingDateDOT> resultList = new ArrayList<>();
        // 法定工作日map;
        Map<String, String> workDateMap = holidayDTO.getWorkDateMap();
        // 法定休息日map
        Map<String, String> restDateMap = holidayDTO.getRestDateMap();
        LocalDateTime beginTime = DateUtils.parse(yearMonth + "-01");
        LocalDateTime endTme = DateUtils.endOfMonth(beginTime);
        // 先获取手动班
        List<AttSchedulingPO> schedulingPOList = attSchedulingMapper.selectList(Wrappers.<AttSchedulingPO>lambdaQuery()
                                                                                        .eq(AttSchedulingPO::getStaffId, staffId)
                                                                                        .eq(AttSchedulingPO::getSchedulingMonth, yearMonth)
                                                                               );
        // 手动排班日期的map
        Map<String, List<AttSchedulingPO>> schedulingPOMap = schedulingPOList.stream()
                .collect(Collectors.groupingBy(po -> po.getStaffId() + DateUtils.formatDate(po.getSchedulingDate())));
        // 获取规则的规则的班次集合
        List<AttSchedulingRuleClassesPO> ruleClassesPOList = new ArrayList<>();
        if (rulePO != null) {
            ruleClassesPOList = attSchedulingRuleClassesMapper.selectList(Wrappers.<AttSchedulingRuleClassesPO>lambdaQuery()
                                                                                  .eq(AttSchedulingRuleClassesPO::getRuleId, rulePO.getId())
                                                                         );
        }

        // 规则星期班次的map
        Map<String, List<AttSchedulingRuleClassesPO>> ruleClassMap = ruleClassesPOList.stream()
                .collect(Collectors.groupingBy(AttSchedulingRuleClassesPO::getWeek));
        Set<Long> notStartClassIdList = new HashSet<>();
        // 遍历日期
        LocalDateTime pointDate = DateUtils.newLocalDateTime(beginTime);
        do {
            AttSchedulingDateDOT schedulingDateDOT = new AttSchedulingDateDOT();
            schedulingDateDOT.setDate(pointDate);
            schedulingDateDOT.setYearMonth(yearMonth);
            schedulingDateDOT.setLawWork(false);
            schedulingDateDOT.setLawRest(false);
            if (rulePO != null && Boolean.TRUE.equals(rulePO.getLegalHolidaysRest()) &&
                restDateMap.get(DateUtils.formatDate(pointDate)) != null) {
                schedulingDateDOT.setLawRest(true);
            } else if (rulePO != null && Boolean.TRUE.equals(rulePO.getLegalHolidaysRest()) &&
                       workDateMap.get(DateUtils.formatDate(pointDate)) != null) {
                schedulingDateDOT.setLawWork(true);
            }

            List<AttSchedulingClassesDTO> schedulingClassesDTOList = new ArrayList<>();
            // 先看当天手动排班，没有手动再看当天的规则排班
            if (schedulingPOMap.get(staffId + DateUtils.formatDate(pointDate)) != null) {
                List<AttSchedulingPO> schedulingPOList1 = schedulingPOMap.get(staffId + DateUtils.formatDate(pointDate));
                for (AttSchedulingPO schedulingPO : schedulingPOList1) {
                    AttSchedulingClassesDTO classesDTO = new AttSchedulingClassesDTO();
                    // 如果当天有排班
                    if (schedulingPO.getClassesId() != null) {
                        classesDTO.setName(schedulingPO.getClassesName());
                        classesDTO.setId(schedulingPO.getClassesId());
                        classesDTO.setStarted(false);
                        classesDTO.setNickName(schedulingPO.getClassesNickName());
                        // 如果已开始说明已经有班次信息了
                        if (schedulingPO.getClassesRealStartTime() != null || Boolean.TRUE.equals(schedulingPO.getStarted())) {
                            classesDTO.setStarted(true);
                            classesDTO.setClassesType(schedulingPO.getClassesType());
                            classesDTO.setName(schedulingPO.getClassesName());
                            classesDTO.setStartClockBeforeTime(schedulingPO.getClassesBeforeStartTime());
                            classesDTO.setStartWorkTime(schedulingPO.getClassesRealStartTime());
                            classesDTO.setStartClockAfterTime(schedulingPO.getClassesAfterStartTime());
                            classesDTO.setEndClockBeforeTime(schedulingPO.getClassesBeforeEndTime());
                            classesDTO.setEndWorkTime(schedulingPO.getClassesRealEndTime());
                            classesDTO.setEndClockAfterTime(schedulingPO.getClassesAfterEndTime());
                            classesDTO.setRestStartTime(schedulingPO.getClassesRestStartTime());
                            classesDTO.setRestEndTime(schedulingPO.getClassesRestEndTime());
                        } else {
                            // 如果还未开始就将classId加入需要查询的id集合中
                            notStartClassIdList.add(schedulingPO.getClassesId());
                        }
                    }
                    schedulingClassesDTOList.add(classesDTO);
                }
                schedulingDateDOT.setClassesDTOList(schedulingClassesDTOList);
            } else {
                // 当天规则的排班集合
                int week = pointDate.getDayOfWeek().getValue();
                List<AttSchedulingRuleClassesPO> ruleClassesDayPOList = ruleClassMap.get(String.valueOf(week));
                // 法定节假日休息
                boolean legalHolidaysRestBool = rulePO != null && Boolean.TRUE.equals(rulePO.getLegalHolidaysRest()) &&
                                                restDateMap.get(DateUtils.formatDate(pointDate)) != null;
                // 法定工作日
                boolean legalHolidaysWorkBool = rulePO != null && Boolean.TRUE.equals(rulePO.getLegalHolidaysRest()) &&
                                                workDateMap.get(DateUtils.formatDate(pointDate)) != null;
                // 之前的日期
                boolean beforeDateBool = pointDate.isBefore(DateUtils.beginOfDay(LocalDateTime.now()));
                // 规则没有排班
                boolean ruleRestBool = ruleClassesPOList.isEmpty() || ruleClassesDayPOList == null;
                if (beforeDateBool) {
                    AttSchedulingClassesDTO classesDTO = new AttSchedulingClassesDTO();
                    classesDTO.setNickName("休息");
                    classesDTO.setName("休息");
                    classesDTO.setStarted(true);
                    schedulingClassesDTOList.add(classesDTO);
                } else if (ruleRestBool && legalHolidaysWorkBool && ruleClassMap.get(String.valueOf(1)) != null) {
                    // (没有手动排班)也没有规则排班，但是有法定节假日上班
                    for (AttSchedulingRuleClassesPO ruleClassesPO : ruleClassMap.get(String.valueOf(1))) {
                        AttSchedulingClassesDTO classesDTO = new AttSchedulingClassesDTO();
                        classesDTO.setId(ruleClassesPO.getId());
                        classesDTO.setStarted(false);
                        schedulingClassesDTOList.add(classesDTO);
                        notStartClassIdList.add(ruleClassesPO.getId());
                    }
                } else if (!ruleRestBool && !legalHolidaysRestBool) {
                    // 规则计算出来的理论上来说全是未开始的班次(因为已经开始的班都已经通过定时写入到排班表了。）
                    for (AttSchedulingRuleClassesPO ruleClassesPO : ruleClassesDayPOList) {
                        AttSchedulingClassesDTO classesDTO = new AttSchedulingClassesDTO();
                        classesDTO.setId(ruleClassesPO.getId());
                        classesDTO.setStarted(false);
                        schedulingClassesDTOList.add(classesDTO);
                        notStartClassIdList.add(ruleClassesPO.getId());
                    }
                } else {
                    AttSchedulingClassesDTO classesDTO = new AttSchedulingClassesDTO();
                    classesDTO.setName("休息");
                    classesDTO.setNickName("休息");
                    classesDTO.setStarted(true);
                    schedulingClassesDTOList.add(classesDTO);
                }
            }
            schedulingDateDOT.setClassesDTOList(schedulingClassesDTOList);
            resultList.add(schedulingDateDOT);
            pointDate = DateUtils.offsetDay(pointDate, 1);
        } while (!pointDate.isAfter(endTme));
        List<AttClassesPO> classesPOList = new ArrayList<>();
        if (!notStartClassIdList.isEmpty()) {
            classesPOList = attClassesMapper.selectByIds(notStartClassIdList);
        }
        Map<Long, AttClassesPO> classPOMap = classesPOList.stream().collect(Collectors.toMap(AttClassesPO::getId, v -> v));
        // 设置班次信息(名称
        for (AttSchedulingDateDOT schedulingDateDOT : resultList) {
            for (AttSchedulingClassesDTO classesDTO : schedulingDateDOT.getClassesDTOList()) {
                if (classesDTO.getId() != null && StringUtils.isBlank(classesDTO.getName())) {
                    AttClassesPO classesPO = classPOMap.get(classesDTO.getId());
                    if (classesPO != null) {
                        classesDTO.setName(classesPO.getName());
                        classesDTO.setNickName(classesPO.getNickName());
                    } else {
                        classesDTO.setName("休息");
                        classesDTO.setNickName("休息");
                    }
                }
            }
        }
        if (Boolean.TRUE.equals(isClassTime)) {
            // 设置日期
            for (AttSchedulingDateDOT schedulingDateDOT : resultList) {
                for (AttSchedulingClassesDTO classesDTO : schedulingDateDOT.getClassesDTOList()) {
                    if (classesDTO.getId() != null && classesDTO.getStartWorkTime() == null) {
                        this.classesPOToSchedulingClassesDTO(DateUtils.newLocalDateTime(schedulingDateDOT.getDate()), classPOMap.get(classesDTO.getId()), classesDTO);
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(resultList)) {
                return resultList;
            }
        }

        return resultList;
    }

    /**
     * 获取某月的三班倒排班
     * 完全日期的排班
     *
     * @param staffId     人员id
     * @param yearMonth   年月(2023-09)
     * @param isClassTime 是否组装成带日期的班次对象
     *
     * @return 排班列表
     */
    public List<AttSchedulingDateDOT> getShiftsScheduling(Long staffId, String yearMonth, Boolean isClassTime) {
        List<AttSchedulingDateDOT> resultList = new ArrayList<>();
        LocalDateTime beginTime = DateUtils.parse(yearMonth + "-01");
        LocalDateTime endTme = DateUtils.endOfMonth(beginTime);
        // 获取手动班
        List<AttSchedulingPO> schedulingPOList = attSchedulingMapper.selectList(Wrappers.<AttSchedulingPO>lambdaQuery()
                                                                                        .eq(AttSchedulingPO::getStaffId, staffId)
                                                                                        .eq(AttSchedulingPO::getSchedulingMonth, yearMonth)
                                                                               );
        // 手动排班日期的map
        Map<String, List<AttSchedulingPO>> schedulingPOMap = schedulingPOList.stream()
                .collect(Collectors.groupingBy(po -> po.getStaffId() + DateUtils.formatDate(po.getSchedulingDate(), "yyyy-MM-dd")));
        // 未开始的班次id
        List<Long> notStartClassIdList = new ArrayList<>();
        LocalDateTime pointDate = DateUtils.newLocalDateTime(beginTime);
        do {
            AttSchedulingDateDOT schedulingDateDOT = new AttSchedulingDateDOT();
            schedulingDateDOT.setYearMonth(yearMonth);
            schedulingDateDOT.setDate(pointDate);
            List<AttSchedulingClassesDTO> schedulingClassesDTOList = new ArrayList<>();
            // 如果已经有手动的排班就组装手动排班数据，如果没有就组装一个休息的排班
            String str = DateUtils.formatDate(pointDate);
            if (schedulingPOMap.get(staffId + str) != null) {
                List<AttSchedulingPO> schedulingPOList1 = schedulingPOMap.get(staffId + str);
                for (AttSchedulingPO schedulingPO : schedulingPOList1) {
                    AttSchedulingClassesDTO classesDTO = new AttSchedulingClassesDTO();
                    // 如果当天有排班
                    if (schedulingPO.getClassesId() != null) {
                        classesDTO.setId(schedulingPO.getClassesId());
                        if (schedulingPO.getClassesRealStartTime() != null) {
                            classesDTO.setClassesType(schedulingPO.getClassesType());
                            classesDTO.setName(schedulingPO.getClassesName());
                            classesDTO.setNickName(schedulingPO.getClassesNickName());
                            classesDTO.setStartClockBeforeTime(schedulingPO.getClassesBeforeStartTime());
                            classesDTO.setStartWorkTime(schedulingPO.getClassesRealStartTime());
                            classesDTO.setStartClockAfterTime(schedulingPO.getClassesAfterStartTime());
                            classesDTO.setEndClockBeforeTime(schedulingPO.getClassesBeforeEndTime());
                            classesDTO.setEndWorkTime(schedulingPO.getClassesRealEndTime());
                            classesDTO.setEndClockAfterTime(schedulingPO.getClassesAfterEndTime());
                            classesDTO.setRestStartTime(schedulingPO.getClassesRestStartTime());
                            classesDTO.setRestEndTime(schedulingPO.getClassesRestEndTime());
                        } else {
                            notStartClassIdList.add(schedulingPO.getClassesId());
                        }
                        schedulingClassesDTOList.add(classesDTO);
                    } else {
                        classesDTO.setName("休息");
                        classesDTO.setNickName("休息");
                        schedulingClassesDTOList.add(classesDTO);
                        break;
                    }
                }
            } else {
                AttSchedulingClassesDTO classesDTO = new AttSchedulingClassesDTO();
                classesDTO.setName("休息");
                classesDTO.setNickName("休息");
                schedulingClassesDTOList.add(classesDTO);
            }

            schedulingDateDOT.setClassesDTOList(schedulingClassesDTOList);
            resultList.add(schedulingDateDOT);
            pointDate = DateUtils.offsetDay(pointDate, 1);
        } while (!pointDate.isAfter(endTme));

        if (CollectionUtils.isEmpty(notStartClassIdList)) {
            return resultList;
        }
        List<AttClassesPO> classesPOList = attClassesMapper.selectByIds(notStartClassIdList);
        Map<Long, AttClassesPO> classesPOMap = classesPOList.stream().collect(Collectors.toMap(AttClassesPO::getId, v -> v));
        // 设置班次信息(名称
        for (AttSchedulingDateDOT schedulingDateDOT : resultList) {
            for (AttSchedulingClassesDTO classesDTO : schedulingDateDOT.getClassesDTOList()) {
                if (classesDTO.getId() != null) {
                    AttClassesPO classesPO = classesPOMap.get(classesDTO.getId());
                    if (classesPO != null) {
                        classesDTO.setName(classesPO.getName());
                        classesDTO.setNickName(classesPO.getNickName());
                    }
                }
            }
        }
        if (Boolean.TRUE.equals(isClassTime)) {
            // 组装日期
            for (AttSchedulingDateDOT schedulingDateDOT : resultList) {
                for (AttSchedulingClassesDTO classesDTO : schedulingDateDOT.getClassesDTOList()) {
                    this.classesPOToSchedulingClassesDTO(schedulingDateDOT.getDate(), classesPOMap.get(classesDTO.getId()), classesDTO);
                }
            }
        }
        return resultList;
    }

    /**
     * 组装有日期的班次
     *
     * @param dateTime 日期
     * @param po       源班次
     *
     * @return 有日期的班次对象
     */
    public AttSchedulingClassesDTO classesPOToSchedulingClassesDTO(LocalDateTime dateTime, AttClockSchedulingClassesDTO po) {
        var dto = new AttSchedulingClassesDTO();
        String dateTimeStr = DateUtils.formatDate(dateTime);
        if (po == null || po.getId() == null) {
            dto.setName("休息");
            dto.setNickName("休息");
            dto.setStarted(DateUtils.beginOfDay(dateTime).isBefore(LocalDateTime.now()) ||
                           DateUtils.beginOfDay(dateTime).isEqual(LocalDateTime.now()));
            return dto;
        }
        BeanUtils.copyProperties(po, dto);
        dto.setStartClockBeforeTime(DateUtils.parse(dateTimeStr + " " + po.getStartClockBeforeTime()));
        dto.setStartWorkTime(DateUtils.parse(dateTimeStr + " " + po.getStartWorkTime()));
        dto.setStartClockAfterTime(DateUtils.parse(dateTimeStr + " " + po.getStartClockAfterTime()));
        dto.setEndClockBeforeTime(DateUtils.parse(dateTimeStr + " " + po.getEndClockBeforeTime()));
        dto.setEndWorkTime(DateUtils.parse(dateTimeStr + " " + po.getEndWorkTime()));
        dto.setEndClockAfterTime(DateUtils.parse(dateTimeStr + " " + po.getEndClockAfterTime()));
        if (StringUtils.isNotBlank(po.getRestStartTime()) && StringUtils.isNotBlank(po.getRestEndTime())) {
            dto.setRestStartTime(DateUtils.parse(dateTimeStr + " " + po.getRestStartTime()));
            dto.setRestEndTime(DateUtils.parse(dateTimeStr + " " + po.getRestEndTime()));
        }

        if (!DateUtils.parse(po.getStartWorkTime(), "HH:mm").isBefore(DateUtils.parse(po.getEndWorkTime(), "HH:mm"))) {
            dto.setEndTimeIsMorrow(true);
            dto.setEndClockBeforeTime(DateUtils.offsetDay(dto.getEndClockBeforeTime(), 1));
            dto.setEndClockAfterTime(DateUtils.offsetDay(dto.getEndClockAfterTime(), 1));
            dto.setEndWorkTime(DateUtils.offsetDay(dto.getEndWorkTime(), 1));

        }

        if (dto.getStartClockBeforeTime().isAfter(dto.getStartWorkTime())) {
            dto.setStartClockBeforeTime(DateUtils.offsetDay(dto.getStartClockBeforeTime(), -1));
        }
        if (dto.getStartClockAfterTime().isBefore(dto.getStartWorkTime())) {
            dto.setStartClockAfterTime(DateUtils.offsetDay(dto.getStartClockAfterTime(), 1));
        }
        if (dto.getEndClockAfterTime().isBefore(dto.getEndWorkTime())) {
            dto.setEndClockAfterTime(DateUtils.offsetDay(dto.getEndClockAfterTime(), 1));
        }
        if (dto.getEndClockBeforeTime().isAfter(dto.getEndWorkTime())) {
            dto.setEndClockBeforeTime(DateUtils.offsetDay(dto.getEndClockBeforeTime(), -1));
        }
        if (!dto.getStartWorkTime().isAfter(LocalDateTime.now())) {
            dto.setStarted(true);
        }

        if (dto.getRestStartTime() != null && dto.getRestEndTime() != null) {
            if (dto.getRestStartTime().isBefore(dto.getStartWorkTime())) {
                dto.setRestStartTime(DateUtils.offsetDay(dto.getRestStartTime(), 1));
            }
            if (dto.getRestEndTime().isBefore(dto.getEndWorkTime())) {
                dto.setRestEndTime(DateUtils.offsetDay(dto.getRestEndTime(), 1));
            }
        }

        return dto;
    }

    public AttSchedulingClassesDTO classesPOToSchedulingClassesDTO(LocalDateTime dateTime, AttClassesPO po, AttSchedulingClassesDTO dto) {
        String dateTimeStr = DateUtils.formatDate(dateTime);
        if (po == null || po.getId() == null) {
            dto.setName("休息");
            dto.setNickName("休息");
            dto.setStarted(DateUtils.beginOfDay(dateTime).isBefore(LocalDateTime.now()) ||
                           DateUtils.beginOfDay(dateTime).isEqual(LocalDateTime.now()));
            return dto;
        }
        BeanUtils.copyProperties(po, dto);
        dto.setStartClockBeforeTime(DateUtils.parse(dateTimeStr + " " + po.getStartClockBeforeTime()));
        dto.setStartWorkTime(DateUtils.parse(dateTimeStr + " " + po.getStartWorkTime()));
        // 设置是否开始
        dto.setStarted(!dto.getStartWorkTime().isAfter(DateUtils.beginOfDay(LocalDateTime.now())));
        dto.setStartClockAfterTime(DateUtils.parse(dateTimeStr + " " + po.getStartClockAfterTime()));
        dto.setEndClockBeforeTime(DateUtils.parse(dateTimeStr + " " + po.getEndClockBeforeTime()));
        dto.setEndWorkTime(DateUtils.parse(dateTimeStr + " " + po.getEndWorkTime()));
        dto.setEndClockAfterTime(DateUtils.parse(dateTimeStr + " " + po.getEndClockAfterTime()));
        if (StringUtils.isNotBlank(po.getRestStartTime()) && StringUtils.isNotBlank(po.getRestEndTime())) {
            dto.setRestStartTime(DateUtils.parse(dateTimeStr + " " + po.getRestStartTime()));
            dto.setRestEndTime(DateUtils.parse(dateTimeStr + " " + po.getRestEndTime()));
        }
        // 判断/处理跨天
        if (!DateUtils.parse(po.getStartWorkTime(), "HH:mm").isBefore(DateUtils.parse(po.getEndWorkTime(), "HH:mm"))) {
            dto.setEndTimeIsMorrow(true);
            dto.setEndClockBeforeTime(DateUtils.offsetDay(dto.getEndClockBeforeTime(), 1));
            dto.setEndClockAfterTime(DateUtils.offsetDay(dto.getEndClockAfterTime(), 1));
            dto.setEndWorkTime(DateUtils.offsetDay(dto.getEndWorkTime(), 1));
        }
        if (dto.getStartClockBeforeTime().isAfter(dto.getStartWorkTime())) {
            dto.setStartClockBeforeTime(DateUtils.offsetDay(dto.getStartClockBeforeTime(), -1));
        }
        if (dto.getStartClockAfterTime().isBefore(dto.getStartWorkTime())) {
            dto.setStartClockAfterTime(DateUtils.offsetDay(dto.getStartClockAfterTime(), 1));
        }
        if (dto.getEndClockAfterTime().isBefore(dto.getEndWorkTime())) {
            dto.setEndClockAfterTime(DateUtils.offsetDay(dto.getEndClockAfterTime(), 1));
        }
        if (dto.getEndClockBeforeTime().isAfter(dto.getEndWorkTime())) {
            dto.setEndClockBeforeTime(DateUtils.offsetDay(dto.getEndClockBeforeTime(), -1));
        }
        if (!dto.getStartWorkTime().isAfter(LocalDateTime.now())) {
            dto.setStarted(true);
        }
        if (dto.getRestStartTime() != null && dto.getRestEndTime() != null) {
            if (dto.getRestStartTime().isBefore(dto.getStartWorkTime())) {
                dto.setRestStartTime(DateUtils.offsetDay(dto.getRestStartTime(), 1));
            }
            if (dto.getRestEndTime().isBefore(dto.getEndWorkTime())) {
                dto.setRestEndTime(DateUtils.offsetDay(dto.getRestEndTime(), 1));
            }
        }
        return dto;
    }

    /**
     * 获取人员某月的手动排班
     *
     * @param staffId 人员id 必填
     * @param month   月份
     *
     * @return 排班对象
     */
    public List<AttSchedulingHandResult> getHandSchedulingByStaffId(Long staffId, String month) {
        if (staffId == null) {
            log.error("人员id为空，月份为：" + month);
        }
        List<AttSchedulingPO> schedulingPOList = attSchedulingMapper.selectList(Wrappers.<AttSchedulingPO>lambdaQuery()
                                                                                        .eq(AttSchedulingPO::getStaffId, staffId)
                                                                                        .eq(StringUtils.isNotBlank(month), AttSchedulingPO::getSchedulingMonth, month)
                                                                               );
        return schedulingPOList.stream().map(po -> {
            AttSchedulingHandResult dateResult = new AttSchedulingHandResult();
            // 如果不是休息且是未开始的班次需要查询班次信息
            if (po.getClassesId() != null && !Boolean.TRUE.equals(po.getStarted())) {
                AttClassesPO classesPO = attClassesMapper.selectById(po.getClassesId());
                this.stringClassToScheduling(po, classesPO);
                BeanUtils.copyProperties(po, dateResult);
            } else {
                BeanUtils.copyProperties(po, dateResult);
            }

            return dateResult;
        }).collect(Collectors.toList());
    }

    /**
     * 获取某人某月手动排班
     *
     * @param staffId
     * @param month
     *
     * @return
     */
    public List<AttSchedulingPO> getHandSchedulingPOByStaffId(Long staffId, String month) {
        if (staffId == null) {
            log.error("人员id为空，月份为：" + month);
        }
        List<AttSchedulingPO> schedulingPOList = attSchedulingMapper.selectList(Wrappers.<AttSchedulingPO>lambdaQuery()
                                                                                        .eq(AttSchedulingPO::getStaffId, staffId)
                                                                                        .eq(StringUtils.isNotBlank(month), AttSchedulingPO::getSchedulingMonth, month)
                                                                               );
        if (schedulingPOList == null) {
            return new ArrayList<>();
        }
        Map<Long, AttClassesPO> classesPOMap = new HashMap<>();
        List<Long> classIdList = schedulingPOList.stream().map(AttSchedulingPO::getClassesId).filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (!classIdList.isEmpty()) {
            List<AttClassesPO> attClassesPOList = attClassesMapper.selectByIds(classIdList);
            if (attClassesPOList != null && !attClassesPOList.isEmpty()) {
                classesPOMap = attClassesPOList.stream().collect(Collectors.toMap(AttClassesPO::getId, v -> v));
            }
        }
        for (AttSchedulingPO po : schedulingPOList) {
            if (po.getClassesId() != null && po.getClassesRealStartTime() == null) {
                this.stringClassToScheduling(po, classesPOMap.get(po.getClassesId()));
            }
        }
        return schedulingPOList;
    }

    /**
     * 无日期的排班转有日期的排班
     * 给排班设置班次时间
     *
     * @param schedulingPO 无日期的班次
     * @param classesPO    班次信息
     *
     * @return 有日期的排班
     */
    public AttSchedulingPO stringClassToScheduling(AttSchedulingPO schedulingPO, AttClassesPO classesPO) {

        schedulingPO.setStarted(false);
        if (classesPO == null) {
            schedulingPO.setClassesId(null);
            schedulingPO.setClassesName("休息");
            if (!schedulingPO.getSchedulingDate().isAfter(LocalDateTime.now())) {
                schedulingPO.setStarted(true);
            }
            return schedulingPO;
        }

        schedulingPO.setClassesId(classesPO.getId());
        schedulingPO.setClassesName(classesPO.getName());
        schedulingPO.setClassesNickName(classesPO.getNickName());
        schedulingPO.setHandScheduling(false);
        schedulingPO.setClassesType(classesPO.getClassesType());
        schedulingPO.setEndTimeIsMorrow(classesPO.getEndTimeIsMorrow());

        String newDateStr = DateUtils.formatDate(schedulingPO.getSchedulingDate());
        schedulingPO.setClassesBeforeStartTime(DateUtils.parse(newDateStr + " " + classesPO.getStartClockBeforeTime() + ":00"));
        schedulingPO.setClassesRealStartTime(DateUtils.parse(newDateStr + " " + classesPO.getStartWorkTime() + ":00"));
        schedulingPO.setClassesAfterStartTime(DateUtils.parse(newDateStr + " " + classesPO.getStartClockAfterTime() + ":00"));

        schedulingPO.setClassesBeforeEndTime(DateUtils.parse(newDateStr + " " + classesPO.getEndClockBeforeTime() + ":00"));
        schedulingPO.setClassesRealEndTime(DateUtils.parse(newDateStr + " " + classesPO.getEndWorkTime() + ":00"));
        schedulingPO.setClassesAfterEndTime(DateUtils.parse(newDateStr + " " + classesPO.getEndClockAfterTime() + ":00"));
        if (StringUtils.isNotBlank(classesPO.getRestEndTime())) {
            schedulingPO.setClassesRestEndTime(DateUtils.parse(newDateStr + " " + classesPO.getRestEndTime() + ":00"));
        }
        if (StringUtils.isNotBlank(classesPO.getRestStartTime())) {
            schedulingPO.setClassesRestStartTime(DateUtils.parse(newDateStr + " " + classesPO.getRestStartTime() + ":00"));
        }

        if (Boolean.TRUE.equals(classesPO.getEndTimeIsMorrow())) {
            schedulingPO.setClassesAfterEndTime(DateUtils.offsetDay(schedulingPO.getClassesAfterEndTime(), 1));
            schedulingPO.setClassesRealEndTime(DateUtils.offsetDay(schedulingPO.getClassesRealEndTime(), 1));
            schedulingPO.setClassesBeforeEndTime(DateUtils.offsetDay(schedulingPO.getClassesBeforeEndTime(), 1));
        }

        if (!schedulingPO.getClassesBeforeStartTime().isBefore(schedulingPO.getClassesRealStartTime())) {
            schedulingPO.setClassesBeforeStartTime(DateUtils.offsetDay(schedulingPO.getClassesBeforeStartTime(), -1));
        }
        if (!schedulingPO.getClassesAfterStartTime().isAfter(schedulingPO.getClassesRealStartTime())) {
            schedulingPO.setClassesAfterStartTime(DateUtils.offsetDay(schedulingPO.getClassesAfterStartTime(), 1));
        }
        if (schedulingPO.getClassesAfterEndTime().isBefore(schedulingPO.getClassesRealEndTime())) {
            schedulingPO.setClassesAfterEndTime(DateUtils.offsetDay(schedulingPO.getClassesAfterEndTime(), 1));
        }
        if (schedulingPO.getClassesBeforeEndTime().isAfter(schedulingPO.getClassesRealEndTime())) {
            schedulingPO.setClassesBeforeEndTime(DateUtils.offsetDay(schedulingPO.getClassesBeforeEndTime(), -1));
        }
        if (!schedulingPO.getClassesRealStartTime().isAfter(LocalDateTime.now())) {
            schedulingPO.setStarted(true);
        }
        return schedulingPO;
    }

    /**
     * 获取人员时间段的排班
     * 固定班，综合规则和手动排班
     *
     * @param staffId           人员id
     * @param startTime         开始时间
     * @param endTime           结束时间
     * @param ruleClassesPOList 规则班次集合
     *
     * @return 排班集合
     */
    public List<AttSchedulingPO> getStaffPeriodScheduling(Long staffId, LocalDateTime startTime, LocalDateTime endTime,
                                                          Boolean legalHolidaysRest,
                                                          List<AttSchedulingRuleClassesPO> ruleClassesPOList
                                                         ) {
        if (startTime == null || endTime == null || staffId == null) {
            return new ArrayList<>();
        }
        startTime = DateUtils.beginOfDay(startTime);
        endTime = DateUtils.endOfDay(endTime);
        // 获取规则排班
        List<AttSchedulingPO> ruleSchedulingPOList = this.getStaffPeriodRuleScheduling(staffId, startTime, endTime, legalHolidaysRest, ruleClassesPOList, null);
        Map<String, List<AttSchedulingPO>> ruleSchedulingPOMap = new HashMap<>();
        if (ruleSchedulingPOList != null) {
            ruleSchedulingPOMap = ruleSchedulingPOList.stream()
                    .collect(Collectors.groupingBy(k -> String.valueOf(k.getStaffId()) + k.getSchedulingDate()));
        }
        // 获取给定的月份手动的排班
        List<AttSchedulingPO> handSchedulingPOList = attSchedulingMapper.selectList(Wrappers.<AttSchedulingPO>lambdaQuery()
                                                                                            .eq(AttSchedulingPO::getStaffId, staffId)
                                                                                            .between(AttSchedulingPO::getSchedulingDate, startTime, endTime)
                                                                                   );
        if (handSchedulingPOList == null) {
            handSchedulingPOList = new ArrayList<>();
        }
        // 遍历手动排班，如果有手动的排班就，把规则的排班移除
        for (AttSchedulingPO handSchedulingPO : handSchedulingPOList) {
            handSchedulingPO.setSchedulingDate(DateUtils.newLocalDateTime(handSchedulingPO.getSchedulingDate()));
            ruleSchedulingPOMap.remove(String.valueOf(handSchedulingPO.getStaffId()) + handSchedulingPO.getSchedulingDate());
        }
        // 将移除后的排班填入手动排班，就是完整的排班
        List<AttSchedulingPO> finalHandSchedulingPOList = handSchedulingPOList;
        ruleSchedulingPOMap.forEach((k, v) -> finalHandSchedulingPOList.addAll(new ArrayList<>(v)));

        return handSchedulingPOList;
    }

    /**
     * 获取人员时间段规则的排班
     *
     * @param staffId           人员id
     * @param startTime         开始时间
     * @param endTime           结束时间
     * @param legalHolidaysRest 法定节假日休息
     * @param ruleClassesPOList 规则班次集合
     *
     * @return 排班集合
     */
    public List<AttSchedulingPO> getStaffPeriodRuleScheduling(Long staffId, LocalDateTime startTime, LocalDateTime endTime,
                                                              Boolean legalHolidaysRest,
                                                              List<AttSchedulingRuleClassesPO> ruleClassesPOList,
                                                              Map<LocalDateTime, List<AttSchedulingPO>> handMap
                                                             ) {
        startTime = DateUtils.beginOfDay(startTime);
        endTime = DateUtils.endOfDay(endTime);
        List<AttSchedulingPO> resultList = new ArrayList<>();
        if (ruleClassesPOList == null || ruleClassesPOList.isEmpty()) {
            return resultList;
        }
        Map<Long, AttClassesPO> classesPOMap = new HashMap<>();
        List<Long> classIdList = ruleClassesPOList.stream().map(AttSchedulingRuleClassesPO::getId).distinct().collect(Collectors.toList());
        if (!classIdList.isEmpty()) {
            List<AttClassesPO> classesPOList = attClassesMapper.selectByIds(classIdList);
            if (classesPOList != null) {
                classesPOMap = classesPOList.stream().collect(Collectors.toMap(AttClassesPO::getId, v -> v));
            }
        }
        Map<Integer, List<AttSchedulingRuleClassesPO>> ruleClassesPOMap = ruleClassesPOList.stream()
                .collect(Collectors.groupingBy(rcp -> Integer.valueOf(rcp.getWeek())));
        LocalDateTime prontDate = DateUtils.newLocalDateTime(startTime);

        // 法定节假日
        List<AttSchedulingRuleClassesPO> oneClassesList = new ArrayList<>();
        Map<String, String> workDateMap = new HashMap<>();
        Map<String, String> restDateMap = new HashMap<>();
        if (Boolean.TRUE.equals(legalHolidaysRest)) {
            List<AttSchedulingRuleClassesPO> oneRuleClassList = ruleClassesPOMap.get(1);
            if (oneRuleClassList != null) {
                oneClassesList.addAll(ruleClassesPOMap.get(1));
            }
            int betweenMonth = (int) DateUtils.betweenMonth(startTime, endTime, true);
            for (int i = 0; i <= betweenMonth; i++) {
                LocalDateTime nextMonthTime = DateUtils.offset(startTime, ChronoUnit.MONTHS, i);
                AttLegalHolidayMapDTO tempHolidayMapDTO = AttLegalHolidayUtils.getMonthLegalHolidayMap(nextMonthTime.getYear(),
                                                                                                       nextMonthTime.getMonthValue());
                workDateMap.putAll(tempHolidayMapDTO.getWorkDateMap());
                restDateMap.putAll(tempHolidayMapDTO.getRestDateMap());
            }
        }
        LocalDateTime nowTime = LocalDateTime.now();
        do {
            // 如果有手动排班或者是之前的日期就不在推算规则的班次
            if ((handMap != null && handMap.get(prontDate) != null) || (prontDate.isBefore(nowTime) || prontDate.equals(nowTime))) {
                prontDate = DateUtils.offsetDay(prontDate, 1);
                continue;
            }
            int week = prontDate.getDayOfWeek().getValue();
            boolean workBool = Boolean.TRUE.equals(legalHolidaysRest) && workDateMap.get(DateUtils.formatDate(prontDate)) != null;
            boolean restBool = Boolean.TRUE.equals(legalHolidaysRest) && restDateMap.get(DateUtils.formatDate(prontDate)) != null;
            List<AttSchedulingRuleClassesPO> rcList = ruleClassesPOMap.get(week);
            if (restBool) {
                rcList = new ArrayList<>();
            } else if (workBool && rcList == null) {
                rcList = oneClassesList;
            }

            if (rcList != null && !rcList.isEmpty()) {
                for (AttSchedulingRuleClassesPO ruleClassesPO : rcList) {
                    AttSchedulingPO po = AttSchedulingPO.builder()
                            .staffId(staffId)
                            .schedulingMonth(DateUtils.format(prontDate, "yyyy-MM"))
                            .schedulingDate(prontDate)
                            .classesId(ruleClassesPO.getId()).build();
                    this.stringClassToScheduling(po, classesPOMap.get(ruleClassesPO.getId()));
                    resultList.add(po);
                }
            }
            prontDate = DateUtils.offsetDay(prontDate, 1);
        } while (prontDate.isBefore(endTime));

        return resultList;
    }

    /**
     * 获取人员当前可以打卡的班次
     *
     * @return 可打卡班次列表
     */
    public List<AttClockSchedulingClassesDTO> getStaffClockClass() {
        return this.getStaffClockClass(LocalDateTime.now());
    }

    /**
     * 获取人员当前可以打卡的班次
     *
     * @param nowTime 时间点
     *
     * @return 可打卡班次列表
     */
    public List<AttClockSchedulingClassesDTO> getStaffClockClass(LocalDateTime nowTime) {
        // 获取当前登录用户
        Principal principal = SecurityContextHolder.getPrincipal();
        Long loginUserId = principal == null ? null : principal.getId();
        return this.getStaffClockClass(loginUserId, nowTime);
    }

    /**
     * 获取人员当前可以打卡的班次
     *
     * @param staffId 人员id
     * @param nowTime 时间点
     *
     * @return 可打卡班次列表
     */
    public List<AttClockSchedulingClassesDTO> getStaffClockClass(Long staffId, LocalDateTime nowTime) {
        List<AttClockSchedulingClassesDTO> result = new ArrayList<>();
        if (staffId == null) {
            return result;
        }
        if (nowTime == null) {
            nowTime = LocalDateTime.now();
        }

        // 查询前一天至后一天是否有手动排班
        List<AttClockSchedulingClassesDTO> handList = this.getClockClassByHand(staffId, nowTime);

        // 2.综合手动和规则的班次
        result = this.getHandAndRuleClass(staffId, nowTime, handList);
        return this.findClickClass(result, nowTime);
    }

    /**
     * 获取人员时间点的排班
     *
     * @param staffId 人员id
     * @param nowTime 时间点
     *
     * @return 排班班次
     */
    public List<AttClockSchedulingClassesDTO> getStaffWorkClass(Long staffId, LocalDateTime nowTime) {
        List<AttClockSchedulingClassesDTO> result = new ArrayList<>();
        if (staffId == null) {
            return result;
        }
        if (nowTime == null) {
            nowTime = LocalDateTime.now();
        }

        // 查询前一天至后一天是否有手动排班
        List<AttClockSchedulingClassesDTO> handList = this.getClockClassByHand(staffId, nowTime);

        // 2.综合手动和规则的班次
        result = this.getHandAndRuleClass(staffId, nowTime, handList);
        if (CollectionUtils.isEmpty(result)) {
            return result;
        }
        LocalDateTime finalNowTime = nowTime;
        result.removeIf(item -> {
            var dto = this.classesPOToSchedulingClassesDTO(item.getClassDate() == null ? finalNowTime : item.getClassDate(), item);
            return dto.getId() == null || finalNowTime.isBefore(dto.getStartWorkTime()) || finalNowTime.isAfter(dto.getEndWorkTime());
        });
        return result;
    }

    /**
     * 获取人员当前时间前后一天的手动排班
     *
     * @param staffId 人员id
     * @param nowTime 时间点
     *
     * @return 可打卡班次列表
     */
    private List<AttClockSchedulingClassesDTO> getClockClassByHand(Long staffId, LocalDateTime nowTime) {
        List<AttClockSchedulingClassesDTO> result = new ArrayList<>();
        List<String> dateList = new ArrayList<>();
        dateList.add(DateUtils.formatDate(DateUtils.offsetDay(nowTime, -1)));
        dateList.add(DateUtils.formatDate(nowTime));
        dateList.add(DateUtils.formatDate(DateUtils.offsetDay(nowTime, 1)));

        List<AttSchedulingPO> handSchedulingPOList = attSchedulingMapper.selectList(Wrappers.<AttSchedulingPO>lambdaQuery()
                                                                                            .eq(AttSchedulingPO::getStaffId, staffId)
                                                                                            .isNotNull(AttSchedulingPO::getClassesId)
                                                                                            .in(AttSchedulingPO::getSchedulingDate, dateList)
                                                                                   );
        // 手动排班
        if (handSchedulingPOList != null && !handSchedulingPOList.isEmpty()) {
            String hourStr = "HH:mm";
            for (AttSchedulingPO schedulingPO : handSchedulingPOList) {
                // 如果是已发生的排班需要将被保存下来的排班组装成班次
                AttClockSchedulingClassesDTO classesDTO = new AttClockSchedulingClassesDTO();
                // 无班id次表示休息
                if (schedulingPO.getClassesId() == null) {
                    classesDTO.setClassDate(schedulingPO.getSchedulingDate());
                    classesDTO.setClassesType(AttClassesTypeEnum.REST.name());
                } else if (schedulingPO.getClassesRealStartTime() != null) {
                    classesDTO.setId(schedulingPO.getClassesId());
                    classesDTO.setClassDate(schedulingPO.getSchedulingDate());
                    classesDTO.setName(schedulingPO.getClassesName());
                    classesDTO.setClassesType(schedulingPO.getClassesType());
                    classesDTO.setStartClockBeforeTime(DateUtils.format(schedulingPO.getClassesBeforeStartTime(), hourStr));
                    classesDTO.setStartWorkTime(DateUtils.format(schedulingPO.getClassesRealStartTime(), hourStr));
                    classesDTO.setStartClockAfterTime(DateUtils.format(schedulingPO.getClassesAfterStartTime(), hourStr));
                    classesDTO.setEndWorkTime(DateUtils.format(schedulingPO.getClassesRealEndTime(), hourStr));
                    classesDTO.setEndClockBeforeTime(DateUtils.format(schedulingPO.getClassesBeforeEndTime(), hourStr));
                    classesDTO.setEndClockAfterTime(DateUtils.format(schedulingPO.getClassesAfterEndTime(), hourStr));
                    classesDTO.setEndTimeIsMorrow(schedulingPO.getEndTimeIsMorrow());
                    classesDTO.setVersion(schedulingPO.getClassesVersion());

                    if (schedulingPO.getClassesRestStartTime() != null) {
                        classesDTO.setRestStartTime(DateUtils.format(schedulingPO.getClassesRestStartTime(), hourStr));
                    }
                    if (schedulingPO.getClassesRestEndTime() != null) {
                        classesDTO.setRestEndTime(DateUtils.format(schedulingPO.getClassesRestEndTime(), hourStr));
                    }
                    if (!schedulingPO.getClassesRealStartTime().isBefore(schedulingPO.getClassesRealEndTime())) {
                        classesDTO.setEndTimeIsMorrow(true);
                    }
                } else {
                    // 返回查询的班次信息
                    AttClassesPO classesPO = attClassesMapper.selectById(schedulingPO.getClassesId());
                    if (classesPO == null) {
                        continue;
                    }
                    BeanUtils.copyProperties(classesPO, classesDTO);
                    classesDTO.setClassDate(schedulingPO.getSchedulingDate());
                }
                result.add(classesDTO);
            }
        }
        return result;
    }

    /**
     * 综合手动和规则的班次
     * 注意: 不是该时间点的班次,而是根据时间点综合其昨天今天明天的手动和规则的班次.
     * 一般不能单独使用,需要自己过滤一下时间点,才能使用
     *
     * @param staffId  人员id
     * @param nowTime  时间点
     * @param handList 当前手动排班的集合(通过getClockClassByHand方法获取）
     *
     * @return 根据时间点综合其昨天今天明天的手动和规则的班次(需要自己过滤一下时间点)
     */
    private List<AttClockSchedulingClassesDTO> getHandAndRuleClass(Long staffId, LocalDateTime nowTime,
                                                                   List<AttClockSchedulingClassesDTO> handList
                                                                  ) {
        List<AttClockSchedulingClassesDTO> result = new ArrayList<>(handList);
        // 获取考勤组
        AttGroupPO groupPO = attGroupRepository.getGroupByStaffId(staffId);
        if (groupPO == null) {
            return result;
        }
        AttSchedulingRulePO rulePO = attSchedulingRuleMapper.selectById(groupPO.getSchedulingRuleId());
        // 如果是倒班就不会有规则生成的班次，直接返回空
        if (rulePO == null || AttSchedulingWaysEnum.SHIFTS.name().equals(rulePO.getWays())) {
            return result;
        }

        // 获取规则班次
        List<AttSchedulingRuleClassesPO> ruleClassesPOList = attSchedulingRuleClassesMapper.selectList(Wrappers
                                                                                                               .<AttSchedulingRuleClassesPO>lambdaQuery()
                                                                                                               .eq(AttSchedulingRuleClassesPO::getRuleId, groupPO.getSchedulingRuleId())
                                                                                                      );
        if (CollectionUtils.isEmpty(ruleClassesPOList)) {
            return result;
        }
        Map<String, List<AttClockSchedulingClassesDTO>> handDateMap = handList.stream()
                .collect(Collectors.groupingBy(d -> DateUtils.formatDate(d.getClassDate())));
        // 循环规则的班次
        LocalDateTime tempDatePre = DateUtils.newLocalDateTime(nowTime);
        LocalDateTime tempDateNex = DateUtils.newLocalDateTime(nowTime);
        LocalDateTime preTime = DateUtils.offset(tempDatePre, ChronoUnit.DAYS, -1);
        LocalDateTime nexTime = DateUtils.offset(tempDateNex, ChronoUnit.DAYS, 1);
        // 获取周一的班次
        List<AttClassesPO> oneWeekClassPOList = new ArrayList<>();
        List<AttClassesPO> preClassPOList = new ArrayList<>();
        List<AttClassesPO> dayClassPOList = new ArrayList<>();
        List<AttClassesPO> nextClassPOList = new ArrayList<>();
        for (AttSchedulingRuleClassesPO ruleClassesPO : ruleClassesPOList) {
            AttClassesPO classesPO = attClassesMapper.selectById(ruleClassesPO.getId());
            if ("1".equals(ruleClassesPO.getWeek())) {
                if (classesPO != null) {
                    oneWeekClassPOList.add(classesPO);
                }
            }
            if (String.valueOf(preTime.getDayOfWeek().getValue()).equals(ruleClassesPO.getWeek())) {
                preClassPOList.add(classesPO);
            } else if (String.valueOf(nowTime.getDayOfWeek().getValue()).equals(ruleClassesPO.getWeek())) {
                dayClassPOList.add(classesPO);
            } else if (String.valueOf(nexTime.getDayOfWeek().getValue()).equals(ruleClassesPO.getWeek())) {
                nextClassPOList.add(classesPO);
            }
        }
        // 如果没有当天的手动排班就加入规则的排班
        if (handDateMap.get(DateUtils.formatDate(preTime)) == null) {
            result.addAll(this.getDayClassDto(preTime, preClassPOList, oneWeekClassPOList, rulePO.getLegalHolidaysRest()));
        }
        if (handDateMap.get(DateUtils.formatDate(nowTime)) == null) {
            result.addAll(this.getDayClassDto(nowTime, dayClassPOList, oneWeekClassPOList, rulePO.getLegalHolidaysRest()));
        }
        if (handDateMap.get(DateUtils.formatDate(nexTime)) == null) {
            result.addAll(this.getDayClassDto(nexTime, nextClassPOList, oneWeekClassPOList, rulePO.getLegalHolidaysRest()));
        }
        return result;
    }

    /**
     * 寻找已可打卡的班次
     *
     * @param checkList 需要寻找的班次集合
     * @param nowTime   打卡时间
     *
     * @return 可打卡班次集合
     */
    private List<AttClockSchedulingClassesDTO> findClickClass(List<AttClockSchedulingClassesDTO> checkList, LocalDateTime nowTime) {
        List<AttClockSchedulingClassesDTO> result = new ArrayList<>();
        if (checkList == null || checkList.isEmpty()) {
            return result;
        }
        // 筛选可以打卡的班次
        for (AttClockSchedulingClassesDTO dto : checkList) {
            if (dto.getId() == null || dto.getStartWorkTime() == null) {
                continue;
            }
            AttSchedulingClassesDTO classesDTO = this.classesPOToSchedulingClassesDTO(dto.getClassDate(), dto);
            // 如果不在打卡范围就跳过
            if (nowTime.isBefore(classesDTO.getStartClockBeforeTime()) || nowTime.isAfter(classesDTO.getEndClockAfterTime())) {
                continue;
            }
            result.add(dto);
        }
        result.sort(Comparator.comparing(AttClockSchedulingClassesDTO::getStartWorkTime));
        return result;
    }

    /**
     * 获取某天规则的排班
     *
     * @param dayTime           当天时间
     * @param classesPOList     dayTime当天的班次
     * @param oneClassList      周一的班次(当天班次为空但法定上班时使用
     * @param legalHolidaysRest 是否法定节假日条件
     *
     * @return 可打卡班次列表
     */
    private List<AttClockSchedulingClassesDTO> getDayClassDto(LocalDateTime dayTime, List<AttClassesPO> classesPOList,
                                                              List<AttClassesPO> oneClassList, Boolean legalHolidaysRest
                                                             ) {
        List<AttClockSchedulingClassesDTO> result = new ArrayList<>();
        String dateStr = DateUtils.formatDate(dayTime);
        boolean workBool = false;
        boolean restBool = false;
        if (Boolean.TRUE.equals(legalHolidaysRest)) {
            AttLegalHolidayMapDTO legalHolidayMapDTO = AttLegalHolidayUtils.getMonthLegalHolidayMap(dayTime.getYear(),
                                                                                                    dayTime.getMonthValue());
            workBool = legalHolidayMapDTO.getWorkDateMap().get(dateStr) != null;
            restBool = legalHolidayMapDTO.getRestDateMap().get(dateStr) != null;
        }
        List<AttClassesPO> loopList = new ArrayList<>();
        if ((classesPOList == null || classesPOList.isEmpty()) && workBool) {
            // 如果推算无班次，但法定上班，就取周一的班次
            loopList = new ArrayList<>(oneClassList);
        } else if (classesPOList != null && !classesPOList.isEmpty() && restBool) {
            // 如果推算有班次，但法定休息，就休息
            loopList = new ArrayList<>();
        } else if (classesPOList != null && !classesPOList.isEmpty()) {
            // 否则就当天的班次
            loopList.addAll(classesPOList);
        }
        for (AttClassesPO classesPO : loopList) {
            AttClockSchedulingClassesDTO dto = new AttClockSchedulingClassesDTO();
            BeanUtils.copyProperties(classesPO, dto);
            dto.setClassDate(DateUtils.parseDate(dateStr));
            result.add(dto);
        }
        return result;
    }

    /**
     * 获取当前登录人今天的班次
     *
     * @return 当天班次集合
     */
    public List<AttSchedulingClassesDTO> getStaffDayScheduling() {
        Principal principal = SecurityContextHolder.getPrincipal();
        if (principal == null || principal.getId() == null) {
            return new ArrayList<>();
        }
        return this.getStaffDayScheduling(principal.getId(), DateUtils.formatDate(LocalDateTime.now()));
    }

    /**
     * 获取某人某天的排班
     * (如果要获取一个月的数据请调用获取某月的方法，不要循环获取某天的）
     *
     * @param staffId 人员id
     * @param day     天（格式：yyyy-MM-dd)
     *
     * @return 当天班次集合
     */
    public List<AttSchedulingClassesDTO> getStaffDayScheduling(Long staffId, String day) {
        List<AttSchedulingClassesDTO> result = new ArrayList<>();
        LocalDateTime dateTime = DateUtils.parseDate(day);
        // 1.获取当天手动排班
        List<AttSchedulingPO> handPOList = attSchedulingMapper.selectList(Wrappers.<AttSchedulingPO>lambdaQuery()
                                                                                  .eq(AttSchedulingPO::getStaffId, staffId)
                                                                                  .eq(AttSchedulingPO::getSchedulingDate, day)
                                                                         );
        if (handPOList != null && !handPOList.isEmpty()) {
            for (AttSchedulingPO schedulingPO : handPOList) {
                if (schedulingPO.getClassesId() == null) {
                    continue;
                }
                AttSchedulingClassesDTO dto = new AttSchedulingClassesDTO();
                if (schedulingPO.getClassesRealStartTime() == null) {
                    AttClassesPO classesPO = attClassesMapper.selectById(schedulingPO.getClassesId());
                    this.classesPOToSchedulingClassesDTO(dateTime, classesPO, dto);
                } else {
                    dto.setId(schedulingPO.getClassesId());
                    dto.setName(schedulingPO.getClassesName());
                    dto.setNickName(schedulingPO.getClassesNickName());
                    dto.setStarted(schedulingPO.getStarted());
                    dto.setClassesType(schedulingPO.getClassesType());
                    dto.setStartWorkTime(schedulingPO.getClassesRealStartTime());
                    dto.setStartClockBeforeTime(schedulingPO.getClassesBeforeStartTime());
                    dto.setStartClockAfterTime(schedulingPO.getClassesAfterStartTime());
                    dto.setEndWorkTime(schedulingPO.getClassesRealEndTime());
                    dto.setEndClockBeforeTime(schedulingPO.getClassesBeforeEndTime());
                    dto.setEndClockAfterTime(schedulingPO.getClassesAfterEndTime());
                    dto.setRestStartTime(schedulingPO.getClassesRestStartTime());
                    dto.setRestEndTime(schedulingPO.getClassesRestEndTime());
                    dto.setEndTimeIsMorrow(schedulingPO.getEndTimeIsMorrow());
                }
                result.add(dto);
            }
            return result.stream().filter(po -> po != null && po.getStartWorkTime() != null)
                    .sorted(Comparator.comparing(AttSchedulingClassesDTO::getStartWorkTime)).collect(Collectors.toList());
        }

        AttGroupPO groupPO = attGroupRepository.getGroupByStaffId(staffId);
        if (groupPO == null || groupPO.getSchedulingRuleId() == null) {
            return result;
        }
        AttSchedulingRulePO rulePO = attSchedulingRuleMapper.selectById(groupPO.getSchedulingRuleId());
        if (rulePO == null || StringUtils.isBlank(rulePO.getWays()) || AttSchedulingWaysEnum.SHIFTS.name().equals(rulePO.getWays())) {
            return result;
        }

        // 2.获取规则的排班
        List<AttSchedulingRuleClassesPO> ruleClassesPOList = attSchedulingRuleClassesMapper.selectList(Wrappers
                                                                                                               .<AttSchedulingRuleClassesPO>lambdaQuery()
                                                                                                               .eq(AttSchedulingRuleClassesPO::getRuleId, rulePO.getId())
                                                                                                      );
        if (ruleClassesPOList == null || ruleClassesPOList.isEmpty()) {
            return result;
        }
        // 法定节假日信息
        boolean workBool = false;
        boolean restBool = false;
        if (Boolean.TRUE.equals(rulePO.getLegalHolidaysRest())) {
            AttLegalHolidayMapDTO holidayMapDTO = AttLegalHolidayUtils.getMonthLegalHolidayMap(dateTime.getYear(),
                                                                                               dateTime.getMonthValue());
            workBool = Boolean.TRUE.equals(rulePO.getLegalHolidaysRest()) && holidayMapDTO.getWorkDateMap().get(day) != null;
            restBool = Boolean.TRUE.equals(rulePO.getLegalHolidaysRest()) && holidayMapDTO.getRestDateMap().get(day) != null;
        }
        // 规则的班次
        List<AttClassesPO> classesPOList = attClassesMapper.selectByIds(ruleClassesPOList.stream().map(AttSchedulingRuleClassesPO::getId)
                                                                                .collect(Collectors.toList()));
        Map<Long, AttClassesPO> classesPOMap = classesPOList.stream().collect(Collectors.toMap(AttClassesPO::getId, v -> v));
        // 当天的班
        List<AttSchedulingClassesDTO> dayClassPOList = new ArrayList<>();
        // 周一的班
        List<AttSchedulingClassesDTO> oneClassPOList = new ArrayList<>();
        for (AttSchedulingRuleClassesPO ruleClassesPO : ruleClassesPOList) {
            if (String.valueOf(dateTime.getDayOfWeek().getValue()).equals(ruleClassesPO.getWeek())) {
                AttClassesPO classesPO = classesPOMap.get(ruleClassesPO.getId());
                if (classesPO == null) {
                    continue;
                }
                AttSchedulingClassesDTO dto = new AttSchedulingClassesDTO();
                this.classesPOToSchedulingClassesDTO(dateTime, classesPO, dto);
                dayClassPOList.add(dto);
            } else if (workBool && String.valueOf(1).equals(ruleClassesPO.getWeek())) {
                AttClassesPO classesPO = classesPOMap.get(ruleClassesPO.getId());
                if (classesPO == null) {
                    continue;
                }
                AttSchedulingClassesDTO dto = new AttSchedulingClassesDTO();
                this.classesPOToSchedulingClassesDTO(dateTime, classesPO, dto);
                oneClassPOList.add(dto);
            }

        }
        if (restBool) {
            // 法定休息置为休息
            result = new ArrayList<>();
        } else if (workBool && dayClassPOList.isEmpty()) {
            // 法定上班，且当天没有排班就周用周一的班
            result.addAll(oneClassPOList);
        } else {
            // 否则就用当天的班
            result.addAll(dayClassPOList);
        }
        return result.stream().filter(po -> po != null && po.getStartWorkTime() != null)
                .sorted(Comparator.comparing(AttSchedulingClassesDTO::getStartWorkTime)).collect(Collectors.toList());
    }

    /**
     * 获取某人某天的排班
     * 没有班返回一个休息班次
     * (如果要获取一个月的数据请调用获取某月的方法，不要循环获取某天的）
     *
     * @param staffId 人员id
     * @param day     天（格式：yyyy-MM-dd)
     *
     * @return 当天班次集合
     */
    public List<AttSchedulingPO> getStaffDaySchedulingPO(Long staffId, String day) {
        List<AttSchedulingPO> result = new ArrayList<>();
        LocalDateTime dateTime = DateUtils.parseDate(day);
        // 1.获取当天手动排班
        List<AttSchedulingPO> handPOList = attSchedulingMapper.selectList(Wrappers.<AttSchedulingPO>lambdaQuery()
                                                                                  .eq(AttSchedulingPO::getStaffId, staffId)
                                                                                  .eq(AttSchedulingPO::getSchedulingDate, day)
                                                                         );
        if (handPOList != null && !handPOList.isEmpty()) {
            for (AttSchedulingPO schedulingPO : handPOList) {
                if (schedulingPO.getClassesRealStartTime() == null) {
                    AttClassesPO classesPO = attClassesMapper.selectById(schedulingPO.getClassesId());
                    this.stringClassToScheduling(schedulingPO, classesPO);
                }
                result.add(schedulingPO);
            }
            return result;
        }

        AttGroupPO groupPO = attGroupRepository.getGroupByStaffId(staffId);
        if (groupPO == null || groupPO.getSchedulingRuleId() == null) {
            return result;
        }
        AttSchedulingRulePO rulePO = attSchedulingRuleMapper.selectById(groupPO.getSchedulingRuleId());
        if (rulePO == null || StringUtils.isBlank(rulePO.getWays()) || AttSchedulingWaysEnum.SHIFTS.name().equals(rulePO.getWays())) {
            return result;
        }

        // 2.获取规则的排班
        List<AttSchedulingRuleClassesPO> ruleClassesPOList = attSchedulingRuleClassesMapper.selectList(Wrappers
                                                                                                               .<AttSchedulingRuleClassesPO>lambdaQuery()
                                                                                                               .eq(AttSchedulingRuleClassesPO::getRuleId, rulePO.getId())
                                                                                                      );
        if (ruleClassesPOList == null || ruleClassesPOList.isEmpty()) {
            return result;
        }
        // 法定节假日信息
        boolean workBool = false;
        boolean restBool = false;
        if (Boolean.TRUE.equals(rulePO.getLegalHolidaysRest())) {
            AttLegalHolidayMapDTO holidayMapDTO = AttLegalHolidayUtils.getMonthLegalHolidayMap(dateTime.getYear(), dateTime.getMonthValue());
            workBool = Boolean.TRUE.equals(rulePO.getLegalHolidaysRest()) && holidayMapDTO.getWorkDateMap().get(day) != null;
            restBool = Boolean.TRUE.equals(rulePO.getLegalHolidaysRest()) && holidayMapDTO.getRestDateMap().get(day) != null;
        }
        // 规则的班次
        List<AttClassesPO> classesPOList = attClassesMapper.selectByIds(ruleClassesPOList.stream().map(AttSchedulingRuleClassesPO::getId)
                                                                                .collect(Collectors.toList()));
        Map<Long, AttClassesPO> classesPOMap = classesPOList.stream().collect(Collectors.toMap(AttClassesPO::getId, v -> v));
        // 当天的班
        List<AttSchedulingPO> dayClassPOList = new ArrayList<>();
        // 周一的班
        List<AttSchedulingPO> oneClassPOList = new ArrayList<>();
        for (AttSchedulingRuleClassesPO ruleClassesPO : ruleClassesPOList) {
            AttSchedulingPO po = AttSchedulingPO.builder()
                    .schedulingMonth(DateUtils.format(dateTime, "yyyy-MM"))
                    .schedulingDate(DateUtils.beginOfDay(dateTime))
                    .staffId(staffId)
                    .build();

            po.setStaffGroupRanges(groupPO.getRanges());
            po.setStaffGroupName(groupPO.getName());
            po.setSchedulingWays(rulePO.getWays());

            if (String.valueOf(dateTime.getDayOfWeek().getValue()).equals(ruleClassesPO.getWeek())) {
                AttClassesPO classesPO = classesPOMap.get(ruleClassesPO.getId());
                this.stringClassToScheduling(po, classesPO);
                dayClassPOList.add(po);
            } else if (workBool && 1 == dateTime.getDayOfWeek().getValue()) {
                AttClassesPO classesPO = classesPOMap.get(ruleClassesPO.getId());
                this.stringClassToScheduling(po, classesPO);
                oneClassPOList.add(po);
            }
        }
        if (restBool) {
            // 法定休息置为休息
            result = new ArrayList<>();
        } else if (workBool && dayClassPOList.isEmpty()) {
            // 法定上班，且当天没有排班就用周一的班
            result.addAll(oneClassPOList);
        } else {
            // 否则就用当天的班
            result.addAll(dayClassPOList);
        }
        return result;
    }

    /**
     * 获取人员某天在某时刻是否有班次
     * 要看今天的班次是否在当前时间 还要看前一天班次是否在当前时间，因为班次可以跨天
     *
     * @param staffId
     * @param time
     * @param groupPO
     * @param rulePO
     * @param classesPOMap
     *
     * @return
     */
    public AttSchedulingPO getStaffTimeSchedulingPO(Long staffId, LocalDateTime time, AttGroupPO groupPO, AttSchedulingRulePO rulePO,
                                                    Map<Long, AttClassesPO> classesPOMap
                                                   ) {
        AttSchedulingPO dayPO = this.getStaffTimeSchedulingPO(staffId, DateUtils.newLocalDateTime(time), time, groupPO, rulePO, classesPOMap);
        if (dayPO != null) {
            return dayPO;
        }
        return this.getStaffTimeSchedulingPO(staffId, DateUtils.offsetDay(time, -1), time, groupPO, rulePO, classesPOMap);
    }

    /**
     * 获取人员某天在某时刻是否有班次
     *
     * @param staffId
     * @param date
     * @param time
     * @param groupPO
     * @param rulePO
     * @param classesPOMap
     *
     * @return
     */
    public AttSchedulingPO getStaffTimeSchedulingPO(Long staffId, LocalDateTime date, LocalDateTime time, AttGroupPO groupPO,
                                                    AttSchedulingRulePO rulePO, Map<Long, AttClassesPO> classesPOMap
                                                   ) {

        List<AttSchedulingPO> handSchedulingList = attSchedulingMapper.selectList(Wrappers.<AttSchedulingPO>lambdaQuery()
                                                                                          .eq(AttSchedulingPO::getStaffId, staffId)
                                                                                          .eq(AttSchedulingPO::getSchedulingDate, DateUtils.formatDate(date))
                                                                                 );
        // 手动排班
        if (handSchedulingList != null) {
            for (AttSchedulingPO schedulingPO : handSchedulingList) {
                if (schedulingPO.getClassesId() == null) {
                    return null;
                }
                if (schedulingPO.getClassesRealStartTime() == null) {
                    this.stringClassToScheduling(schedulingPO, classesPOMap.get(schedulingPO.getClassesId()));
                }
                if ((time.isAfter(schedulingPO.getClassesRealStartTime()) || time.isEqual(schedulingPO.getClassesRealStartTime())) &&
                    (time.isBefore(schedulingPO.getClassesRealEndTime()) || time.isEqual(schedulingPO.getClassesRealEndTime()))) {
                    return schedulingPO;
                }
            }
            return null;
        }

        if (rulePO != null && AttSchedulingWaysEnum.FIXED.name().equals(rulePO.getWays())) {
            List<AttSchedulingRuleClassesPO> ruleClassesPOList = attSchedulingRuleClassesMapper.selectList(Wrappers
                                                                                                                   .<AttSchedulingRuleClassesPO>lambdaQuery()
                                                                                                                   .eq(AttSchedulingRuleClassesPO::getRuleId, rulePO.getId())
                                                                                                          );
            if (ruleClassesPOList == null || ruleClassesPOList.isEmpty()) {
                return null;
            }
            boolean workBool = false;
            boolean restBool = false;
            if (Boolean.TRUE.equals(rulePO.getLegalHolidaysRest())) {
                AttLegalHolidayMapDTO holidayMapDTO = AttLegalHolidayUtils.getMonthLegalHolidayMap(date.getYear(), date.getMonthValue());
                if (holidayMapDTO.getWorkDateMap().get(DateUtils.formatDate(date)) != null) {
                    workBool = true;
                } else if (holidayMapDTO.getRestDateMap().get(DateUtils.formatDate(date)) != null) {
                    restBool = true;
                }
            }
            Map<Integer, List<AttSchedulingRuleClassesPO>> weekRuleClassMap = ruleClassesPOList.stream()
                    .collect(Collectors.groupingBy(v -> Integer.parseInt(v.getWeek())));
            List<AttSchedulingRuleClassesPO> dayRuleClassList = new ArrayList<>();
            if (restBool) {
                dayRuleClassList = null;
            } else if (workBool && weekRuleClassMap.get(date.getDayOfWeek().getValue()) == null) {
                dayRuleClassList = weekRuleClassMap.get(1);
            } else {
                dayRuleClassList = weekRuleClassMap.get(date.getDayOfWeek().getValue());
            }

            if (dayRuleClassList == null || dayRuleClassList.isEmpty()) {
                return null;
            }
            for (AttSchedulingRuleClassesPO ruleClassesPO : dayRuleClassList) {

                AttSchedulingPO attSchedulingPO = AttSchedulingPO.builder()
                        .schedulingMonth(DateUtils.format(date, "yyyy-MM"))
                        .schedulingDate(DateUtils.beginOfDay(date))
                        .staffId(staffId)
                        .schedulingRuleId(rulePO.getId())
                        .schedulingWays(rulePO.getWays())
                        .build();
                if (groupPO != null) {
                    attSchedulingPO.setStaffGroupId(groupPO.getId());
                    attSchedulingPO.setStaffGroupName(groupPO.getName());
                    attSchedulingPO.setStaffGroupRanges(groupPO.getRanges());
                }

                this.stringClassToScheduling(attSchedulingPO, classesPOMap.get(ruleClassesPO.getId()));
                if ((time.isAfter(attSchedulingPO.getClassesRealStartTime()) || time.isEqual(attSchedulingPO.getClassesRealStartTime())) &&
                    (time.isBefore(attSchedulingPO.getClassesRealEndTime()) || time.isEqual(attSchedulingPO.getClassesRealEndTime()))) {
                    return attSchedulingPO;
                }
            }
        }
        return null;
    }

    /**
     * 行政班排班校验
     *
     * @param staffName                人员名称
     * @param paramAddSchedulingPOList 需要校验的集合(不能有休息的班次,休息的班次不需要校验
     */
    public void fixedHandSchedulingCheck(String staffName, List<AttSchedulingPO> paramAddSchedulingPOList) {
        if (paramAddSchedulingPOList == null || paramAddSchedulingPOList.isEmpty()) {
            return;
        }
        paramAddSchedulingPOList.sort(Comparator.comparing(AttSchedulingPO::getClassesRealStartTime));
        int loopSize = paramAddSchedulingPOList.size();
        // 班次交叉+规则校验
        for (int i = 0; i < loopSize; i++) {
            int nextI = i + 1;
            AttSchedulingPO poI = paramAddSchedulingPOList.get(i);
            // 没有下一个班次校验就结束了
            if (nextI >= loopSize) {
                continue;
            }
            AttSchedulingPO poNextI = paramAddSchedulingPOList.get(nextI);
            boolean bool = poI.getClassesRealEndTime().isAfter(poNextI.getClassesRealStartTime());
            // 如果是同一天的班次
            if (poI.getSchedulingDate().equals(poNextI.getSchedulingDate())) {
                // 校验班次交叉
                Assert.isConflict(bool, staffName + DateUtils.formatDate(poI.getSchedulingDate()) + "的班次有交叉。");
            } else {
                // 校验班次交叉
                Assert.isConflict(bool, staffName + DateUtils.formatDate(poI.getSchedulingDate()) + "与" +
                                        DateUtils.formatDate(poNextI.getSchedulingDate()) + "的班次有交叉");
            }
        }
    }

    /**
     * 三班倒排班校验
     *
     * @param staffName                员工名称
     * @param rulePO                   规则
     * @param paramAddSchedulingPOList 需要校验的集合(不能有休息的班次,休息的班次不需要校验
     */
    public void shiftsHandSchedulingCheck(String staffName, AttSchedulingRulePO rulePO, List<AttSchedulingPO> paramAddSchedulingPOList) {
        paramAddSchedulingPOList.sort(Comparator.comparing(AttSchedulingPO::getClassesRealStartTime));
        int addListListLength = paramAddSchedulingPOList.size();
        // 连续排班天数
        int countDay = 1;
        // 最大连续排班天数
        int maxDay = 0;
        // 每种班次的数量map
        Map<String, Integer> classTypeNumMap = new HashMap<>();
        for (AttClassesTypeEnum value : AttClassesTypeEnum.values()) {
            classTypeNumMap.put(value.name(), 0);
        }
        // 规则的每月班次类型
        List<AttSchedulingRuleSameShiftDTO> ruleSameList = null;
        if (StringUtils.isNotBlank(rulePO.getSameShift()) && rulePO.getSameShift().length() > 3) {
            ruleSameList = JsonUtils.deserialize(rulePO.getSameShift(), new
                    TypeReference<>() {});
        }

        // 班次交叉+规则校验
        for (int i = 0; i < addListListLength; i++) {
            int nextI = i + 1;
            AttSchedulingPO poI = paramAddSchedulingPOList.get(i);

            // 累计对应班次类型数量
            classTypeNumMap.compute(poI.getClassesType(), (k, num) -> num + 1);

            // 没有下一个班次则没有校验了
            if (nextI >= addListListLength) {
                continue;
            }
            AttSchedulingPO poNextI = paramAddSchedulingPOList.get(nextI);
            // 间隔时间
            long t = Math.abs(DateUtils.betweenMs(poNextI.getClassesRealStartTime(), poI.getClassesRealEndTime()));
            // 班次交叉
            boolean bool = poI.getClassesRealEndTime().isAfter(poNextI.getClassesRealStartTime());
            // 如果是同一天的班次
            if (poI.getSchedulingDate().equals(poNextI.getSchedulingDate())) {
                // 校验班次交叉
                Assert.isConflict(bool, staffName + DateUtils.formatDate(poI.getSchedulingDate()) + "的班次有交叉。");

                Assert.isParameterError(rulePO.getIntervalHour() > 0 && poNextI.getClassesRealStartTime().isAfter(LocalDateTime.now()) &&
                                        t / (1000 * 60 * 60) < rulePO.getIntervalHour(),
                                        staffName + DateUtils.formatDate(poI.getSchedulingDate()) + "的班次间隔时间不足" +
                                        rulePO.getIntervalHour() +
                                        "小时");

            } else {
                // 校验班次交叉
                Assert.isConflict(bool, staffName + DateUtils.formatDate(poI.getSchedulingDate()) + "与" +
                                        DateUtils.formatDate(poNextI.getSchedulingDate()) + "的班次有交叉");
                // 连续排班天数
                if (DateUtils.offsetDay(poI.getSchedulingDate(), 1).equals(poNextI.getSchedulingDate())) {
                    countDay++;
                    maxDay = Math.max(maxDay, countDay);
                } else {
                    countDay = 1;
                }
            }
        }

        if (ruleSameList != null && !ruleSameList.isEmpty()) {
            for (AttSchedulingRuleSameShiftDTO sameDOT : ruleSameList) {
                int num = 0;
                if (sameDOT.getSameShiftNumber() == null || sameDOT.getSameShiftNumber() <= 0 ||
                    CollectionUtils.isEmpty(sameDOT.getSameShiftType())) {
                    continue;
                }
                List<String> ruleTypeList = JsonUtils.deserialize(sameDOT.getSameShiftType(), new TypeReference<>() {});
                StringBuilder stringBuffer = new StringBuilder();
                for (String ruleType : ruleTypeList) {
                    num += classTypeNumMap.get(ruleType);
                    stringBuffer.append(" ").append(AttClassesTypeEnum.getTypeValue(ruleType));
                }
                Assert.isNotAcceptable(
                        num > sameDOT.getSameShiftNumber(),
                        staffName + stringBuffer + "共" + num + "个，超过了" + sameDOT.getSameShiftNumber());
            }
        }

        // 校验连续排班天数
        if (addListListLength == 0) {
            maxDay = 0;
        }
        if (rulePO.getContinuousDays() > 0 && rulePO.getContinuousDays() < maxDay) {
            throw new ServiceException(staffName + "连续排班" + maxDay + "天，超过了" + rulePO.getContinuousDays());
        }

    }

    /**
     * 获取人员一段时间的手动排班
     *
     * @param staffId
     * @param startTime
     * @param endTime
     *
     * @return
     */
    public List<AttSchedulingPO> getPeriodHandScheduling(Long staffId, LocalDateTime startTime, LocalDateTime endTime) {
        startTime = DateUtils.beginOfDay(startTime);
        endTime = DateUtils.endOfDay(endTime);
        List<AttSchedulingPO> schedulingPOList = attSchedulingMapper.selectList(Wrappers.<AttSchedulingPO>lambdaQuery()
                                                                                        .eq(AttSchedulingPO::getStaffId, staffId)
                                                                                        .between(AttSchedulingPO::getSchedulingDate, startTime, endTime)
                                                                               );
        if (schedulingPOList != null && !schedulingPOList.isEmpty()) {
            Map<Long, AttClassesPO> classesPOMap = new HashMap<>();
            List<AttClassesPO> classesPOList = attClassesMapper.selectList(Wrappers.lambdaQuery());
            if (classesPOList != null && !classesPOList.isEmpty()) {
                classesPOMap = classesPOList.stream().collect(Collectors.toMap(AttClassesPO::getId, v -> v));
            }
            Map<Long, AttClassesPO> finalClassesPOMap = classesPOMap;
            return schedulingPOList.stream().map(po -> {
                if (po.getClassesId() != null && po.getClassesRealStartTime() == null) {
                    return this.stringClassToScheduling(po, finalClassesPOMap.get(po.getClassesId()));
                }
                return po;
            }).filter(Objects::nonNull).collect(Collectors.toList());
        }
        return schedulingPOList;
    }

    /**
     * 获取人员一段时间的排班
     *
     * @param staffId
     * @param startTime
     * @param endTime
     * @param groupPO
     * @param rulePO
     *
     * @return
     */
    public List<AttSchedulingPO> getPeriodScheduling(Long staffId, LocalDateTime startTime, LocalDateTime endTime, AttGroupPO groupPO,
                                                     AttSchedulingRulePO rulePO
                                                    ) {
        if (startTime == null || endTime == null || staffId == null) {
            return new ArrayList<>();
        }
        List<AttSchedulingPO> handSchedulingPOList = this.getPeriodHandScheduling(staffId, DateUtils.beginOfDay(startTime), DateUtils.endOfDay(endTime));

        if (rulePO != null && AttSchedulingWaysEnum.FIXED.name().equals(rulePO.getWays())) {
            List<AttSchedulingRuleClassesPO> ruleClassesPOList = attSchedulingRuleClassesMapper.selectList(Wrappers
                                                                                                                   .<AttSchedulingRuleClassesPO>lambdaQuery()
                                                                                                                   .eq(AttSchedulingRuleClassesPO::getRuleId, rulePO.getId())
                                                                                                          );
            Map<LocalDateTime, List<AttSchedulingPO>> handMap = null;
            if (handSchedulingPOList != null && !handSchedulingPOList.isEmpty()) {
                handMap = handSchedulingPOList.stream().collect(Collectors.groupingBy(AttSchedulingPO::getSchedulingDate));
            }
            List<AttSchedulingPO> rulePOList = this.getStaffPeriodRuleScheduling(staffId, startTime, endTime, rulePO.getLegalHolidaysRest(), ruleClassesPOList, handMap);
            if (handSchedulingPOList == null) {
                return rulePOList;
            }
            if (rulePOList != null) {
                handSchedulingPOList.addAll(rulePOList);
            }
        }
        if (handSchedulingPOList != null) {
            handSchedulingPOList = handSchedulingPOList.stream().filter(po -> po != null && po.getClassesId() != null)
                    .collect(Collectors.toList());
        }
        return handSchedulingPOList;
    }

    /**
     * 获取人员一段时间的排班
     *
     * @param staffId
     * @param startTime
     * @param endTime
     *
     * @return
     */
    public List<AttSchedulingPO> getPeriodScheduling(Long staffId, LocalDateTime startTime, LocalDateTime endTime) {
        AttGroupPO groupPO = attGroupRepository.getGroupByStaffId(staffId);
        AttSchedulingRulePO rulePO = null;
        if (groupPO != null) {
            rulePO = attSchedulingRuleMapper.selectById(groupPO.getSchedulingRuleId());
        }
        return this.getPeriodScheduling(staffId, startTime, endTime, groupPO, rulePO);
    }

    public List<AttSchedulingPO> getMonthScheduling(Long staffId, String month, AttGroupPO groupPO, AttSchedulingRulePO rulePO) {
        LocalDateTime startTime = DateUtils.parse(month, "yyyy-MM");
        LocalDateTime endTime = DateUtils.endOfMonth(startTime);
        return this.getPeriodScheduling(staffId, startTime, endTime, groupPO, rulePO);
    }

    /**
     * 获取指定时间段员工的上班时间
     *
     * @param staffId
     * @param period
     *
     * @return
     */
    public long getStaffWorkTime(@NonNull Long staffId, @NonNull Period period) {
        long workTime = AttConstants.zeroLong;
        LocalDateTime beginTime = DateUtils.beginOfDay(period.getBegin());
        LocalDateTime endTime = DateUtils.endOfDay(period.getEnd());

        boolean legalHolidaysRest = false;
        List<AttSchedulingRuleClassesPO> ruleClassesPOList = new ArrayList<>();

        AttGroupPO groupPO = attGroupRepository.getGroupByStaffId(staffId);
        if (groupPO != null && groupPO.getSchedulingRuleId() != null) {
            AttSchedulingRulePO rulePO = attSchedulingRuleMapper.selectById(groupPO.getSchedulingRuleId());
            if (rulePO != null && Boolean.TRUE.equals(rulePO.getLegalHolidaysRest())) {
                legalHolidaysRest = true;
            }
            ruleClassesPOList = attSchedulingRuleClassesMapper.selectList(Wrappers.<AttSchedulingRuleClassesPO>lambdaQuery()
                                                                                  .eq(AttSchedulingRuleClassesPO::getRuleId, groupPO.getSchedulingRuleId())
                                                                         );
        }
        // 获取排班
        List<AttSchedulingPO> schedulingPOList = this.getStaffPeriodScheduling(staffId, DateUtils.offsetDay(beginTime, -1), endTime, legalHolidaysRest, ruleClassesPOList);
        if (schedulingPOList == null || schedulingPOList.isEmpty()) {
            return workTime;
        }

        Map<Long, AttClassesPO> classesPOMap = new HashMap<>();
        List<Long> classIdList = schedulingPOList.stream().map(AttSchedulingPO::getClassesId).distinct().collect(Collectors.toList());
        if (!classIdList.isEmpty()) {
            List<AttClassesPO> classesPOList = attClassesMapper.selectByIds(classIdList);
            if (classesPOList != null && !classesPOList.isEmpty()) {
                classesPOMap = classesPOList.stream().collect(Collectors.toMap(AttClassesPO::getId, v -> v));
            }
        }

        // 填充班次时间
        List<AttSchedulingPO> workPOList = new ArrayList<>();
        for (AttSchedulingPO poi : schedulingPOList) {
            if (poi.getClassesId() == null) {
                continue;
            }
            if (poi.getClassesRealStartTime() == null) {
                this.stringClassToScheduling(poi, classesPOMap.get(poi.getClassesId()));
            }
            if (poi.getClassesRealStartTime() == null) {
                continue;
            }
            workPOList.add(poi);
        }
        workPOList.sort(Comparator.comparing(AttSchedulingPO::getClassesRealStartTime));
        // 真正在这个时间端的班次（查询是安一整天查询的）
        List<AttSchedulingPO> calcList = new ArrayList<>();
        for (AttSchedulingPO schedulingPO : workPOList) {
            if (schedulingPO.getClassesRealEndTime().isAfter(period.getBegin()) &&
                schedulingPO.getClassesRealStartTime().isBefore(period.getEnd())) {
                calcList.add(schedulingPO);
            }
        }
        if (calcList.isEmpty()) {
            return workTime;
        }

        int size = calcList.size();
        for (int i = 0; i < size; i++) {
            AttSchedulingPO attSchedulingPO = calcList.get(i);
            if (i == 0 || i == size - 1) {

                workTime +=
                        Math.min(DateUtils.toTimestamp(attSchedulingPO.getClassesRealEndTime()), DateUtils.toTimestamp(period.getEnd())) -
                        Math.max(DateUtils.toTimestamp(attSchedulingPO.getClassesRealStartTime()), DateUtils.toTimestamp(period.getBegin()));
            } else {
                workTime += DateUtils.betweenMs(attSchedulingPO.getClassesRealStartTime(), attSchedulingPO.getClassesRealEndTime());
            }

            if (attSchedulingPO.getClassesRestStartTime() != null && attSchedulingPO.getClassesRestEndTime() != null) {
                // 减去班次中间的休息时间
                long restTime =
                        Math.min(DateUtils.toTimestamp(attSchedulingPO.getClassesRestEndTime()), DateUtils.toTimestamp(period.getEnd())) -
                        Math.max(DateUtils.toTimestamp(attSchedulingPO.getClassesRestStartTime()), DateUtils.toTimestamp(period.getBegin()));
                if (restTime > AttConstants.zeroLong) {
                    workTime -= restTime;
                }
            }
        }
        return Math.max(workTime, AttConstants.zeroLong);

    }

    /**
     * 获取在岗人员
     *
     * @param param 条件
     *
     * @return 人员
     */
    public AttWorkingResult workingStaff(AttWorkingStaffParam param) {
        AttWorkingResult result = new AttWorkingResult();
        result.setTotalCount(AttConstants.zeroInt);
        result.setWorkCount(AttConstants.zeroInt);
        result.setStaffResultList(new ArrayList<>());
        LocalDateTime nowTime = LocalDateTime.now();

        WorkStaffIdSetDTO workStaffDTO = this.getWorkStaffDTO(param);
        Map<Long, AttGroupStaffPO> staffPOMap = workStaffDTO.getGroupStaffPOMap();
        Set<Long> workIdList = workStaffDTO.getWorkIdSet();
        Set<Long> postIdList = workStaffDTO.getRestIdSet();
        if (staffPOMap.isEmpty()) {
            return result;
        }
        result.setWorkCount(workIdList.size());
        result.setTotalCount(workIdList.size() + postIdList.size());

        List<AttWorkingStaffResult> statusStaffResultList = new ArrayList<>();
        // 遍历在岗人员设置班次信息
        for (Long staffId : workIdList) {
            AttWorkingStaffResult staffResult = new AttWorkingStaffResult();
            staffResult.setStaffId(staffId);
            AttGroupStaffPO groupStaff = staffPOMap.get(staffId);
            if (groupStaff != null) {
                staffResult.setStaffName(groupStaff.getName());
                staffResult.setMobile(groupStaff.getMobile());
            }
            // 获取班次（有跨天班次概念，所以获取前一天和今天）
            List<AttSchedulingClassesDTO> befDayList = this.getStaffDayScheduling(staffId, DateUtils.formatDate(DateUtils.offsetDay(nowTime, -1)));
            if (befDayList == null) {
                befDayList = new ArrayList<>();
            }
            List<AttSchedulingClassesDTO> dayList = this.getStaffDayScheduling(staffId, DateUtils.formatDate(nowTime));
            if (dayList == null) {
                dayList = new ArrayList<>();
            }
            dayList.addAll(befDayList);

            // 如果只有一个班次直接取班次
            if (dayList.size() == AttConstants.oneInt) {
                AttSchedulingClassesDTO classesDTO = dayList.getFirst();
                staffResult.setClassName(classesDTO.getName());
                staffResult.setStartWorkTime(classesDTO.getStartWorkTime());
                staffResult.setEndWorkTime(classesDTO.getEndWorkTime());
                staffResult.setEndTimeIsMorrow(classesDTO.getEndTimeIsMorrow());
            } else if (dayList.size() > AttConstants.oneInt) {
                // 如果有多个班次就判断哪个在当前时间
                for (AttSchedulingClassesDTO classesDTO : dayList) {
                    if (classesDTO.getStartWorkTime() != null && classesDTO.getEndWorkTime() != null &&
                        (nowTime.isAfter(classesDTO.getStartWorkTime()) || nowTime.isEqual(classesDTO.getStartWorkTime()))
                        && (nowTime.isBefore(classesDTO.getEndWorkTime()) || nowTime.isEqual(classesDTO.getEndWorkTime()))) {
                        staffResult.setClassName(classesDTO.getName());
                        staffResult.setStartWorkTime(classesDTO.getStartWorkTime());
                        staffResult.setEndWorkTime(classesDTO.getEndWorkTime());
                        staffResult.setEndTimeIsMorrow(classesDTO.getEndTimeIsMorrow());
                        break;
                    }
                }
            }
            statusStaffResultList.add(staffResult);
        }
        result.setStaffResultList(statusStaffResultList);

        return result;
    }

    /**
     * 获取人员开始时间的班次
     * 只匹配开始时间刚好为入参时间的班次
     *
     * @param staffId  员工id
     * @param dateTime 时间
     *
     * @return 班次
     */
    public AttSchedulingPO getStaffStartTimeScheduling(Long staffId, LocalDateTime dateTime) {
        AttSchedulingPO result = AttSchedulingPO.builder().build();
        if (staffId == null || dateTime == null) {
            return result;
        }
        List<AttSchedulingPO> schedulingPOList = this.getStaffDaySchedulingPO(staffId, DateUtils.formatDate(dateTime));
        if (schedulingPOList != null && !schedulingPOList.isEmpty()) {
            for (AttSchedulingPO attSchedulingPO : schedulingPOList) {
                if (attSchedulingPO == null || attSchedulingPO.getClassesRealStartTime() == null) {
                    continue;
                }
                if (dateTime.equals(attSchedulingPO.getClassesRealStartTime()) ||
                    (DateUtils.toTimestamp(attSchedulingPO.getClassesRealStartTime()) >= DateUtils.toTimestamp(dateTime) &&
                     DateUtils.toTimestamp(attSchedulingPO.getClassesRealStartTime()) <= DateUtils.toTimestamp(dateTime) + 59900L)) {
                    return attSchedulingPO;
                }
            }
        }
        return result;
    }

    public List<Long> getStaffStartTimeScheduling(Set<Long> staffIds, LocalDateTime dateTime) {
        List<Long> staffIdList = new ArrayList<>();
        LocalDateTime offsetMinute = DateUtils.offset(dateTime, ChronoUnit.MINUTES, 1);
        staffIds.forEach(ex -> {
            List<AttClockSchedulingClassesDTO> attSchedulingPO = this.getStaffWorkClass(ex, offsetMinute);
            if (CollectionUtils.isNotEmpty(attSchedulingPO)) {
                staffIdList.add(ex);
            }
        });

        return staffIdList;
    }

    /**
     * 获取在岗或不在岗人员分页列表
     *
     * @param param
     *
     * @return
     */
    public Pagination<UserDTO> getWorkStatusStaffPage(AttWorkStaffPageParam param) {
        WorkStaffIdSetDTO dto = this.getWorkStaffDTO(new AttWorkingStaffParam());
        Set<Long> workIdSet = dto.getWorkIdSet();
        Set<Long> postIdSet = dto.getRestIdSet();
        // 查询在岗人员列表
        if (Boolean.TRUE.equals(param.getWorkBool()) && !workIdSet.isEmpty()) {
            UserQueryParam userParam = new UserQueryParam();
            userParam.setPageNumber(param.getPageNumber());
            userParam.setPageSize(param.getPageSize());
            userParam.setIds(new ArrayList<>(workIdSet));
            return userClient.postListUsers(userParam);
        }
        // 查询不在岗人员列表
        if (Boolean.FALSE.equals(param.getWorkBool()) && !postIdSet.isEmpty()) {
            UserQueryParam userParam = new UserQueryParam();
            userParam.setPageNumber(param.getPageNumber());
            userParam.setPageSize(param.getPageSize());
            userParam.setIds(new ArrayList<>(postIdSet));
            return userClient.postListUsers(userParam);
        }

        return Pagination.empty();
    }

    private WorkStaffIdSetDTO getWorkStaffDTO(AttWorkingStaffParam param) {
        WorkStaffIdSetDTO result = new WorkStaffIdSetDTO();
        result.setWorkIdSet(new HashSet<>());
        result.setRestIdSet(new HashSet<>());
        result.setGroupStaffPOMap(new HashMap<>());

        // 获取总考勤人数
        List<AttGroupStaffPO> groupStaffPOList = attGroupStaffMapper.selectList(Wrappers.<AttGroupStaffPO>lambdaQuery()
                                                                                        .eq(param.getStaffId() !=
                                                                                            null, AttGroupStaffPO::getStaffId, param.getStaffId())
                                                                                        .eq(param.getDeptId() !=
                                                                                            null, AttGroupStaffPO::getDeptId, param.getDeptId())
                                                                                        .eq(param.getGroupId() !=
                                                                                            null, AttGroupStaffPO::getGroupId, param.getGroupId())
                                                                                        .like(StringUtils.isNotBlank(param.getStaffName()), AttGroupStaffPO::getName, param.getStaffName())
                                                                                        .groupBy(AttGroupStaffPO::getStaffId)
                                                                               );
        if (groupStaffPOList != null && !groupStaffPOList.isEmpty()) {
            // 获取考勤在岗人员
            Set<Long> staffIdSet = new HashSet<>();
            Map<Long, AttGroupStaffPO> staffMap = new HashMap<>();
            for (AttGroupStaffPO groupStaffPO : groupStaffPOList) {
                staffIdSet.add(groupStaffPO.getStaffId());
                staffMap.put(groupStaffPO.getStaffId(), groupStaffPO);
            }
            result.setGroupStaffPOMap(staffMap);
            Set<Long> workStaffIdSet = new HashSet<>();
            Set<Long> postStaffIdSet = new HashSet<>();
            Integer tenant = SessionContextHolder.getRequester().getTenant();
            WorkStatusQueryParam workStatusQueryParam = new WorkStatusQueryParam();
            workStatusQueryParam.setTenant(tenant);
            workStatusQueryParam.setUserIdList(staffIdSet);
            List<UserTenantDTO> userTenantDTOS = terminalClient.getTenantUserWorkStatusPost(workStatusQueryParam);
            if (userTenantDTOS != null && !userTenantDTOS.isEmpty()) {
                for (UserTenantDTO userTenantDTO : userTenantDTOS) {
                    if (AttStaffWorkStatusEnum.WORKING.name().equals(userTenantDTO.getWorkStatus())) {
                        workStaffIdSet.add(userTenantDTO.getUserId());
                    } else {
                        postStaffIdSet.add(userTenantDTO.getUserId());
                    }
                }
            }
            result.setWorkIdSet(workStaffIdSet);
            result.setRestIdSet(postStaffIdSet);
        }
        return result;
    }

    @Data
    static class WorkStaffIdSetDTO {
        private Set<Long> workIdSet;
        private Set<Long> restIdSet;
        private Map<Long, AttGroupStaffPO> groupStaffPOMap;
    }
}
