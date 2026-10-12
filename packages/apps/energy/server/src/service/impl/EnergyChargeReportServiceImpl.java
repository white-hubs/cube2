package com.guhecloud.cube.srv.gen.energymanagement.server.service.impl;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.EnergyChargeReportMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyChargeReport;
import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.InstrumentTypeEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.TimePeriodQueryEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.EnergyChargeReportQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.EnergyChargeReportService;
import com.guhecloud.cube.srv.gen.energymanagement.server.util.TrendTimeUtils;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.ChargeTotalReportVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.ChargeTypeReportVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyChargeRatioVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.TrendResultVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.cloud.util.ExcelUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 费用报表服务
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EnergyChargeReportServiceImpl extends ServiceImpl<EnergyChargeReportMapper, EnergyChargeReport> implements EnergyChargeReportService {

    private final EnergyChargeReportMapper energyChargeReportMapper;

    /**
     * 获取分类的费用报表分页
     *
     * @param param 条件
     *
     * @return 分类的费用报表
     */
    @Override
    public Pagination<ChargeTypeReportVO> getChargeTypeReportPage(EnergyChargeReportQueryParam param) {
        Page<EnergyChargeReport> page = this.page(PagingUtils.convert(param), this.getChargeTypeReportWrapper(param));
        return PagingUtils.convert(page, ChargeTypeReportVO.class);
    }

    /**
     * 获取总费用报表分页
     *
     * @param param 条件
     *
     * @return 总费用报表
     */
    @Override
    public Pagination<ChargeTotalReportVO> getChargeTotalReportPage(EnergyChargeReportQueryParam param) {
        Page<ChargeTotalReportVO> page = energyChargeReportMapper.getTypeDaySumCostPage(PagingUtils.convert(param), param);
        return PagingUtils.convert(page);
    }

    /**
     * 电费报表导出
     *
     * @param param 条件
     */
    @Override
    public void getChargeElectricityReportImport(EnergyChargeReportQueryParam param, HttpServletRequest request,
                                                 HttpServletResponse response
                                                ) {
        List<EnergyChargeReport> dataList = this.list(this.getChargeTypeReportWrapper(param));
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.addHeaderAlias("statDateStr", "日期");
        writer.addHeaderAlias("areaName", "区域");
        writer.addHeaderAlias("dvCost", "电费(元)");
        writer.addHeaderAlias("fvCost", "峰电费(元)");
        writer.addHeaderAlias("pvCost", "平电费(元)");
        writer.addHeaderAlias("gvCost", "谷电费(元)");
        writer.addHeaderAlias("jvCost", "尖电费(元)");
        writer.setOnlyAlias(true);
        //默认配置
        writer.write(dataList, true);
        writer.setColumnWidth(-1, 25);

        String fileName = "电费报表";
        ExcelUtils.downloadExcel(request, response, fileName, writer);
    }

    /**
     * 除电费外的其他费用报表导出
     *
     * @param param 条件
     */
    @Override
    public void getChargeTypeOtherReportImport(EnergyChargeReportQueryParam param, HttpServletRequest request,
                                               HttpServletResponse response
                                              ) {
        List<EnergyChargeReport> dataList = this.list(this.getChargeTypeReportWrapper(param));
        String typeName = InstrumentTypeEnum.getFireinspEnum(param.getInstrumentTypeList().get(BigDecimal.ZERO.intValue()));
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.addHeaderAlias("statDateStr", "日期");
        writer.addHeaderAlias("areaName", "区域");
        writer.addHeaderAlias("totalCost", typeName + "费(元)");
        writer.setOnlyAlias(true);
        //默认配置
        writer.write(dataList, true);
        writer.setColumnWidth(-1, 25);

        String fileName = typeName + "费报表";
        ExcelUtils.downloadExcel(request, response, fileName, writer);
    }

    /**
     * 总费用报表导出
     *
     * @param param 条件
     */
    @Override
    public void getChargeTotalReportImport(EnergyChargeReportQueryParam param, HttpServletRequest request, HttpServletResponse response) {
        List<ChargeTotalReportVO> dataList = energyChargeReportMapper.getTypeDaySumCostList(param);
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.addHeaderAlias("statDateStr", "日期");
        writer.addHeaderAlias("totalCost", "总费用(元)");
        writer.addHeaderAlias("electricityCost", "电费(元)");
        writer.addHeaderAlias("waterCost", "水费(元)");
        writer.addHeaderAlias("gasCost", "气费(元)");
        writer.setOnlyAlias(true);
        //默认配置
        writer.write(dataList, true);
        writer.setColumnWidth(-1, 25);

        String fileName = "总费用报表";
        ExcelUtils.downloadExcel(request, response, fileName, writer);
    }

    /**
     * 综合费用
     *
     * @param param 条件
     *
     * @return 综合费用
     */
    @Override
    public Map<String, EnergyChargeRatioVO> getComprehensiveRatioCost(EnergyChargeReportQueryParam param) {
        Map<String, EnergyChargeRatioVO> result = new HashMap<>();
        Map<String, BigDecimal> current = this.getCostTypeSum(param);
        EnergyChargeReportQueryParam beforeParam = this.getRatioParam(param);
        Map<String, BigDecimal> before = this.getCostTypeSum(beforeParam);
        final BigDecimal[] currentTotal = { BigDecimal.ZERO };
        final BigDecimal[] beforeTotal = { BigDecimal.ZERO };
        current.forEach((type, cost) -> {
            if (cost != null) {
                currentTotal[BigDecimal.ZERO.intValue()] = currentTotal[BigDecimal.ZERO.intValue()].add(cost);
            }
            BigDecimal befVal = before.get(type);
            if (befVal != null) {
                beforeTotal[BigDecimal.ZERO.intValue()] = beforeTotal[BigDecimal.ZERO.intValue()].add(befVal);
            }
            result.put(type, this.handlerRatio(cost, befVal));
        });
        result.put("total", this.handlerRatio(currentTotal[BigDecimal.ZERO.intValue()], beforeTotal[BigDecimal.ZERO.intValue()]));

        return result;
    }

    /**
     * 各类型一段时间的和费用
     * 按类型分类，统计每个类型在一段时间的合计费用
     *
     * @param param 条件
     *
     * @return 各类型一段时间的和费用
     */
    @Override
    public Map<String, BigDecimal> getCostTypeSum(EnergyChargeReportQueryParam param) {
        Map<String, BigDecimal> result = new HashMap<>();
        List<ChargeTypeReportVO> typeReportVOList = energyChargeReportMapper.getTypePeriodSumCost(param);
        // 如果入参的类型集合不为空就返回入参的类型，否则返回查询到的类型
        if (!CollectionUtils.isEmpty(param.getInstrumentTypeList())) {
            Map<String, BigDecimal> typeReportMap = new HashMap<>();
            Optional.ofNullable(typeReportVOList).orElse(new ArrayList<>())
                    .forEach(v -> typeReportMap.put(v.getInstrumentType(), v.getTotalCost()));
            param.getInstrumentTypeList()
                    .forEach(type -> result.put(type, Optional.ofNullable(typeReportMap.get(type)).orElse(BigDecimal.ZERO)));
        } else {
            Optional.ofNullable(typeReportVOList).orElse(new ArrayList<>())
                    .forEach(t -> result.put(t.getInstrumentType(), t.getTotalCost()));
        }

        return result;
    }

    /**
     * 获取费用趋势(单个类型或总费用趋势）
     *
     * @param param 条件
     *
     * @return 趋势
     */
    @Override
    public List<TrendResultVO> getChargeReportTrend(EnergyChargeReportQueryParam param) {
        TrendTimeUtils.setFormatAndPeriod(param);
        return energyChargeReportMapper.selectTrend(param);
    }

    /**
     * 获取费用趋势(多个类型趋势同时返回)
     *
     * @param param 条件
     *
     * @return 趋势
     */
    @Override
    public Map<String, List<TrendResultVO>> getChargeReportBathTrend(EnergyChargeReportQueryParam param) {
        Map<String, List<TrendResultVO>> result = new HashMap<>();
        Assert.isParameterError(CollectionUtils.isEmpty(param.getInstrumentTypeList()), "类型不能为空");
        TrendTimeUtils.setFormatAndPeriod(param);
        for (String type : param.getInstrumentTypeList()) {
            EnergyChargeReportQueryParam typeParam = new EnergyChargeReportQueryParam();
            BeanUtils.copyProperties(param, typeParam);
            typeParam.setInstrumentTypeList(Collections.singletonList(type));
            result.put(type, energyChargeReportMapper.selectTrend(typeParam));
        }
        return result;
    }

    private EnergyChargeReportQueryParam getRatioParam(EnergyChargeReportQueryParam param) {
        EnergyChargeReportQueryParam result = new EnergyChargeReportQueryParam();
        BeanUtils.copyProperties(param, result);
        if (TimePeriodQueryEnum.THIS_YEAR.name().equals(param.getTimePeriodType())) {
            result.setPeriod(new Period(DateUtils.offsetMonth(param.getPeriod().getBegin(), -12), DateUtils.offsetMonth(param.getPeriod()
                                                                                                                                .getEnd(), -12)));
        } else if (TimePeriodQueryEnum.THIS_WEEK.name().equals(param.getTimePeriodType())) {
            result.setPeriod(new Period(DateUtils.offsetDay(param.getPeriod().getBegin(), -7), DateUtils.offsetDay(param.getPeriod()
                                                                                                                           .getEnd(), 7)));
        } else {
            result.setPeriod(new Period(DateUtils.offsetMonth(param.getPeriod().getBegin(), -1), DateUtils.offsetMonth(param.getPeriod()
                                                                                                                               .getEnd(), -1)));
        }
        return result;
    }

    private EnergyChargeRatioVO handlerRatio(BigDecimal current, BigDecimal before) {
        if (current == null) {
            current = BigDecimal.ZERO;
        }
        if (before == null) {
            before = BigDecimal.ZERO;
        }
        EnergyChargeRatioVO result = new EnergyChargeRatioVO();
        result.setCurrent(current);
        result.setBefore(before);
        if (BigDecimal.ZERO.compareTo(before) != BigDecimal.ZERO.intValue()) {
            result.setRingRatio((current.subtract(before)).divide(before, BigDecimal.ROUND_CEILING, RoundingMode.HALF_UP));
        }
        return result;
    }

    private Wrapper<EnergyChargeReport> getChargeTypeReportWrapper(EnergyChargeReportQueryParam param) {
        Period period = param.getPeriod();
        boolean periodBool = period != null && period.getBegin() != null && period.getEnd() != null;
        return Wrappers.<EnergyChargeReport>lambdaQuery()
                .eq(param.getId() != null, EnergyChargeReport::getId, param.getId())
                .between(periodBool, EnergyChargeReport::getStatDate, ObjectUtils.ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, Period::getEnd))
                .in(!CollectionUtils.isEmpty(param.getInstrumentTypeList()), EnergyChargeReport::getInstrumentType, param.getInstrumentTypeList())
                .orderByDesc(EnergyChargeReport::getStatDate)
                .orderByAsc(EnergyChargeReport::getAreaName, EnergyChargeReport::getAreaId);
    }
}
