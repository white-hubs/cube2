package com.guhecloud.cube.srv.gen.official.management.server.interfaces.util;

import cn.hutool.poi.excel.ExcelWriter;
import lombok.extern.slf4j.Slf4j;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
public class ExcelUtils {
    public static void downloadExcel(HttpServletRequest request, HttpServletResponse response, String fileName, ExcelWriter writer) {
        response.reset();
        //        response.setContentType("application/x-download;charset=utf-8");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset:utf-8");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name()); // 字符集编码
        ServletOutputStream out;
        try {
            // 设置请求头属性
            String encodeFileName = encodeFileName(fileName, request);
            response.setHeader("Content-Disposition", "attachment;filename=" + encodeFileName + ".xlsx");
            out = response.getOutputStream();
            // 写出到文件
            writer.flush(out, true);
        } catch (IOException e) {
            log.error("下载Excel报错", e);
            e.printStackTrace();
        } finally {
            // 关闭writer，释放内存
            writer.close();
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
            if (null != agent && -1 != agent.indexOf("MSIE") || null != agent && -1 != agent.indexOf("Trident") ||
                null != agent && -1 != agent.indexOf("Edge")) {// ie浏览器及Edge浏览器
                String name = URLEncoder.encode(fileName, "UTF-8");
                codedFilename = name;
            } else if (null != agent && -1 != agent.indexOf("Mozilla")) {
                // 火狐,Chrome等浏览器
                codedFilename = new String(fileName.getBytes(StandardCharsets.UTF_8), "ISO8859-1");
            } else {
                codedFilename = URLEncoder.encode(fileName, "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return codedFilename;
    }
}
