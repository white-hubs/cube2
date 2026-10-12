package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.querygateway;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.style.StyleUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.mapper.AttAskLeaveLedgerMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.po.AttAskLeaveLedgerPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces.param.AttAskLeaveLedgerPageParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces.result.AttAskLeaveLedgerPageDateResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces.result.AttAskLeaveLedgerPageResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.AttConstants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper.AttGroupStaffMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttGroupStaffPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.mapper.AttHolidayRuleMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces.result.AttHolidayRuleListResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.util.ExcelUtils;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 请假台账查询服务
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttAskLeaveLedgerQueryService {
    private final AttAskLeaveLedgerMapper attAskLeaveLedgerMapper;
    private final AttHolidayRuleMapper attHolidayRuleMapper;
    private final AttGroupStaffMapper attGroupStaffMapper;

    /**
     * 请假台账分页列表
     *
     * @param param 条件入参
     *
     * @return 台账列表
     */
    public AttAskLeaveLedgerPageResult askLeaveLedgerPage(AttAskLeaveLedgerPageParam param) {
        AttAskLeaveLedgerPageResult result = new AttAskLeaveLedgerPageResult();

        if (param.getStartDay() != null && param.getEndDay() != null && param.getStartDay().isAfter(param.getEndDay())) {
            throw new ServiceException("结束时间必须大于开始时间");
        }

        // 设置(获取)表头
        result.setTableTitle(attHolidayRuleMapper.listHolidayRuleList());

        // 查询人员分页信息
        Page<AttAskLeaveLedgerPO> page = attAskLeaveLedgerMapper.selectPage(PagingUtils.convert(param), Wrappers
                                                                                    .<AttAskLeaveLedgerPO>lambdaQuery()
                                                                                    .eq(param.getDeptId() != null, AttAskLeaveLedgerPO::getDeptId, param.getDeptId())
                                                                                    .like(StringUtils.isNotBlank(param.getStaffName()), AttAskLeaveLedgerPO::getStaffName, param.getStaffName())
                                                                                    .ge(param.getStartDay() != null, AttAskLeaveLedgerPO::getLeaveDate, param.getStartDay())
                                                                                    .le(param.getEndDay() != null, AttAskLeaveLedgerPO::getLeaveDate, param.getEndDay())
                                                                                    .groupBy(AttAskLeaveLedgerPO::getStaffId)
                                                                                    .orderByAsc(AttAskLeaveLedgerPO::getStaffName)
                                                                           );
        result.setPages(page.getPages());
        result.setTotal(page.getTotal());
        // 处理台账数据（将查询的台账数据按人员id分组
        Map<Long, List<AttAskLeaveLedgerPageDateResult>> detailMap = new HashMap<>();
        if (page.getRecords() != null && !page.getRecords().isEmpty()) {
            List<AttAskLeaveLedgerPageDateResult> detailPOList = attAskLeaveLedgerMapper.selectLedgerList(Wrappers
                                                                                                                  .<AttAskLeaveLedgerPO>lambdaQuery()
                                                                                                                  .eq(param.getDeptId() !=
                                                                                                                      null, AttAskLeaveLedgerPO::getDeptId, param.getDeptId())
                                                                                                                  .like(StringUtils.isNotBlank(param.getStaffName()), AttAskLeaveLedgerPO::getStaffName, param.getStaffName())
                                                                                                                  .ge(param.getStartDay() !=
                                                                                                                      null, AttAskLeaveLedgerPO::getLeaveDate, param.getStartDay())
                                                                                                                  .le(param.getEndDay() !=
                                                                                                                      null, AttAskLeaveLedgerPO::getLeaveDate, param.getEndDay())
                                                                                                         );
            if (detailPOList != null && !detailPOList.isEmpty()) {
                detailMap = detailPOList.stream().collect(Collectors.groupingBy(AttAskLeaveLedgerPageDateResult::getStaffId));
            }
        }
        // 设置(处理)内容
        Map<Long, List<AttAskLeaveLedgerPageDateResult>> finalDetailMap = detailMap;
        result.setRecords(page.getRecords().stream().map(po -> {
            AttAskLeaveLedgerPageResult.AttAskLeaveLedgerPageData data = new AttAskLeaveLedgerPageResult.AttAskLeaveLedgerPageData();
            BeanUtils.copyProperties(po, data);
            data.setLedgerValueList(finalDetailMap.get(po.getStaffId()));
            return data;
        }).collect(Collectors.toList()));

        return result;
    }

    /**
     * 导出请假台账
     *
     * @param param    条件对象
     * @param request  请求
     * @param response 响应
     */
    public void exportLeaveLedger(AttAskLeaveLedgerPageParam param, HttpServletRequest request, HttpServletResponse response) {
        // 查询人员信息
        List<AttGroupStaffPO> groupStaffList = attGroupStaffMapper.selectList(Wrappers.<AttGroupStaffPO>lambdaQuery()
                                                                                      .eq(param.getGroupId() !=
                                                                                          null, AttGroupStaffPO::getGroupId, param.getGroupId())
                                                                                      .eq(param.getDeptId() !=
                                                                                          null, AttGroupStaffPO::getDeptId, param.getDeptId())
                                                                                      .like(StringUtils.isNotBlank(param.getStaffName()), AttGroupStaffPO::getName, param.getStaffName())
                                                                                      .orderByAsc(AttGroupStaffPO::getName)
                                                                             );

        //
        List<AttHolidayRuleListResult> holidayRuleList = attHolidayRuleMapper.listHolidayRuleList();

        // 处理台账数据（将查询的台账数据按人员id分组
        Map<Long, List<AttAskLeaveLedgerPageDateResult>> detailMap = new HashMap<>();
        if (groupStaffList != null && !groupStaffList.isEmpty()) {
            List<Long> staffIdList = groupStaffList.stream().map(AttGroupStaffPO::getStaffId).collect(Collectors.toList());
            List<AttAskLeaveLedgerPageDateResult> detailPOList = attAskLeaveLedgerMapper.selectLedgerList(Wrappers
                                                                                                                  .<AttAskLeaveLedgerPO>lambdaQuery()
                                                                                                                  .in(AttAskLeaveLedgerPO::getStaffId, staffIdList)
                                                                                                                  .ge(param.getStartDay() !=
                                                                                                                      null, AttAskLeaveLedgerPO::getLeaveDate, param.getStartDay())
                                                                                                                  .le(param.getEndDay() !=
                                                                                                                      null, AttAskLeaveLedgerPO::getLeaveDate, param.getEndDay())
                                                                                                         );
            if (detailPOList != null && !detailPOList.isEmpty()) {
                detailMap = detailPOList.stream().collect(Collectors.groupingBy(AttAskLeaveLedgerPageDateResult::getStaffId));
            }
        }

        // 处理表头
        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("staffName", "员工姓名");
        headers.put("deptName", "部门");
        for (AttHolidayRuleListResult rule : holidayRuleList) {
            headers.put(rule.getId().toString(), rule.getName());
        }
        // 处理excel数据
        List<Map<String, String>> excelDataList = this.getExcelData(groupStaffList, holidayRuleList, detailMap);

        ExcelWriter writer = ExcelUtil.getWriter(true);
        /* 设置样式 */

        // 设置表头背景色
        CellStyle headCellStyle = writer.getHeadCellStyle();
        StyleUtil.setColor(headCellStyle, IndexedColors.AQUA, FillPatternType.SOLID_FOREGROUND);
        //设置单元格自动换行
        CellStyle cellStyle = writer.getCellStyle();
        cellStyle.setWrapText(true);
        // 设置列宽
        writer.setColumnWidth(-1, 20);
        writer.setRowHeight(-1, 25);

        /* 设置数据 */
        writer.setHeaderAlias(headers);
        writer.write(excelDataList, true);
        String fileName = "leaveLedger";
        ExcelUtils.downloadExcel(request, response, fileName, writer);
    }

    private List<Map<String, String>> getExcelData(List<AttGroupStaffPO> groupStaffList, List<AttHolidayRuleListResult> holidayRuleList,
                                                   Map<Long, List<AttAskLeaveLedgerPageDateResult>> detailMap
                                                  ) {
        List<Map<String, String>> excelData = new ArrayList<>();
        if (groupStaffList == null || groupStaffList.isEmpty()) {
            return excelData;
        }
        if (holidayRuleList == null) {
            holidayRuleList = new ArrayList<>();
        }
        if (detailMap == null) {
            detailMap = new HashMap<>();
        }
        for (AttGroupStaffPO staff : groupStaffList) {
            Map<String, String> dataMap = new LinkedHashMap<>();
            dataMap.put("staffName", staff.getName());
            dataMap.put("deptName", staff.getDeptName());
            HashMap<Long, String> staffLedgerMap = new HashMap<>();
            List<AttAskLeaveLedgerPageDateResult> staffLedgerList = detailMap.get(staff.getStaffId());
            if (staffLedgerList != null) {
                for (AttAskLeaveLedgerPageDateResult staffLedger : staffLedgerList) {
                    staffLedgerMap.put(staffLedger.getHolidayRuleId(), staffLedger.getLeaveNumberString());
                }
            }
            for (AttHolidayRuleListResult rule : holidayRuleList) {
                String val = staffLedgerMap.get(rule.getId());
                dataMap.put(rule.getId().toString(), StringUtils.isNotBlank(val) ? val : AttConstants.zeroString);
            }

            excelData.add(dataMap);
        }
        return excelData;
    }

}
