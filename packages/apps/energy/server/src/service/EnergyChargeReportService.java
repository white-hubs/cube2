package com.guhecloud.cube.srv.gen.energymanagement.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyChargeReport;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.EnergyChargeReportQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.ChargeTotalReportVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.ChargeTypeReportVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyChargeRatioVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.TrendResultVO;
import com.guhecloud.platform.base.model.Pagination;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 用能费用报表服务
 */
public interface EnergyChargeReportService extends IService<EnergyChargeReport> {
    /**
     * 获取分类的费用报表
     *
     * @param param 条件
     *
     * @return 分类的费用报表
     */
    Pagination<ChargeTypeReportVO> getChargeTypeReportPage(EnergyChargeReportQueryParam param);

    /**
     * 获取总费用报表
     *
     * @param param 条件
     *
     * @return 总费用报表
     */
    Pagination<ChargeTotalReportVO> getChargeTotalReportPage(EnergyChargeReportQueryParam param);

    /**
     * 电费报表导出
     *
     * @param param 条件
     */
    void getChargeElectricityReportImport(EnergyChargeReportQueryParam param, HttpServletRequest request, HttpServletResponse response);

    /**
     * 除电费外的其他费用报表导出
     *
     * @param param 条件
     */
    void getChargeTypeOtherReportImport(EnergyChargeReportQueryParam param, HttpServletRequest request, HttpServletResponse response);

    /**
     * 总费用报表导出
     *
     * @param param 条件
     */
    void getChargeTotalReportImport(EnergyChargeReportQueryParam param, HttpServletRequest request, HttpServletResponse response);

    /**
     * 综合费用
     *
     * @param param 条件
     *
     * @return 综合费用
     */
    Map<String, EnergyChargeRatioVO> getComprehensiveRatioCost(EnergyChargeReportQueryParam param);

    /**
     * 获取费用趋势(单个类型或总费用趋势）
     *
     * @param param 条件
     *
     * @return 趋势
     */
    List<TrendResultVO> getChargeReportTrend(EnergyChargeReportQueryParam param);

    /**
     * 获取费用趋势(多个类型趋势同时返回)
     *
     * @param param 条件
     *
     * @return 趋势
     */
    Map<String, List<TrendResultVO>> getChargeReportBathTrend(EnergyChargeReportQueryParam param);

    /**
     * 各类型一段时间的和费用
     * 按类型分类，统计每个类型在一段时间的合计费用
     *
     * @param param 条件
     *
     * @return 各类型一段时间的和费用
     */
    Map<String, BigDecimal> getCostTypeSum(EnergyChargeReportQueryParam param);

}
