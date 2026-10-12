package com.guhecloud.cube.srv.gen.product.market.server.pricing.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.style.StyleUtil;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsSafetyCheck;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.enumeration.GoodsCheckResultEnum;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsSafetyCheckParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsSafetyCheckQueryParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.service.GoodsSafetyCheckService;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsSafetyCheckVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.cloud.util.ExcelUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * @author Appet
 * @Description 食品安全检查
 * @date 2023-12-04 10:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GoodsSafetyCheckController {
    private final GoodsSafetyCheckService goodsSafetyCheckService;

    /**
     * 功能描述： 食品安全检查添加
     */
    @PostMapping(value = "/goods-safety-check")
    public Long add(@Valid @RequestBody GoodsSafetyCheckParam param) {
        return goodsSafetyCheckService.add(param);
    }

    /**
     * 功能描述： 食品安全检查修改
     */
    @PutMapping(value = "/goods-safety-check/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody GoodsSafetyCheckParam param) {
        goodsSafetyCheckService.update(id, param);
    }

    /**
     * 功能描述： 食品安全检查删除
     */
    @DeleteMapping(value = "/goods-safety-check/{id}")
    public void delete(@PathVariable Long id) {
        goodsSafetyCheckService.delete(id);
    }

    /**
     * 功能描述： 食品安全检查批量删除
     */
    @DeleteMapping(value = "/goods-safety-check/batchdelete")
    public void deleteBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") List<Long> ids) {
        goodsSafetyCheckService.batchDelete(ids);
    }

    /**
     * 功能描述： 食品安全检查分页
     */
    @GetMapping(value = "/goods-safety-check/page")
    public Pagination<GoodsSafetyCheckVO> getPage(GoodsSafetyCheckQueryParam param) {
        return goodsSafetyCheckService.getPage(param);
    }

    /**
     * 功能描述： 食品安全检查详情
     */
    @GetMapping(value = "/goods-safety-check/{id}")
    public GoodsSafetyCheckVO getById(@PathVariable Long id) {
        return goodsSafetyCheckService.getById(id);
    }

    /**
     * 食安检查模板下载
     *
     * @param request
     * @param response
     */
    @PostMapping(value = "/goods-safety-check/template")
    public void GoodsSafetyCheckTemplate(HttpServletRequest request,
                                         HttpServletResponse response
                                        ) {
        //导出excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.merge(6, "食安检测数据模板", true);
        CellStyle firstCellStyle = writer.getHeadCellStyle();
        StyleUtil.setColor(firstCellStyle, IndexedColors.LIGHT_GREEN, FillPatternType.SOLID_FOREGROUND);
        Workbook workbook = writer.getWorkbook();
        Font firstCellFont = workbook.createFont();
        firstCellFont.setBold(true);
        firstCellFont.setFontHeight((short) 500);
        firstCellStyle.setFont(firstCellFont);
        writer.setRowHeight(0, 30);
        //定义表头
        String[] header = { "档位号", "样品名称", "来源地", "检测日期", "检测项目", "检测结果", "样品编号" };
        writer.writeRow(Arrays.asList(header));
        writer.setRowHeight(1, 25);
        writer.setColumnWidth(-1, 25);
        CellStyle cellStyle = writer.getCellStyle();
        //设置单元格自动换行
        cellStyle.setWrapText(true);
        writer.addSelect(new CellRangeAddressList(2, 2000, 5, 5), "阳性", "阴性");

        CellRangeAddressList dateCellRange = new CellRangeAddressList(1, 65535, 3, 3);
        DataValidationHelper helper = writer.getSheet().getDataValidationHelper();
        String dateFormat = "yyyy-MM-dd";
        String start = "Date(2020,01,01)";
        String end = "Date(2999,12,30)";
        DataValidationConstraint dateCellValidate = helper.createDateConstraint(DataValidationConstraint.OperatorType.BETWEEN, start, end, dateFormat);
        DataValidation dateValidation = helper.createValidation(dateCellValidate, dateCellRange);
        //错误提示信息
        dateValidation.createErrorBox("提示", "请输入[yyyy-MM-dd]格式；且时间大于2020小于2999年");
        dateValidation.setShowErrorBox(true);
        writer.addValidationData(dateValidation);

        String fileName = "食安检查模板";
        ExcelUtils.downloadExcel(request, response, fileName, writer);
    }

    /**
     * 食安检查导入
     */
    @PostMapping(value = "/goods-safety-check/import")
    public void GoodsSafetyCheckImport(@RequestParam("file") MultipartFile file) {
        Assert.isNotFound(file, "请选择要导入的文件");
        try {
            InputStream inputStream = file.getInputStream();
            ExcelReader reader = ExcelUtil.getReader(inputStream);
            reader.addHeaderAlias("档位号", "stallCode");
            reader.addHeaderAlias("样品名称", "goodsCategoryName");
            reader.addHeaderAlias("来源地", "goodsFrom");
            reader.addHeaderAlias("检测日期", "checkDate");
            reader.addHeaderAlias("检测项目", "checkItem");
            reader.addHeaderAlias("检测结果", "checkResult");
            reader.addHeaderAlias("样品编号", "sampleCode");

            List<GoodsSafetyCheck> dataList = reader.read(1, 2, GoodsSafetyCheck.class);
            if (CollectionUtils.isNotEmpty(dataList)) {
                dataList.forEach(d -> {
                    if (!StringUtils.isEmpty(d.getCheckResult())) {
                        d.setCheckResult(GoodsCheckResultEnum.getCodeByLabel(d.getCheckResult().trim()));
                    }
                    d.setCheckerName(SecurityContextHolder.getPrincipal().getName());
                    d.setTenant(SessionContextHolder.getRequester().getTenant());
                    EntityAuditor.populateAudit(d);
                    d.setDeleted(false);
                });
                goodsSafetyCheckService.insertOrUpdateBatch(dataList);
            }
        } catch (Exception e) {
            log.error("导入食安检测数据失败", e);
        }
    }
}