package com.guhecloud.cube.srv.gen.attendance.management.server.arrange.querygateway;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.cell.CellUtil;
import cn.hutool.poi.excel.style.StyleUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.common.commandgateway.WorkService;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.dao.ArrangeWorkClassesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.dao.ArrangeWorkMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.dao.dto.ArrangeWorkDaysDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.interfaces.vo.ArrangeWorkClassesVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.interfaces.vo.ArrangeWorkTodayStatVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.interfaces.vo.ArrangeWorkVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.interfaces.vo.CurrentClassesVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.po.ArrangeWorkClassesPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.po.ArrangeWorkPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.querygateway.dto.ClassesDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.querygateway.dto.LeaveDetailDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.querygateway.query.ArrangeWorkQueryParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.utils.PageResponse;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.domain.repository.AttGroupRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper.AttClassesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttClassesPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttGroupPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.application.querygateway.AttSchedulingQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.mapper.AttSchedulingRuleClassesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.mapper.AttSchedulingRuleMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.po.AttSchedulingPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.po.AttSchedulingRuleClassesPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.po.AttSchedulingRulePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.clock.dao.ClassesClockMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.clock.dao.dto.ClassesClockStatDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.clock.enumeration.ClockStatusEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.clock.po.ClassesClockPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.clock.querygateway.ClockQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.ClassesStatusTriggerEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.querygateway.query.AttendanceStatQueryParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.util.ExcelUtils;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.Holiday;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.domain.aggregate.LeaveForm;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.domain.entity.LeaveFormDetail;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.domain.valueobject.LeaveStatus;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.infrastructure.mapper.LeaveFormMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.infrastructure.po.LeaveFormPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.leavepost.dao.LeavePostSituationMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.leavepost.dao.dto.LeavePostDurationStatDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.leavepost.po.LeavePostSituationPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.overtime.dao.WorkOvertimeSituationMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.overtime.dao.dto.OvertimeDurationGroupByTypeDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.overtime.po.WorkOvertimeSituationPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.AttendanceGroupMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.domain.classes.Classes;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.AttendanceGroupPunchCardWayEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.AttendanceGroupRangesEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.AttendanceGroupTypeEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.interfaces.vo.ClassesVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.AttendanceGroupPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.querygateway.AttendanceGroupQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.querygateway.ClassesQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.querygateway.query.ClassesQueryParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.stat.querygateway.AttendanceStatsQueryServiceImpl;
import com.guhecloud.cube.srv.gen.attendance.management.server.stat.vo.req.AttendanceStatParam;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author bq
 * @date 2022/11/14
 * @description 排班查询
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ArrangeWorkQueryService extends ServiceImpl<ArrangeWorkMapper, ArrangeWorkPO> {

    private final static String JUMP_RECORD = "jump_record";
    private final static String JUMP_CLOCK = "jump_clock";

    private final ArrangeWorkClassesMapper arrangeWorkClassesMapper;
    private final ClassesClockMapper classesClockMapper;
    private final WorkService workService;
    private final AttendanceGroupMapper attendanceGroupMapper;
    private final AttendanceGroupQueryService attendanceGroupQueryService;
    private final ClassesQueryService classesQueryService;
    private final ClockQueryService clockQueryService;
    private final ArrangeWorkMapper arrangeWorkMapper;
    private final OrganizationClient organizationClient;
    private final WorkOvertimeSituationMapper workOvertimeSituationMapper;
    private final LeavePostSituationMapper leavePostSituationMapper;
    private final LeaveFormMapper leaveFormMapper;
    private final AttendanceStatsQueryServiceImpl attendanceStatsQueryService;

    /// //////////////////1.5
    private final AttSchedulingRuleMapper attSchedulingRuleMapper;
    private final AttSchedulingQueryService attSchedulingQueryService;
    private final AttGroupRepository attGroupRepository;
    private final AttClassesMapper attClassesMapper;
    private final AttSchedulingRuleClassesMapper attSchedulingRuleClassesMapper;

    /**
     * 分页查询
     *
     * @param param
     *
     * @return
     */
    public Pagination<ArrangeWorkVO> getPageV2(ArrangeWorkQueryParam param) {
        if (Objects.nonNull(param.getIsCurrentUser()) && param.getIsCurrentUser()) {
            param.setCurrentUserId(SecurityContextHolder.getPrincipal().getId());
        }
        Page<ArrangeWorkPO> page = arrangeWorkMapper.getPageStaffId(PagingUtils.convert(param), Wrappers.<ArrangeWorkPO>lambdaQuery()
                                                                            .eq(StringUtils.isNotEmpty(param.getMonth()), ArrangeWorkPO::getMonth, param.getMonth())
                                                                            .eq(param.getGroupId() != null, ArrangeWorkPO::getGroupId, param.getGroupId())
                                                                            .eq(param.getOrganizationId() != null, ArrangeWorkPO::getStaffOrganizationId, param.getOrganizationId())
                                                                            .like(StringUtils.isNotEmpty(param.getKeyword()), ArrangeWorkPO::getStaffName, param.getKeyword())
                                                                            .groupBy(ArrangeWorkPO::getStaffId).orderByAsc(ArrangeWorkPO::getStaffId)
                                                                   );
        List<Long> staffIdList = page.getRecords().stream().map(ArrangeWorkPO::getStaffId).collect(Collectors.toList());
        if (staffIdList.isEmpty()) {
            return PageResponse.doPage(new ArrayList<>(), param.getPageNumber(), param.getPageSize());
        }
        param.setUserIds(staffIdList);
        // 处理分页后的数据
        List<ArrangeWorkVO> rsList = getListV2(param);
        // 返回分页
        return new Pagination<>(page.getTotal(), page.getPages(), rsList);
    }

    public List<ArrangeWorkVO> getListV2(ArrangeWorkQueryParam param) {
        List<ArrangeWorkVO> arrangeWorkVOS = arrangeWorkMapper.getListV2(param);
        List<ArrangeWorkVO> vos = this.groupByStaff(arrangeWorkVOS);
        vos.forEach(p -> p.setDayOfClasses(workService.sortDateOfClasses(p.getDayOfClasses(), param.getMonth(), param.getPeriod())));
        return vos;
    }

    public void exportTemplate(ArrangeWorkQueryParam param, HttpServletRequest request, HttpServletResponse response) {
        String groupName = "";
        if (null != param.getGroupId()) {
            AttendanceGroupPO groupPO = attendanceGroupQueryService.getById(param.getGroupId());
            groupName = (groupPO == null ? groupName : groupPO.getName());
        }
        // 查询有考勤组的排班人员列表
        List<ArrangeWorkVO> arrangeWorkVOS = this.getWorkListHaveGroup(param);
        Assert.isNotAcceptable(CollectionUtils.isEmpty(arrangeWorkVOS), "未查询到排班人员，无法导出");
        List<Map<String, String>> excelDatas = this.formatTemplateData(arrangeWorkVOS, false);
        //取出表头 同时删除表头数据
        Map<String, String> headers = excelDatas.removeLast();
        //导出Excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.merge(excelDatas.getFirst().size() - 1, groupName + param.getMonth() + "月排班表", false);
        writer.setHeaderAlias(headers);
        writer.setOnlyAlias(true);
        // 设置表头背景色
        CellStyle headCellStyle = writer.getHeadCellStyle();
        StyleUtil.setColor(headCellStyle, IndexedColors.AQUA, FillPatternType.SOLID_FOREGROUND);
        // 配置班次下拉
        List<String> classesNames = classesQueryService.getList(new ClassesQueryParam()).parallelStream().map(ClassesVO::getSummaryName)
                .collect(Collectors.toList());
        Assert.isNotAcceptable(CollectionUtils.isEmpty(classesNames), "系统中未查询到班次列表，无法导出");
        CellRangeAddressList cellRangeAddressList = new CellRangeAddressList(2, excelDatas.size() + 1, 4, excelDatas.getFirst().size() - 1);
        // 最大的班次长度
        String max = classesNames.stream().max(Comparator.comparing(String::length)).get();
        //默认配置
        writer.write(excelDatas, true);
        writer.setColumnWidth(-1, max.length() + 8);
        //设置单元格自动换行
        CellStyle cellStyle = writer.getCellStyle();
        cellStyle.setWrapText(true);
        String fileName = (StringUtils.isNotEmpty(param.getMonth()) ? param.getMonth() + "_" : "") + "排班";
        this.createClassesSelect(writer, classesNames, cellRangeAddressList);
        ExcelUtils.downloadExcel(request, response, fileName, writer);
    }

    /**
     * 创建班次的下拉选择
     * 直接使用：writer.addSelect(cellRangeAddressList, String.join(",", classesNames)); 这种方式配置下拉时，当下拉数据超过255个字符，导出表格中的下拉数据将无法选择
     * 解决方法：建立一个新的sheet,将下拉数据放在新sheet中的某一列，然后再原sheet中需要下拉的地方配置公式引用
     *
     * @param writer
     * @param classesNames
     * @param cellRangeAddressList
     */
    public void createClassesSelect(ExcelWriter writer, List<String> classesNames, CellRangeAddressList cellRangeAddressList) {
        String classesNameSheet = "班次列表";
        writer.setSheet(classesNameSheet);
        for (int i = 0; i < classesNames.size(); i++) {
            writer.writeCellValue(0, i, classesNames.get(i));
        }
        writer.setSheet("sheet1");
        String classesValida = "=" + classesNameSheet + "!$A$1:$A$" + classesNames.size();
        DataValidationHelper helper = new XSSFDataValidationHelper((XSSFSheet) writer.getSheet());
        DataValidationConstraint formulaListConstraint = helper.createFormulaListConstraint(classesValida);
        DataValidation dataValidation = helper.createValidation(formulaListConstraint, cellRangeAddressList);
        writer.addValidationData(dataValidation);
        // 将创建的班次列表sheet隐藏
        writer.getWorkbook().setSheetHidden(1, true);
    }

    public void exportArrangeWork(ArrangeWorkQueryParam param, HttpServletRequest request, HttpServletResponse response) {
        List<ArrangeWorkPO> arrangeWorkPOS = this.getQueryChainWrapper(param).list();
        List<ArrangeWorkVO> arrangeWorkVOS = this.extend(arrangeWorkPOS, param);

        Assert.isNotAcceptable(CollectionUtils.isEmpty(arrangeWorkVOS), "未查询到排班数据，无法导出");
        List<Map<String, String>> excelDatas = this.formatExportData(arrangeWorkVOS, true);
        //取出表头 同时删除表头数据
        Map<String, String> headers = excelDatas.removeLast();
        //导出Excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.setHeaderAlias(headers);
        writer.setOnlyAlias(true);
        //默认配置
        writer.write(excelDatas, true);
        writer.setColumnWidth(-1, 15);
        //设置单元格自动换行
        CellStyle cellStyle = writer.getCellStyle();
        cellStyle.setWrapText(true);
        String fileName = (StringUtils.isNotEmpty(param.getMonth()) ? param.getMonth() + "_" : "") + "排班";
        ExcelUtils.downloadExcel(request, response, fileName, writer);
    }

    /**
     * 考勤结果统计 分页
     *
     * @param param 分页参数
     *
     * @return 分页列表
     */
    public Pagination<ArrangeWorkVO> getPageExtendV2(ArrangeWorkQueryParam param) {
        // 以人员为维度的统计先将人员分组分页(之后就只扩展每一页的数据)
        Page<ArrangeWorkPO> page = arrangeWorkMapper.getPageOnlyStaffId(new Page<>(param.getPageNumber(), param.getPageSize()), this.getQuerySummaryWrapper(param));

        List<ArrangeWorkVO> rs = this.extWorkVo(page.getRecords(), param);
        return new Pagination<>(page.getTotal(), page.getPages(), rs);
    }

    /**
     * 考勤结果统计 导出
     *
     * @param param    查询参数
     * @param request  请求对象
     * @param response 返回对象
     */
    public void exportArrangeWorkExtend(ArrangeWorkQueryParam param, HttpServletRequest request, HttpServletResponse response) {
        AttendanceStatParam statParam = new AttendanceStatParam();
        BeanUtils.copyProperties(param, statParam);
        List<ArrangeWorkVO> arrangeWorkVOS = attendanceStatsQueryService.getStaffStatList(statParam);

        Assert.isNotAcceptable(CollectionUtils.isEmpty(arrangeWorkVOS), "未查询到相关数据，无法导出");
        List<Map<String, String>> excelDatas = this.formatExportDataDTO(arrangeWorkVOS);
        //取出表头 同时删除表头数据
        Map<String, String> headers = excelDatas.removeLast();
        //导出Excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.setHeaderAlias(headers);
        writer.setOnlyAlias(true);
        //默认配置
        writer.write(excelDatas, true);
        writer.setColumnWidth(-1, 15);
        //设置单元格自动换行
        CellStyle cellStyle = writer.getCellStyle();
        cellStyle.setWrapText(true);

        for (int i = 1; i <= excelDatas.size(); i++) {
            for (int j = 16; j < headers.size(); j++) {
                Cell call = writer.getCell(j, i);
                String callVale = String.valueOf(CellUtil.getCellValue(call));
                if (StringUtils.isEmpty(callVale)) {
                    continue;
                }
                if (callVale.contains("正常")) {
                    // 根据x,y轴获取当前单元格样式
                    CellStyle cellStyle1 = writer.createCellStyle(j, i);
                    this.cellStyle(writer, cellStyle1, IndexedColors.BLUE1.getIndex());
                } else if (callVale.contains("休息")) {
                } else if (callVale.contains("缺卡")) {
                    // 根据x,y轴获取当前单元格样式
                    CellStyle cellStyle1 = writer.createCellStyle(j, i);
                    this.cellStyle(writer, cellStyle1, IndexedColors.VIOLET.getIndex());
                } else if (callVale.contains("旷工")) {
                    // 根据x,y轴获取当前单元格样式
                    CellStyle cellStyle1 = writer.createCellStyle(j, i);
                    this.cellStyle(writer, cellStyle1, IndexedColors.RED1.getIndex());
                } else if (callVale.contains("事假") || callVale.contains("调休") || callVale.contains("年假")) {
                    // 根据x,y轴获取当前单元格样式
                    CellStyle cellStyle1 = writer.createCellStyle(j, i);
                    this.cellStyle(writer, cellStyle1, IndexedColors.GREEN.getIndex());
                } else if (callVale.contains("迟到")) {
                    // 根据x,y轴获取当前单元格样式
                    CellStyle cellStyle1 = writer.createCellStyle(j, i);
                    this.cellStyle(writer, cellStyle1, IndexedColors.GOLD.getIndex());
                } else if (callVale.contains("早退")) {
                    // 根据x,y轴获取当前单元格样式
                    CellStyle cellStyle1 = writer.createCellStyle(j, i);
                    this.cellStyle(writer, cellStyle1, IndexedColors.GOLD.getIndex());
                } else if (callVale.contains("补卡")) {
                    // 根据x,y轴获取当前单元格样式
                    CellStyle cellStyle1 = writer.createCellStyle(j, i);
                    this.cellStyle(writer, cellStyle1, IndexedColors.GOLD.getIndex());
                }
            }
        }
        String fileName = (StringUtils.isNotEmpty(param.getMonth()) ? param.getMonth() + "_" : "") + "员工考勤统计";
        ExcelUtils.downloadExcel(request, response, fileName, writer);
    }

    private void cellStyle(ExcelWriter writer, CellStyle cellStyle, short color) {
        Font font = writer.createFont();
        font.setColor(color);
        // 字体颜色标红
        cellStyle.setFont(font);
        // 内容水平居中
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        // 内容垂直居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 自动换行
        cellStyle.setWrapText(true);
        // 设置边框
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
    }

    private LambdaQueryWrapper<ArrangeWorkPO> getQuerySummaryWrapper(ArrangeWorkQueryParam param) {
        return Wrappers.<ArrangeWorkPO>lambdaQuery()
                .like(StringUtils.isNotEmpty(param.getKeyword()), ArrangeWorkPO::getStaffName, param.getKeyword())
                .eq(null != param.getStaffId(), ArrangeWorkPO::getStaffId, param.getStaffId())
                .eq(null != param.getGroupId(), ArrangeWorkPO::getGroupId, param.getGroupId())
                .eq(StringUtils.isNotEmpty(param.getMonth()), ArrangeWorkPO::getMonth, param.getMonth())
                .and(null != param.getPeriod(), q -> q
                        .between(null != param.getPeriod().getBegin() && null != param.getPeriod().getEnd(), ArrangeWorkPO::getDate, param
                                .getPeriod().getBegin(), param.getPeriod().getEnd())
                    )
                .eq(null != param.getOrganizationId(), ArrangeWorkPO::getStaffOrganizationId, param.getOrganizationId())
                .eq(param.getIsCurrentUser() != null && param.getIsCurrentUser(), ArrangeWorkPO::getStaffId, SecurityContextHolder
                        .getPrincipal().getId())
                ;
    }

    public List<ArrangeWorkVO> extWorkVo(List<ArrangeWorkPO> staffSource, ArrangeWorkQueryParam param) {
        if (staffSource == null || staffSource.isEmpty()) {
            return new ArrayList<>();
        }
        // 人员ids
        List<Long> staffIds = staffSource.stream().map(ArrangeWorkPO::getStaffId).collect(Collectors.toList());
        AttendanceStatQueryParam statQueryParam = new AttendanceStatQueryParam();
        ObjectUtils.copying(param, statQueryParam);
        statQueryParam.setUserIds(staffIds);
        statQueryParam.setTenant(SessionContextHolder.getRequester().getTenant());

        //排班统计
        List<ArrangeWorkDaysDTO> arrangeWorkDaysDTOList = arrangeWorkMapper.getArrangeWorkDays(statQueryParam);//arrangeWorkDaysFuture.get();
        Map<Long, ArrangeWorkDaysDTO> workMap = arrangeWorkDaysDTOList.stream()
                .collect(Collectors.toMap(ArrangeWorkDaysDTO::getUserId, clockSummary -> clockSummary));
        Period oldPeriod = new Period(DateUtils.newLocalDateTime(param.getPeriod().getBegin()), DateUtils.newLocalDateTime(param.getPeriod()
                                                                                                                                   .getEnd()));
        // 如果结束时间大于当前时间，就取当前时间的前一天
        if (param.getPeriod().getEnd().isAfter(LocalDateTime.now())) {
            // 前一天 如果结束时间大于当前时间就用取当前时间的前一天，否则取结束时间
            LocalDateTime endDate = DateUtils.offsetDay(DateUtils.endOfDay(LocalDateTime.now()), -1);
            if (DateUtils.newLocalDateTime(param.getPeriod().getBegin()).isAfter(endDate)) {
                endDate = param.getPeriod().getBegin();
            }
            Period befOneDay = new Period(DateUtils.newLocalDateTime(param.getPeriod().getBegin()), endDate);
            param.setPeriod(befOneDay);
            statQueryParam.setPeriod(befOneDay);
        }
        //打卡统计
        List<ClassesClockStatDTO> classesClockStatDTOList = classesClockMapper.getClassesClockStat(statQueryParam);//classesClockStatFuture.get();
        Map<Long, ClassesClockStatDTO> clockMap = classesClockStatDTOList.stream()
                .collect(Collectors.toMap(ClassesClockStatDTO::getUserId, clockSummary -> clockSummary));
        //加班统计
        List<OvertimeDurationGroupByTypeDTO> overtimeDurationGroupByTypeDTOS = workOvertimeSituationMapper.getOvertimeDurationStatGroupByType(statQueryParam);//overtimeDurationFuture.get();
        Map<Long, OvertimeDurationGroupByTypeDTO> overtimeMap = overtimeDurationGroupByTypeDTOS.stream()
                .collect(Collectors.toMap(OvertimeDurationGroupByTypeDTO::getUserId, clockSummary -> clockSummary));
        //离岗统计
        List<LeavePostDurationStatDTO> leavePostDurationStatDTOS = leavePostSituationMapper.getLeavePostDurationStat(statQueryParam);//leavePostDurationFuture.get();
        Map<Long, LeavePostDurationStatDTO> leavePostMap = leavePostDurationStatDTOS.stream()
                .collect(Collectors.toMap(LeavePostDurationStatDTO::getUserId, clockSummary -> clockSummary));

        // 先查询有没有全部人员的考勤组。
        AttendanceGroupPO attendanceGroupAll = attendanceGroupMapper.selectOne(Wrappers.<AttendanceGroupPO>lambdaQuery()
                                                                                       .eq(AttendanceGroupPO::getRanges, AttendanceGroupRangesEnum.All_STAFF.name())
                                                                                       .eq(AttendanceGroupPO::getType, AttendanceGroupTypeEnum.ARRANGE.name()));
        // 循环扩展每个人的数据
        return staffSource.stream().map(staff -> {
            Long staffId = staff.getStaffId();
            ArrangeWorkVO workVO = new ArrangeWorkVO();

            /* 处理基础信息 */
            workVO.setStaffId(staffId);
            workVO.setStaffName(staff.getStaffName());
            workVO.setGroupId(staff.getGroupId());
            workVO.setStaffOrganizationId(staff.getStaffOrganizationId());
            // 设置部门
            OrganizationDTO organizationDTO = organizationClient.getOrganizationsById(staff.getStaffOrganizationId());
            workVO.setDeptName(organizationDTO == null ? "" : organizationDTO.getName());
            // 全部人考勤组为空时，查询自己的部分人考勤组
            AttendanceGroupPO attendanceGroup =
                    attendanceGroupAll != null ? attendanceGroupAll : attendanceGroupMapper.getAttendanceGroupByUserId(staffId);
            // 设置考勤组名称
            workVO.setGroupName(attendanceGroup != null ? attendanceGroup.getName() : "");

            ArrangeWorkDaysDTO workDto = workMap.get(staffId);
            if (workDto == null) {
                workDto = new ArrangeWorkDaysDTO();
            }
            ClassesClockStatDTO clockDto = clockMap.get(staffId);
            if (clockDto == null) {
                clockDto = new ClassesClockStatDTO();
            }
            OvertimeDurationGroupByTypeDTO overtimeDto = overtimeMap.get(staffId);
            if (overtimeDto == null) {
                overtimeDto = new OvertimeDurationGroupByTypeDTO();
            }
            LeavePostDurationStatDTO leavepostDto = leavePostMap.get(staffId);

            // 设置打卡汇总信息
            workVO.setArrangeWorkDays(workDto.getWorkDays());//应出勤天数
            workVO.setRealWorkDays(clockDto.getRealWorkDays());//实际出勤天数
            workVO.setNoClockEndNum(clockDto.getNoClockEndNum());//下班缺卡次数
            workVO.setNoClockStartNum(clockDto.getNoClockStartNum());//上班缺卡次数
            workVO.setNoClockNum(clockDto.getNoClockNum());//缺卡次数
            workVO.setStartRepairClockNum(clockDto.getStartRepairClockNum());//上班补卡次数
            workVO.setEndRepairClockNum(clockDto.getEndRepairClockNum());//下班补卡次数
            workVO.setRepairClockNum(clockDto.getRepairClockNum());//补卡次数
            workVO.setEarlyNum(clockDto.getEarlyNum());//早退次数
            workVO.setLateNum(clockDto.getLateNum());//迟到次数
            workVO.setSkipWorkNum(clockDto.getSkipWorkNum());//矿工次数
            workVO.setEarlyTotalTime(clockDto.getEarlyTotalTime());//早退分钟数
            workVO.setLateTotalTime(clockDto.getLateTotalTime());//迟到分钟数
            // 设置加班信息
            workVO.setOvertimeWorkDuration(overtimeDto.getOvertimeWorkDuration());
            workVO.setOvertimeRestDuration(overtimeDto.getOvertimeRestDuration());
            workVO.setOvertimeFestDuration(overtimeDto.getOvertimeFestDuration());
            if (leavepostDto != null && leavepostDto.getLeavePostDuration() != null) {
                // 设置离岗信息
                workVO.setLeavePostDuration(leavepostDto.getLeavePostDuration()
                                                    .divide(BigDecimal.valueOf(60), 1, RoundingMode.HALF_DOWN).floatValue());
            }
            /* 处理每日打卡数据*/
            param.setStaffId(staffId);

            List<ArrangeWorkPO> pos = this.getQueryChainWrapper(param).list();
            // 循环处理统计数据
            List<ArrangeWorkVO.DateOfClasses> dateOfClasses = pos.stream().map(po -> {
                ArrangeWorkVO.DateOfClasses date = ObjectUtils.copying(po, ArrangeWorkVO.DateOfClasses.class);
                String arrangeWorkClassesIds = po.getArrangeWorkClassesIds();

                // 查询整体的请假
                List<LeaveFormPO> leaveFormList = leaveFormMapper.selectList(Wrappers.<LeaveFormPO>lambdaQuery()
                                                                                     .eq(LeaveFormPO::getStatus, "PASS")
                                                                                     .and(i -> i
                                                                                             .like(LeaveFormPO::getLeaveStartTime, DateUtils.formatDate(po.getDate()))
                                                                                             .or()
                                                                                             .like(LeaveFormPO::getLeaveEndTime, DateUtils.formatDate(po.getDate()))
                                                                                             .or(j -> j
                                                                                                     .le(LeaveFormPO::getLeaveStartTime, DateUtils.beginOfDay(po.getDate()))
                                                                                                     .ge(LeaveFormPO::getLeaveEndTime, DateUtils.endOfDay(po.getDate())))
                                                                                         )
                                                                            );
                List<LeaveDetailDTO> leaveDetailDTOS = new ArrayList<>();
                if (leaveFormList != null && !leaveFormList.isEmpty()) {
                    leaveFormList.forEach(leaveFormPO -> {
                        LeaveForm leaveForm = JsonUtils.deserialize(leaveFormPO.getLeaveFormDetail(), LeaveForm.class);
                        if (leaveForm != null && staffId.equals(leaveForm.getDetail().getUserId())) {
                            LeaveDetailDTO leaveDto = new LeaveDetailDTO(null, leaveForm.getDetail().getLeaveType().name(), leaveForm
                                    .getDetail().getLeaveTime());
                            leaveDetailDTOS.add(leaveDto);
                        }
                    });
                }
                date.setLeaveDetailDTOS(leaveDetailDTOS);

                if (StringUtils.isNotEmpty(arrangeWorkClassesIds)) {
                    List<String> classesIds = Arrays.asList(arrangeWorkClassesIds.split(","));
                    List<ArrangeWorkClassesPO> arrangeWorkClassesPOS = arrangeWorkClassesMapper
                            .selectList(Wrappers.<ArrangeWorkClassesPO>lambdaQuery().in(ArrangeWorkClassesPO::getId, classesIds));
                    date.setClassesDTOList(this.getWorkClassesDTOStatus(po.getId(), classesIds, arrangeWorkClassesPOS));
                }
                return date;
            }).collect(Collectors.toList());
            workVO.setDayOfClasses(workService.sortDateOfClasses(dateOfClasses, "", param.getPeriod()));
            return workVO;
        }).collect(Collectors.toList());
    }

    /**
     * 获取有考勤组的排班列表
     *
     * @param param
     *
     * @return
     */
    private List<ArrangeWorkVO> getWorkListHaveGroup(ArrangeWorkQueryParam param) {
        List<ArrangeWorkVO> arrangeWorkVOS = arrangeWorkMapper.getWorkListHaveGroup(param);
        List<ArrangeWorkVO> vos = this.groupByStaff(arrangeWorkVOS);
        vos.forEach(p -> p.setDayOfClasses(workService.sortDateOfClasses(p.getDayOfClasses(), param.getMonth(), param.getPeriod())));
        return vos;
    }

    // 排班数据按照用户分组
    private List<ArrangeWorkVO> groupByStaff(List<ArrangeWorkVO> arrangeWorkVOS) {
        List<ArrangeWorkVO> result = new ArrayList<>();
        arrangeWorkVOS.stream().collect(Collectors.groupingBy(ArrangeWorkVO::getStaffId)).forEach((staffId, vos) -> {
            ArrangeWorkVO vo = ObjectUtils.copying(vos.getFirst(), ArrangeWorkVO.class);
            List<ArrangeWorkVO.DateOfClasses> dayOfClasses = new ArrayList<>();
            vos.forEach(classes -> dayOfClasses.addAll(classes.getDayOfClasses()));
            vo.setDayOfClasses(dayOfClasses);
            result.add(vo);
        });
        return result;
    }

    private List<Map<String, String>> formatTemplateData(List<ArrangeWorkVO> arrangeWorkVOS, boolean isArrangeWork) {
        Map<String, String> headerAlias = new LinkedHashMap<>();
        List<Map<String, String>> datas = new ArrayList<>();
        for (int i = 0; i < arrangeWorkVOS.size(); i++) {
            ArrangeWorkVO arrangeWorkVO = arrangeWorkVOS.get(i);
            Map<String, String> data = new LinkedHashMap<>();
            data.put("staffName", arrangeWorkVO.getStaffName());
            if (!isArrangeWork) {
                data.put("staffMobile", arrangeWorkVO.getStaffMobile());
                data.put("deptName", arrangeWorkVO.getDeptName());
                data.put("groupName", arrangeWorkVO.getGroupName());
            }
            if (i == 0) {
                headerAlias.put("staffName", "员工");
                if (!isArrangeWork) {
                    headerAlias.put("staffMobile", "电话");
                    headerAlias.put("deptName", "部门");
                    headerAlias.put("groupName", "考勤组");
                }
            }
            if (!CollectionUtils.isEmpty(arrangeWorkVO.getDayOfClasses())) {
                List<ArrangeWorkVO.DateOfClasses> dayOfClasses = arrangeWorkVO.getDayOfClasses();
                for (int j = 0; j < dayOfClasses.size(); j++) {
                    ArrangeWorkVO.DateOfClasses dateOfClasses = dayOfClasses.get(j);
                    List<Classes> classesVOS = dateOfClasses.getClassesVOS();
                    if (!CollectionUtils.isEmpty(classesVOS)) {
                        //TODO 待优化 取当天的班次，如果是多班次的暂时不回显，属于后续优化
                        String classesName = classesVOS.getFirst().getSummaryName();
                        data.put("dateOfClasses" + j, classesName == null ? "" : classesName);
                    } else {
                        data.put("dateOfClasses" + j, "");
                    }
                    if (i == 0) {
                        int dayOfMonth = dateOfClasses.getDate().getDayOfMonth();
                        String headerName = (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth + "") + "/" +
                                            DateUtils.dayOfWeekFormat(dateOfClasses.getDate());
                        headerAlias.put("dateOfClasses" + j, headerName);
                    }
                }
            }
            datas.add(data);
        }
        //插入表头
        datas.add(headerAlias);
        return datas;
    }

    private List<Map<String, String>> formatExportDataDTO(List<ArrangeWorkVO> arrangeWorkVOS) {
        Map<String, String> headerAlias = new LinkedHashMap<>();
        List<Map<String, String>> datas = new ArrayList<>();
        for (int i = 0; i < arrangeWorkVOS.size(); i++) {
            ArrangeWorkVO arrangeWorkVO = arrangeWorkVOS.get(i);
            Map<String, String> data = new LinkedHashMap<>();
            data.put("staffName", arrangeWorkVO.getStaffName());

            data.put("groupName", arrangeWorkVO.getGroupName());
            data.put("deptName", arrangeWorkVO.getDeptName());

            data.put("arrangeWorkDays", String.valueOf(arrangeWorkVO.getArrangeWorkDays()));
            data.put("realWorkDays", String.valueOf(arrangeWorkVO.getRealWorkDays()));
            data.put("noClockNum", String.valueOf(arrangeWorkVO.getNoClockNum()));
            data.put("repairClockNum", String.valueOf(arrangeWorkVO.getRepairClockNum()));
            data.put("lateNum", String.valueOf(arrangeWorkVO.getLateNum()));
            data.put("lateTotalTime", String.valueOf(arrangeWorkVO.getLateTotalTime()));
            data.put("earlyNum", String.valueOf(arrangeWorkVO.getEarlyNum()));
            data.put("earlyTotalTime", String.valueOf(arrangeWorkVO.getEarlyTotalTime()));
            data.put("skipWorkNum", String.valueOf(arrangeWorkVO.getSkipWorkNum()));
            data.put("overtimeWorkDuration", String.valueOf(arrangeWorkVO.getOvertimeWorkDuration()));
            data.put("overtimeRestDuration", String.valueOf(arrangeWorkVO.getOvertimeRestDuration()));
            data.put("overtimeFestDuration", String.valueOf(arrangeWorkVO.getOvertimeFestDuration()));
            data.put("leavePostDuration", String.valueOf(arrangeWorkVO.getLeavePostDuration()));

            if (i == 0) {
                headerAlias.put("staffName", "员工");
                headerAlias.put("groupName", "考勤组");
                headerAlias.put("deptName", "部门");

                headerAlias.put("arrangeWorkDays", "应出勤天数");
                headerAlias.put("realWorkDays", "实际出勤天数");
                headerAlias.put("noClockNum", "缺卡次数");
                headerAlias.put("repairClockNum", "补卡次数");
                headerAlias.put("lateNum", "迟到次数");
                headerAlias.put("lateTotalTime", "迟到时长(分钟)");
                headerAlias.put("earlyNum", "早退次数");
                headerAlias.put("earlyTotalTime", "早退时长(分钟)");
                headerAlias.put("skipWorkNum", "旷工次数");
                headerAlias.put("overtimeWorkDuration", "工作日加班(小时)");
                headerAlias.put("overtimeRestDuration", "休息日加班(小时)");
                headerAlias.put("overtimeFestDuration", "节假日加班(小时)");
                headerAlias.put("leavePostDuration", "离岗外出时长(小时)");

            }
            if (!CollectionUtils.isEmpty(arrangeWorkVO.getDayOfClasses())) {
                List<ArrangeWorkVO.DateOfClasses> dayOfClasses = arrangeWorkVO.getDayOfClasses();
                for (int j = 0; j < dayOfClasses.size(); j++) {
                    ArrangeWorkVO.DateOfClasses dateOfClasses = dayOfClasses.get(j);
                    List<ClassesDTO> classesVOS = dateOfClasses.getClassesDTOList();
                    if (!CollectionUtils.isEmpty(classesVOS)) {
                        StringBuffer leaveBf = new StringBuffer();
                        if (dateOfClasses.getLeaveDetailDTOS() != null && !dateOfClasses.getLeaveDetailDTOS().isEmpty()) {
                            dateOfClasses.getLeaveDetailDTOS().forEach(leaveDetailDTO -> {
                                List<String> jsonArray = JsonUtils.deserialize(leaveDetailDTO.getLeaveTime().getOriginalTime()
                                                                                       .replaceAll("AM", "上午")
                                                                                       .replaceAll("PM", "下午"), new TypeReference<List<String>>() {});
                                leaveBf.append(Holiday.HolidayType.valueOf(leaveDetailDTO.getLeaveType()).getLabel());
                                if (jsonArray != null) {
                                    jsonArray.forEach(obj -> leaveBf.append(obj).append("\r\n"));
                                }
                            });
                        }
                        String dayOfClassesStr = classesVOS.stream().map(classes -> {
                            StringBuilder classesInfo = new StringBuilder();
                            if (StringUtils.isNotEmpty(classes.getClassesStatus())) {
                                if (ClockStatusEnum.NORMAL.name().equals(classes.getClassesStatus())) {
                                    classesInfo.append("正常");
                                } else if (ClockStatusEnum.EXP_LATE.name().equals(classes.getClassesStatus())) {
                                    classesInfo.append("迟到").append(classes.getStartLateMinute()).append("分钟");
                                } else if (ClockStatusEnum.EXP_LEAVE_EARLY.name().equals(classes.getClassesStatus())) {
                                    classesInfo.append("早退").append(classes.getEndLateMinute()).append("分钟");
                                } else if (ClockStatusEnum.EXP_NOCARD_LEAVE_EARLY.name().equals(classes.getClassesStatus())) {
                                    classesInfo.append("缺卡,").append("早退").append(classes.getEndLateMinute()).append("分钟");
                                } else if (ClockStatusEnum.EXP_LATE_LEAVE_EARLY.name().equals(classes.getClassesStatus())) {
                                    classesInfo.append("迟到").append(classes.getStartLateMinute()).append("分钟,").append("早退")
                                            .append(classes.getEndLateMinute()).append("分钟");
                                } else if (ClockStatusEnum.EXP_LATE_NOCARD.name().equals(classes.getClassesStatus())) {
                                    classesInfo.append("迟到").append(classes.getStartLateMinute()).append("分钟,").append("缺卡");
                                } else {
                                    classesInfo.append(ClockStatusEnum.getValueByCode(classes.getClassesStatus()));
                                }
                            }

                            //String trigger = StringUtils.isNotEmpty(classes.getTrigger()) ? classes.getTrigger(): "";

                            classesInfo.append("\r\n");
                            String startClockTime = StringUtils.isNotEmpty(classes.getStartClockTime()) ? classes.getStartClockTime() : "-";
                            String endClockTime = StringUtils.isNotEmpty(classes.getEndClockTime()) ? classes.getEndClockTime() : "-";
                            classesInfo.append(startClockTime).append("-").append(endClockTime);
                            return classesInfo.toString();
                        }).collect(Collectors.joining("\r\n"));
                        data.put("dateOfClasses" + j, leaveBf + dayOfClassesStr);
                    } else {
                        data.put("dateOfClasses" + j, "休息");
                    }
                    if (i == 0) {
                        int dayOfMonth = dateOfClasses.getDate().getDayOfMonth();
                        String headerName = (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth + "") + "/" +
                                            DateUtils.dayOfWeekFormat(dateOfClasses.getDate());
                        headerAlias.put("dateOfClasses" + j, headerName);
                    }
                }
            }
            datas.add(data);
        }
        //插入表头
        datas.add(headerAlias);
        return datas;
    }

    private List<Map<String, String>> formatExportData(List<ArrangeWorkVO> arrangeWorkVOS, boolean isArrangeWork) {
        Map<String, String> headerAlias = new LinkedHashMap<>();
        List<Map<String, String>> datas = new ArrayList<>();
        for (int i = 0; i < arrangeWorkVOS.size(); i++) {
            ArrangeWorkVO arrangeWorkVO = arrangeWorkVOS.get(i);
            Map<String, String> data = new LinkedHashMap<>();
            data.put("staffName", arrangeWorkVO.getStaffName());
            if (!isArrangeWork) {
                data.put("groupName", arrangeWorkVO.getGroupName());
                data.put("deptName", arrangeWorkVO.getDeptName());

                data.put("arrangeWorkDays", String.valueOf(arrangeWorkVO.getArrangeWorkDays()));
                data.put("realWorkDays", String.valueOf(arrangeWorkVO.getRealWorkDays()));
                data.put("noClockNum", String.valueOf(arrangeWorkVO.getNoClockNum()));
                data.put("repairClockNum", String.valueOf(arrangeWorkVO.getRepairClockNum()));
                data.put("lateNum", String.valueOf(arrangeWorkVO.getLateNum()));
                data.put("lateTotalTime", String.valueOf(arrangeWorkVO.getLateTotalTime()));
                data.put("earlyNum", String.valueOf(arrangeWorkVO.getEarlyNum()));
                data.put("earlyTotalTime", String.valueOf(arrangeWorkVO.getEarlyTotalTime()));
                data.put("skipWorkNum", String.valueOf(arrangeWorkVO.getSkipWorkNum()));
                data.put("overtimeWorkDuration", String.valueOf(arrangeWorkVO.getOvertimeWorkDuration()));
                data.put("overtimeRestDuration", String.valueOf(arrangeWorkVO.getOvertimeRestDuration()));
                data.put("overtimeFestDuration", String.valueOf(arrangeWorkVO.getOvertimeFestDuration()));
                data.put("leavePostDuration", String.valueOf(arrangeWorkVO.getLeavePostDuration()));
            }
            if (i == 0) {
                headerAlias.put("staffName", "员工");
                if (!isArrangeWork) {
                    headerAlias.put("groupName", "考勤组");
                    headerAlias.put("deptName", "部门");

                    headerAlias.put("arrangeWorkDays", "应出勤天数");
                    headerAlias.put("realWorkDays", "实际出勤天数");
                    headerAlias.put("noClockNum", "缺卡次数");
                    headerAlias.put("repairClockNum", "补卡次数");
                    headerAlias.put("lateNum", "迟到次数");
                    headerAlias.put("lateTotalTime", "迟到时长(分钟)");
                    headerAlias.put("earlyNum", "早退次数");
                    headerAlias.put("earlyTotalTime", "早退时长(分钟)");
                    headerAlias.put("skipWorkNum", "旷工次数");
                    headerAlias.put("overtimeWorkDuration", "工作日加班(小时)");
                    headerAlias.put("overtimeRestDuration", "休息日加班(小时)");
                    headerAlias.put("overtimeFestDuration", "节假日加班(小时)");
                    headerAlias.put("leavePostDuration", "离岗外出时长(小时)");
                }
            }
            if (!CollectionUtils.isEmpty(arrangeWorkVO.getDayOfClasses())) {
                List<ArrangeWorkVO.DateOfClasses> dayOfClasses = arrangeWorkVO.getDayOfClasses();
                for (int j = 0; j < dayOfClasses.size(); j++) {
                    ArrangeWorkVO.DateOfClasses dateOfClasses = dayOfClasses.get(j);
                    List<Classes> classesVOS = dateOfClasses.getClassesVOS();
                    if (!CollectionUtils.isEmpty(classesVOS)) {
                        String dayOfClassesStr = classesVOS.stream().map(classes -> {
                            StringBuilder classesInfo = new StringBuilder();
                            if (isArrangeWork) {
                                classesInfo.append(classes.getName());
                            } else {
                                classesInfo.append(StringUtils.isNotEmpty(classes.getClassesStatus()) ? classes.getClassesStatus() : "");
                                classesInfo.append(StringUtils.isNotEmpty(classes.getTrigger()) ? "," + classes.getTrigger() : "");
                            }
                            classesInfo.append("\r\n");
                            classesInfo.append(classes.getStartWorkTime()).append("-").append(classes.getEndWorkTime());
                            return classesInfo.toString();
                        }).collect(Collectors.joining("\r\n"));
                        data.put("dateOfClasses" + j, dayOfClassesStr);
                    } else {
                        data.put("dateOfClasses" + j, "休息");
                    }
                    if (i == 0) {
                        int dayOfMonth = dateOfClasses.getDate().getDayOfMonth();
                        String headerName = (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth + "") + "/" +
                                            DateUtils.dayOfWeekFormat(dateOfClasses.getDate());
                        headerAlias.put("dateOfClasses" + j, headerName);
                    }
                }
            }
            datas.add(data);
        }
        //插入表头
        datas.add(headerAlias);
        return datas;
    }

    /**
     * 列表查询
     *
     * @param param
     *
     * @return
     */
    public List<ArrangeWorkVO> getList(ArrangeWorkQueryParam param) {
        List<ArrangeWorkVO> arrangeWorkVOS = arrangeWorkMapper.getList(param);
        List<ArrangeWorkVO> vos = this.groupByStaff(arrangeWorkVOS);
        vos.forEach(p -> p.setDayOfClasses(workService.sortDateOfClasses(p.getDayOfClasses(), param.getMonth(), param.getPeriod())));
        return vos;
    }

    /**
     * 当前用户班次信息 叠加考勤记录
     *
     * @param param
     *
     * @return
     */
    public List<ArrangeWorkVO.DateOfClasses> getCurrentUserList(ArrangeWorkQueryParam param) {
        param.setIsCurrentUser(true);
        List<ArrangeWorkPO> arrangeWorkPOS = this.getQueryChainWrapper(param).list();
        return this.extendV3(arrangeWorkPOS, param);
    }

    /**
     * 获取员工指定时间段内的非上班时长（中休+班次外）
     * 工作日计算是需要查请假时间段内的班次信息。除了需要减去休息时间，还需减去非上班时间，只计算包含班次的时间
     *
     * @param staffId 员工ID
     * @param period  请假时间段
     *
     * @return 非上班时长（单位：小时）
     */
    public float getStaffNotClassesTime(@NonNull Long staffId, @NonNull Period period) {
        LocalDateTime begin = period.getBegin();
        LocalDateTime end = period.getEnd();
        if (begin == null || end == null) {
            return 0f;
        }
        List<ArrangeWorkClassesVO> userClassesInPeriod = arrangeWorkClassesMapper.getUserClassesInPeriod(staffId, period);
        if (CollectionUtils.isEmpty(userClassesInPeriod)) {
            return this.betweenHourByDateTime(period.getBegin(), period.getEnd());
        }
        AtomicReference<Long> lAtomic = new AtomicReference<>(0L);
        ArrangeWorkClassesVO firstArrangeWorkClassesVO = userClassesInPeriod.getFirst();
        if (DateUtils.compare(firstArrangeWorkClassesVO.getStartRealTime(), begin) > 0) {
            lAtomic.updateAndGet(v -> v + Math.abs(DateUtils.betweenMs(firstArrangeWorkClassesVO.getStartRealTime(), begin)));
        }
        ArrangeWorkClassesVO lastArrangeWorkClassesVO = userClassesInPeriod.getLast();
        if (DateUtils.compare(lastArrangeWorkClassesVO.getEndRealTime(), end) < 0) {
            lAtomic.updateAndGet(v -> v + Math.abs(DateUtils.betweenMs(end, lastArrangeWorkClassesVO.getEndRealTime())));
        }
        for (int i = 0; i < userClassesInPeriod.size(); i++) {
            ArrangeWorkClassesVO c = userClassesInPeriod.get(i);
            LocalDateTime endRealTime = c.getEndRealTime();
            LocalDateTime restStartRealTime = c.getRestStartRealTime();
            LocalDateTime restEndRealTime = c.getRestEndRealTime();
            if (restStartRealTime != null && restEndRealTime != null) {
                float restTime = this.betweenHourByDateTime(
                        DateUtils.compare(begin, restStartRealTime) > 0 ? begin : restStartRealTime,
                        DateUtils.compare(end, restEndRealTime) < 0 ? end : restEndRealTime);
                Long t1 = DateUtils.compare(end, restEndRealTime) < 0 ? DateUtils.toTimestamp(end) : DateUtils.toTimestamp(restEndRealTime);
                Long t2 = DateUtils.compare(begin, restStartRealTime) > 0 ? DateUtils.toTimestamp(begin)
                                                                          : DateUtils.toTimestamp(restStartRealTime);
                Long t3 = t2 - t1;
                if (restTime > 0) {
                    lAtomic.updateAndGet(v -> v + t3);
                }
            }
            if (i != userClassesInPeriod.size() - 1) {
                Long lt = DateUtils.betweenMs(endRealTime, userClassesInPeriod.get(i + 1).getStartRealTime());
                lAtomic.updateAndGet(v -> v + lt);

            }
        }
        double db = lAtomic.get().doubleValue() / (1000 * 60 * 60);
        BigDecimal bigDecimal = BigDecimal.valueOf(db);
        return bigDecimal.setScale(0, RoundingMode.DOWN).floatValue();
    }

    /**
     * 获取员工指定时间范围内的非上班时间天数
     *
     * @param staffId
     * @param period
     *
     * @return 非上班天数（单位：天数）
     */
    public float getStaffNotClassesDay(@NonNull Long staffId, @NonNull Period period, LeaveFormDetail.LeaveTimeUnit leaveTimeUnit) {
        if (LeaveFormDetail.LeaveTimeUnit.DAY != leaveTimeUnit) {
            List<ArrangeWorkClassesVO> userClassesInPeriod = arrangeWorkClassesMapper.getUserClassesInPeriod(staffId, period);
            return CollectionUtils.isEmpty(userClassesInPeriod) ? 1f : 0f;
        } else {
            LocalDateTime begin = period.getBegin();
            LocalDateTime end = DateUtils.endOfDay(period.getEnd());
            int betweenDay = (int) DateUtils.between(begin, end, ChronoUnit.DAYS, true) + 1;
            int count = 0;
            for (int i = 0; i < betweenDay; i++) {
                end = DateUtils.endOfDay(begin);
                if (i != 0) {
                    begin = DateUtils.offsetDay(begin, 1);
                    end = DateUtils.offsetDay(end, 1);
                }
                Period period1 = new Period(begin, end);
                List<ArrangeWorkClassesVO> userClassesInPeriod = arrangeWorkClassesMapper.getUserClassesInPeriod(staffId, period1);
                if (CollectionUtils.isEmpty(userClassesInPeriod)) {
                    count++;
                }
            }
            return count;
        }
    }

    /**
     * 查询当前用户当前时间所在的班次
     * 2023-07-05已处理：??这里有个问题就是前一个班次的下班卡和下一个班次的上班卡在同一天，当前时间处于下班卡和上班卡之间时，现在查询无当前进行班次，那么直接查询当前时间之后的班次，这样就成了打下个班次的上班卡，打不了下班卡了
     *
     * @return
     */
    public CurrentClassesVO getUserClassesCurrentDate(Long staffId) {
        Long userId = staffId == null ? SecurityContextHolder.getPrincipal().getId() : staffId;
        LocalDateTime date = LocalDateTime.now();
        CurrentClassesVO userClassesCurrentDate = arrangeWorkClassesMapper.getUserClassesInCurrentDate(userId, null, date);
        // 如果没有当前正在进行的班次，就查询当前时间之后开始的班次
        if (null == userClassesCurrentDate) {
            // 之前的班次
            CurrentClassesVO befClasses = null;
            List<CurrentClassesVO> userClassesBeforeCurrentDate = arrangeWorkClassesMapper.getUserClassesBeforeCurrentDateNotBeforeTimeWhere(userId, null, date);
            if (!CollectionUtils.isEmpty(userClassesBeforeCurrentDate)) {
                befClasses = userClassesBeforeCurrentDate.getFirst();
            }
            // 之后的班次
            CurrentClassesVO afterClasses = null;
            List<CurrentClassesVO> userClassesAfterCurrentDate = arrangeWorkClassesMapper.getUserClassesAfterCurrentDate(userId, null, date);
            if (!CollectionUtils.isEmpty(userClassesAfterCurrentDate)) {
                afterClasses = userClassesAfterCurrentDate.getFirst();
            }
            if (befClasses == null) {
                userClassesCurrentDate = afterClasses;
            } else if (afterClasses == null) {
                userClassesCurrentDate = befClasses;
            } else {
                // 如果配置了结束打卡的结束时间，和开始打卡的开始时间就以该时间为准。否则就取中间时间
                long newLong = DateUtils.toTimestamp(LocalDateTime.now());
                if (befClasses.getAfterEndRealTime() != null && afterClasses.getBeforeStartRealTime() != null) {
                    if (newLong < DateUtils.toTimestamp(befClasses.getAfterEndRealTime())) {
                        userClassesCurrentDate = befClasses;
                    } else if (newLong > DateUtils.toTimestamp(afterClasses.getBeforeStartRealTime())) {
                        userClassesCurrentDate = afterClasses;
                    }
                } else if (befClasses.getAfterEndRealTime() == null && afterClasses.getBeforeStartRealTime() == null) {
                    // 取中间值
                    long l = (DateUtils.toTimestamp(afterClasses.getStartRealTime()) - DateUtils.toTimestamp(befClasses.getEndRealTime())) /
                             2;
                    long centerDateLong = DateUtils.toTimestamp(befClasses.getEndRealTime()) + l;
                    if (newLong <= centerDateLong) {
                        userClassesCurrentDate = befClasses;
                    } else {
                        userClassesCurrentDate = afterClasses;
                    }
                } else {
                    if (befClasses.getAfterEndRealTime() != null && newLong < DateUtils.toTimestamp(befClasses.getAfterEndRealTime())) {
                        userClassesCurrentDate = afterClasses;
                    } else if (afterClasses.getBeforeStartRealTime() != null &&
                               newLong > DateUtils.toTimestamp(afterClasses.getBeforeStartRealTime())) {
                        userClassesCurrentDate = befClasses;
                    }
                }
            }
        }
        this.parseAppJumpWhere(userClassesCurrentDate);
        return userClassesCurrentDate;
    }

    /**
     * 查询今日当班人员（可视化使用）
     *
     * @return
     */
    public ArrangeWorkTodayStatVO getToadyStaff() {
        ArrangeWorkTodayStatVO statVO = new ArrangeWorkTodayStatVO();
        // 查询今日当班人员（人员的班次时间在今日之内的）
        LocalDateTime date = LocalDateTime.now();
        List<ArrangeWorkTodayStatVO.Staff> staffList = arrangeWorkClassesMapper.getToadyStaff(DateUtils.formatDate(date), DateUtils.beginOfDay(date), DateUtils.endOfDay(date));
        if (CollectionUtils.isEmpty(staffList)) {
            return statVO;
        }
        // 计算在岗/总数
        int onDutyCount = (int) staffList.stream().filter(ArrangeWorkTodayStatVO.Staff::getOnDuty).count();
        String staffNum = onDutyCount + "/" + staffList.size();
        statVO.setStatNum(staffNum);
        statVO.setStaffList(staffList);
        return statVO;
    }

    public List<Long> filterUserInClasses(List<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return Collections.emptyList();
        }
        return clockQueryService.filterUserInClasses(userIds);
    }

    private void parseAppJumpWhere(CurrentClassesVO userClassesCurrentDate) {
        // 背景：在app端用户点击考勤打卡，查询当日的班次和打卡方式，如果是gps和wifi则在手机端打卡，如果是考勤机类型打卡方式，则不打卡
        // 当打卡方式含有wifi或者gps时，跳打卡
        // 当打卡方式只有考勤机或一种方式都没有是，跳记录
        if (null == userClassesCurrentDate) {
            return;
        }
        List<CurrentClassesVO.Way> wayList = userClassesCurrentDate.getWayList();
        if (CollectionUtils.isEmpty(wayList) ||
            wayList.stream().allMatch(w -> AttendanceGroupPunchCardWayEnum.MACHINE.name().equals(w.getWay()))) {
            userClassesCurrentDate.setAppJumpWhere(JUMP_RECORD);
        } else {
            userClassesCurrentDate.setAppJumpWhere(JUMP_CLOCK);
        }
    }

    private LambdaQueryChainWrapper<ArrangeWorkPO> getQueryChainWrapper(ArrangeWorkQueryParam param) {
        if (param.getIsCurrentUser() != null && param.getIsCurrentUser()) {
            return this.lambdaQuery()
                    .like(StringUtils.isNotEmpty(param.getKeyword()), ArrangeWorkPO::getStaffName, param.getKeyword())
                    .eq(null != param.getStaffId(), ArrangeWorkPO::getStaffId, param.getStaffId())
                    .eq(null != param.getGroupId(), ArrangeWorkPO::getGroupId, param.getGroupId())
                    .eq(StringUtils.isNotEmpty(param.getMonth()), ArrangeWorkPO::getMonth, param.getMonth())
                    .and(null != param.getPeriod(), q -> q
                            .between(null != param.getPeriod().getBegin() &&
                                     null != param.getPeriod().getEnd(), ArrangeWorkPO::getDate, param.getPeriod().getBegin(), param
                                             .getPeriod().getEnd())
                        )
                    .eq(null != param.getOrganizationId(), ArrangeWorkPO::getStaffOrganizationId, param.getOrganizationId())
                    .eq(param.getIsCurrentUser() != null && param.getIsCurrentUser(), ArrangeWorkPO::getStaffId, SecurityContextHolder
                            .getPrincipal().getId())
                    ;
        }
        return this.lambdaQuery()
                .like(StringUtils.isNotEmpty(param.getKeyword()), ArrangeWorkPO::getStaffName, param.getKeyword())
                .eq(null != param.getStaffId(), ArrangeWorkPO::getStaffId, param.getStaffId())
                .eq(null != param.getGroupId(), ArrangeWorkPO::getGroupId, param.getGroupId())
                .eq(StringUtils.isNotEmpty(param.getMonth()), ArrangeWorkPO::getMonth, param.getMonth())
                .and(null != param.getPeriod(), q -> q
                        .between(null != param.getPeriod().getBegin() && null != param.getPeriod().getEnd(), ArrangeWorkPO::getDate, param
                                .getPeriod().getBegin(), param.getPeriod().getEnd())
                    )
                .eq(null != param.getOrganizationId(), ArrangeWorkPO::getStaffOrganizationId, param.getOrganizationId())
                ;
    }

    // 计算两个时间相差的小时数
    private float betweenHourByDateTime(LocalDateTime date1, LocalDateTime date2) {
        long hourly = DateUtils.between(date1, date2, ChronoUnit.HOURS);
        return Float.parseFloat(String.valueOf(hourly));
    }

    private List<ArrangeWorkVO> extend(List<ArrangeWorkPO> records, ArrangeWorkQueryParam param) {
        List<ArrangeWorkVO> arrangeWorkVOS = new ArrayList<>();
        records.stream().collect(Collectors.groupingBy(ArrangeWorkPO::getStaffId)).forEach((staffId, pos) -> {
            if (CollectionUtils.isEmpty(pos)) {
                return;
            }
            ArrangeWorkVO workVO = ObjectUtils.copying(pos.getFirst(), ArrangeWorkVO.class);
            List<ArrangeWorkVO.DateOfClasses> dateOfClasses = new ArrayList<>();
            pos.forEach(po -> {
                ArrangeWorkVO.DateOfClasses date = ObjectUtils.copying(po, ArrangeWorkVO.DateOfClasses.class);
                String arrangeWorkClassesIds = po.getArrangeWorkClassesIds();
                if (StringUtils.isNotEmpty(arrangeWorkClassesIds)) {
                    List<String> classesIds = Arrays.asList(arrangeWorkClassesIds.split(","));
                    List<ArrangeWorkClassesPO> arrangeWorkClassesPOS = arrangeWorkClassesMapper.selectList(Wrappers
                                                                                                                   .<ArrangeWorkClassesPO>lambdaQuery()
                                                                                                                   .in(ArrangeWorkClassesPO::getId, classesIds));
                    List<Classes> classesList = ObjectUtils.copying(arrangeWorkClassesPOS, Classes.class);
                    classesList.sort(Comparator.comparing(c -> DateUtils.parse(c.getStartWorkTime(), "HH:mm")));
                    date.setClassesVOS(classesList);
                }
                dateOfClasses.add(date);
            });
            workVO.setDayOfClasses(workService.sortDateOfClasses(dateOfClasses, param.getMonth(), param.getPeriod()));
            arrangeWorkVOS.add(workVO);
        });
        return arrangeWorkVOS;
    }

    private List<ArrangeWorkVO.DateOfClasses> extendV3(List<ArrangeWorkPO> records, ArrangeWorkQueryParam param) {
        List<ArrangeWorkVO.DateOfClasses> dateOfClasses = new ArrayList<>();
        records.forEach(record -> {
            ArrangeWorkVO.DateOfClasses day = ObjectUtils.copying(record, ArrangeWorkVO.DateOfClasses.class);
            String arrangeWorkClassesIds = record.getArrangeWorkClassesIds();
            if (StringUtils.isNotEmpty(arrangeWorkClassesIds)) {
                List<String> classesIds = Arrays.asList(arrangeWorkClassesIds.split(","));

                this.getWorkClassesStatus(record.getId(), day, classesIds, record.getStaffId());
            }
            // 今日及之后,有班次的给与一个标识
            if (DateUtils.compare(day.getDate(), DateUtils.parseDate(DateUtils.formatDate(LocalDateTime.now()))) >= 0) {
                day.setClockStatus((StringUtils.isNotEmpty(arrangeWorkClassesIds) && StringUtils.isEmpty(day.getClockStatus())) ? "NO_CARD"
                                                                                                                                : day.getClockStatus());
            }
            dateOfClasses.add(day);
        });
        return workService.sortDateOfClasses(dateOfClasses, param.getMonth(), param.getPeriod());
    }

    private List<ClassesDTO> getWorkClassesDTOStatus(Long workId, List<String> classesIds, List<ArrangeWorkClassesPO> classesList) {
        if (CollectionUtils.isEmpty(classesIds)) {
            return ObjectUtils.copying(classesList, ClassesDTO.class);
        }
        //查询班次的状态
        List<ClassesClockPO> classesStatusList = classesClockMapper.selectList(Wrappers.<ClassesClockPO>lambdaQuery()
                                                                                       .eq(ClassesClockPO::getWorkId, workId)
                                                                                       .in(ClassesClockPO::getArrangeClassesId, classesIds));
        // 上班打卡时间
        // 下班打卡时间
        // 处理上班触发状态
        // 补卡标记
        // 离岗申请
        // 下班触发状态
        // 补卡标记
        // 离岗申请
        // 迟到缺卡
        // 迟到早退
        // 缺卡早退
        return classesList.stream().map(classes -> {
            ClassesDTO dto = new ClassesDTO();
            ObjectUtils.copying(classes, dto);
            ClassesClockPO status = classesStatusList.stream()
                    .filter(s -> classes.getId().longValue() == s.getArrangeClassesId().longValue())
                    .findAny().orElse(null);

            if (status != null) {

                dto.setClassesStatus(status.getClassesClockStatus());
                dto.setStartClockStatus(status.getStartWorkClockStatus());
                dto.setStartClockTime(
                        status.getStartWorkClockTime() != null ? DateUtils.format(status.getStartWorkClockTime(), "HH:mm:ss") : "");
                dto.setEndClockStatus(status.getEndWorkClockStatus());
                dto.setEndClockTime(
                        status.getEndWorkClockTime() != null ? DateUtils.format(status.getEndWorkClockTime(), "HH:mm:ss") : "");
                dto.setStartTrigger(status.getStartTrigger());
                dto.setEndTrigger(status.getEndTrigger());
                if (status.getStartWorkClockTime() != null) {
                    // 上班打卡时间
                    dto.setStartClockTime(DateUtils.format(status.getStartWorkClockTime(), "HH:mm:ss"));
                    long lateTime = DateUtils.betweenMs(status.getStartRealTime(), status.getStartWorkClockTime());
                    if (lateTime > 0) {
                        dto.setStartClockStatus(ClockStatusEnum.EXP_LATE.name());
                        dto.setStartLateMinute(lateTime / (1000 * 60));
                    }
                }

                if (status.getEndWorkClockTime() != null) {
                    // 下班打卡时间
                    dto.setEndClockTime(DateUtils.format(status.getEndWorkClockTime(), "HH:mm:ss"));
                    long lateTime = DateUtils.betweenMs(status.getEndWorkClockTime(), status.getEndRealTime());
                    if (lateTime > 0) {
                        dto.setEndClockStatus(ClockStatusEnum.EXP_LEAVE_EARLY.name());
                        dto.setEndLateMinute(lateTime / (1000 * 60));
                    }
                }

                // 处理上班触发状态
                if (StringUtils.isNotEmpty(status.getStartTrigger())) {
                    dto.setTrigger(ClassesStatusTriggerEnum.valueOf(status.getStartTrigger()).getDesc());
                    // 补卡标记
                    if (ClassesStatusTriggerEnum.REPAIR_CLOCK.name().equals(status.getStartTrigger())) {
                        dto.setStartRepairFlag(true);
                        dto.setStartClockTime(ClassesStatusTriggerEnum.REPAIR_CLOCK.getDesc());
                        dto.setStartClockStatus(ClockStatusEnum.NORMAL.name());
                    }
                    // 离岗申请
                    if (ClassesStatusTriggerEnum.LEAVE_THE_POST.toString().equals(status.getStartTrigger())) {
                        dto.setStartClockStatus(ClockStatusEnum.NORMAL.name());
                    }
                }

                // 下班触发状态
                if (StringUtils.isNotEmpty(status.getEndTrigger())) {
                    String endTrigger = ClassesStatusTriggerEnum.valueOf(status.getEndTrigger()).getDesc();
                    dto.setTrigger(dto.getTrigger() == null ? endTrigger : dto.getTrigger() + "," + endTrigger);
                    // 补卡标记
                    if (ClassesStatusTriggerEnum.REPAIR_CLOCK.name().equals(status.getEndTrigger())) {
                        dto.setEndRepairFlag(true);
                        dto.setEndClockTime(ClassesStatusTriggerEnum.REPAIR_CLOCK.getDesc());
                    }
                    // 离岗申请
                    if (ClassesStatusTriggerEnum.LEAVE_THE_POST.name().equals(status.getEndTrigger())) {
                        dto.setEndClockStatus(ClockStatusEnum.NORMAL.name());
                    }

                }

                if (ClockStatusEnum.EXP_LATE.name().equals(dto.getStartClockStatus()) &&
                    ClockStatusEnum.EXP_NOCARD.name().equals(dto.getEndClockStatus())) {
                    // 迟到缺卡
                    dto.setClassesStatus(ClockStatusEnum.EXP_LATE_NOCARD.name());
                } else if (ClockStatusEnum.EXP_LATE.name().equals(dto.getStartClockStatus()) &&
                           ClockStatusEnum.EXP_LEAVE_EARLY.name().equals(dto.getEndClockStatus())) {
                    // 迟到早退
                    dto.setClassesStatus(ClockStatusEnum.EXP_LATE_LEAVE_EARLY.name());
                } else if (ClockStatusEnum.EXP_NOCARD.name().equals(dto.getStartClockStatus()) &&
                           ClockStatusEnum.EXP_LEAVE_EARLY.name().equals(dto.getEndClockStatus())) {
                    // 缺卡早退
                    dto.setClassesStatus(ClockStatusEnum.EXP_NOCARD_LEAVE_EARLY.name());
                } else {
                    dto.setClassesStatus(status.getClassesClockStatus());
                }

            }

            return dto;
        }).collect(Collectors.toList());
    }

    private void getWorkClassesStatus(Long workId, ArrangeWorkVO.DateOfClasses day, List<String> classesIds, Long staffId) {
        if (CollectionUtils.isEmpty(classesIds)) {
            return;
        }
        //查询班次的状态
        List<ClassesClockPO> classesClockList = classesClockMapper.selectList(Wrappers.<ClassesClockPO>lambdaQuery()
                                                                                      .eq(ClassesClockPO::getWorkId, workId));
        if (CollectionUtils.isEmpty(classesClockList)) {
            day.setClockStatus("");
            day.setTrigger("");
        } else {
            // 全部打卡状态都未正常时，整体为正常
            day.setClockStatus(classesClockList.stream().allMatch(cs -> ClockStatusEnum.NORMAL.name().equals(cs.getClassesClockStatus()))
                               ? ClockStatusEnum.NORMAL.name() : "ABNORMAL");
            // 只要上班或者下班有任意一个审批，整体审批就有值
            boolean startTrigger = classesClockList.stream().anyMatch(cs -> StringUtils.isNotEmpty(cs.getStartTrigger()));
            boolean endTrigger = classesClockList.stream().anyMatch(cs -> StringUtils.isNotEmpty(cs.getEndTrigger()));
            day.setTrigger(startTrigger || endTrigger ? "ABNORMAL" : "");
        }
        if (StringUtils.isNotEmpty(day.getTrigger())) {
            return;
        }
        /**
         * 查询申请信息
         */
        StringBuffer triggerBuffer = new StringBuffer();
        String cd = DateUtils.formatDate(day.getDate());
        // 查询请假
        List<LeaveFormPO> leaveFormList = leaveFormMapper.selectList(Wrappers.<LeaveFormPO>lambdaQuery()
                                                                             .eq(LeaveFormPO::getStatus, LeaveStatus.PASS.name())
                                                                             .and(i -> i.or().like(LeaveFormPO::getLeaveStartTime, cd)
                                                                                     .or().like(LeaveFormPO::getLeaveEndTime, cd)
                                                                                 )
                                                                    );
        if (leaveFormList != null && !leaveFormList.isEmpty()) {
            leaveFormList.stream().filter(leaveFormPO -> {
                LeaveForm leaveForm = JsonUtils.deserialize(leaveFormPO.getLeaveFormDetail(), LeaveForm.class);
                return staffId.equals(leaveForm.getDetail().getUserId());
            }).findAny().ifPresent(p -> triggerBuffer.append(",").append(ClassesStatusTriggerEnum.LEAVE.name()));
        }
        // 查询离岗
        List<LeavePostSituationPO> leavePostSituationList = leavePostSituationMapper.selectList(Wrappers.<LeavePostSituationPO>lambdaQuery()
                                                                                                        .eq(LeavePostSituationPO::getApplicantId, staffId)
                                                                                                        .and(i -> i.or()
                                                                                                                .like(LeavePostSituationPO::getStartTime, cd)
                                                                                                                .or()
                                                                                                                .like(LeavePostSituationPO::getEndTime, cd)
                                                                                                            )
                                                                                               );
        if (leavePostSituationList != null && !leavePostSituationList.isEmpty()) {
            triggerBuffer.append(",").append(ClassesStatusTriggerEnum.LEAVE_THE_POST.name());
        }
        // 查询加班
        List<WorkOvertimeSituationPO> workOvertimeSituationList = workOvertimeSituationMapper.selectList(Wrappers
                                                                                                                 .<WorkOvertimeSituationPO>lambdaQuery()
                                                                                                                 .eq(WorkOvertimeSituationPO::getApplicantId, staffId)
                                                                                                                 .eq(WorkOvertimeSituationPO::getWorkDate, cd)
                                                                                                        );
        if (workOvertimeSituationList != null && !workOvertimeSituationList.isEmpty()) {
            triggerBuffer.append(",").append(ClassesStatusTriggerEnum.OVERTIME.name());
        }
        if (StringUtils.isNotEmpty(triggerBuffer)) {
            String str = triggerBuffer.substring(1);
            day.setTrigger(str);
        }
    }

    /////////////////////////1.5
    /**
     * 获取员工指定时间范围内的非上班时间天数
     *
     * @param staffId
     * @param period
     *
     * @return 非上班天数（单位：天数）
     */
    public float getStaffNotWorkDay(@NonNull Long staffId, @NonNull Period period, LeaveFormDetail.LeaveTimeUnit leaveTimeUnit) {
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
        List<AttSchedulingPO> schedulingPOList = attSchedulingQueryService.getStaffPeriodScheduling(staffId, beginTime, endTime, legalHolidaysRest, ruleClassesPOList);
        if (LeaveFormDetail.LeaveTimeUnit.DAY != leaveTimeUnit) {
            if (schedulingPOList == null || schedulingPOList.isEmpty()) {
                return 1F;
            }
            Map<Long, AttClassesPO> classesPOMap = new HashMap<>();
            List<Long> classIdList = schedulingPOList.stream().map(AttSchedulingPO::getClassesId).distinct().collect(Collectors.toList());
            if (!classIdList.isEmpty()) {
                List<AttClassesPO> classesPOList = attClassesMapper.selectBatchIds(classIdList);
                if (classesPOList != null && !classesPOList.isEmpty()) {
                    classesPOMap = classesPOList.stream().collect(Collectors.toMap(AttClassesPO::getId, v -> v));
                }
            }

            for (AttSchedulingPO poi : schedulingPOList) {
                if (poi.getClassesId() == null) {
                    continue;
                }
                if (poi.getClassesRealStartTime() == null) {
                    attSchedulingQueryService.stringClassToScheduling(poi, classesPOMap.get(poi.getClassesId()));
                }
                if (poi.getClassesRealStartTime() != null && !poi.getClassesRealStartTime().isAfter(period.getEnd()) &&
                    !poi.getClassesRealEndTime().isBefore(period.getBegin())) {
                    return 0F;
                }
            }
            return 1F;
        } else {
            int count = (int) DateUtils.between(period.getBegin(), period.getEnd(), ChronoUnit.DAYS, true) + 1;
            if (schedulingPOList == null || schedulingPOList.isEmpty()) {
                return Math.max(count, 0);
            }
            Map<String, List<AttSchedulingPO>> schedulingMap = schedulingPOList.stream()
                    .collect(Collectors.groupingBy(p -> DateUtils.formatDate(p.getSchedulingDate())));
            LocalDateTime printTime = DateUtils.newLocalDateTime(period.getBegin());
            do {
                List<AttSchedulingPO> tempList = schedulingMap.get(DateUtils.formatDate(printTime));
                if (tempList != null && !tempList.isEmpty() && tempList.getFirst().getClassesId() != null) {
                    count--;
                }
                printTime = DateUtils.offsetDay(printTime, 1);
            } while (printTime.isBefore(period.getEnd()));
            return Math.max(count, 0);
        }
    }

    /**
     * 获取指定时间段员工的非上班时间
     *
     * @param staffId
     * @param period
     *
     * @return
     */
    public long getStaffNotWorkTime(@NonNull Long staffId, @NonNull Period period) {
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
        List<AttSchedulingPO> schedulingPOList = attSchedulingQueryService.getStaffPeriodScheduling(staffId, DateUtils.offsetDay(beginTime, -1), endTime, legalHolidaysRest, ruleClassesPOList);
        if (schedulingPOList == null || schedulingPOList.isEmpty()) {
            return DateUtils.betweenMs(period.getBegin(), period.getEnd());
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
                attSchedulingQueryService.stringClassToScheduling(poi, classesPOMap.get(poi.getClassesId()));
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
            return DateUtils.betweenMs(period.getBegin(), period.getEnd());
        }

        long restTime = 0L;
        int size = calcList.size();
        if (calcList.getFirst().getClassesRealStartTime().isAfter(period.getBegin())) {
            long bs = DateUtils.betweenMs(period.getBegin(), calcList.getFirst().getClassesRealStartTime());
            if (bs > 0) {
                // 请假开始时间与第一个班次开始时间之间的时间
                restTime += bs;
            }
        }
        if (calcList.get(size - 1).getClassesRealEndTime().isBefore(period.getEnd())) {
            long ae = DateUtils.betweenMs(calcList.get(size - 1).getClassesRealEndTime(), period.getEnd());
            if (ae > 0) {
                // 请假结束时间与最后一个班次结束时间之间的时间
                restTime += ae;
            }
        }
        for (int i = 0; i < size; i++) {
            AttSchedulingPO attSchedulingPO = calcList.get(i);
            if (i < size - 1) {
                AttSchedulingPO nextPO = calcList.get(i + 1);
                long r = DateUtils.betweenMs(attSchedulingPO.getClassesRealEndTime(), nextPO.getClassesRealStartTime());
                if (r > 0) {
                    // 前后班次间隔时间
                    restTime += r;
                }
            }

            if (attSchedulingPO.getClassesRestStartTime() != null && attSchedulingPO.getClassesRestEndTime() != null) {
                // 班次中间的休息时间
                restTime += DateUtils.betweenMs(attSchedulingPO.getClassesRestStartTime(), attSchedulingPO.getClassesRestEndTime());
            }
        }
        return restTime;
    }
}
