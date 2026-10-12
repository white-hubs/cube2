package com.guhecloud.cube.srv.gen.energymanagement.server.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.style.StyleUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyCollectConfig;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyDailyUsage;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyDailyUsageProject;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.EnergyDailyUsageParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.EnergyDailyUsageStatParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.EnergyRingRatioQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.EnergyTrendUsageParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.EnergyCollectConfigService;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.EnergyDailyUsageProjectService;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.EnergyDailyUsageService;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyDailyUsageForExportVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyDailyUsageProjectForExportVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyDailyUsageProjectResultVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyDailyUsageProjectVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyDailyUsageResultVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyDailyUsageRingRatioVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyDailyUsageStatVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyDailyUsageVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyProjectAreaStatVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyTypeStatVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyUsageStatWithQoqVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.TrendStatVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.cloud.util.ExcelUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.guhecloud.platform.base.constants.DateFormat.DEFAULT_DATE_FORMAT;

/**
 * @author appet
 * @Description 每日能耗用量统计
 * @date 2022-07-04 15:12:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EnergyDailyUsageController {

    private final EnergyDailyUsageService energyDailyUsageService;
    private final EnergyDailyUsageProjectService energyDailyUsageProjectService;
    private final EnergyCollectConfigService energyCollectConfigService;

    /**
     * 分类的每日分页列表
     *
     * @param param 条件
     *
     * @return 列表
     */
    @GetMapping("/energy-daily-usage/typePage")
    public Pagination<EnergyDailyUsageResultVO> getTypePage(EnergyDailyUsageParam param) {
        return energyDailyUsageService.getTypePage(param);
    }

    /**
     * 分项每日分页列表
     *
     * @param param 条件
     *
     * @return 分页列表
     */
    @GetMapping("/energy-daily-usage/projectPage")
    public Pagination<EnergyDailyUsageProjectResultVO> getProjectPage(EnergyDailyUsageParam param) {
        return energyDailyUsageProjectService.getProjectPage(param);
    }

    /**
     * 分页查询
     * 按分类统计的每日用量
     *
     * @param param
     *
     * @return
     */
    @GetMapping(value = "/energy-daily-usage/category")
    public Pagination<EnergyDailyUsageVO> getPageForCategory(EnergyDailyUsageParam param) {
        return energyDailyUsageService.getPageForCategory(param);
    }

    /**
     * 分页查询
     * 按分项/区统计的每日用量
     *
     * @param param
     *
     * @return
     */
    @GetMapping(value = "/energy-daily-usage/project")
    public Pagination<EnergyDailyUsageProjectVO> getPageForProject(EnergyDailyUsageParam param) {
        return energyDailyUsageService.getPageForProject(param);
    }

    /**
     * 根据类型和时间统计用量
     * 日月年用量并带有环比
     *
     * @param param
     *
     * @return
     */
    @GetMapping(value = "/energy-daily-usage/stat/dmy")
    public EnergyDailyUsageStatVO getUsageStatForDMY(EnergyDailyUsageParam param) {
        return energyDailyUsageService.getUsageStatForDMY(param);
    }

    /**
     * 统计日月年的碳排并带有环比
     * 通过exportType：CARBON 碳排 ALL 综合
     *
     * @param param
     *
     * @return
     */
    @GetMapping(value = "/energy-daily-usage/stat/carbon/dmy")
    public EnergyDailyUsageStatVO getUsageStatWithCarbonForDMY(EnergyDailyUsageParam param) {
        return energyDailyUsageService.getUsageStatWithCarbonForDMY(param);
    }

    /**
     * 分项趋势统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping(value = "/energy-daily-usage/project/stat/trend")
    public List<TrendStatVO> getProjectUsageStatForTrend(EnergyTrendUsageParam param) {
        return energyDailyUsageProjectService.getProjectUsageStatForTrend(param);
    }

    /**
     * 碳排按时间统计，
     * 满足趋势相关功能
     *
     * @param param
     *
     * @return
     */
    @GetMapping(value = "/energy-daily-usage/dc/stat/trend")
    public List<TrendStatVO> getUsageToDcStatForTrend(EnergyTrendUsageParam param) {
        return energyDailyUsageService.getUsageToDcStatForTrend(param);
    }

    /**
     * 分项各种类型统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping(value = "/energy-daily-usage/project/stat/time/category")
    public List<TrendStatVO> getProjectUsageStatByTimeAndCategory(EnergyTrendUsageParam param) {
        return energyDailyUsageProjectService.getProjectUsageStatByTime(param);
    }

    /**
     * 碳排按照时间和类型统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping(value = "/energy-daily-usage/dc/stat/time/category")
    public List<TrendStatVO> getUsageToDcStatByTimeAndType(EnergyTrendUsageParam param) {
        return energyDailyUsageService.getUsageToDcStatByTimeAndType(param);
    }

    /**
     * 分项分类占比统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping(value = "/energy-daily-usage/project/stat/category")
    public List<TrendStatVO> getProjectUsageStatByCategory(EnergyTrendUsageParam param) {
        return energyDailyUsageProjectService.getProjectUsageStatByCategory(param);
    }

    /**
     * 所有能耗分类的用量统计以及碳排
     *
     * @param param
     *
     * @return
     */
    @GetMapping(value = "/energy-daily-usage/stat/category/all")
    public List<EnergyUsageStatWithQoqVO> getUsageStatWithCarbonByTypeForAll(EnergyTrendUsageParam param) {
        return energyDailyUsageService.getUsageStatWithCarbonByTypeForAll(param);
    }

    /**
     * 能耗流向，基于区域分项统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping(value = "/energy-daily-usage-project/area/stat/flow")
    public Map<String, List<Map<String, String>>> getProjectUsageStatByTypeAndProject(EnergyTrendUsageParam param) {
        return energyDailyUsageProjectService.getProjectUsageStatByTypeAndProject(param);
    }

    /**
     * 能耗分类用量导入
     *
     * @param request
     * @param response
     */
    @PostMapping(value = "/energy-daily-usage/template")
    public void energyDailyUsageTemplate(HttpServletRequest request,
                                         HttpServletResponse response
                                        ) {
        //导出excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.merge(2, "能耗分类导入数据模板", true);
        CellStyle firstCellStyle = writer.getHeadCellStyle();
        StyleUtil.setColor(firstCellStyle, IndexedColors.LIGHT_GREEN, FillPatternType.SOLID_FOREGROUND);
        Workbook workbook = writer.getWorkbook();
        Font firstCellFont = workbook.createFont();
        firstCellFont.setBold(true);
        firstCellFont.setFontHeight((short) 500);
        firstCellStyle.setFont(firstCellFont);
        writer.setRowHeight(0, 30);
        //定义表头
        String[] header = { "日期", "能源分类", "用量（kwh）" };
        writer.writeRow(Arrays.asList(header));
        writer.setRowHeight(1, 25);
        writer.setColumnWidth(-1, 30);
        CellStyle cellStyle = writer.getCellStyle();
        //设置单元格自动换行
        cellStyle.setWrapText(true);
        //能耗类型
        //查询能源分类配置
        List<EnergyCollectConfig> configList = energyCollectConfigService.list();
        List<String> categoryList = configList.stream().map(c -> c.getCategoryName() + "#" + c.getCategoryCode())
                .collect(Collectors.toList());
        writer.addSelect(new CellRangeAddressList(2, 65535, 1, 1), categoryList.toArray(categoryList.toArray(new String[0])));
        //日期列加上限制
        CellRangeAddressList dateCellRange = new CellRangeAddressList(2, 65535, 0, 0);
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

        String fileName = "能耗分类导入数据模板";
        ExcelUtils.downloadExcel(request, response, fileName, writer);
    }

    /**
     * 能耗分类用量数据导入
     *
     * @param file
     */
    @PostMapping(value = "/energy-daily-usage/import")
    public void energyDailyUsageImport(@RequestParam("file") MultipartFile file) {
        Assert.isNotFound(file, "请选择要导入的文件");
        try {
            InputStream inputStream = file.getInputStream();
            ExcelReader reader = ExcelUtil.getReader(inputStream);
            reader.addHeaderAlias("日期", "statDate");
            reader.addHeaderAlias("能源分类", "instrumentTypeName");
            reader.addHeaderAlias("用量（kwh）", "dValue");
            List<EnergyDailyUsage> dataList = reader.read(1, 2, EnergyDailyUsage.class);
            //            log.info("导入数据：" + JsonUtils.serialize(dataList));
            if (ListUtils.nonEmpty(dataList)) {
                dataList.forEach(d -> {
                    //查找仪表类型
                    if (StringUtils.isNotBlank(d.getInstrumentTypeName())) {
                        String[] info = d.getInstrumentTypeName().split("#");
                        d.setInstrumentType(info[1]);
                        d.setInstrumentTypeName(info[0]);
                    }
                    d.setTenant(SessionContextHolder.getRequester().getTenant());
                    d.setDeleted(false);
                    EntityAuditor.populateAudit(d);
                });
                energyDailyUsageService.batchSaveOrUpdateEnergyDailyUsage(dataList);
            }
        } catch (Exception e) {
            log.error("能耗分类日用量导入失败", e);
        }
    }

    /**
     * 能耗分类日报表
     *
     * @param param
     * @param request
     * @param response
     */
    @PostMapping(value = "/energy-daily-usage/export")
    public void energyDailyUsageExport(@RequestBody EnergyDailyUsageParam param,
                                       HttpServletRequest request, HttpServletResponse response
                                      ) {
        Period period = param.getPeriod();
        List<EnergyDailyUsage> dataList = this.energyDailyUsageService.list(Wrappers.<EnergyDailyUsage>lambdaQuery()
                                                                                    .eq(StringUtils.isNotBlank(param.getInstrumentType()), EnergyDailyUsage::getInstrumentType, param.getInstrumentType())
                                                                                    .between(period !=
                                                                                             null, EnergyDailyUsage::getStatDate, ObjectUtils.ifNull(period, p -> p.getBegin()), ObjectUtils.ifNull(period, p -> p.getEnd())));
        Assert.isNotAcceptable(ListUtils.isEmpty(dataList), "未查询到数据，无法导出");
        //格式化数据
        List<EnergyDailyUsageForExportVO> finalDataList = dataList.stream().map(data -> {
            EnergyDailyUsageForExportVO export = ObjectUtils.copying(data, EnergyDailyUsageForExportVO.class);
            export.setStatDateStr(DateUtils.format(export.getStatDate(), DEFAULT_DATE_FORMAT));
            export.setDdValue(data.getDValue());
            return export;
        }).collect(Collectors.toList());
        //导出excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer = this.buildWriterHeader(writer, param.getProjectCategory(), param.getExportType());

        writer.setOnlyAlias(true);
        //默认配置
        writer.write(finalDataList, true);
        writer.setColumnWidth(-1, 25);

        String fileName = "能耗分类日报表";
        ExcelUtils.downloadExcel(request, response, fileName, writer);
    }

    /**
     * 能耗分项导出
     *
     * @param param
     * @param request
     * @param response
     */
    @PostMapping(value = "/energy-daily-usage/project/export")
    public void energyDailyUsageForProjectExport(@RequestBody EnergyDailyUsageParam param,
                                                 HttpServletRequest request, HttpServletResponse response
                                                ) {
        Period period = param.getPeriod();
        List<EnergyDailyUsageProject> dataList = this.energyDailyUsageProjectService.list(Wrappers.<EnergyDailyUsageProject>lambdaQuery()
                                                                                                  .eq(StringUtils.isNotEmpty(param.getProjectCategory()), EnergyDailyUsageProject::getProjectCategory, param.getProjectCategory())
                                                                                                  .eq(StringUtils.isNotBlank(param.getInstrumentType()), EnergyDailyUsageProject::getInstrumentType, param.getInstrumentType())
                                                                                                  .eq(param.getProjectId() !=
                                                                                                      null, EnergyDailyUsageProject::getProjectId, param.getProjectId())
                                                                                                  .between(period !=
                                                                                                           null, EnergyDailyUsageProject::getStatDate, ObjectUtils.ifNull(period, p -> p.getBegin()), ObjectUtils.ifNull(period, p -> p.getEnd())));
        Assert.isNotAcceptable(ListUtils.isEmpty(dataList), "未查询到数据，无法导出");
        //格式化数据
        List<EnergyDailyUsageProjectForExportVO> finalDataList = dataList.stream().map(data -> {
            EnergyDailyUsageProjectForExportVO export = ObjectUtils.copying(data, EnergyDailyUsageProjectForExportVO.class);
            export.setStatDateStr(DateUtils.format(export.getStatDate(), DEFAULT_DATE_FORMAT));
            return export;
        }).collect(Collectors.toList());
        //导出excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer = this.buildWriterHeader(writer, param.getProjectCategory(), param.getExportType());

        writer.setOnlyAlias(true);
        //默认配置
        writer.write(finalDataList, true);
        writer.setColumnWidth(-1, 25);

        String fileName = "能耗分项日报表-" + param.getProjectCategory();
        ExcelUtils.downloadExcel(request, response, fileName, writer);
    }

    /**
     * 查询单个用能类型统计
     *
     * @param param period 时间段 instrumentType 用能类型
     *
     * @return 用能 碳排 综合能耗 f p g j
     */
    @GetMapping("/energy-daily-usage/stat/type/single")
    public EnergyTypeStatVO getDailyUsageStatType(EnergyDailyUsageStatParam param) {
        return energyDailyUsageService.getDailyUsageStatType(param);
    }

    /**
     * 所有开启的用能类型统计
     *
     * @param param 时间段
     *
     * @return 每个分类的 用量 碳排 综合能耗 f p g j
     */
    @GetMapping("/energy-daily-usage/stat/type/list")
    public List<EnergyTypeStatVO> getDailyUsageStat(EnergyDailyUsageStatParam param) {
        return energyDailyUsageService.getDailyUsageStatOn(param);
    }

    /**
     * 所有开启的用能类型总和统计(类型加起来)
     *
     * @param param 时间段
     *
     * @return 用能 碳排 综合能耗 f p g j
     */
    @GetMapping("/energy-daily-usage/stat/all/single")
    public EnergyTypeStatVO getDailyUsageStatAllOn(EnergyDailyUsageStatParam param) {
        return energyDailyUsageService.getDailyUsageStatAllOn(param);
    }

    /**
     * 分项/分区 汇总统计总和统计(分项/分区加起来)
     * 如果不传id就是一级分项/分区汇总起来
     * 如果传入id就是传入的id汇总起来
     *
     * @param param 条件
     *
     * @return 汇总
     */
    @GetMapping("/energy-daily-usage/stat/project/single")
    public EnergyProjectAreaStatVO getDailyUsageProjectStatAllOne(EnergyTrendUsageParam param) {
        return energyDailyUsageProjectService.getDailyUsageProjectStatAllOne(param);
    }

    /**
     * 环比
     *
     * @param param 条件
     *
     * @return 环比
     */
    @GetMapping("/energy-daily-usage/stat/type/ringRatio")
    public EnergyDailyUsageRingRatioVO getDailyUsageRingRatio(EnergyRingRatioQueryParam param) {
        return energyDailyUsageService.getDailyUsageRingRatio(param);
    }

    /**
     * excel表头处理
     *
     * @param writer
     * @param projectCategory CATEGORY分类 AREA区域 ITEM分项 ROUTE支路
     * @param exportType      USAGE 用量 CARBON 碳排 ALL 综合
     *
     * @return
     */
    private ExcelWriter buildWriterHeader(ExcelWriter writer, String projectCategory, String exportType) {
        if ("CATEGORY".equals(projectCategory) && "USAGE".equals(exportType)) {
            writer.addHeaderAlias("statDateStr", "日期");
            writer.addHeaderAlias("instrumentTypeName", "能源分类");
            writer.addHeaderAlias("dValue", "用量(kwh)");
            writer.addHeaderAlias("fValue", "峰用量(kwh)");
            writer.addHeaderAlias("pValue", "平用量(kwh)");
            writer.addHeaderAlias("gValue", "谷用量(kwh)");
            writer.addHeaderAlias("jValue", "尖用量(kwh)");
        } else if ("CATEGORY".equals(projectCategory) && "CARBON".equals(exportType)) {
            writer.addHeaderAlias("statDateStr", "日期");
            writer.addHeaderAlias("instrumentTypeName", "能源分类");
            writer.addHeaderAlias("dcValue", "碳排量 tCO2e");
        } else if ("CATEGORY".equals(projectCategory) && "ALL".equals(exportType)) {
            writer.addHeaderAlias("statDateStr", "日期");
            writer.addHeaderAlias("instrumentTypeName", "能源分类");
            writer.addHeaderAlias("zhValue", "综合能耗tce");
        } else if ("ITEM".equals(projectCategory) && "USAGE".equals(exportType)) {
            writer.addHeaderAlias("statDateStr", "日期");
            writer.addHeaderAlias("projectCode", "编码");
            writer.addHeaderAlias("projectName", "分项名称");
            writer.addHeaderAlias("instrumentTypeName", "能源分类");
            writer.addHeaderAlias("dValue", "用量");
        } else if ("ITEM".equals(projectCategory) && "CARBON".equals(exportType)) {
            writer.addHeaderAlias("statDateStr", "日期");
            writer.addHeaderAlias("projectName", "分项名称");
            writer.addHeaderAlias("instrumentTypeName", "能源分类");
            writer.addHeaderAlias("dcValue", "碳排量 tCO2e");
        } else if ("ITEM".equals(projectCategory) && "ALL".equals(exportType)) {
            writer.addHeaderAlias("statDateStr", "日期");
            writer.addHeaderAlias("projectName", "分项名称");
            writer.addHeaderAlias("instrumentTypeName", "能源分类");
            writer.addHeaderAlias("zhValue", "综合能耗tce");
        } else if ("AREA".equals(projectCategory) && "USAGE".equals(exportType)) {
            writer.addHeaderAlias("statDateStr", "日期");
            writer.addHeaderAlias("projectName", "区域名称");
            writer.addHeaderAlias("instrumentTypeName", "能源分类");
            writer.addHeaderAlias("dValue", "用量");
        } else if ("AREA".equals(projectCategory) && "CARBON".equals(exportType)) {
            writer.addHeaderAlias("statDateStr", "日期");
            writer.addHeaderAlias("projectName", "区域名称");
            writer.addHeaderAlias("instrumentTypeName", "能源分类");
            writer.addHeaderAlias("dcValue", "碳排量 tCO2e");
        } else if ("AREA".equals(projectCategory) && "ALL".equals(exportType)) {
            writer.addHeaderAlias("statDateStr", "日期");
            writer.addHeaderAlias("projectName", "区域名称");
            writer.addHeaderAlias("instrumentTypeName", "能源分类");
            writer.addHeaderAlias("zhValue", "综合能耗tce");
        }
        return writer;
    }
}
