package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.querygateway;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.mapper.AttLeavePostLedgerMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.po.AttLeavePostLedgerPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces.param.AttLeavePostLedgerPageParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces.result.AttLeavePostLedgerPageResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.AttConstants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.AttHolidayDomain;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.util.ExcelUtils;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 离岗台账查询服务
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttLeavePostLedgerQueryService {

    private final AttLeavePostLedgerMapper attLeavePostLedgerMapper;

    /**
     * 台账分页列表
     *
     * @param param 条件入参
     *
     * @return 分页列表对象
     */
    public Pagination<AttLeavePostLedgerPageResult> leavePostLedgerPage(AttLeavePostLedgerPageParam param) {
        // 查询台账分页信息
        Page<AttLeavePostLedgerPageResult> page = attLeavePostLedgerMapper.selectLedgerPage(PagingUtils.convert(param), this.getLedgerWrapper(param));
        return PagingUtils.convert(page);

    }

    /**
     * 离岗台账导出
     *
     * @param param    条件
     * @param request  请求
     * @param response 返回
     */
    public void leavePostLedgerExport(AttLeavePostLedgerPageParam param, HttpServletRequest request, HttpServletResponse response) {
        // 查询台账信息
        List<AttLeavePostLedgerPageResult> list = attLeavePostLedgerMapper.selectLedgerList(this.getLedgerWrapper(param));
        Assert.isNotFound(list, "暂无台账信息，无法导出！");
        /* 遍历组装导出数据 */
        // 表头
        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("staffName", "员工姓名");
        headers.put("deptName", "部门");
        headers.put("postCount", "离岗次数");
        headers.put("postNumber", "离岗时长(h)");
        List<Map<String, String>> excelDataList = new ArrayList<>();
        for (AttLeavePostLedgerPageResult ledger : list) {
            Map<String, String> ledgerData = new HashMap<>();
            ledgerData.put("staffName", ledger.getStaffName());
            ledgerData.put("deptName", ledger.getDeptName());
            String postCount = "1";
            if (ledger.getPostCount() != null) {
                postCount = String.valueOf(ledger.getPostCount());
            }
            ledgerData.put("postCount", postCount);
            ledgerData.put("postNumber", AttHolidayDomain.longToUnit(ledger.getPostNumber(), AttConstants.HOUR, null, false));
            excelDataList.add(ledgerData);
        }

        /* 设置数据 */
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.setHeaderAlias(headers);
        writer.write(excelDataList, true);
        /* 设置格式*/
        writer.setColumnWidth(-1, 25);
        writer.setRowHeight(-1, 20);
        /* 导出 */
        ExcelUtils.downloadExcel(request, response, "离岗台账", writer);
    }

    private LambdaQueryWrapper<AttLeavePostLedgerPO> getLedgerWrapper(AttLeavePostLedgerPageParam param) {
        if (param == null) {
            param = new AttLeavePostLedgerPageParam();
        }
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        if (param.getPeriod() != null && param.getPeriod().getBegin() != null && param.getPeriod().getEnd() != null) {
            startTime = param.getPeriod().getBegin();
            endTime = param.getPeriod().getEnd();
        }
        return Wrappers.<AttLeavePostLedgerPO>lambdaQuery()
                .eq(param.getGroupId() != null, AttLeavePostLedgerPO::getGroupId, param.getGroupId())
                .eq(param.getDeptId() != null, AttLeavePostLedgerPO::getDeptId, param.getDeptId())
                .like(StringUtils.isNotBlank(param.getStaffName()), AttLeavePostLedgerPO::getStaffName, param.getStaffName())
                .ge(startTime != null, AttLeavePostLedgerPO::getPostDate, startTime)
                .le(endTime != null, AttLeavePostLedgerPO::getPostDate, endTime)
                .groupBy(AttLeavePostLedgerPO::getStaffId)
                .orderByAsc(AttLeavePostLedgerPO::getStaffName);

    }
}
