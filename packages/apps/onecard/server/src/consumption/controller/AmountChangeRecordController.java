package com.guhecloud.cube.onecard.server.consumption.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import com.guhecloud.cube.onecard.server.consumption.enumeration.TransactionTypeEnum;
import com.guhecloud.cube.onecard.server.consumption.param.AmountRecordQueryParam;
import com.guhecloud.cube.onecard.server.consumption.service.AmountChangeRecordService;
import com.guhecloud.cube.onecard.server.consumption.vo.AmountChangeRecordVo;
import com.guhecloud.cube.onecard.server.consumption.vo.AmountChangeVo;
import com.guhecloud.cube.onecard.server.consumption.vo.AmountRecordExcelVo;
import com.guhecloud.cube.onecard.server.consumption.vo.TransactionTypeVo;
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
public class AmountChangeRecordController {

    private final AmountChangeRecordService amountChangeRecordService;

    /**
     * @Description 金额变更明细分页
     * @Date 2023/2/28 12:35
     * @Author tangzhiqiang
     */
    @GetMapping("/amount-change-record")
    public Pagination<AmountChangeRecordVo> getAmountChangeRecord(AmountRecordQueryParam param) {
        return amountChangeRecordService.getAmountChangeRecord(param);
    }

    /**
     * @Description 按消费类型统计变更金额
     * @Date 2023/3/1 14:23
     * @Author tangzhiqiang
     */
    @GetMapping("/amount-change-classification")
    public AmountChangeVo getAmountChangeClassification(AmountRecordQueryParam param) {
        return amountChangeRecordService.getAmountClassification(param);
    }

    /**
     * @Description 交易类型下拉框
     * @Date 2023/3/1 11:44
     * @Author tangzhiqiang
     */
    @GetMapping("/transaction-type")
    public List<TransactionTypeVo> getTransactionType() {
        return TransactionTypeEnum.list();
    }

    /**
     * @Description 金额变更记录导出
     * @Date 2023/3/2 10:38
     * @Author tangzhiqiang
     */
    @GetMapping("/amount-record-excel")
    public void downloadAmountRecordExcel(HttpServletResponse response, AmountRecordQueryParam param) throws Exception {
        Pagination<AmountChangeRecordVo> amountChangeRecord = amountChangeRecordService.getAmountChangeRecord(param);
        if (amountChangeRecord != null) {
            List<AmountChangeRecordVo> records = amountChangeRecord.getRecords();
            List<AmountRecordExcelVo> copying = ObjectUtils.copying(records, AmountRecordExcelVo.class);
            TemplateExportParams params = new TemplateExportParams("static/AmountRecordTemplate.xlsx", true);
            Map<String, Object> map = new HashMap<>();
            map.put("amountlist", copying);
            Workbook workbook = ExcelExportUtil.exportExcel(params, map);
            String fileName = URLEncoder.encode("金额变更明细.xlsx", "UTF-8");
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
