package com.guhecloud.cube.biz.meattrade.server.pork.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.mdata.client.SupplierClient;
import com.guhecloud.cube.biz.mdata.client.dto.SupplierDTO;
import com.guhecloud.cube.biz.mdata.client.temp.RiskLevelEnum;
import com.guhecloud.cube.biz.meattrade.server.base.dto.AbattoirStatDTO;
import com.guhecloud.cube.biz.meattrade.server.base.entity.Abattoir;
import com.guhecloud.cube.biz.meattrade.server.base.entity.Config;
import com.guhecloud.cube.biz.meattrade.server.base.enumeration.ConfigEnum;
import com.guhecloud.cube.biz.meattrade.server.base.enumeration.DateEnum;
import com.guhecloud.cube.biz.meattrade.server.base.param.AbattoirQueryParam;
import com.guhecloud.cube.biz.meattrade.server.base.service.AbattoirService;
import com.guhecloud.cube.biz.meattrade.server.base.service.ConfigService;
import com.guhecloud.cube.biz.meattrade.server.common.facade.device.service.MeattradeDeviceQueryService;
import com.guhecloud.cube.biz.meattrade.server.common.facade.device.vo.MeattradeDeviceVO;
import com.guhecloud.cube.biz.meattrade.server.order.entity.Order;
import com.guhecloud.cube.biz.meattrade.server.order.entity.OrderDetail;
import com.guhecloud.cube.biz.meattrade.server.order.service.OrderDetailService;
import com.guhecloud.cube.biz.meattrade.server.order.service.OrderService;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkArrange;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkArrangeAudit;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkArrangeDetail;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkReport;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PratiqueRegister;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.SpotCheck;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.PorkArrangeAuditEnum;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.PorkArrangeEnum;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.PorkReportEnum;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.SpotCheckEnum;
import com.guhecloud.cube.biz.meattrade.server.pork.param.StatQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PorkArrangeAuditService;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PorkArrangeDetailService;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PorkArrangeService;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PorkReportService;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PratiqueRegisterService;
import com.guhecloud.cube.biz.meattrade.server.pork.service.SpotCheckService;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.PorkStatArrangeDetailVO;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.PorkStatSummaryVO;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.PorkStatSupplyCapacityVO;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.PorkStatTradeAverageVO;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.PorkStatTradeCapacityVO;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.PorkStatVO;
import com.guhecloud.cube.biz.meattrade.server.utlis.DateUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.guhecloud.cube.biz.meattrade.server.utlis.DateUtils.addDays;
import static com.guhecloud.cube.biz.meattrade.server.utlis.DateUtils.getDaysBetweenDates;
import static com.guhecloud.cube.biz.meattrade.server.utlis.DateUtils.getMidTime;

/**
 * 猪肉滑轨看板数据接口
 *
 * @author lzy
 * @date 2021/9/3
 */
@RestController("meattrade-porkStatController")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PorkStatController {

    private final AbattoirService abattoirService;
    private final PorkReportService porkReportService;
    private final PorkArrangeService porkArrangeService;
    private final PorkArrangeDetailService porkArrangeDetailService;
    private final PorkArrangeAuditService porkArrangeAuditService;
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;
    private final ConfigService configService;
    private final PratiqueRegisterService pratiqueRegisterService;
    private final SpotCheckService spotCheckService;
    private final MeattradeDeviceQueryService meattradeDeviceQueryService;

    private final SupplierClient supplierClient;

    /**
     * 交易数据汇总
     *
     * @param queryParam 查询参数
     *
     * @return 交易数据汇总信息
     */
    @GetMapping("/pork-stat/summary")
    public PorkStatSummaryVO getPorkStatSummary(@Valid StatQueryParam queryParam) {
        // 市场租户编号
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        // 获取时间
        queryParam.setPeriod(DateUtils.formatDateForType(queryParam.getType(), queryParam.getPeriod()));
        queryParam.setTenant(tenant);
        // 获取交易完成订单数据
        List<Order> orders = this.orderService.getDealOrder(queryParam);
        // 构建信息
        PorkStatSummaryVO summary = new PorkStatSummaryVO();
        // 获取交易数据信息
        this.setTradeInfo(orders, summary);

        // 获取供货数据信息，先要获取来货报备信息;
        PorkStatSummaryVO porkStatSummaryVO = porkArrangeDetailService.statSupplyMeatInfo(queryParam);
        summary.setSupplier(porkStatSummaryVO.getSupplier() == null ? 0 : porkStatSummaryVO.getSupplier());
        summary.setSupplyWeight(porkStatSummaryVO.getSupplyWeight() == null ? new BigDecimal("0.00") : porkStatSummaryVO.getSupplyWeight());
        summary.setSupplySlices(porkStatSummaryVO.getSupplySlices() == null ? 0 : porkStatSummaryVO.getSupplySlices());
        return summary;
    }

    /**
     * 白条交易量信息
     *
     * @param queryParam 查询参数
     *
     * @return 统计信息
     */
    @GetMapping("/pork-stat/trade-capacity")
    public PorkStatTradeCapacityVO getTradeCapacity(@Valid StatQueryParam queryParam) {
        PorkStatTradeCapacityVO tradeCapacity = new PorkStatTradeCapacityVO();
        // 构建白条交易量信息
        this.setTradeCapacityInfo(tradeCapacity);
        return tradeCapacity;
    }

    @GetMapping("/pork-stat/trade-capacity/line")
    public List<PorkStatVO.PorkStatLineVO> getTradeCapacityLine(@Valid StatQueryParam queryParam) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        // 获取交易数据
        queryParam.setPeriod(DateUtils.formatDateForType(queryParam.getType(), queryParam.getPeriod()));
        queryParam.setTenant(tenant);
        List<Order> orders = this.orderService.getDealOrderPage(queryParam);
        List<OrderDetail> orderDetails = this.orderDetailService.listByOrderIds(orders.stream().map(Order::getId)
                                                                                        .collect(Collectors.toList()), tenant);
        //组装数据
        LocalDateTime currentDate = LocalDateTime.now();
        Boolean isLess12 = DateUtils.toTimestamp(currentDate) < DateUtils.toTimestamp(DateUtils.getMidTime(currentDate));
        List<LocalDateTime> dates = getDaysBetweenDates(queryParam.getPeriod().getBegin(), queryParam.getPeriod().getEnd());
        return this.setTradeCapacityLine(dates, orders, orderDetails, isLess12);
    }

    /**
     * 白条交易均价
     *
     * @param queryParam 查询参数
     *
     * @return 统计信息
     */
    @GetMapping("/pork-stat/trade-average")
    public PorkStatTradeAverageVO getTradeAverage(@Valid StatQueryParam queryParam) {
        PorkStatTradeAverageVO tradeAverage = new PorkStatTradeAverageVO();
        // 构建白条交易均价信息
        this.setTradeAverageInfo(tradeAverage);
        return tradeAverage;
    }

    @GetMapping("/pork-stat/trade-average/line")
    public List<PorkStatVO.PorkStatLineVO> getTradeAverageLine(@Valid StatQueryParam queryParam) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        // 获取交易数据
        queryParam.setPeriod(DateUtils.formatDateForType(queryParam.getType(), queryParam.getPeriod()));
        queryParam.setTenant(tenant);
        List<Order> orders = this.orderService.getDealOrderPage(queryParam);
        //组装数据
        LocalDateTime currentDate = LocalDateTime.now();
        Boolean isLess12 = DateUtils.toTimestamp(currentDate) < DateUtils.toTimestamp(DateUtils.getMidTime(currentDate));
        List<LocalDateTime> dates = getDaysBetweenDates(queryParam.getPeriod().getBegin(), queryParam.getPeriod().getEnd());
        return this.setTradeAverageLine(dates, orders, isLess12);
    }

    /**
     * 白条供货量
     *
     * @param queryParam 查询参数
     *
     * @return 统计信息
     */
    @GetMapping("/pork-stat/supply-capacity")
    public PorkStatSupplyCapacityVO getSupplyCapacity(@Valid StatQueryParam queryParam) {
        PorkStatSupplyCapacityVO supplyCapacity = new PorkStatSupplyCapacityVO();
        // 构建白条供货量
        this.setSupplyCapacityInfo(supplyCapacity);
        return supplyCapacity;
    }

    @GetMapping("/pork-stat/supply-capacity/line")
    public List<PorkStatVO.PorkStatLineVO> getSupplyCapacityLine(@Valid StatQueryParam queryParam) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        // 获取供货数据
        queryParam.setPeriod(DateUtils.formatDateForType(queryParam.getType(), queryParam.getPeriod()));
        queryParam.setTenant(tenant);
        List<PorkArrange> porkArranges = this.porkArrangeService.listByComplex(queryParam);
        // 组装数据
        LocalDateTime currentDate = LocalDateTime.now();
        Boolean isLess12 = DateUtils.toTimestamp(currentDate) < DateUtils.toTimestamp(DateUtils.getMidTime(currentDate));
        List<LocalDateTime> dates = getDaysBetweenDates(queryParam.getPeriod().getBegin(), queryParam.getPeriod().getEnd());
        return this.setSupplyCapacityLine(dates, porkArranges, isLess12);
    }

    /**
     * 食安信息
     *
     * @param queryParam 查询参数
     *
     * @return 统计信息
     */
    @GetMapping("/pork-stat/spot-check")
    public PorkStatVO.PorkStatSpotCheck getPorkStatSpotCheck(@Valid StatQueryParam queryParam) {
        // 获取时间
        queryParam.setPeriod(DateUtils.formatDateForType(queryParam.getType(), queryParam.getPeriod()));

        PorkStatVO.PorkStatSpotCheck spotCheck = new PorkStatVO.PorkStatSpotCheck();
        // 获取供货数据信息，先要获取来货报备信息
        queryParam.setTenant(SessionContextHolder.getRequester().getTenant());
        List<PorkReport> porkReports = this.porkReportService.getStatPorkReports(queryParam);
        spotCheck.setSupplyReports(CollectionUtils.isEmpty(porkReports) ? 0 : porkReports.size());
        // 获取检疫票信息
        this.setSpotCheckInfo(porkReports, spotCheck);
        return spotCheck;
    }

    /**
     * 理货数据审核
     *
     * @param queryParam 查询参数
     *
     * @return 统计信息
     */
    @GetMapping("/pork-stat/arrange-audit")
    public PorkStatVO.PorkStatArrangeAudit getPorkStatArrangeAudit(@Valid StatQueryParam queryParam) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();

        // 获取时间
        queryParam.setPeriod(DateUtils.formatDateForType(queryParam.getType(), queryParam.getPeriod()));
        queryParam.setTenant(tenant);

        PorkStatVO.PorkStatArrangeAudit arrangeAudit = new PorkStatVO.PorkStatArrangeAudit();

        List<PorkArrangeAudit> porkArrangeAudits = this.porkArrangeAuditService.lambdaQuery()
                .eq(PorkArrangeAudit::getTenant, queryParam.getTenant())
                .ne(PorkArrangeAudit::getAuditStatus, PorkArrangeAuditEnum.AUDIT)
                .between(PorkArrangeAudit::getUpdateTime, queryParam.getPeriod().getBegin(), queryParam.getPeriod().getEnd())
                .list();

        // 构建信息
        int total = porkArrangeAudits.size();
        int failed = (int) porkArrangeAudits.stream()
                .filter(porkArrangeAudit -> PorkArrangeAuditEnum.FAILED.equals(porkArrangeAudit.getAuditStatus()))
                .count();
        int passed = (int) porkArrangeAudits.stream()
                .filter(porkArrangeAudit -> PorkArrangeAuditEnum.PASSED.equals(porkArrangeAudit.getAuditStatus()))
                .count();
        arrangeAudit.setTotal(total);
        arrangeAudit.setPorkArrangeAuditFailedNum(failed);
        arrangeAudit.setPorkArrangeAuditPassedNum(passed);
        arrangeAudit.setPorkArrangeAuditFailed(
                total == 0 ? BigDecimal.ZERO : BigDecimal.valueOf((double) failed / total).multiply(BigDecimal.valueOf(100)));
        arrangeAudit.setPorkArrangeAuditPassed(
                total == 0 ? BigDecimal.ZERO : BigDecimal.valueOf((double) passed / total).multiply(BigDecimal.valueOf(100)));
        return arrangeAudit;
    }

    /**
     * 来货报备信息
     *
     * @param queryParam 查询参数
     *
     * @return 统计信息
     */
    @GetMapping("/pork-stat/report")
    public PorkStatVO.PorkStatReport getPorkStatReport(@Valid StatQueryParam queryParam) {
        // 获取时间
        queryParam.setPeriod(DateUtils.formatDateForType(queryParam.getType(), queryParam.getPeriod()));

        PorkStatVO.PorkStatReport report = new PorkStatVO.PorkStatReport();
        // 获取报备信息，通过报备信息获取报备审核信息
        queryParam.setTenant(SessionContextHolder.getRequester().getTenant());
        List<PorkReport> porkReports = this.porkReportService.getStatPorkReports(queryParam);
        if (CollectionUtils.isEmpty(porkReports)) {
            report.setTotal(0);
            report.setPorkReportPassedNum(0);
            report.setPorkReportFailedNum(0);
            report.setPorkReportPassed(BigDecimal.ZERO);
            report.setPorkReportFailed(BigDecimal.ZERO);
            return report;
        }

        // 构建信息
        int total = porkReports.size();
        int passed = (int) porkReports.stream().filter(porkReport -> PorkReportEnum.PASSED.equals(porkReport.getStatus())).count();
        int failed = (int) porkReports.stream().filter(porkReport -> PorkReportEnum.FAILED.equals(porkReport.getStatus())).count();
        report.setTotal(total);
        report.setPorkReportPassedNum(passed);
        report.setPorkReportFailedNum(failed);
        report.setPorkReportPassed(
                total == 0 ? BigDecimal.ZERO : BigDecimal.valueOf((double) passed / total).multiply(BigDecimal.valueOf(100)));
        report.setPorkReportFailed(
                total == 0 ? BigDecimal.ZERO : BigDecimal.valueOf((double) failed / total).multiply(BigDecimal.valueOf(100)));

        return report;
    }

    /**
     * 报备商家风险信息
     *
     * @param queryParam 查询参数
     *
     * @return 统计信息
     */
    @GetMapping("/pork-stat/risk-level")
    public PorkStatVO.PorkStatRiskLevel getPorkStatRiskLevel(@Valid StatQueryParam queryParam) {
        // 获取时间
        queryParam.setPeriod(DateUtils.formatDateForType(queryParam.getType(), queryParam.getPeriod()));

        PorkStatVO.PorkStatRiskLevel riskLevel = new PorkStatVO.PorkStatRiskLevel();
        // 获取报备信息，通过报备信息获取报备审核信息
        queryParam.setTenant(SessionContextHolder.getRequester().getTenant());
        List<PorkReport> porkReports = this.porkReportService.getStatPorkReports(queryParam);
        if (CollectionUtils.isEmpty(porkReports)) {
            riskLevel.setTotal(0);
            riskLevel.setLowRiskLevel(BigDecimal.ZERO);
            riskLevel.setMediumRiskLevel(BigDecimal.ZERO);
            riskLevel.setHighRiskLevel(BigDecimal.ZERO);
            riskLevel.setLowRiskLevelNum(0);
            riskLevel.setMediumRiskLevelNum(0);
            riskLevel.setHighRiskLevelNum(0);
            return riskLevel;
        }
        Set<Long> supplierIds = porkReports.stream().map(PorkReport::getSupplierId).collect(Collectors.toSet());
        List<SupplierDTO> suppliers = this.supplierClient.listByIds(supplierIds);

        // 构建信息
        int total = suppliers.size();
        int lowRiskLevel = (int) suppliers.stream()
                .filter(merchant -> merchant.getRiskLevel() == null || RiskLevelEnum.LOW.equals(merchant.getRiskLevel())).count();
        int mediumRiskLevel = (int) suppliers.stream().filter(merchant -> RiskLevelEnum.MEDIUM.equals(merchant.getRiskLevel())).count();
        int highRiskLevel = (int) suppliers.stream().filter(merchant -> RiskLevelEnum.HIGH.equals(merchant.getRiskLevel())).count();

        riskLevel.setTotal(total);
        riskLevel.setLowRiskLevelNum(lowRiskLevel);
        riskLevel.setMediumRiskLevelNum(mediumRiskLevel);
        riskLevel.setHighRiskLevelNum(highRiskLevel);
        riskLevel.setLowRiskLevel(BigDecimal.valueOf((double) lowRiskLevel / total).multiply(BigDecimal.valueOf(100)));
        riskLevel.setMediumRiskLevel(BigDecimal.valueOf((double) mediumRiskLevel / total).multiply(BigDecimal.valueOf(100)));
        riskLevel.setHighRiskLevel(BigDecimal.valueOf((double) highRiskLevel / total).multiply(BigDecimal.valueOf(100)));

        return riskLevel;
    }

    /**
     * 供货商交易排名
     *
     * @param queryParam 查询参数
     *
     * @return 统计数据
     */
    @GetMapping("/pork-stat/supply-trade/rank")
    public Pagination<PorkStatSummaryVO> getSupplyTradeRank(@Valid StatQueryParam queryParam) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        // 获取时间
        queryParam.setPeriod(DateUtils.formatDateForType(queryParam.getType(), queryParam.getPeriod()));
        // 获取订单完成信息
        queryParam.setTenant(tenant);
        List<Order> orders = this.orderService.getDealOrderPage(queryParam);

        Map<Long, List<Order>> orderMap = orders.stream().collect(Collectors.groupingBy(Order::getSupplierId));
        List<SupplierDTO> suppliers = this.supplierClient.listSuppliers(queryParam.getKeyword(), tenant);

        // 构建信息
        List<PorkStatSummaryVO> summaryVOS = this.setSupplyTradeRank(suppliers, orderMap);
        // 对金额进行降序排序
        summaryVOS = summaryVOS.stream().sorted(Comparator.comparing(PorkStatSummaryVO::getTradeAmount).reversed())
                .limit(queryParam.getPageSize()).collect(Collectors.toList());
        for (int i = 0; i < summaryVOS.size(); i++) {
            summaryVOS.get(i).setSort(i + 1);
        }
        Page<SupplierDTO> page = new Page<>();
        page.setTotal(summaryVOS.size());
        page.setRecords(suppliers);
        return PagingUtils.convert(page, summaryVOS);
    }

    /**
     * 白条出场情况
     *
     * @param queryParam 查询参数
     *
     * @return 统计信息
     */
    @GetMapping("/pork-stat/abattoir")
    public Pagination<PorkStatVO.PorkStatAbattoir> getPorkStatAbattoir(@Valid StatQueryParam queryParam) {
        // 获取时间
        queryParam.setPeriod(DateUtils.formatDateForType(queryParam.getType(), queryParam.getPeriod()));

        // 获取屠宰场信息
        AbattoirQueryParam abattoirQueryParam = new AbattoirQueryParam();
        abattoirQueryParam.setPeriod(queryParam.getPeriod());
        abattoirQueryParam.setKeyword(queryParam.getKeyword());
        abattoirQueryParam.setPageNumber(queryParam.getPageNumber());
        abattoirQueryParam.setPageSize(queryParam.getPageSize());
        Page<AbattoirStatDTO> page = this.abattoirService.statPage(abattoirQueryParam);

        List<Abattoir> allAbattoirs = this.abattoirService.list(abattoirQueryParam);
        //没有数据时，随机显示屠宰场的数据
        if (page == null || CollectionUtils.isEmpty(page.getRecords())) {
            List<PorkStatVO.PorkStatAbattoir> abattoirList = allAbattoirs.stream().map(abattoir -> {
                PorkStatVO.PorkStatAbattoir abattoirStat = new PorkStatVO.PorkStatAbattoir();
                abattoirStat.setAbattoirName(abattoir.getName());
                abattoirStat.setTradeSlices(0);
                abattoirStat.setTradeAmount(BigDecimal.ZERO);
                abattoirStat.setTradeCommission(BigDecimal.ZERO);
                abattoirStat.setTradeWeight(BigDecimal.ZERO);
                return abattoirStat;
            }).collect(Collectors.toList());
            return PagingUtils.convert(page, abattoirList);
        }

        Map<Long, AbattoirStatDTO> abattoirMap = page.getRecords().stream().collect(Collectors.toMap(AbattoirStatDTO::getId, s -> s));

        // 构建信息
        List<PorkStatVO.PorkStatAbattoir> abattoirs = allAbattoirs.stream().map(dto -> {
            PorkStatVO.PorkStatAbattoir abattoir = new PorkStatVO.PorkStatAbattoir();
            if (abattoirMap.containsKey(dto.getId())) {
                AbattoirStatDTO abattoirStatDTO = abattoirMap.get(dto.getId());
                abattoir.setAbattoirName(abattoirStatDTO.getAbattoirName());
                abattoir.setTradeSlices(abattoirStatDTO.getTradeSlices());
                abattoir.setTradeAmount(abattoirStatDTO.getDealAmount());
                abattoir.setTradeCommission(abattoirStatDTO.getCommission());
                abattoir.setTradeWeight(abattoirStatDTO.getDealWeight());
            } else {
                abattoir.setAbattoirName(dto.getName());
                abattoir.setTradeSlices(0);
                abattoir.setTradeAmount(BigDecimal.ZERO);
                abattoir.setTradeCommission(BigDecimal.ZERO);
                abattoir.setTradeWeight(BigDecimal.ZERO);
            }
            return abattoir;
        }).sorted(Comparator.comparing(PorkStatSummaryVO::getTradeAmount).reversed()).collect(Collectors.toList());

        for (int i = 0; i < abattoirs.size(); i++) {
            abattoirs.get(i).setSort(i + 1);
        }
        return PagingUtils.convert(page, abattoirs);
    }

    /**
     * 理货商品明细
     *
     * @param queryParam 查询参数
     *
     * @return 统计信息
     */
    @GetMapping("/pork-stat/arrange-detail")
    public PorkStatArrangeDetailVO getPorkArrangeDetail(@Valid StatQueryParam queryParam) {
        // 获取时间
        queryParam.setPeriod(DateUtils.formatDateForType(queryParam.getType(), queryParam.getPeriod()));
        PorkStatArrangeDetailVO detailVO = new PorkStatArrangeDetailVO();
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        if (StringUtils.isNotEmpty(queryParam.getKeyword())) {
            List<SupplierDTO> suppliers = this.supplierClient.listSuppliers(queryParam.getKeyword(), tenant);
            if (CollectionUtils.isEmpty(suppliers)) {
                detailVO.setDetails(Collections.emptyList());
                return detailVO;
            }
            Set<Long> supplierIds = suppliers.stream().map(SupplierDTO::getId).collect(Collectors.toSet());
            List<PorkReport> porkReports = this.porkReportService.listPorkReportBySuppliers(supplierIds, tenant);
            Set<Long> porkReportIds = porkReports.stream().map(PorkReport::getId).collect(Collectors.toSet());
            queryParam.setPorkReportIds(porkReportIds);
        }
        queryParam.setTenant(tenant);
        // 获取报备信息，通过报备信息获取报备审核信息
        Page<PorkArrange> page = this.porkArrangeService.statPage(queryParam);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return detailVO;
        }
        // 构建信息
        PorkStatArrangeDetailVO porkStatArrangeDetailVO = this.setArrangeDetailInfo(page.getRecords(), detailVO, queryParam);
        //理货完成批次数
        porkStatArrangeDetailVO.setReportArrangeTotal((int) page.getTotal());
        // 作弊嫌疑批次数
        porkStatArrangeDetailVO.setReportSuspicionTotal(this.porkArrangeService.getCheatCount(queryParam));
        //页数总数
        porkStatArrangeDetailVO.setTotal((int) page.getPages());
        return porkStatArrangeDetailVO;
    }

    private List<PorkStatSummaryVO> setSupplyTradeRank(List<SupplierDTO> suppliers, Map<Long, List<Order>> orderMap) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        Set<Long> orderIds = new HashSet<>();
        orderMap.forEach((k, v) -> {
            orderIds.addAll(v.stream().map(Order::getId).collect(Collectors.toSet()));
        });
        // 获取订单详情信息
        List<OrderDetail> orderDetails = this.orderDetailService.listByOrderIds(orderIds, tenant);
        Map<Long, List<OrderDetail>> orderDetailMap = orderDetails.stream().collect(Collectors.groupingBy(OrderDetail::getOrderId));

        return suppliers.stream().map(supplier -> {
            PorkStatSummaryVO summary = new PorkStatSummaryVO();
            summary.setSupplyName(Objects.isNull(supplier) ? "" : supplier.getName());
            summary.setSupplierCode(Objects.isNull(supplier) ? "" : supplier.getCode());
            if (orderMap.containsKey(supplier.getId())) {
                List<Order> orderList = orderMap.get(supplier.getId());
                summary.setTradeWeight(orderList.stream().map(Order::getWeight).reduce(BigDecimal.ZERO, BigDecimal::add));
                summary.setTradeCommission(orderList.stream().map(Order::getCommission).reduce(BigDecimal.ZERO, BigDecimal::add));
                summary.setTradeAmount(orderList.stream().map(Order::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                int tradeSlices = orderList.stream().filter(order -> orderDetailMap.containsKey(order.getId()))
                        .mapToInt(order -> CollectionUtils.isEmpty(orderDetailMap.get(order.getId())) ? 0
                                                                                                      : orderDetailMap.get(order.getId())
                                                   .size()).sum();
                summary.setTradeSlices(tradeSlices);
            } else {
                summary.setTradeWeight(BigDecimal.ZERO);
                summary.setTradeAmount(BigDecimal.ZERO);
            }
            return summary;
        }).collect(Collectors.toList());

    }

    private void setTradeInfo(List<Order> orders, PorkStatSummaryVO summary) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        // 交易重量，交易金额，交易佣金信息获取
        BigDecimal tradeWeight = orders.stream().map(Order::getWeight).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal tradeAmount = orders.stream().map(Order::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal tradeCommission = orders.stream().map(Order::getCommission).reduce(BigDecimal.ZERO, BigDecimal::add);

        // 交易片数，交易均价信息获取
        List<Long> orderIds = orders.stream().map(Order::getId).collect(Collectors.toList());
        List<OrderDetail> orderDetails = this.orderDetailService.listByOrderIds(orderIds, tenant);
        int tradeSlices = orderDetails.size();
        BigDecimal tradeAverageAmount = tradeSlices == 0 ? BigDecimal.ZERO : tradeAmount.divide(tradeWeight, 2, RoundingMode.DOWN);

        // 实际均价信息获取，先获取该市场的最低价格
        Config config = this.configService.getByType(ConfigEnum.SELLING_PRICE);
        BigDecimal lowerPrice = Objects.isNull(config) ? BigDecimal.ZERO : BigDecimal.valueOf(config.getValue());
        //去除最低限价交易的 总销售额
        BigDecimal actualAmount = orderDetails.stream().filter(od -> od.getUnitPrice().compareTo(lowerPrice) > 0)
                .map(OrderDetail::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        //去除最低限价交易的 总重量
        BigDecimal actualWeight = orderDetails.stream().filter(od -> od.getUnitPrice().compareTo(lowerPrice) > 0)
                .map(OrderDetail::getWeight).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (actualWeight.intValue() != 0) {
            BigDecimal actualAverageAmount = tradeSlices == 0 ? BigDecimal.ZERO : actualAmount.divide(actualWeight, 2, RoundingMode.DOWN);
            summary.setActualAverageAmount(actualAverageAmount);
        } else {
            summary.setActualAverageAmount(new BigDecimal("0.00"));
        }
        // 信息构建
        summary.setTradeAmount(tradeAmount);
        summary.setTradeAverageAmount(tradeAverageAmount);
        summary.setTradeWeight(tradeWeight);
        summary.setTradeCommission(tradeCommission);
        summary.setTradeSlices(tradeSlices);

    }

    private void setSpotCheckInfo(List<PorkReport> porkReports, PorkStatVO.PorkStatSpotCheck spotCheck) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        List<Long> porkReportIds = porkReports.stream().map(PorkReport::getId).collect(Collectors.toList());
        // 获取抽检信息
        List<SpotCheck> spotChecks = this.spotCheckService.listByPorkReports(porkReportIds, tenant);
        List<PratiqueRegister> failedPratoqueRegisters = this.pratiqueRegisterService.getFailedPratiqueRegister(porkReportIds, tenant);
        spotCheck.setPratiqueFailed(failedPratoqueRegisters.size());
        spotCheck.setPratiqueFailIds(failedPratoqueRegisters.stream().map(PratiqueRegister::getId).collect(Collectors.toList()));
        // 构建信息
        int clenbuterolFailed = (int) spotChecks.stream()
                .filter(sp -> Objects.nonNull(sp.getClenbuterol()) && sp.getClenbuterol().equals(SpotCheckEnum.POSITIVE)).count();
        int wateredFailed = (int) spotChecks.stream()
                .filter(sp -> Objects.nonNull(sp.getWatered()) && sp.getWatered().equals(SpotCheckEnum.POSITIVE)).count();

        spotCheck.setClenbuterolFailed(clenbuterolFailed);
        spotCheck.setWatered(wateredFailed);
    }

    private void setReportCheatInfo(List<PorkReport> porkReports, PorkStatVO.ReportSuspicion suspicion) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        List<String> porkReportCodes = porkReports.stream().map(PorkReport::getCode).collect(Collectors.toList());
        // 根据批次号去判断该批次号下是否有作弊的理货片数
        List<PorkArrangeDetail> porkArrangeDetails = this.porkArrangeDetailService.listByPorkReportCodes(porkReportCodes, tenant);
        Map<String, List<PorkArrangeDetail>> porkArrangeDetailMap =
                porkArrangeDetails.stream().collect(Collectors.groupingBy(PorkArrangeDetail::getPorkReportCode));

        // 构建信息
        int total = porkReportCodes.size();
        int cheat = (int) porkReportCodes.stream().filter(code -> {
            if (porkArrangeDetailMap.containsKey(code)) {
                return porkArrangeDetailMap.get(code).stream().anyMatch(PorkArrangeDetail::getCheat);
            }
            return false;
        }).count();

        suspicion.setTotal(total);
        suspicion.setSuspicion(BigDecimal.valueOf((double) cheat / total).multiply(BigDecimal.valueOf(100)));
        suspicion.setSuspicionNum(cheat);
        suspicion.setNoDoubt(BigDecimal.valueOf(100).subtract(suspicion.getSuspicion()));
        suspicion.setNoDoubtNum(total - cheat);
    }

    private PorkStatArrangeDetailVO setArrangeDetailInfo(List<PorkArrange> porkArranges, PorkStatArrangeDetailVO detailVO,
                                                         StatQueryParam queryParam
                                                        ) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        // 根据报备信息获取理货信息,理货详情信息
        List<Long> porkReportIds = porkArranges.stream().map(PorkArrange::getPorkReportId).collect(Collectors.toList());
        List<PorkReport> porkReports = this.porkReportService.listByIds(porkReportIds);
        List<String> porkReportCodes = porkReports.stream().map(PorkReport::getCode).collect(Collectors.toList());
        List<PorkArrangeDetail> porkArrangeDetails = this.porkArrangeDetailService.listByPorkReportCodes(porkReportCodes, tenant);

        Map<Long, List<PorkReport>> porkReportMap = porkReports.stream().collect(Collectors.groupingBy(PorkReport::getId));
        Map<String, List<PorkArrangeDetail>> porkArrangeDetailMap =
                porkArrangeDetails.stream().collect(Collectors.groupingBy(PorkArrangeDetail::getPorkReportCode));

        // 获取商户信息
        Set<Long> supplierIds = porkArrangeDetails.stream().map(PorkArrangeDetail::getSupplierId).collect(Collectors.toSet());
        List<SupplierDTO> suppliers = this.supplierClient.listByIds(supplierIds);
        Map<Long, List<SupplierDTO>> supplierMap = suppliers.stream().collect(Collectors.groupingBy(SupplierDTO::getId));

        // 构建信息
        List<PorkStatVO.PorkStatArrange> arranges = new ArrayList<>();
        int i = (queryParam.getPageNumber() - 1) * queryParam.getPageSize() + 1;
        //获取理货设备信息
        Set<Long> deviceIds = porkArranges.stream().filter(porkArrange -> StringUtils.isNotEmpty(porkArrange.getPlace())
                                                                          && StringUtils.isNumeric(porkArrange.getPlace()))
                .map(porkArrange -> Long.valueOf(porkArrange.getPlace()))
                .collect(Collectors.toSet());
        Map<Long, MeattradeDeviceVO> simpleInstanceDTOMap = this.meattradeDeviceQueryService.queryDeviceByIds(deviceIds)
                .stream().collect(Collectors.toMap(MeattradeDeviceVO::getId, obj -> obj, (o, n) -> n));
        for (PorkArrange porkArrange : porkArranges) {
            PorkStatVO.PorkStatArrange arrange = new PorkStatVO.PorkStatArrange();
            if (StringUtils.isNotEmpty(porkArrange.getPlace()) &&
                StringUtils.isNumeric(porkArrange.getPlace()) &&
                simpleInstanceDTOMap.containsKey(Long.valueOf(porkArrange.getPlace()))) {
                MeattradeDeviceVO simpleInstanceDTO = simpleInstanceDTOMap.get(Long.valueOf(porkArrange.getPlace()));
                arrange.setMachineName(simpleInstanceDTO.getName());
            }
            arrange.setPorkArrangeSlices(porkArrange.getSlices());
            arrange.setResult(true);
            if (porkReportMap.containsKey(porkArrange.getPorkReportId())) {
                arrange.setPorkReportCode(CollectionUtils.isEmpty(porkReportMap.get(porkArrange.getPorkReportId())) ?
                                          "" : porkReportMap.get(porkArrange.getPorkReportId()).get(0).getCode());
                Long merchantId = CollectionUtils.isEmpty(porkReportMap.get(porkArrange.getPorkReportId())) ?
                                  null : porkReportMap.get(porkArrange.getPorkReportId()).get(0).getSupplierId();
                if (supplierMap.containsKey(merchantId)) {
                    arrange.setSupplierName(CollectionUtils.isEmpty(supplierMap.get(merchantId)) ?
                                            "" : supplierMap.get(merchantId).get(0).getName());
                    arrange.setSupplierCode(CollectionUtils.isEmpty(supplierMap.get(merchantId)) ?
                                            "" : supplierMap.get(merchantId).get(0).getCode());
                }
            }
            if (porkArrangeDetailMap.containsKey(arrange.getPorkReportCode())) {
                List<PorkArrangeDetail> details = porkArrangeDetailMap.get(arrange.getPorkReportCode());
                arrange.setResult(details.stream().noneMatch(PorkArrangeDetail::getCheat));
            }
            // 构建序号
            arrange.setSort(i);
            i++;

            arranges.add(arrange);
        }
        detailVO.setDetails(arranges);
        return detailVO;
    }

    private void setTradeCapacityInfo(PorkStatTradeCapacityVO tradeCapacity) {
        // 获取今天时间，构建今日交易信息
        List<OrderDetail> todayOrderDetails = this.getOrderDetailsByPeriod(DateEnum.TODAY);
        tradeCapacity.setTodayTradeSlices(todayOrderDetails.size());
        tradeCapacity.setTodayTradeWeight(todayOrderDetails.stream().map(OrderDetail::getWeight).reduce(BigDecimal.ZERO, BigDecimal::add));
        // 获取昨天时间，构建昨天交易信息
        List<OrderDetail> yesterdayOrderDetails = this.getOrderDetailsByPeriod(DateEnum.YESTERDAY);
        tradeCapacity.setYesterdayTradeSlices(yesterdayOrderDetails.size());
        tradeCapacity.setYesterdayTradeWeight(yesterdayOrderDetails.stream().map(OrderDetail::getWeight)
                                                      .reduce(BigDecimal.ZERO, BigDecimal::add));
        tradeCapacity.setComparedWithYesterdaySlices(tradeCapacity.getTodayTradeSlices() - tradeCapacity.getYesterdayTradeSlices());
        tradeCapacity.setComparedWithYesterdayWeight(tradeCapacity.getTodayTradeWeight().subtract(tradeCapacity.getYesterdayTradeWeight()));

        // 获取本周时间，构建本周交易信息
        List<OrderDetail> thisWeekOrderDetails = this.getOrderDetailsByPeriod(DateEnum.THIS_WEEK);
        tradeCapacity.setThisWeekTradeSlices(thisWeekOrderDetails.size());
        tradeCapacity.setThisWeekTradeWeight(thisWeekOrderDetails.stream().map(OrderDetail::getWeight)
                                                     .reduce(BigDecimal.ZERO, BigDecimal::add));
        // 获取上周时间，构建上周交易信息
        List<OrderDetail> lastWeekOrderDetails = this.getOrderDetailsByPeriod(DateEnum.LAST_WEEK);
        tradeCapacity.setLastWeekTradeSlices(lastWeekOrderDetails.size());
        tradeCapacity.setLastWeekTradeWeight(lastWeekOrderDetails.stream().map(OrderDetail::getWeight)
                                                     .reduce(BigDecimal.ZERO, BigDecimal::add));
        tradeCapacity.setComparedWithLastWeekSlices(tradeCapacity.getThisWeekTradeSlices() - tradeCapacity.getLastWeekTradeSlices());
        tradeCapacity.setComparedWithLastWeekWeight(tradeCapacity.getThisWeekTradeWeight()
                                                            .subtract(tradeCapacity.getLastWeekTradeWeight()));

        // 获取本月时间，构建本月交易信息
        List<OrderDetail> thisMonthOrderDetails = this.getOrderDetailsByPeriod(DateEnum.THIS_MONTH);
        tradeCapacity.setThisMonthTradeSlices(thisMonthOrderDetails.size());
        tradeCapacity.setThisMonthTradeWeight(thisMonthOrderDetails.stream().map(OrderDetail::getWeight)
                                                      .reduce(BigDecimal.ZERO, BigDecimal::add));
        // 获取上月时间，构建上月交易信息
        List<OrderDetail> lastMonthOrderDetails = this.getOrderDetailsByPeriod(DateEnum.LAST_MONTH);
        tradeCapacity.setLastMonthTradeSlices(lastMonthOrderDetails.size());
        tradeCapacity.setLastMonthTradeWeight(lastMonthOrderDetails.stream().map(OrderDetail::getWeight)
                                                      .reduce(BigDecimal.ZERO, BigDecimal::add));
        tradeCapacity.setComparedWithLastMonthSlices(tradeCapacity.getThisMonthTradeSlices() - tradeCapacity.getLastMonthTradeSlices());
        tradeCapacity.setComparedWithLastMonthWeight(tradeCapacity.getThisMonthTradeWeight()
                                                             .subtract(tradeCapacity.getLastMonthTradeWeight()));

        // 获取近30天时间，构建近30天交易信息
        List<OrderDetail> recentThirtyDaysOrderDetails = this.getOrderDetailsByPeriod(DateEnum.MONTH);
        tradeCapacity.setRecentThirtyDaysTradeSlices(recentThirtyDaysOrderDetails.size());
        tradeCapacity.setRecentThirtyDaysTradeWeight(recentThirtyDaysOrderDetails.stream().map(OrderDetail::getWeight)
                                                             .reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    private void setSupplyCapacityInfo(PorkStatSupplyCapacityVO supplyCapacity) {
        // 获取今天时间，构建今日供货信息
        List<PorkArrange> todayPorkArranges = this.getPorkArrangeByPeriod(DateEnum.TODAY);
        supplyCapacity.setTodaySupplySlices((todayPorkArranges.stream().mapToInt(PorkArrange::getSlices).sum()));
        supplyCapacity.setTodaySupplyWeight(todayPorkArranges.stream().map(PorkArrange::getWeight)
                                                    .reduce(BigDecimal.ZERO, BigDecimal::add));
        // 获取昨天时间，构建昨天供货信息
        List<PorkArrange> yesterdayPorkArranges = this.getPorkArrangeByPeriod(DateEnum.YESTERDAY);
        supplyCapacity.setYesterdaySupplySlices(yesterdayPorkArranges.stream().mapToInt(PorkArrange::getSlices).sum());
        supplyCapacity.setYesterdaySupplyWeight(yesterdayPorkArranges.stream().map(PorkArrange::getWeight)
                                                        .reduce(BigDecimal.ZERO, BigDecimal::add));
        supplyCapacity.setComparedWithYesterdaySlices(supplyCapacity.getTodaySupplySlices() - supplyCapacity.getYesterdaySupplySlices());
        supplyCapacity.setComparedWithYesterdayWeight(supplyCapacity.getTodaySupplyWeight()
                                                              .subtract(supplyCapacity.getYesterdaySupplyWeight()));

        // 获取本周时间，构建本周供货信息
        List<PorkArrange> thisWeekPorkArranges = this.getPorkArrangeByPeriod(DateEnum.THIS_WEEK);
        supplyCapacity.setThisWeekSupplySlices(thisWeekPorkArranges.stream().mapToInt(PorkArrange::getSlices).sum());
        supplyCapacity.setThisWeekSupplyWeight(thisWeekPorkArranges.stream().map(PorkArrange::getWeight)
                                                       .reduce(BigDecimal.ZERO, BigDecimal::add));
        // 获取上周时间，构建上周供货信息
        List<PorkArrange> lastWeekPorkArranges = this.getPorkArrangeByPeriod(DateEnum.LAST_WEEK);
        supplyCapacity.setLastWeekSupplySlices(lastWeekPorkArranges.stream().mapToInt(PorkArrange::getSlices).sum());
        supplyCapacity.setLastWeekSupplyWeight(lastWeekPorkArranges.stream().map(PorkArrange::getWeight)
                                                       .reduce(BigDecimal.ZERO, BigDecimal::add));
        supplyCapacity.setComparedWithLastWeekSlices(supplyCapacity.getThisWeekSupplySlices() - supplyCapacity.getLastWeekSupplySlices());
        supplyCapacity.setComparedWithLastWeekWeight(supplyCapacity.getThisWeekSupplyWeight()
                                                             .subtract(supplyCapacity.getLastWeekSupplyWeight()));

        // 获取本月时间，构建本月供货信息
        List<PorkArrange> thisMonthPorkArranges = this.getPorkArrangeByPeriod(DateEnum.THIS_MONTH);
        supplyCapacity.setThisMonthSupplySlices(thisMonthPorkArranges.stream().mapToInt(PorkArrange::getSlices).sum());
        supplyCapacity.setThisMonthSupplyWeight(thisMonthPorkArranges.stream().map(PorkArrange::getWeight)
                                                        .reduce(BigDecimal.ZERO, BigDecimal::add));
        // 获取上月时间，构建上月供货信息
        List<PorkArrange> lastMonthPorkArranges = this.getPorkArrangeByPeriod(DateEnum.LAST_MONTH);
        supplyCapacity.setLastMonthSupplySlices(lastMonthPorkArranges.stream().mapToInt(PorkArrange::getSlices).sum());
        supplyCapacity.setLastMonthSupplyWeight(lastMonthPorkArranges.stream().map(PorkArrange::getWeight)
                                                        .reduce(BigDecimal.ZERO, BigDecimal::add));
        supplyCapacity.setComparedWithLastMonthSlices(
                supplyCapacity.getThisMonthSupplySlices() - supplyCapacity.getLastMonthSupplySlices());
        supplyCapacity.setComparedWithLastMonthWeight(supplyCapacity.getThisMonthSupplyWeight()
                                                              .subtract(supplyCapacity.getLastMonthSupplyWeight()));

        // 获取近30天时间，构建近30天交易信息
        List<PorkArrange> recentThirtyDaysPorkArranges = this.getPorkArrangeByPeriod(DateEnum.MONTH);
        supplyCapacity.setRecentThirtyDaysSupplySlices(recentThirtyDaysPorkArranges.stream().mapToInt(PorkArrange::getSlices).sum());
        supplyCapacity.setRecentThirtyDaysSupplyWeight(recentThirtyDaysPorkArranges.stream().map(PorkArrange::getWeight)
                                                               .reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    private void setTradeAverageInfo(PorkStatTradeAverageVO tradeAverage) {
        StatQueryParam queryParam = new StatQueryParam();
        // 获取今天时间，构建今日交易信息
        List<OrderDetail> todayOrderDetails = this.getOrderDetailsByPeriod(DateEnum.TODAY);
        BigDecimal todayAmount = todayOrderDetails.stream().map(OrderDetail::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal todayWeight = todayOrderDetails.stream().map(OrderDetail::getWeight).reduce(BigDecimal.ZERO, BigDecimal::add);
        tradeAverage.setTodayAverageUnitAmount(
                Objects.equals(todayAmount, BigDecimal.ZERO) ? BigDecimal.ZERO : todayAmount.divide(todayWeight, 2, RoundingMode.DOWN));
        // 获取昨天时间，构建昨天交易信息
        List<OrderDetail> yesterdayOrderDetails = this.getOrderDetailsByPeriod(DateEnum.YESTERDAY);
        BigDecimal yesterdayAmount = yesterdayOrderDetails.stream().map(OrderDetail::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal yesterdayWeight = yesterdayOrderDetails.stream().map(OrderDetail::getWeight).reduce(BigDecimal.ZERO, BigDecimal::add);
        tradeAverage.setYesterdayAverageUnitAmount(Objects.equals(yesterdayAmount, BigDecimal.ZERO) ? BigDecimal.ZERO
                                                                                                    : yesterdayAmount.divide(yesterdayWeight, 2, RoundingMode.DOWN));
        tradeAverage.setComparedWithYesterdayUnitAmount(tradeAverage.getTodayAverageUnitAmount()
                                                                .subtract(tradeAverage.getYesterdayAverageUnitAmount()));

        // 获取本周时间，构建本周交易信息
        List<OrderDetail> thisWeekOrderDetails = this.getOrderDetailsByPeriod(DateEnum.THIS_WEEK);
        BigDecimal thisWeekAmount = thisWeekOrderDetails.stream().map(OrderDetail::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal thisWeekWeight = thisWeekOrderDetails.stream().map(OrderDetail::getWeight).reduce(BigDecimal.ZERO, BigDecimal::add);
        tradeAverage.setThisWeekAverageUnitAmount(Objects.equals(thisWeekAmount, BigDecimal.ZERO) ? BigDecimal.ZERO
                                                                                                  : thisWeekAmount.divide(thisWeekWeight, 2, RoundingMode.DOWN));
        // 获取上周时间，构建上周交易信息
        List<OrderDetail> lastWeekOrderDetails = this.getOrderDetailsByPeriod(DateEnum.LAST_WEEK);
        BigDecimal lastWeekAmount = lastWeekOrderDetails.stream().map(OrderDetail::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal lastWeekWeight = lastWeekOrderDetails.stream().map(OrderDetail::getWeight).reduce(BigDecimal.ZERO, BigDecimal::add);
        tradeAverage.setLastWeekAverageUnitAmount(Objects.equals(lastWeekAmount, BigDecimal.ZERO) ? BigDecimal.ZERO
                                                                                                  : lastWeekAmount.divide(lastWeekWeight, 2, RoundingMode.DOWN));
        tradeAverage.setComparedWithLastWeekUnitAmount(tradeAverage.getThisWeekAverageUnitAmount()
                                                               .subtract(tradeAverage.getLastWeekAverageUnitAmount()));

        // 获取本月时间，构建本月交易信息
        List<OrderDetail> thisMonthOrderDetails = this.getOrderDetailsByPeriod(DateEnum.THIS_MONTH);
        BigDecimal thisMonthAmount = thisMonthOrderDetails.stream().map(OrderDetail::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal thisMonthWeight = thisMonthOrderDetails.stream().map(OrderDetail::getWeight).reduce(BigDecimal.ZERO, BigDecimal::add);
        tradeAverage.setThisMonthAverageUnitAmount(Objects.equals(thisMonthAmount, BigDecimal.ZERO) ? BigDecimal.ZERO
                                                                                                    : thisMonthAmount.divide(thisMonthWeight, 2, RoundingMode.DOWN));
        // 获取上月时间，构建上月交易信息
        List<OrderDetail> lastMonthOrderDetails = this.getOrderDetailsByPeriod(DateEnum.LAST_WEEK);
        BigDecimal lastMonthAmount = lastMonthOrderDetails.stream().map(OrderDetail::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal lastMonthWeight = lastMonthOrderDetails.stream().map(OrderDetail::getWeight).reduce(BigDecimal.ZERO, BigDecimal::add);
        tradeAverage.setLastMonthAverageUnitAmount(Objects.equals(lastMonthAmount, BigDecimal.ZERO) ? BigDecimal.ZERO
                                                                                                    : lastMonthAmount.divide(lastMonthWeight, 2, RoundingMode.DOWN));
        tradeAverage.setComparedWithLastMonthUnitAmount(tradeAverage.getThisMonthAverageUnitAmount()
                                                                .subtract(tradeAverage.getLastMonthAverageUnitAmount()));

        // 获取近30天时间，构建近30天交易信息
        List<OrderDetail> recentThirtyDayOrderDetails = this.getOrderDetailsByPeriod(DateEnum.MONTH);
        BigDecimal recentThirtyDayAmount = recentThirtyDayOrderDetails.stream().map(OrderDetail::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal recentThirtyDayWeight = recentThirtyDayOrderDetails.stream().map(OrderDetail::getWeight)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        tradeAverage.setRecentThirtyDayAverageUnitAmount(Objects.equals(recentThirtyDayAmount, BigDecimal.ZERO) ? BigDecimal.ZERO
                                                                                                                : recentThirtyDayAmount.divide(recentThirtyDayWeight, 2, RoundingMode.DOWN));
    }

    private List<OrderDetail> getOrderDetailsByPeriod(DateEnum date) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        StatQueryParam queryParam = new StatQueryParam();
        queryParam.setType(date);
        queryParam.setTenant(tenant);
        queryParam.setPeriod(DateUtils.formatDateForType(queryParam.getType(), queryParam.getPeriod()));
        List<Order> orders = this.orderService.getDealOrderPage(queryParam);
        return this.orderDetailService.listByOrderIds(orders.stream().map(Order::getId).collect(Collectors.toList()), tenant);
    }

    private List<PorkStatVO.PorkStatLineVO> setSupplyCapacityLine(List<LocalDateTime> dates, List<PorkArrange> porkArranges,
                                                                  Boolean isLess12
                                                                 ) {
        return dates.stream().map(date -> {
            PorkStatVO.PorkStatLineVO line = new PorkStatVO.PorkStatLineVO();
            if (isLess12) {
                line.setDay(DateUtils.addHours(date, 12));
            } else {
                line.setDay(DateUtils.addHours(date, -12));
            }
            LocalDateTime midTime = getMidTime(date);
            LocalDateTime nextMidTime = addDays(midTime, 1);
            List<PorkArrange> arranges = porkArranges.stream().map(arrange -> {
                if (DateUtils.toTimestamp(arrange.getPorkArrangeTime()) >= DateUtils.toTimestamp(midTime) &&
                    DateUtils.toTimestamp(arrange.getPorkArrangeTime()) < DateUtils.toTimestamp(nextMidTime)) {
                    return arrange;
                }
                return null;
            }).filter(Objects::nonNull).collect(Collectors.toList());
            line.setValue(BigDecimal.valueOf(arranges.stream()
                                                     .filter(porkArrange -> Objects.nonNull(porkArrange.getSlices()))
                                                     .mapToInt(PorkArrange::getSlices).sum()));
            return line;
        }).collect(Collectors.toList());
    }

    private List<PorkStatVO.PorkStatLineVO> setTradeAverageLine(List<LocalDateTime> dates, List<Order> orders, Boolean isLess12) {
        return dates.stream().map(date -> {
            PorkStatVO.PorkStatLineVO line = new PorkStatVO.PorkStatLineVO();
            if (isLess12) {
                line.setDay(DateUtils.addHours(date, 12));
            } else {
                line.setDay(DateUtils.addHours(date, -12));
            }
            LocalDateTime midTime = getMidTime(date);
            LocalDateTime nextMidTime = addDays(midTime, 1);
            List<Order> ordersByDate = orders.stream().map(order -> {
                if (DateUtils.toTimestamp(order.getOrderTime()) >= DateUtils.toTimestamp(midTime) &&
                    DateUtils.toTimestamp(order.getOrderTime()) < DateUtils.toTimestamp(nextMidTime)) {
                    return order;
                }
                return null;
            }).filter(Objects::nonNull).collect(Collectors.toList());
            BigDecimal amount = ordersByDate.stream().map(Order::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal weight = ordersByDate.stream().map(Order::getWeight).reduce(BigDecimal.ZERO, BigDecimal::add);
            line.setValue(amount == BigDecimal.ZERO ? BigDecimal.ZERO : amount.divide(weight, 2, RoundingMode.DOWN));
            return line;
        }).collect(Collectors.toList());
    }

    private List<PorkStatVO.PorkStatLineVO> setTradeCapacityLine(List<LocalDateTime> dates, List<Order> orders,
                                                                 List<OrderDetail> orderDetails,
                                                                 Boolean isLess12
                                                                ) {
        return dates.stream().map(date -> {
            PorkStatVO.PorkStatLineVO line = new PorkStatVO.PorkStatLineVO();
            if (isLess12) {
                line.setDay(DateUtils.addHours(date, 12));
            } else {
                line.setDay(DateUtils.addHours(date, -12));
            }
            LocalDateTime midTime = getMidTime(date);
            LocalDateTime nextMidTime = addDays(midTime, 1);
            List<Long> orderIds = orders.stream().map(order -> {
                if (DateUtils.toTimestamp(order.getOrderTime()) >= DateUtils.toTimestamp(midTime) &&
                    DateUtils.toTimestamp(order.getOrderTime()) < DateUtils.toTimestamp(nextMidTime)) {
                    return order.getId();
                }
                return null;
            }).filter(Objects::nonNull).collect(Collectors.toList());
            BigDecimal value = BigDecimal.valueOf(orderDetails.stream().filter(orderDetail -> orderIds.contains(orderDetail.getOrderId()))
                                                          .count());
            line.setValue(value);
            return line;
        }).collect(Collectors.toList());
    }

    /**
     * @param date
     *
     * @return
     */
    public List<PorkArrange> getPorkArrangeByPeriod(DateEnum date) {
        StatQueryParam queryParam = new StatQueryParam();
        queryParam.setType(date);
        queryParam.setPeriod(DateUtils.formatDateForType(queryParam.getType(), queryParam.getPeriod()));
        List<PorkArrangeAudit> porkArrangeAudits = this.porkArrangeAuditService.lambdaQuery()
                .ne(PorkArrangeAudit::getAuditStatus, PorkArrangeAuditEnum.AUDIT)
                .between(Objects.nonNull(queryParam.getPeriod()),
                         PorkArrangeAudit::getAuditTime, queryParam.getPeriod().getBegin(), queryParam.getPeriod().getEnd())
                .list();
        Set<Long> porkArrangeIds = porkArrangeAudits.stream().map(PorkArrangeAudit::getPorkArrangeId).collect(Collectors.toSet());
        return CollectionUtils.isEmpty(porkArrangeIds) ? Collections.emptyList() :
               this.porkArrangeService.lambdaQuery()
                       .eq(PorkArrange::getStatus, PorkArrangeEnum.FINISHED)
                       .eq(PorkArrange::getTenant, SessionContextHolder.getRequester().getTenant())
                       .in(PorkArrange::getId, porkArrangeIds).list();
    }
}