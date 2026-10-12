package com.guhecloud.cube.srv.gen.safety.management.monthlyreport.controller;

import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.dto.request.MarketMonthlyReportModifyRequest;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.dto.request.query.MarketMonthlyReportQuery;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.dto.vo.MarketMonthlyReportVO;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.enumeration.MarketMonthlyReportStatusEnum;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.po.MarketMonthlyReportPO;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.service.MarketMonthlyReportService;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.service.MarketMonthlyWindUpService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 月报管理
 *
 * @Author: lijie
 * @CreateTime: 2023-09-07  15:52
 * @Description:
 * @Version: 1.0
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MarketMonthlyReportController {
    private final MarketMonthlyReportService marketMonthlyReportService;

    private final MarketMonthlyWindUpService marketMonthlyWindUpService;

    /**
     * 分页查询
     *
     * @param query 筛选条件
     *
     * @return 查询结果
     */
    @GetMapping("/market-monthly-report/queryByPage")
    public Pagination<MarketMonthlyReportVO> queryByPage(@Valid MarketMonthlyReportQuery query) {
        return marketMonthlyReportService.queryByPage(query);
    }

    /**
     * 根据总部月报id获取市场月报列表
     *
     * @param monthlyReportId
     *
     * @return
     */
    @GetMapping("/market-monthly-reports/{monthlyReportId}/by-monthly-report")
    public List<MarketMonthlyReportVO> getMarketReportsByReportId(@PathVariable("monthlyReportId") Long monthlyReportId) {
        return marketMonthlyReportService.getMarketReportsByMonthlyReportId(monthlyReportId);
    }

    /**
     * 根据总部月报id获取已上报市场月报列表
     *
     * @param monthlyReportId
     *
     * @return
     */
    @GetMapping("/public/market-monthly-reports/wind-up/{monthlyReportId}/by-monthly-report")
    public List<MarketMonthlyReportVO> getWindUpMarketReportsByMonthlyReportId(@PathVariable("monthlyReportId") Long monthlyReportId) {
        return marketMonthlyWindUpService.getWindUpMarketReportsByMonthlyReportId(monthlyReportId);
    }

    /**
     * 根据总部月报id获取未上报市场月报列表
     *
     * @param monthlyReportId
     *
     * @return
     */
    @GetMapping("/public/market-monthly-reports/non-wind-up/{monthlyReportId}/by-monthly-report")
    public List<MarketMonthlyReportVO> getNonWindUpMarketReportByMonthlyReportId(@PathVariable("monthlyReportId") Long monthlyReportId) {
        List<MarketMonthlyReportVO> marketReportsByMonthlies = marketMonthlyReportService.getMarketReportsByMonthlyReportId(monthlyReportId);
        List<MarketMonthlyReportVO> windUpMarketReportsByMonthlies = marketMonthlyWindUpService.getWindUpMarketReportsByMonthlyReportId(monthlyReportId);
        Set<Integer> winUpTenants = windUpMarketReportsByMonthlies.stream().map(MarketMonthlyReportVO::getMarketTenant)
                .collect(Collectors.toSet());
        return marketReportsByMonthlies.stream()
                .filter(marketMonthlyReportVO -> !winUpTenants.contains(marketMonthlyReportVO.getMarketTenant()))
                .collect(Collectors.toList());
    }

    /**
     * 月报详情
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/market-monthly-report/{id}")
    public MarketMonthlyReportVO getDetail(@PathVariable("id") Long id) {
        MarketMonthlyReportPO marketMonthlyReport = this.marketMonthlyReportService.getById(id);
        Assert.isNotFound(marketMonthlyReport, "月报信息不存在,请联系管理员");
        return ObjectUtils.copying(marketMonthlyReport, MarketMonthlyReportVO.class);
    }

    /**
     * 月报详情(跳过鉴权)
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/public/market-monthly-report/{id}")
    public MarketMonthlyReportVO getPublicDetail(@PathVariable("id") Long id) {
        return this.getDetail(id);
    }

    /**
     * 月报记录保存
     *
     * @param reportModifyRequest
     */
    @PostMapping("/market-monthly-report")
    public void save(@Valid @RequestBody MarketMonthlyReportModifyRequest reportModifyRequest) {
        MarketMonthlyReportPO marketMonthlyReport = this.marketMonthlyReportService.getById(reportModifyRequest.getId());
        Assert.isNotFound(marketMonthlyReport, "市场月报信息不存在,保存失败");
        SessionContextHolder.requestTenant(marketMonthlyReport.getTenant());
        marketMonthlyReport.setReportData(reportModifyRequest.getReportData());
        marketMonthlyReport.setStatus(MarketMonthlyReportStatusEnum.ONGOING.name());
        this.marketMonthlyReportService.updateById(marketMonthlyReport);
    }

    /**
     * 月报记录保存
     *
     * @param reportModifyRequest
     */
    @PostMapping("/public/market-monthly-report")
    public void publicSave(@Valid @RequestBody MarketMonthlyReportModifyRequest reportModifyRequest) {
        this.save(reportModifyRequest);
    }

    /**
     * 分页查询
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/public/market-monthly-report/queryByPage")
    public Pagination<MarketMonthlyReportVO> publicQueryByPage(@Valid MarketMonthlyReportQuery query) {
        return marketMonthlyReportService.queryByPage(query);
    }

    /**
     * 上报
     *
     * @param id 月报id
     *
     * @return 实例对象
     */
    @PutMapping("/market-monthly-report/escalation/{id}")
    public Boolean escalation(@PathVariable("id") Long id) {
        return marketMonthlyReportService.escalation(id);
    }

    /**
     * 上报
     *
     * @param id 月报id
     *
     * @return 实例对象
     */
    @PutMapping("/public/market-monthly-report/escalation/{id}")
    public Boolean publicEscalation(@PathVariable("id") Long id) {
        return this.escalation(id);
    }

}
