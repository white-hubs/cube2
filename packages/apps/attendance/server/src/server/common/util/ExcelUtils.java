package com.guhecloud.cube.srv.gen.attendance.management.server.common.util;

import cn.hutool.poi.excel.ExcelWriter;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
public class ExcelUtils {
    public static void downloadExcel(HttpServletRequest request, HttpServletResponse response, String fileName, ExcelWriter writer) {
        response.reset();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset:utf-8");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name()); // 字符集编码
        ServletOutputStream out;
        try (writer) {
            // 设置请求头属性
            String encodeFileName = encodeFileName(fileName, request);
            response.setHeader("Content-Disposition", "attachment;filename=" + encodeFileName + ".xlsx");
            out = response.getOutputStream();
            // 写出到文件
            writer.flush(out, true);
        } catch (IOException e) {
            log.error("下载Excel报错", e);
        }
    }

    /**
     * @param fileName
     * @param request
     *
     * @Description: 导出文件转换文件名称编码
     */
    public static String encodeFileName(String fileName, HttpServletRequest request) {
        String codedFilename = null;
        try {
            String agent = request.getHeader("USER-AGENT");
            if (null != agent && agent.contains("MSIE") || null != agent && agent.contains("Trident") ||
                null != agent && agent.contains("Edge")) {// ie浏览器及Edge浏览器
                codedFilename = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
            } else if (null != agent && agent.contains("Mozilla")) {
                // 火狐,Chrome等浏览器
                codedFilename = new String(fileName.getBytes(StandardCharsets.UTF_8), "ISO8859-1");
            } else {
                codedFilename = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            log.error("编码文件失败", e);
        }
        return codedFilename;
    }
}
