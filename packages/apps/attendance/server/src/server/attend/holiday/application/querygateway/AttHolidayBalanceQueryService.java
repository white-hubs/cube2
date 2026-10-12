package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.application.querygateway;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.style.StyleUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.AttConstants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.domain.repository.AttGroupRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttGroupStaffPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.param.AttGroupStaffQueryParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.AttHolidayDomain;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.enums.AttHolidayGrantWayEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.mapper.AttHolidayBalanceMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.mapper.AttHolidayBalanceRecordMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.mapper.AttHolidayRuleMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.po.AttHolidayBalancePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.po.AttHolidayBalanceRecordPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.po.AttHolidayRulePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces.param.AttHolidayBalancePageParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces.param.AttHolidayBalanceRecordPageParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces.param.AttStaffTotalBalanceParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces.result.AttHolidayBalancePageDateResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces.result.AttHolidayBalancePageResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces.result.AttHolidayBalanceRecordPageResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces.result.AttHolidayRuleListResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces.result.AttStaffTotalBalanceResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.util.ExcelUtils;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 假期余额
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttHolidayBalanceQueryService {
    private final AttHolidayRuleMapper attHolidayRuleMapper;
    private final AttHolidayBalanceMapper attHolidayBalanceMapper;
    private final AttHolidayBalanceRecordMapper attHolidayBalanceRecordMapper;
    private final AttGroupRepository attGroupRepository;

    /**
     * 假期余额分页列表
     *
     * @param param 条件入参
     *
     * @return 分页列表
     */
    public AttHolidayBalancePageResult getHolidayBalancePage(AttHolidayBalancePageParam param) {
        AttHolidayBalancePageResult result = new AttHolidayBalancePageResult();
        // 设置(获取)表头
        List<AttHolidayRuleListResult> holidayTitleList = new ArrayList<>();//attHolidayRuleMapper.listHolidayRuleList();
        Map<Long, AttHolidayRulePO> holidayRuleMap = new HashMap<>();
        List<AttHolidayRulePO> holidayRuleList = attHolidayRuleMapper.selectList(Wrappers.emptyWrapper());
        if (CollectionUtils.isNotEmpty(holidayRuleList)) {
            for (AttHolidayRulePO rulePO : holidayRuleList) {
                holidayRuleMap.put(rulePO.getId(), rulePO);
                AttHolidayRuleListResult ruleListResult = new AttHolidayRuleListResult();
                ruleListResult.setId(rulePO.getId());
                ruleListResult.setName(rulePO.getName());
                ruleListResult.setNotLimit(rulePO.getNotLimit());
                if (AttHolidayGrantWayEnum.OVERTIME_APPEND.name().equals(rulePO.getGrantWay())) {
                    ruleListResult.setUnit(AttConstants.HOUR);
                } else {
                    ruleListResult.setUnit(AttConstants.DAYS);
                }
                holidayTitleList.add(ruleListResult);
            }
        }
        result.setTableTitle(holidayTitleList);

        // 查询人员分页信息
        AttGroupStaffQueryParam queryParam = new AttGroupStaffQueryParam();
        BeanUtils.copyProperties(param, queryParam);
        Pagination<AttGroupStaffPO> pagination = attGroupRepository.getGroupStaffPage(queryParam);
        result.setTotal(pagination.getTotal());
        result.setPages(pagination.getPages());
        // 获取余额
        Map<Long, List<AttHolidayBalancePO>> finalDetailMap = this.getStaffBalance(pagination.getRecords());

        // 设置(处理)余额
        result.setRecords(pagination.getRecords().stream().map(po -> {
            AttHolidayBalancePageResult.AttHolidayBalancePageData data = new AttHolidayBalancePageResult.AttHolidayBalancePageData();
            BeanUtils.copyProperties(po, data);
            data.setStaffName(po.getName());
            data.setEnterDate(po.getEntryDate());
            List<AttHolidayBalancePageDateResult> dateResultList = new ArrayList<>();
            List<AttHolidayBalancePO> detailPOList = finalDetailMap.get(po.getStaffId());
            if (detailPOList != null && !detailPOList.isEmpty()) {
                for (AttHolidayBalancePO balancePO : detailPOList) {
                    AttHolidayBalancePageDateResult temp = new AttHolidayBalancePageDateResult();
                    temp.setStaffId(balancePO.getStaffId());
                    temp.setHolidayRuleId(balancePO.getHolidayRuleId());
                    // 如果是调休需要换算成小时（其他换算成天
                    String unit = AttConstants.DAYS;
                    int dayOfHour = AttConstants.dayOfHour;
                    AttHolidayRulePO rulePO = holidayRuleMap.get(balancePO.getHolidayRuleId());
                    if (rulePO != null) {
                        dayOfHour = rulePO.getDayOfHour();
                        if (AttHolidayGrantWayEnum.OVERTIME_APPEND.name().equals(rulePO.getGrantWay())) {
                            unit = AttConstants.HOUR;
                        }
                    }
                    temp.setAvailableBalance(AttHolidayDomain.longToUnit(balancePO.getAvailableBalance(), unit, dayOfHour, false));
                    dateResultList.add(temp);
                }
            }
            data.setTypeValueList(dateResultList);
            return data;
        }).collect(Collectors.toList()));

        return result;
    }

    /**
     * 导出假期余额
     *
     * @param param    条件
     * @param request  请求对象
     * @param response 返回对象
     */
    public void exportHolidayBalance(AttHolidayBalancePageParam param, HttpServletRequest request, HttpServletResponse response) {

        Map<Long, AttHolidayRulePO> holidayRulePOMap = new HashMap<>();
        List<AttHolidayRuleListResult> holidayTitleList = new ArrayList<>();
        // 表头
        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("staffName", "姓名");
        headers.put("groupName", "考勤组");
        headers.put("deptName", "部门");
        headers.put("enterData", "入职日期");

        List<AttHolidayRulePO> holidayRulePOList = attHolidayRuleMapper.selectList(Wrappers.emptyWrapper());
        if (holidayRulePOList != null && !holidayRulePOList.isEmpty()) {
            for (AttHolidayRulePO holidayRulePO : holidayRulePOList) {
                holidayRulePOMap.put(holidayRulePO.getId(), holidayRulePO);

                AttHolidayRuleListResult ruleListResult = new AttHolidayRuleListResult();
                ruleListResult.setId(holidayRulePO.getId());
                ruleListResult.setNotLimit(holidayRulePO.getNotLimit());
                String unit = AttConstants.DAYS;
                if (AttHolidayGrantWayEnum.OVERTIME_APPEND.name().equals(holidayRulePO.getGrantWay())) {
                    unit = AttConstants.HOUR;
                }
                ruleListResult.setUnit(unit);
                ruleListResult.setName(holidayRulePO.getName() + "(" + unit + ")");
                // 顺便设置导出的表头
                headers.put(holidayRulePO.getId().toString(), holidayRulePO.getName() + "(" + unit + ")");
                holidayTitleList.add(ruleListResult);
            }
        }

        // 查询人员信息
        AttGroupStaffQueryParam queryParam = new AttGroupStaffQueryParam();
        BeanUtils.copyProperties(param, queryParam);
        List<AttGroupStaffPO> staffList = attGroupRepository.getGroupStaffList(queryParam);
        // 获取余额
        Map<Long, List<AttHolidayBalancePO>> finalDetailMap = this.getStaffBalance(staffList);

        // 内容
        List<Map<String, String>> excelDataList = new ArrayList<>();
        if (staffList != null && !staffList.isEmpty()) {
            for (AttGroupStaffPO po : staffList) {
                Map<String, String> data = new HashMap<>();
                LocalDateTime enterDate = po.getEntryDate();
                String enterString = enterDate == null ? "" : DateUtils.formatDate(enterDate);
                data.put("staffName", po.getName());
                data.put("groupName", po.getGroupName());
                data.put("deptName", po.getDeptName());
                data.put("enterData", enterString);
                Map<Long, AttHolidayBalancePO> ruleBalanceMap = new HashMap<>();
                List<AttHolidayBalancePO> staffBalanceList = finalDetailMap.get(po.getStaffId());
                if (staffBalanceList != null && !staffBalanceList.isEmpty()) {
                    for (AttHolidayBalancePO balancePO : staffBalanceList) {
                        ruleBalanceMap.put(balancePO.getHolidayRuleId(), balancePO);
                    }
                }
                if (!holidayTitleList.isEmpty()) {
                    for (AttHolidayRuleListResult holidayTitle : holidayTitleList) {
                        AttHolidayRulePO holidayRulePO = holidayRulePOMap.get(holidayTitle.getId());
                        AttHolidayBalancePO balancePO = ruleBalanceMap.get(holidayTitle.getId());
                        String balanceShow = "0";
                        if (balancePO == null) {
                            balanceShow = "";
                        } else if (holidayRulePO != null && Boolean.TRUE.equals(holidayRulePO.getNotLimit())) {
                            balanceShow = "不限额";
                        } else if (balancePO.getAvailableBalance() != null && balancePO.getAvailableBalance() > AttConstants.zeroLong) {
                            int dayOfHour = AttConstants.dayOfHour;
                            String unit = AttConstants.DAYS;
                            if (holidayRulePO != null) {
                                dayOfHour = holidayRulePO.getDayOfHour();
                                if (AttHolidayGrantWayEnum.OVERTIME_APPEND.name().equals(holidayRulePO.getGrantWay())) {
                                    unit = AttConstants.HOUR;
                                }
                            }
                            balanceShow = AttHolidayDomain.longToUnit(balancePO.getAvailableBalance(), unit, dayOfHour, false);
                        }
                        data.put(String.valueOf(holidayTitle.getId()), balanceShow);
                    }
                }
                excelDataList.add(data);
            }
        }

        ExcelWriter writer = ExcelUtil.getWriter(true);
        /* 设置数据 */
        writer.setHeaderAlias(headers);
        writer.write(excelDataList, true);
        writer.setColumnWidth(-1, 13);
        String fileName = "假期余额";
        ExcelUtils.downloadExcel(request, response, fileName, writer);
    }

    /**
     * 假期余额记录分页列表
     *
     * @param param 条件入参
     *
     * @return 分页列表
     */
    public Pagination<AttHolidayBalanceRecordPageResult> pageHolidayBalanceRecord(AttHolidayBalanceRecordPageParam param) {
        // 获取假期规则
        Map<Long, AttHolidayRulePO> holidayRuleMap = new HashMap<>();
        List<AttHolidayRulePO> holidayRulePOList = attHolidayRuleMapper.selectList(Wrappers.emptyWrapper());
        if (holidayRulePOList != null && !holidayRulePOList.isEmpty()) {
            for (AttHolidayRulePO rulePO : holidayRulePOList) {
                holidayRuleMap.put(rulePO.getId(), rulePO);
            }
        }
        // 查询条件
        LambdaQueryWrapper<AttHolidayBalanceRecordPO> queryWrapper = Wrappers.<AttHolidayBalanceRecordPO>lambdaQuery()
                .eq(AttHolidayBalanceRecordPO::getStaffId, param.getStaffId())
                .eq(param.getHolidayRuleId() != null, AttHolidayBalanceRecordPO::getHolidayRuleId, param.getHolidayRuleId())
                .orderByDesc(AttHolidayBalanceRecordPO::getCreateTime);
        // 查询分页数据
        Page<AttHolidayBalanceRecordPO> page = attHolidayBalanceRecordMapper.selectPage(PagingUtils.convert(param), queryWrapper);
        return new Pagination<>(page.getTotal(), page.getPages(), page.getRecords().stream().map(po -> {
            AttHolidayBalanceRecordPageResult result = new AttHolidayBalanceRecordPageResult();
            BeanUtils.copyProperties(po, result);
            AttHolidayRulePO rule = holidayRuleMap.get(po.getHolidayRuleId());
            int dayOfHor = AttConstants.dayOfHour;
            String unit = AttConstants.DAYS;
            if (rule != null) {
                dayOfHor = rule.getDayOfHour();
                if (AttHolidayGrantWayEnum.OVERTIME_APPEND.name().equals(rule.getGrantWay())) {
                    unit = AttConstants.HOUR;
                }
            }
            result.setChangeNumber(AttHolidayDomain.longToUnit(po.getChangeNumber(), unit, dayOfHor, true));
            return result;
        }).collect(Collectors.toList()));
    }

    /**
     * 获取人员假期总余额
     *
     * @return 余额
     */
    public AttStaffTotalBalanceResult getStaffTotalBalance(AttStaffTotalBalanceParam param) {
        AttStaffTotalBalanceResult result = new AttStaffTotalBalanceResult();
        result.setTotal("0.0");
        result.setNotLimit(false);
        long balanceTotal = AttConstants.zeroLong;
        AttHolidayRulePO rulePO = attHolidayRuleMapper.selectById(param.getHolidayRuleId());
        if (rulePO == null) {
            throw new ServiceException("假期规则不存在");
        }
        if (Boolean.TRUE.equals(rulePO.getNotLimit())) {
            result.setNotLimit(true);
        }
        // 查询统计数据
        List<AttHolidayBalancePO> balancePOS = attHolidayBalanceMapper.selectList(Wrappers.<AttHolidayBalancePO>lambdaQuery()
                                                                                          .eq(AttHolidayBalancePO::getStaffId, param.getStaffId())
                                                                                          .eq(AttHolidayBalancePO::getHolidayRuleId, param.getHolidayRuleId())
                                                                                 );
        if (balancePOS != null && !balancePOS.isEmpty()) {
            balanceTotal = balancePOS.getFirst().getAvailableBalance();
        }

        result.setTotal(AttHolidayDomain.longToUnit(balanceTotal, param.getUnit(), rulePO.getDayOfHour(), false));
        return result;
    }

    /**
     * 下载假期余额模板
     *
     * @param param    条件入参
     * @param request  请求
     * @param response 返回
     */
    public void downloadTemplate(@RequestBody AttHolidayBalancePageParam param, HttpServletRequest request, HttpServletResponse response) {
        // 校验入参
        Assert.isParameterError(param == null || CollectionUtils.isEmpty(param.getHolidayIdList()), "至少选择一个假期！");
        // 获取假期
        List<AttHolidayRulePO> holidayRulePOList = attHolidayRuleMapper.selectList(Wrappers.<AttHolidayRulePO>lambdaQuery()
                                                                                           .in(AttHolidayRulePO::getId, param.getHolidayIdList()));
        Assert.isNotFound(holidayRulePOList, "未查询到假期，请刷新后重试！");
        AttGroupStaffQueryParam staffQueryParam = new AttGroupStaffQueryParam();
        BeanUtils.copyProperties(param, staffQueryParam);
        List<AttGroupStaffPO> staffList = attGroupRepository.getGroupStaffList(staffQueryParam);
        Assert.isNotFound(staffList, "暂无考勤人员，无法导出");
        ExcelWriter writer = ExcelUtil.getWriter(true);
        /* 处理表头 */
        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("staffId", "员工UserID");
        headers.put("name", "员工姓名");
        headers.put("deptName", "部门");
        headers.put("balance", "额度");
        headers.put("deadline", "有效期");
        /* 处理导出内容 */
        List<Map<String, String>> excelDataList = new ArrayList<>();
        for (AttGroupStaffPO groupStaffPO : staffList) {
            Map<String, String> staffData = new HashMap<>();
            staffData.put("staffId", String.valueOf(groupStaffPO.getStaffId()));
            staffData.put("name", groupStaffPO.getName());
            staffData.put("deptName", groupStaffPO.getDeptName());
            staffData.put("balance", "");
            staffData.put("deadline", "");
            excelDataList.add(staffData);
        }
        // 首行说明文字
        String describe =
                "1.导入员工「假期余额」后系统会自动累加，不影响之前的历史余额（若张三系统内还余3天年假，导入1天年假后，则张三假期总年假为4天。）\n" +
                "2.Excel中Userid、姓名、部门、都不可改变顺序或删除重新添加，否则导入不成功。\n" +
                "3.有效期的格式请按照 2024/05/26 格式来输入，否则导入不成功。(余额为空默认0，有效期为空默认2099年12月12日)。\n" +
                "4.不需要修改的人员可以删除整行(注意是删除整行的单元格而不是删除该行单元格的内容)。\n" +
                "5.复制人员需要复制、粘贴整行(保证人员id和名称跟系统对应)，然后再修改余额和有效期。\n" +
                "6.员工ID和名称不匹配时以员工ID为准。";
        if (Boolean.TRUE.equals(param.getInitTemplate())) {
            describe = "1.导入员工「假期余额」后会替换掉原来的假期余额数据（若张三系统内还余3天年假，导入1天年假后，则张三年假总余额为1天。）\n" +
                       "2.Excel中Userid、姓名、部门、都不可改变顺序或删除重新添加，否则会导入不成功。\n" +
                       "3.有效期的格式请按照 2024/05/26 格式来输入，否则导入不成功。(余额为空默认0，有效期为空默认2099年12月12日)。\n" +
                       "4.不需要修改的人员可以删除整行(注意是删除整行的单元格而不是删除该行单元格的内容)。\n" +
                       "5.复制人员需要复制、粘贴整行(保证人员id和名称跟系统对应)，然后再修改余额和有效期。\n" +
                       "6.员工ID和名称不匹配时以员工ID为准。";
        }
        // 遍历添加sheet
        for (AttHolidayRulePO rulePO : holidayRulePOList) {
            handSheets(writer, rulePO.getName(), describe, headers, excelDataList);
        }
        // 隐藏默认的空sheet
        writer.getWorkbook().setSheetHidden(AttConstants.zeroInt, true);
        // 导出
        String fileName = "假期额度模板" + DateUtils.format(LocalDateTime.now(), "yyyyMMdd");
        ExcelUtils.downloadExcel(request, response, fileName, writer);
    }

    /**
     * 获取余额
     *
     * @param staffList 人员集合
     *
     * @return 余额
     */
    private Map<Long, List<AttHolidayBalancePO>> getStaffBalance(List<AttGroupStaffPO> staffList) {
        // 获取余额
        Map<Long, List<AttHolidayBalancePO>> detailMap = new HashMap<>();
        if (staffList != null && !staffList.isEmpty()) {
            List<Long> staffIdList = staffList.stream().map(AttGroupStaffPO::getStaffId).collect(Collectors.toList());

            List<AttHolidayBalancePO> detailPOList = attHolidayBalanceMapper.selectList(Wrappers.<AttHolidayBalancePO>lambdaQuery()
                                                                                                .in(AttHolidayBalancePO::getStaffId, staffIdList)
                                                                                       );
            if (detailPOList != null && !detailPOList.isEmpty()) {
                detailMap = detailPOList.stream().collect(Collectors.groupingBy(AttHolidayBalancePO::getStaffId));
            }
        }
        return detailMap;
    }

    private void handSheets(ExcelWriter writer, String sheetName, String describe, Map<String, String> headers,
                            List<Map<String, String>> excelDataList
                           ) {
        writer.setSheet(sheetName);
        writer.writeCellValue(0, 0, describe);
        // 设置表头背景色
        CellStyle headCellStyle = writer.getHeadCellStyle();
        headCellStyle.setLocked(true);
        StyleUtil.setColor(headCellStyle, IndexedColors.AQUA, FillPatternType.SOLID_FOREGROUND);
        //设置单元格自动换行
        CellStyle cellStyle = writer.getCellStyle();
        cellStyle.setWrapText(true);
        cellStyle.setLocked(false);
        // 内容字体颜色黑色
        Font font = writer.createFont();
        font.setColor(IndexedColors.BLACK1.getIndex());
        cellStyle.setFont(font);
        /* 首行样式设置 */
        CellStyle describeStyle = writer.createCellStyle(0, 0);
        // 内容水平居中
        describeStyle.setAlignment(HorizontalAlignment.LEFT);
        // 内容垂直居中
        describeStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 设置边框
        describeStyle.setBorderBottom(BorderStyle.THIN);
        describeStyle.setBorderLeft(BorderStyle.THIN);
        describeStyle.setBorderRight(BorderStyle.THIN);
        // 字体颜色标红
        Font describeFont = writer.createFont();
        describeFont.setColor(Font.COLOR_RED);
        describeStyle.setFont(describeFont);

        // 设置列宽
        writer.setColumnWidth(-1, 25);
        writer.setRowHeight(-1, 28);
        /* 设置数据 */
        writer.passCurrentRow();
        writer.setHeaderAlias(headers);
        writer.write(excelDataList, true);
    }

}
