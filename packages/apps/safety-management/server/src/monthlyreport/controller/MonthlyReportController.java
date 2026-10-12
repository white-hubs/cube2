package com.guhecloud.cube.srv.gen.safety.management.monthlyreport.controller;

import com.alibaba.excel.support.ExcelTypeEnum;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.dto.request.query.MonthlyReportQuery;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.dto.vo.MonthlyReportVO;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.excel.MonthlyReportExcel;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.po.MonthlyReportPO;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.service.MonthlyReportService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.Objects;

/**
 * 市场月报管理
 *
 * @author lanchun
 * @date 2023/9/14 17:53
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MonthlyReportController {

    private final MonthlyReportService monthlyReportService;

    private final FileClient fileClient;

    /**
     * 安全月报分页列表
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/monthly/report/manage/queryByPage")
    public Pagination<MonthlyReportVO> getPage(@Valid MonthlyReportQuery query) {
        return this.monthlyReportService.queryPage(query);
    }

    /**
     * 安全月报详情
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/monthly/report/manage/{id}")
    public MonthlyReportVO getDetail(@PathVariable("id") Long id) {
        MonthlyReportPO monthlyReportPO = this.monthlyReportService.getById(id);
        Assert.isNotFound(monthlyReportPO, "月报信息不存在,请联系管理员");
        return ObjectUtils.copying(monthlyReportPO, MonthlyReportVO.class);
    }

    /**
     * 总部月报导出
     *
     * @param id
     */
    @PostMapping("/monthly/report/manage/{id}/export")
    public void export(@PathVariable("id") Long id) throws UnsupportedEncodingException {
        MonthlyReportPO monthlyReportPO = this.monthlyReportService.getById(id);
        Assert.isNotFound(monthlyReportPO, "月报文件不存在,导出失败");
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.nonNull(attr)) {
            HttpServletResponse response = attr.getResponse();
            if (Objects.nonNull(response)) {
                response.setContentType("application/vnd.ms-excel");
                response.setCharacterEncoding("utf-8");
                // 这里URLEncoder.encode可以防止中文乱码
                String excelName = URLEncoder.encode(monthlyReportPO.getTitle(), "UTF-8");
                response.setHeader("Content-disposition", "attachment;filename=" + excelName + ExcelTypeEnum.XLSX.getValue());
                byte[] fileDecodeBytes = this.fileClient.getFileByte(monthlyReportPO.getReportId());
                byte[] fileBytes = Base64.getDecoder().decode(new String(fileDecodeBytes).replace("\"", ""));
                try (BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())) {
                    bos.write(fileBytes);
                } catch (IOException e) {
                    log.error("导出总部月报文件失败,失败原因,{}", e);
                }
            }
        }
    }

    /**
     * 总部月报导出
     *
     * @param id
     */
    @GetMapping("/public/monthly/report/manage/{id}/work-book/export")
    public void exportWorkBook(@PathVariable("id") Long id) {
        MonthlyReportPO monthlyReportPO = this.monthlyReportService.getById(id);
        Assert.isNotFound(monthlyReportPO, "月报文件不存在,导出失败");
        //获取文件字节
        byte[] fileDecodeBytes = this.fileClient.getFileByte(monthlyReportPO.getReportId());
        byte[] fileBytes = Base64.getDecoder().decode(new String(fileDecodeBytes).replace("\"", ""));
        ZipSecureFile.setMinInflateRatio(-1.0d);
        try (InputStream inputStream = new ByteArrayInputStream(fileBytes);
             XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {
            MonthlyReportExcel.setFormulaRecalculationByCell(workbook);
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (Objects.nonNull(attr)) {
                HttpServletResponse response = attr.getResponse();
                if (Objects.nonNull(response)) {
                    // 这里URLEncoder.encode可以防止中文乱码
                    String excelName = URLEncoder.encode(monthlyReportPO.getTitle(), "UTF-8");
                    response.setHeader("Accept-Ranges", "bytes");
                    //response.setContentLength(100);
                    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
                    response.setHeader("Content-disposition", "attachment;filename=" + excelName + ExcelTypeEnum.XLSX.getValue());
                    workbook.write(response.getOutputStream());
                }
            }
        } catch (Exception e) {
            log.error("导出总部月报工作空间失败,失败原因{}", e);
        }
    }
}
