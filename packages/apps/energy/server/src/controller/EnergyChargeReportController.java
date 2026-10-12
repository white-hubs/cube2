package com.guhecloud.cube.srv.gen.energymanagement.server.controller;

import com.guhecloud.cube.srv.gen.energymanagement.server.param.EnergyChargeReportQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.EnergyChargeReportService;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.ChargeTotalReportVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.ChargeTypeReportVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyChargeRatioVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.TrendResultVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 费用报表控制层
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EnergyChargeReportController {
    private final EnergyChargeReportService energyChargeReportService;

    /**
     * 获取电的费用报表
     *
     * @param param 条件
     *
     * @return 电费用报表
     */
    @GetMapping("/energy/report/electricity/page")
    public Pagination<ChargeTypeReportVO> getChargeElectricityReportPage(EnergyChargeReportQueryParam param) {
        this.paramDefaultPeriod(param);
        param.setInstrumentTypeList(Collections.singletonList("electricity_meter"));
        return energyChargeReportService.getChargeTypeReportPage(param);
    }

    /**
     * 获取除电以外的分类的费用报表
     *
     * @param param 条件
     *
     * @return 分类的费用报表
     */
    @GetMapping("/energy/report/typeOther/page")
    public Pagination<ChargeTypeReportVO> getChargeTypeOtherReportPage(EnergyChargeReportQueryParam param) {
        this.paramDefaultPeriod(param);
        return energyChargeReportService.getChargeTypeReportPage(param);
    }

    /**
     * 获取总费用报表
     *
     * @param param 条件
     *
     * @return 总费用报表
     */
    @GetMapping("/energy/report/total/page")
    public Pagination<ChargeTotalReportVO> getChargeTotalReportPage(EnergyChargeReportQueryParam param) {
        this.paramDefaultPeriod(param);
        return energyChargeReportService.getChargeTotalReportPage(param);
    }

    /**
     * 电费报表导出
     *
     * @param param 条件
     */
    @GetMapping("/energy/report/electricity/import")
    public void getChargeElectricityReportImport(EnergyChargeReportQueryParam param, HttpServletRequest request,
                                                 HttpServletResponse response
                                                ) {
        this.paramDefaultPeriod(param);
        param.setInstrumentTypeList(Collections.singletonList("electricity_meter"));
        energyChargeReportService.getChargeElectricityReportImport(param, request, response);
    }

    /**
     * 除电费外的其他费用报表导出
     *
     * @param param 条件
     */
    @GetMapping("/energy/report/typeOther/import")
    public void getChargeTypeOtherReportImport(EnergyChargeReportQueryParam param, HttpServletRequest request,
                                               HttpServletResponse response
                                              ) {
        Assert.isParameterError(CollectionUtils.isEmpty(param.getInstrumentTypeList()), "类型不能为空！");
        this.paramDefaultPeriod(param);
        energyChargeReportService.getChargeTypeOtherReportImport(param, request, response);
    }

    /**
     * 总费用报表导出
     *
     * @param param 条件
     */
    @GetMapping("/energy/report/total/import")
    public void getChargeTotalReportImport(EnergyChargeReportQueryParam param, HttpServletRequest request, HttpServletResponse response) {
        this.paramDefaultPeriod(param);
        energyChargeReportService.getChargeTotalReportImport(param, request, response);
    }

    /**
     * 综合费用
     *
     * @param param 条件
     *
     * @return 综合费用
     */
    @GetMapping("/energy/report/comprehensiveRatioCost")
    public Map<String, EnergyChargeRatioVO> getComprehensiveRatioCost(EnergyChargeReportQueryParam param) {
        this.paramDefaultPeriod(param);
        return energyChargeReportService.getComprehensiveRatioCost(param);
    }

    /**
     * 获取费用趋势（单个类型或总费用趋势）
     *
     * @param param 条件
     *
     * @return 趋势
     */
    @GetMapping("/energy/report/chargeTrend")
    public List<TrendResultVO> getChargeReportTrend(EnergyChargeReportQueryParam param) {
        this.paramDefaultPeriod(param);
        return energyChargeReportService.getChargeReportTrend(param);
    }

    /**
     * 获取费用趋势(多个类型趋势同时返回)
     *
     * @param param 条件
     *
     * @return 趋势
     */
    @GetMapping("/energy/report/chargeBathTrend")
    public Map<String, List<TrendResultVO>> getChargeReportBathTrend(EnergyChargeReportQueryParam param) {
        this.paramDefaultPeriod(param);
        return energyChargeReportService.getChargeReportBathTrend(param);
    }

    /**
     * 费用占比
     * 按类型分类，统计每个类型在一段时间的合计费用
     *
     * @param param 条件
     *
     * @return 各类型一段时间的和费用
     */
    @GetMapping("/energy/report/pieChart")
    public Map<String, BigDecimal> getPieChart(EnergyChargeReportQueryParam param) {
        this.paramDefaultPeriod(param);
        return energyChargeReportService.getCostTypeSum(param);
    }

    private void paramDefaultPeriod(EnergyChargeReportQueryParam param) {
        if (param.getPeriod() == null || param.getPeriod().getBegin() == null || param.getPeriod().getEnd() == null) {
            param.setPeriod(new Period(DateUtils.beginOfMonth(LocalDateTime.now()), DateUtils.endOfMonth(LocalDateTime.now())));
        }
    }

}
