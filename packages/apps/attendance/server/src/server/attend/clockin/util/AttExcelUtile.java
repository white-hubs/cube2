package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class AttExcelUtile {
    /**
     * 按照模板导出
     *
     * @param map          每个sheet页的数据
     * @param title        填充标题
     * @param templateName 模板名称
     */
    public static void uploadExcelList(HttpServletResponse response,
                                       Map<String, List<?>> map,
                                       String templateName
                                      ) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode(templateName, StandardCharsets.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        //切记一定要关闭才能完成下载
        try (ExcelWriter ignored = genTemplateMultiSheet(response, map, templateName)) {
        }
    }

    /**
     * 填充多个sheet模板数据
     *
     * @param response
     */
    /**
     * 填充多个sheet模板数据
     *
     * @param response
     */
    public static ExcelWriter genTemplateMultiSheet(HttpServletResponse response,
                                                    Map<String, List<?>> map,
                                                    String templateName
                                                   ) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("static/" + templateName);
        InputStream inputStream = classPathResource.getInputStream();
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        //原模板只有一个sheet，通过poi复制出需要的sheet个数的模板
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        //根据传入的map新建多个sheet并设置name
        int j = 0;
        for (String ex : map.keySet()) {
            if (j == 0) {
                //首先设置第一个sheet的名称
                workbook.setSheetName(j, ex);
                j++;
                continue;
            }
            //循环克隆模板第一个sheet，修改为其他名称
            workbook.cloneSheet(0, ex);
        }
        //重新构建新的输入流
        workbook.write(bos);
        byte[] bArray = bos.toByteArray();
        InputStream is = new ByteArrayInputStream(bArray);
        //创建新的excelWriter
        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).withTemplate(is).build();
        //将map数据分页填充头部和监测值
        FillConfig fillConfig = FillConfig.builder()
                .forceNewRow(Boolean.TRUE).build();
        for (Map.Entry<String, List<?>> entry : map.entrySet()) {
            WriteSheet writeSheet = EasyExcel.writerSheet(entry.getKey()).build();
            excelWriter.fill(entry.getValue(), fillConfig, writeSheet);
        }

        return excelWriter;
    }

}
