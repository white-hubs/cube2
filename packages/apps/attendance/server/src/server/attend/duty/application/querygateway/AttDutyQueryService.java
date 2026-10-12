package com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.application.querygateway;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.style.StyleUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.AttConstants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.infrastructure.mapper.AttDutyMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.infrastructure.mapper.AttDutyStaffMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.infrastructure.po.AttDutyPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.infrastructure.po.AttDutyStaffPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.interfaces.param.AttAppPeriodDutyParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.interfaces.param.AttDutyPageParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.interfaces.result.AttAppDutyPeriodResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.interfaces.result.AttDutyAppDayStaffResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.interfaces.result.AttDutyDateClassResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.interfaces.result.AttDutyPageResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.interfaces.result.AttDutyStaffIdNameResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper.AttClassesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttClassesPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.util.ExcelUtils;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 值班查询服务
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttDutyQueryService {

    private final AttDutyMapper attDutyMapper;
    private final AttDutyStaffMapper attDutyStaffMapper;
    private final AttClassesMapper attClassesMapper;

    /**
     * 值班分页列表
     *
     * @param param 条件
     *
     * @return 分页列表
     */
    public Pagination<AttDutyPageResult> pageDuty(AttDutyPageParam param) {
        List<AttDutyPageResult> resultList = new ArrayList<>();
        // 获取人员id分页对象
        Page<AttDutyStaffPO> dutyStaffPage = this.getDutyStaffPage(param);
        dutyStaffPage.getRecords().forEach(dutyStaff -> {
            AttDutyPageResult pageResult = new AttDutyPageResult();
            pageResult.setStaffId(dutyStaff.getStaffId());
            pageResult.setStaffName(dutyStaff.getStaffName());
            List<AttDutyPO> poList = attDutyMapper.selectList(Wrappers.<AttDutyPO>lambdaQuery()
                                                                      .eq(AttDutyPO::getDutyMonth, param.getYearMonth())
                                                                      .eq(AttDutyPO::getStaffId, dutyStaff.getStaffId())
                                                             );
            List<AttDutyDateClassResult> dateClassList = new ArrayList<>();
            if (poList != null) {
                for (AttDutyPO dutyDatePO : poList) {
                    AttDutyDateClassResult dateClass = new AttDutyDateClassResult();
                    BeanUtils.copyProperties(dutyDatePO, dateClass);
                    dateClassList.add(dateClass);
                }
            }
            pageResult.setDateClassList(dateClassList);
            resultList.add(pageResult);
        });

        return new Pagination<>(dutyStaffPage.getTotal(), dutyStaffPage.getPages(), resultList);
    }

    /**
     * 获取某月所有的值班人员
     *
     * @param yearMonth 年月
     *
     * @return 值班人员集合
     */
    public List<AttDutyStaffIdNameResult> getDutyAllStaffIdAndName(String yearMonth) {
        List<AttDutyStaffIdNameResult> resultList = new ArrayList<>();
        List<AttDutyStaffPO> dutyStaffPOList = attDutyStaffMapper.selectList(Wrappers.<AttDutyStaffPO>lambdaQuery()
                                                                                     .eq(AttDutyStaffPO::getDutyMonth, yearMonth));
        for (int i = 0; i < dutyStaffPOList.size(); i++) {
            AttDutyStaffPO dutyStaffPO = dutyStaffPOList.get(i);
            AttDutyStaffIdNameResult result = new AttDutyStaffIdNameResult();
            result.setId(dutyStaffPO.getStaffId());
            result.setName(dutyStaffPO.getStaffName());
            resultList.add(result);
        }
        return resultList;
    }

    private Page<AttDutyStaffPO> getDutyStaffPage(AttDutyPageParam param) {
        return attDutyStaffMapper.selectPage(PagingUtils.convert(param), Wrappers.<AttDutyStaffPO>lambdaQuery()
                                                     .eq(AttDutyStaffPO::getDutyMonth, param.getYearMonth())
                                                     .like(StringUtils.isNotBlank(param.getKeyword()), AttDutyStaffPO::getStaffName, param.getKeyword())
                                            );
    }

    /**
     * 导出值班
     *
     * @param param    条件
     * @param request  请求
     * @param response 返回
     */
    public void exportAttDuty(AttDutyPageParam param, HttpServletRequest request, HttpServletResponse response) {
        // 获取全部人员
        List<AttDutyStaffPO> dutyStaffPOList = attDutyStaffMapper.selectList(Wrappers.<AttDutyStaffPO>lambdaQuery()
                                                                                     .eq(AttDutyStaffPO::getDutyMonth, param.getYearMonth())
                                                                                     .like(StringUtils.isNotBlank(param.getKeyword()), AttDutyStaffPO::getStaffName, param.getKeyword())
                                                                            );
        if (dutyStaffPOList == null || dutyStaffPOList.isEmpty()) {
            throw new ServiceException("暂无值班人员！");
        }
        // 获取全部值班
        List<AttDutyPO> dutyPOList = attDutyMapper.selectList(Wrappers.<AttDutyPO>lambdaQuery()
                                                                      .eq(AttDutyPO::getDutyMonth, param.getYearMonth())
                                                                      .like(StringUtils.isNotBlank(param.getKeyword()), AttDutyPO::getStaffName, param.getKeyword())
                                                             );
        // 导出的数据对象
        List<Map<String, String>> excelDataList = new ArrayList<>();
        // 导出的表头
        Map<String, String> headers = new LinkedHashMap<>();
        // 根据人员分组
        Map<Long, List<AttDutyPO>> dutyPOMap = dutyPOList.stream().collect(Collectors.groupingBy(AttDutyPO::getStaffId));
        // 遍历人员
        for (int i = 0; i < dutyStaffPOList.size(); i++) {
            AttDutyStaffPO dutyStaffPO = dutyStaffPOList.get(i);
            // 值班数据
            List<AttDutyPO> attDutyPOList = dutyPOMap.get(dutyStaffPO.getStaffId());
            // 人员日期的map
            Map<String, List<AttDutyPO>> dateMap = new HashMap<>();
            if (attDutyPOList != null && !attDutyPOList.isEmpty()) {
                dateMap = attDutyPOList.stream().collect(Collectors.groupingBy(p -> DateUtils.formatDate(p.getDutyDate())));
            }
            // excel的一行数据
            Map<String, String> excelDataMap = new LinkedHashMap<>();
            excelDataMap.put("staffName", dutyStaffPO.getStaffName());
            if (i == 0) {
                headers.put("staffName", "员工");
            }
            // 生成一个月的数据
            LocalDateTime startTime = DateUtils.parseDate(param.getYearMonth() + "-01");
            LocalDateTime endTime = DateUtils.endOfMonth(startTime);
            LocalDateTime printTime = startTime;
            int j = 0;
            do {
                if (i == 0) {
                    headers.put("classes" + j, DateUtils.formatDate(printTime));
                }
                // 如果有值班就遍历班次写入班次名称,否则组装一个休息班次
                if (dateMap.get(DateUtils.formatDate(printTime)) != null) {
                    StringBuffer stringBuffer = new StringBuffer();
                    dateMap.get(DateUtils.formatDate(printTime))
                            .forEach(cla -> stringBuffer.append(cla.getClassesNickName()).append("\r\n"));
                    excelDataMap.put("classes" + j, stringBuffer.toString());
                } else {
                    excelDataMap.put("classes" + j, "休息");
                }
                j++;
                printTime = DateUtils.offsetDay(printTime, 1);
            } while (!printTime.isAfter(endTime));
            excelDataList.add(excelDataMap);
        }

        //导出Excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.setOnlyAlias(true);
        writer.setHeaderAlias(headers);
        //默认配置
        writer.write(excelDataList, true);
        writer.setColumnWidth(-1, 15);
        //设置单元格自动换行
        CellStyle cellStyle = writer.getCellStyle();
        cellStyle.setWrapText(true);
        String fileName = (StringUtils.isNotBlank(param.getYearMonth()) ? param.getYearMonth() + "_" : "") + "值班";
        ExcelUtils.downloadExcel(request, response, fileName, writer);
    }

    /**
     * app获取某月的值班
     *
     * @param param 时间段
     *
     * @return 值班列表
     */
    public List<AttAppDutyPeriodResult> getAppPeriodDuty(AttAppPeriodDutyParam param) {
        List<AttAppDutyPeriodResult> resultList = new ArrayList<>();

        List<AttDutyPO> dutyPOList = attDutyMapper.selectList(Wrappers.<AttDutyPO>lambdaQuery()
                                                                      .eq(StringUtils.isNotBlank(param.getMonth()), AttDutyPO::getDutyMonth, param.getMonth())
                                                                      .between(AttDutyPO::getDutyDate, param.getPeriod().getBegin(), param
                                                                              .getPeriod().getEnd())
                                                             );

        if (dutyPOList != null && !dutyPOList.isEmpty()) {
            Map<Long, AttClassesPO> classesPOMap = new HashMap<>();
            List<Long> classIdList = dutyPOList.stream().map(AttDutyPO::getClassesId).filter(Objects::nonNull).collect(Collectors.toList());
            List<AttClassesPO> classesPOList = attClassesMapper.selectByIds(classIdList);
            if (classesPOList != null) {
                classesPOMap = classesPOList.stream().collect(Collectors.toMap(AttClassesPO::getId, v -> v));
            }
            Map<Long, AttClassesPO> finalClassesPOMap = classesPOMap;
            dutyPOList.stream().collect(Collectors.groupingBy(AttDutyPO::getDutyDate)).forEach((k, v) -> {
                AttAppDutyPeriodResult rs = new AttAppDutyPeriodResult();
                rs.setDay(DateUtils.formatDate(k));
                rs.setStaffList(v.stream().filter(Objects::nonNull).map(
                                duty -> AttDutyAppDayStaffResult.builder().staffId(duty.getStaffId()).staffName(duty.getStaffName())
                                        .className(duty.getClassesName() + " " + (finalClassesPOMap.get(duty.getClassesId()) == null ? "" :
                                                                                  finalClassesPOMap.get(duty.getClassesId()).getStartWorkTime() +
                                                                                  "-" +
                                                                                  finalClassesPOMap.get(duty.getClassesId()).getEndWorkTime()))
                                        .isCurrentUser(SecurityContextHolder.getPrincipal().getId().equals(duty.getStaffId())).build()
                                                                       )
                                        .sorted(Comparator.comparing(AttDutyAppDayStaffResult::getStaffName)).collect(Collectors.toList()));
                resultList.add(rs);
            });
        }
        Map<String, List<AttAppDutyPeriodResult>> resultDayMap = resultList.stream()
                .collect(Collectors.groupingBy(AttAppDutyPeriodResult::getDay));
        LocalDateTime printDate = DateUtils.newLocalDateTime(param.getPeriod().getBegin());
        while (param.getPeriod().getEnd().isAfter(printDate)) {
            if (resultDayMap.get(DateUtils.formatDate(printDate)) == null) {
                AttAppDutyPeriodResult rs = new AttAppDutyPeriodResult();
                rs.setDay(DateUtils.formatDate(printDate));
                rs.setStaffList(new ArrayList<>());
                resultList.add(rs);
            }
            printDate = DateUtils.offsetDay(printDate, 1);
        }
        resultList.sort(Comparator.comparing(AttAppDutyPeriodResult::getDay));
        return resultList;
    }

    /**
     * 下载值班模板
     *
     * @param param    条件入参
     * @param request  请求
     * @param response 返回
     */
    public void downloadTemplate(AttDutyPageParam param, HttpServletRequest request, HttpServletResponse response) {
        // 获取全部人员
        List<AttDutyStaffPO> dutyStaffPOList = attDutyStaffMapper.selectList(Wrappers.<AttDutyStaffPO>lambdaQuery()
                                                                                     .eq(AttDutyStaffPO::getDutyMonth, param.getYearMonth())
                                                                                     .like(StringUtils.isNotBlank(param.getKeyword()), AttDutyStaffPO::getStaffName, param.getKeyword())
                                                                            );
        if (dutyStaffPOList == null || dutyStaffPOList.isEmpty()) {
            throw new ServiceException("暂无值班人员！");
        }
        List<AttClassesPO> classesList = attClassesMapper.selectList(Wrappers.<AttClassesPO>lambdaQuery()
                                                                             .orderByAsc(AttClassesPO::getStartWorkTime));
        if (classesList == null || classesList.isEmpty()) {
            throw new ServiceException("当前系统还没有班次");
        }

        ExcelWriter writer = ExcelUtil.getWriter(true);
        /* 处理表头 */
        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("staffId", "员工id");
        headers.put("staffName", "员工姓名");
        LocalDateTime printDate = DateUtils.parseDate(param.getYearMonth() + "-01");
        LocalDateTime monthEndDate = DateUtils.endOfMonth(printDate);
        String[] yearMonths = param.getYearMonth().split("-");
        int index = AttConstants.oneInt;
        do {
            String heardString =
                    yearMonths[AttConstants.oneInt] + "-" + String.format("%02d", index)/*+"\r\n"+printDate.dayOfWeekEnum().name()*/;
            headers.put("staffDay" + index, heardString);
            printDate = DateUtils.offsetDay(printDate, AttConstants.oneInt);
            index++;
        } while (monthEndDate.isAfter(printDate));
        int headerSize = headers.size();
        /* 处理导出内容 */
        List<Map<String, String>> excelDataList = new ArrayList<>();
        int size = dutyStaffPOList.size();
        for (AttDutyStaffPO staffPO : dutyStaffPOList) {
            Map<String, String> staffData = new HashMap<>();
            staffData.put("staffId", String.valueOf(staffPO.getStaffId()));
            staffData.put("staffName", staffPO.getStaffName());
            for (int j = 1; j < headerSize - 1; j++) {
                staffData.put("staffDay" + j, "休息");
            }
            excelDataList.add(staffData);
        }

        List<String> classNameList = classesList.stream().map(p -> p.getName() + ":" + p.getStartWorkTime() + "-" + p.getEndWorkTime())
                .collect(Collectors.toList());
        CellRangeAddressList cellRangeAddressList = new CellRangeAddressList(AttConstants.oneInt, excelDataList.size(), 2, headerSize - 1);

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
        //sheet.protectSheet("abc123");
        // 设置列宽
        writer.setColumnWidth(-1, 25);
        writer.setRowHeight(-1, 28);
        // 隐藏id列
        sheet.setColumnHidden(0, true);

        /* 设置数据 */
        writer.setHeaderAlias(headers);
        writer.write(excelDataList, true);
        // 设置下拉班次
        this.createClassesSelect(writer, classNameList, cellRangeAddressList, param.getYearMonth());
        String fileName =
                (com.guhecloud.platform.base.util.StringUtils.isNotEmpty(param.getYearMonth()) ? param.getYearMonth() + "_" : "") + "ZB";
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
        writer.writeCellValue(2, 0, "DUTY");
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
}
