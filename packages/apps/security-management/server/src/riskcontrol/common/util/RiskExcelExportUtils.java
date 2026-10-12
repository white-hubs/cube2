package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.util;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public final class RiskExcelExportUtils {
    private RiskExcelExportUtils() {
    }

    public static void write(HttpServletResponse response, String fileName, List<Map<String, Object>> rows) {
        ExcelWriter writer = ExcelUtil.getWriter(true);
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("+", "%20");
            response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + encodedFileName + ".xlsx");
            writer.write(rows);
            writer.setColumnWidth(-1, 20);
            writer.flush(response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException("导出失败", e);
        } finally {
            writer.close();
        }
    }
}
