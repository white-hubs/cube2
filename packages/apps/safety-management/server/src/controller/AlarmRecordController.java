package com.guhecloud.cube.srv.gen.safety.management.controller;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.AlarmRecordQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.AlarmRecordPageVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.AlarmReportPageVO;
import com.guhecloud.cube.srv.gen.safety.management.service.AlarmRecordService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URLEncoder;
import java.util.List;

@Slf4j
@RestController(value = "safetyAlarmRecordController")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmRecordController {

    private final AlarmRecordService alarmRecordService;

    /**
     * 报警记录分页列表
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/alarm-record/page")
    public Pagination<AlarmRecordPageVO> getAlarmRecordPage(@Valid AlarmRecordQuery query) {
        Page<AlarmRecordPageVO> alarmRecordPage = this.alarmRecordService.getAlarmRecordPage(query);
        return PagingUtils.convert(alarmRecordPage);
    }

    /**
     * 报警安全报表分页列表
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/alarm-record/report")
    public Pagination<AlarmReportPageVO> getAlarmReportPage(@Valid AlarmRecordQuery query) {
        Page<AlarmReportPageVO> alarmReportPage = this.alarmRecordService.getAlarmReportPage(query);
        return PagingUtils.convert(alarmReportPage);
    }

    /**
     * 报警记录导出
     *
     * @param query
     */
    @PostMapping("/alarm-record/export")
    public void exportAlarmRecords(@Valid @RequestBody AlarmRecordQuery query) {
        try {
            ServletRequestAttributes attr = null;
            HttpServletResponse response = null;
            attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attr != null) {
                response = attr.getResponse();
                if (response != null) {
                    response.setContentType("application/vnd.ms-excel");
                    response.setCharacterEncoding("utf-8");
                    // 这里URLEncoder.encode可以防止中文乱码
                    String excelName = URLEncoder.encode("报警记录", "UTF-8");
                    response.setHeader("Content-disposition", "attachment;filename=" + excelName + ExcelTypeEnum.XLSX.getValue());
                    List<AlarmRecordPageVO> alarmRecords = this.alarmRecordService.getAlarmRecords(query);
                    EasyExcelFactory.write(response.getOutputStream(), AlarmRecordPageVO.class).sheet("报警记录")
                            .doWrite(alarmRecords);
                }
            }
        } catch (Exception e) {
            log.error("报警记录导出失败,失败原因{}", e);
            throw new ServiceException("报警记录导出失败,请联系管理员");
        }
    }

    /**
     * 报警安全月报导出
     *
     * @param query
     */
    @PostMapping("/alarm-record/report/export")
    public void exportAlarmReport(@Valid @RequestBody AlarmRecordQuery query) {
        try {
            ServletRequestAttributes attr = null;
            HttpServletResponse response = null;
            attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attr != null) {
                response = attr.getResponse();
                if (response != null) {
                    response.setContentType("application/vnd.ms-excel");
                    response.setCharacterEncoding("utf-8");
                    // 这里URLEncoder.encode可以防止中文乱码
                    String excelName = URLEncoder.encode("安全报表", "UTF-8");
                    response.setHeader("Content-disposition", "attachment;filename=" + excelName + ExcelTypeEnum.XLSX.getValue());
                    List<AlarmReportPageVO> alarmReports = this.alarmRecordService.getAlarmReports(query);
                    EasyExcelFactory.write(response.getOutputStream(), AlarmReportPageVO.class).sheet("安全报表")
                            .doWrite(alarmReports);
                }
            }
        } catch (Exception e) {
            log.error("报警安全月报导出失败,失败原因{}", e);
            throw new ServiceException("安全月报导出失败,请联系管理员");
        }
    }
}
