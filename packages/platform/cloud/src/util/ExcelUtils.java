package com.guhecloud.platform.cloud.util;

import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ListUtils;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExcelUtils {
    private final HttpServletRequest request;
    private final HttpServletResponse response;

    public static void downloadExcel(HttpServletRequest request, HttpServletResponse response, String fileName, ExcelWriter writer) {
        response.reset();
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

    public <T> void downloadExcel(String fileName, List<T> data, Class<T> clazz, SheetWriteHandler... handlers) {
        String sheetName = fileName;
        response.reset();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset:utf-8");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name()); // 字符集编码
        String encodeFileName = encodeFileName(fileName, request);
        try {
            fileName = java.net.URLEncoder.encode(encodeFileName, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            ExcelWriterBuilder writerBuilder = EasyExcel
                    .write(response.getOutputStream(), clazz)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy());
            if (handlers.length > 0) {
                for (SheetWriteHandler handler : handlers) {
                    writerBuilder.registerWriteHandler(handler);
                }
            }
            writerBuilder.sheet(sheetName)
                    .doWrite(data);
        } catch (IOException e) {
            log.error("下载Excel报错", e);
            throw new RuntimeException(e);
        }
    }

    public <T> void downloadExcel(String fileName, List<T> data, Class<T> clazz, List<SheetWriteHandler> sheetWriteHandlers,
                                  List<CellWriteHandler> cellWriteHandlers
                                 ) {
        String sheetName = fileName;
        response.reset();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset:utf-8");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name()); // 字符集编码
        String encodeFileName = encodeFileName(fileName, request);
        try {
            fileName = java.net.URLEncoder.encode(encodeFileName, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            ExcelWriterBuilder writerBuilder = EasyExcel
                    .write(response.getOutputStream(), clazz)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy());
            if (ListUtils.nonEmpty(sheetWriteHandlers)) {
                for (SheetWriteHandler sheetWriteHandler : sheetWriteHandlers) {
                    writerBuilder.registerWriteHandler(sheetWriteHandler);
                }
            }
            if (ListUtils.nonEmpty(cellWriteHandlers)) {
                for (CellWriteHandler cellWriteHandler : cellWriteHandlers) {
                    writerBuilder.registerWriteHandler(cellWriteHandler);
                }
            }
            writerBuilder.sheet(sheetName)
                    .doWrite(data);
        } catch (IOException e) {
            log.error("下载Excel报错", e);
            throw new RuntimeException(e);
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

    public <T> List<T> readExcelFile(MultipartFile file, Class<T> clazz) throws IOException {
        Assert.parameterIsNull(file, "请选择要导入的文件！");
        return EasyExcel.read(file.getInputStream())
                .head(clazz)
                .sheet()
                .doReadSync();
    }
}
