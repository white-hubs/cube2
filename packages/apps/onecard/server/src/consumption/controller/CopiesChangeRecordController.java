package com.guhecloud.cube.onecard.server.consumption.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import com.guhecloud.cube.onecard.server.consumption.param.CopiesRecordQueryParam;
import com.guhecloud.cube.onecard.server.consumption.service.CopiesChangeRecordService;
import com.guhecloud.cube.onecard.server.consumption.vo.CopiesChangeRecordVo;
import com.guhecloud.cube.onecard.server.consumption.vo.CopiesChangeVo;
import com.guhecloud.cube.onecard.server.consumption.vo.CopiesRecordExcelVo;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CopiesChangeRecordController {

    private final CopiesChangeRecordService copiesChangeRecordService;

    /**
     * @Description 份变更明细分页
     * @Date 2023/2/28 12:35
     * @Author tangzhiqiang
     */
    @GetMapping("/copies-change-record")
    public Pagination<CopiesChangeRecordVo> getCopiesChangeRecord(CopiesRecordQueryParam param) {
        return copiesChangeRecordService.getCopiesChangeRecord(param);
    }

    /**
     * @Description 统计份变更相关数据
     * @Date 2023/3/1 16:37
     * @Author tangzhiqiang
     */
    @GetMapping("/share-statistics")
    public CopiesChangeVo getShareStatistics(CopiesRecordQueryParam param) {
        return copiesChangeRecordService.getShareStatistics(param);
    }

    /**
     * @Description 份变更更记录导出
     * @Date 2023/3/2 10:38
     * @Author tangzhiqiang
     */
    @GetMapping("/copies-record-excel")
    public void downloadCopiesRecordExcel(HttpServletResponse response, CopiesRecordQueryParam param) throws Exception {
        Pagination<CopiesChangeRecordVo> copiesChangeRecord = copiesChangeRecordService.getCopiesChangeRecord(param);
        if (copiesChangeRecord != null) {
            List<CopiesChangeRecordVo> records = copiesChangeRecord.getRecords();
            List<CopiesRecordExcelVo> copying = ObjectUtils.copying(records, CopiesRecordExcelVo.class);
            TemplateExportParams params = new TemplateExportParams("static/CopiesRecordTemplate.xlsx", true);
            Map<String, Object> map = new HashMap<>();
            map.put("copieslist", copying);
            Workbook workbook = ExcelExportUtil.exportExcel(params, map);
            String fileName = URLEncoder.encode("份变更明细.xlsx", "UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            try {
                response.flushBuffer();
                workbook.write(response.getOutputStream());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (workbook != null) {
                    try {
                        workbook.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }
}
