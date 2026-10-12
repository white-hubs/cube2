package com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.controller;

import cn.hutool.poi.excel.ExcelWriter;
import com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.param.AttendanceLogQueryParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.querygateway.AttendanceLogQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.vo.AttendanceOperationLogVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.DateUtils;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * @author wql
 * @describe 考勤操作日志
 * @date 2023/6/12 15:12
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttendanceLogController {
    private final AttendanceLogQueryService attendanceLogQueryService;

    /**
     * 考勤管理操作日志记录列表
     *
     * @param param 查询参数
     *
     * @return 操作日志记录
     */
    @GetMapping("/attendanceLog/page")
    public Pagination<AttendanceOperationLogVO> getPage(AttendanceLogQueryParam param) {
        return attendanceLogQueryService.getPage(param);
    }

    /**
     * 导出考勤管理操作日志记录列表
     *
     * @param param 查询参数
     *
     * @return 导出文件
     */
    @GetMapping("/attendanceLog/export")
    public void export(AttendanceLogQueryParam param, HttpServletResponse response) throws IOException {
        try (ExcelWriter writer = attendanceLogQueryService.export(param); ServletOutputStream outputStream = response.getOutputStream()) {
            setResponseInfo(response, "考勤管理操作日志");
            writer.flush(outputStream, true);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException("操作日志导出失败！");
        }
    }

    private void setResponseInfo(HttpServletResponse response, String name) throws UnsupportedEncodingException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset:utf-8");
        String fileName = URLEncoder.encode(name + DateUtils.format(LocalDateTime.now(), "yyyyMMddHHmmss"), StandardCharsets.UTF_8);
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
    }

}
