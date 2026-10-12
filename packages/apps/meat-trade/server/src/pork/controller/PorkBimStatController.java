package com.guhecloud.cube.biz.meattrade.server.pork.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.mdata.client.SupplierClient;
import com.guhecloud.cube.biz.mdata.client.dto.SupplierDTO;
import com.guhecloud.cube.biz.meattrade.server.base.dto.AbattoirBimStatDTO;
import com.guhecloud.cube.biz.meattrade.server.base.entity.Abattoir;
import com.guhecloud.cube.biz.meattrade.server.base.entity.Config;
import com.guhecloud.cube.biz.meattrade.server.base.entity.Inspector;
import com.guhecloud.cube.biz.meattrade.server.base.enumeration.ConfigEnum;
import com.guhecloud.cube.biz.meattrade.server.base.enumeration.DateEnum;
import com.guhecloud.cube.biz.meattrade.server.base.param.AbattoirQueryParam;
import com.guhecloud.cube.biz.meattrade.server.base.service.AbattoirService;
import com.guhecloud.cube.biz.meattrade.server.base.service.ConfigService;
import com.guhecloud.cube.biz.meattrade.server.base.service.InspectorService;
import com.guhecloud.cube.biz.meattrade.server.common.facade.device.service.MeattradeDeviceQueryService;
import com.guhecloud.cube.biz.meattrade.server.order.service.OrderService;
import com.guhecloud.cube.biz.meattrade.server.order.vo.OrderDateWeightVO;
import com.guhecloud.cube.biz.meattrade.server.order.vo.OrderTradeStatisticsVO;
import com.guhecloud.cube.biz.meattrade.server.order.vo.SupplierOrderTradeStatisticsVO;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkArrange;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkArrangeAudit;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkArrangeDetail;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkReport;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PratiqueRegister;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.SpotCheck;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.PorkArrangeAuditEnum;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.SpotCheckEnum;
import com.guhecloud.cube.biz.meattrade.server.pork.param.PlaceStatQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.PorkArrangeQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.SpotCheckQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.StatQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PorkArrangeAuditService;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PorkArrangeDetailService;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PorkArrangeService;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PorkReportService;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PratiqueRegisterService;
import com.guhecloud.cube.biz.meattrade.server.pork.service.SpotCheckService;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.DeviceLastArrangeInfoVO;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.PorkArrangeDetailVO;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.PorkArrangeSlicesStatisticsVO;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.PorkArrangeSuspicionVO;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.PorkArrangeVO;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.PorkDeviceStatisticsVO;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.PorkPratiqueRegisterVO;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.PorkReportVO;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.PorkSpotCheckVO;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.PorkStatDateVO;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.PorkStatSummaryAmountListVO;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.PorkStatSummaryAveragePriceListVO;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.PorkStatSummaryCommissionListVO;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.PorkStatSummaryListVO;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.PorkStatSummarySlicesListVO;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.PorkStatSummaryVO;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.PorkStatVO;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.PorkSupplyStatisticsVO;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.SupplierInfoVO;
import com.guhecloud.cube.biz.meattrade.server.utlis.DateUtils;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.MathUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.guhecloud.cube.biz.meattrade.server.utlis.DateUtils.TIMERANGE_MONTH;
import static com.guhecloud.cube.biz.meattrade.server.utlis.DateUtils.TIMERANGE_WEEK;

/**
 * 猪肉滑轨三维数据统计接口
 *
 * @author lzy
 * @date 2022/4/7
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PorkBimStatController {

    private final AbattoirService abattoirService;
    private final OrderService orderService;
    private final PorkReportService porkReportService;
    private final SpotCheckService spotCheckService;
    private final PratiqueRegisterService pratiqueRegisterService;
    private final PorkArrangeDetailService porkArrangeDetailService;
    private final PorkArrangeAuditService porkArrangeAuditService;
    private final PorkArrangeService porkArrangeService;
    private final InspectorService inspectorService;
    private final ConfigService configService;
    private final MeattradeDeviceQueryService meattradeDeviceQueryService;

    private final SupplierClient supplierClient;
    private final UserClient userClient;
    private final FileClient fileClient;

    /**
     * 三维白条交易大户
     *
     * @param queryParam 查询参数
     *
     * @return 统计数据
     */
    @GetMapping("/pork-bim-stat/supply-trade/rank")
    public Pagination<PorkStatSummaryVO> getSupplyTradeRank(@Valid StatQueryParam queryParam) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        // 获取时间
        this.setDateTime(queryParam);
        // 获取订单完成信息
        queryParam.setTenant(tenant);
        List<SupplierDTO> suppliers = this.supplierClient.listSuppliers(queryParam.getKeyword(), tenant);
        //商户交易信息统计
        List<SupplierOrderTradeStatisticsVO> supplierOrderTradeStatistics = this.orderService.getSupplierOrderTradeStatistics(queryParam);
        Map<Long, SupplierOrderTradeStatisticsVO> supplierOrderTradeStatisticsVOMap = supplierOrderTradeStatistics
                .stream()
                .collect(Collectors.toMap(SupplierOrderTradeStatisticsVO::getSupplierId, obj -> obj, (o, n) -> n));
        // 构建信息
        List<PorkStatSummaryVO> summaryVOS = this.setSupplyTradeRank(suppliers, supplierOrderTradeStatisticsVOMap);
        // 构建环比信息
        this.setQoqDateTime(queryParam);
        //上期商户交易信息统计
        List<SupplierOrderTradeStatisticsVO> qoqSupplierOrderTradeStatistics = this.orderService.getSupplierOrderTradeStatistics(queryParam);
        Map<Long, SupplierOrderTradeStatisticsVO> qoqSupplierOrderTradeStatisticsVOMap = qoqSupplierOrderTradeStatistics
                .stream()
                .collect(Collectors.toMap(SupplierOrderTradeStatisticsVO::getSupplierId, obj -> obj, (o, n) -> n));
        this.setSupplyTradeRankQOQ(summaryVOS, qoqSupplierOrderTradeStatisticsVOMap);

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
     * 三维白条来货数据统计
     *
     * @param queryParam 查询参数
     *
     * @return 屠宰场信息
     */
    @GetMapping("/pork-bim-stat/abattoir")
    public Pagination<PorkStatVO.PorkStatAbattoir> getPorkBimStatAbattoir(@Valid StatQueryParam queryParam) {
        this.setDateTime(queryParam);
        // 获取屠宰场信息
        AbattoirQueryParam abattoirQueryParam = new AbattoirQueryParam();
        abattoirQueryParam.setPageNumber(queryParam.getPageNumber());
        abattoirQueryParam.setPageSize(queryParam.getPageSize());
        abattoirQueryParam.setTenant(SessionContextHolder.getRequester().getTenant());
        abattoirQueryParam.setPeriod(queryParam.getPeriod());
        // 获取时间
        Page<AbattoirBimStatDTO> page = this.abattoirService.bimStatPage(abattoirQueryParam);

        Map<Long, List<AbattoirBimStatDTO>> abattoirMap = page.getRecords().stream()
                .collect(Collectors.groupingBy(AbattoirBimStatDTO::getId));

        // 获取环比时间
        this.setQoqDateTime(queryParam);
        abattoirQueryParam.setPeriod(queryParam.getPeriod());
        List<AbattoirBimStatDTO> qoqAbattoirs = this.abattoirService.bimStatList(abattoirQueryParam);
        Map<Long, List<AbattoirBimStatDTO>> qoqAbattoirMap = qoqAbattoirs.stream()
                .collect(Collectors.groupingBy(AbattoirBimStatDTO::getId));

        // 构建信息
        List<PorkStatVO.PorkStatAbattoir> records = abattoirMap.keySet().stream().map(abattoirId -> {
            PorkStatVO.PorkStatAbattoir abattoir = new PorkStatVO.PorkStatAbattoir();
            AbattoirBimStatDTO abattoirDTO = abattoirMap.get(abattoirId).get(0);
            abattoir.setSupplySlices(abattoirDTO.getSlices() == null ? 0 : abattoirDTO.getSlices());
            abattoir.setSupplyWeight(abattoirDTO.getWeight() == null ? BigDecimal.ZERO : abattoirDTO.getWeight());
            abattoir.setAbattoirName(abattoirDTO.getName());
            if (qoqAbattoirMap.containsKey(abattoirId)) {
                AbattoirBimStatDTO qoqStatAbattoirDTO = qoqAbattoirMap.get(abattoirId).get(0);
                abattoir.setQoq(Objects.equals(qoqStatAbattoirDTO.getWeight(), BigDecimal.ZERO) ? BigDecimal.ZERO :
                                (abattoir.getSupplyWeight().subtract(qoqStatAbattoirDTO.getWeight()))
                                        .divide(qoqStatAbattoirDTO.getWeight(), 4, RoundingMode.DOWN)
                                        .multiply(BigDecimal.valueOf(100)));
            } else {
                abattoir.setQoq(BigDecimal.ZERO);
            }
            return abattoir;
        }).collect(Collectors.toList());
        return PagingUtils.convert(page, records);
    }

    /**
     * 交易数据汇总
     *
     * @param queryParam 查询参数
     *
     * @return 交易数据汇总信息
     */
    @GetMapping("/pork-bim-stat/summary")
    public PorkStatSummaryVO getPorkStatSummary(@Valid StatQueryParam queryParam) {
        PorkStatSummaryVO summary = getPorkStatSummaryVO(queryParam);
        // 获取环比交易数
        this.setQoqTradeInfo(summary, queryParam);
        return summary;
    }

    private PorkStatSummaryVO getPorkStatSummaryVO(StatQueryParam queryParam) {
        // 市场租户编号
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        // 获取时间
        queryParam.setPeriod(DateUtils.formatDateForType(queryParam.getType(), queryParam.getPeriod()));
        queryParam.setTenant(tenant);
        // 实际均价信息获取，先获取该市场的最低价格
        Config config = this.configService.getByType(ConfigEnum.SELLING_PRICE);
        BigDecimal lowerPrice = Objects.isNull(config) ? BigDecimal.ZERO : BigDecimal.valueOf(config.getValue());
        queryParam.setLowerPrice(lowerPrice);
        // 获取交易完成订单数据
        OrderTradeStatisticsVO order = this.orderService.getOrderTradeStatistics(queryParam);
        // 构建信息
        PorkStatSummaryVO summary = new PorkStatSummaryVO();
        // 获取交易数据信息
        this.setTradeInfo(order, summary);
        PorkStatSummaryVO porkStatSummaryVO = porkArrangeDetailService.statSupplyMeatInfo(queryParam);
        summary.setSupplier(porkStatSummaryVO.getSupplier() == null ? 0 : porkStatSummaryVO.getSupplier());
        summary.setSupplyWeight(porkStatSummaryVO.getSupplyWeight() == null ? new BigDecimal("0.00") : porkStatSummaryVO.getSupplyWeight());
        summary.setSupplySlices(porkStatSummaryVO.getSupplySlices() == null ? 0 : porkStatSummaryVO.getSupplySlices());
        return summary;
    }

    /**
     * 交易重量列表
     *
     * @param queryParam
     *
     * @return
     */
    @GetMapping("/pork-bim-stat/summary-weight/list")
    public PorkStatSummaryListVO getPorkStatSummaryWeightList(@Valid StatQueryParam queryParam) {
        // 获取时间
        queryParam.setPeriod(DateUtils.formatDateForType(queryParam.getType(), queryParam.getPeriod()));
        // 市场租户编号
        queryParam.setTenant(SessionContextHolder.getRequester().getTenant());
        // 获取交易完成订单重量数据
        List<OrderDateWeightVO> currentOrderDateWeightVOS = this.orderService.orderDateGroupWeights(queryParam);
        //本期总重量
        BigDecimal currentWeight = currentOrderDateWeightVOS.stream().map(OrderDateWeightVO::getSummaryWeight)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        // 构建环比信息
        this.setQoqDateTime(queryParam);
        // 上期交易完成订单重量数据
        List<OrderDateWeightVO> lastOrderDateWeightVOS = this.orderService.orderDateGroupWeights(queryParam);
        //上期总重量
        BigDecimal lastWeight = lastOrderDateWeightVOS.stream().map(OrderDateWeightVO::getSummaryWeight)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new PorkStatSummaryListVO(currentWeight,
                                         currentOrderDateWeightVOS,
                                         lastWeight,
                                         lastOrderDateWeightVOS,
                                         Objects.equals(lastWeight, BigDecimal.ZERO) ? BigDecimal.ZERO : (currentWeight.subtract(lastWeight)
                                                 .divide(lastWeight, 4, RoundingMode.DOWN))
                                                 .multiply(BigDecimal.valueOf(100)));
    }

    /**
     * 交易金额列表
     *
     * @param queryParam
     *
     * @return
     */
    @GetMapping("/pork-bim-stat/summary/amount/list")
    public PorkStatSummaryAmountListVO getPorkStatSummaryAmountList(@Valid StatQueryParam queryParam) {
        return null;
    }

    /**
     * 交易佣金列表
     *
     * @param queryParam
     *
     * @return
     */
    @GetMapping("/pork-bim-stat/summary/commission/list")
    public PorkStatSummaryCommissionListVO getPorkStatSummaryCommissionList(@Valid StatQueryParam queryParam) {
        return null;
    }

    /**
     * 交易均价列表
     *
     * @param queryParam
     *
     * @return
     */
    @GetMapping("/pork-bim-stat/summary/average-price/list")
    public PorkStatSummaryAveragePriceListVO getPorkStatSummaryAveragePriceList(@Valid StatQueryParam queryParam) {
        return null;
    }

    /**
     * 实际均价列表
     *
     * @param queryParam
     *
     * @return
     */
    @GetMapping("/pork-bim-stat/summary/actual-average-price/list")
    public PorkStatSummaryAveragePriceListVO getPorkStatSummaryActualAveragePriceList(@Valid StatQueryParam queryParam) {
        return null;
    }

    /**
     * 交易片数
     *
     * @param queryParam
     *
     * @return
     */
    @GetMapping("/pork-bim-stat/summary/slices/list")
    public PorkStatSummarySlicesListVO getPorkStatSummarySlicesList(@Valid StatQueryParam queryParam) {
        return null;
    }

    /**
     * 供货数据信息
     *
     * @param queryParam 查询信息
     *
     * @return 统计信息
     */
    @GetMapping("/pork-bim-stat/supply-data")
    public PorkStatSummaryVO getSupplyData(@Valid StatQueryParam queryParam) {
        // 市场租户编号
        Integer tenant = SessionContextHolder.getRequester().getTenant();

        // 构建信息
        PorkStatSummaryVO summary = new PorkStatSummaryVO();
        queryParam.setTenant(tenant);
        queryParam.setPeriod(DateUtils.formatDateForType(queryParam.getType(), queryParam.getPeriod()));
        PorkSupplyStatisticsVO supplyStatistics = this.porkReportService.getSupplyStatistics(queryParam);
        Integer supplierCount = this.porkReportService.getSupplySupplierCount(queryParam);
        this.setSupplyInfo(supplyStatistics, summary, supplierCount);

        // 构建环比信息-
        this.setQoqDateTime(queryParam);
        PorkSupplyStatisticsVO qoqSupplyStatistics = this.porkReportService.getSupplyStatistics(queryParam);
        Integer qoqSupplierCount = this.porkReportService.getSupplySupplierCount(queryParam);
        this.setQoqSupplyInfo(qoqSupplyStatistics, qoqSupplierCount, summary);
        return summary;
    }

    /**
     * 食安信息
     *
     * @param queryParam 查询参数
     *
     * @return 统计信息
     */
    @GetMapping("/pork-bim-stat/spot-check")
    public PorkStatVO.PorkStatSpotCheck getPorkStatSpotCheck(@Valid StatQueryParam queryParam) {
        // 获取时间
        this.setDateTime(queryParam);

        PorkStatVO.PorkStatSpotCheck spotCheck = new PorkStatVO.PorkStatSpotCheck();
        // 获取供货数据信息，先要获取来货报备信息
        queryParam.setTenant(SessionContextHolder.getRequester().getTenant());
        List<PorkReport> porkReports = this.porkReportService.getStatPorkReports(queryParam);
        spotCheck.setSupplyReports(CollectionUtils.isEmpty(porkReports) ? 0 : porkReports.size());
        // 获取检疫票信息
        this.setSpotCheckInfo(porkReports, spotCheck);

        // 构建环比信息
        this.setQoqDateTime(queryParam);
        List<PorkReport> qoqPorkReports = this.porkReportService.getStatPorkReports(queryParam);
        this.setSpotCheckQOQ(qoqPorkReports, spotCheck);
        return spotCheck;
    }

    /**
     * 批次作弊嫌疑率
     *
     * @param queryParam 查询参数
     *
     * @return 统计信息
     */
    @GetMapping("/pork-bim-stat/suspicion")
    public PorkStatVO.ReportSuspicion getReportSuspicion(@Valid StatQueryParam queryParam) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        // 获取时间
        this.setDateTime(queryParam);
        queryParam.setTenant(tenant);
        PorkStatVO.ReportSuspicion suspicion = new PorkStatVO.ReportSuspicion();
        Integer arrangeCount = this.porkArrangeService.getArrangeCount(queryParam);
        //        // 获取报备信息
        //        List<PorkReport> porkReports = this.porkReportService.getStatPorkReports(queryParam);
        if (Objects.isNull(arrangeCount) || 0 == arrangeCount) {
            suspicion.setTotal(0);
            suspicion.setSuspicion(BigDecimal.ZERO);
            suspicion.setSuspicionNum(0);
            suspicion.setNoDoubt(BigDecimal.ZERO);
            suspicion.setNoDoubtNum(0);
            suspicion.setQoqSuspicion(BigDecimal.ZERO);
            suspicion.setQoqNoDoubt(BigDecimal.ZERO);
            return suspicion;
        }
        //作弊数量
        Integer cheatCount = this.porkArrangeService.getCheatCount(queryParam);
        // 构建信息
        this.setReportCheatInfo(arrangeCount, cheatCount, suspicion);
        // 构建环比信息
        this.setQoqDateTime(queryParam);
        Integer qaqArrangeCount = this.porkArrangeService.getArrangeCount(queryParam);
        Integer qaqCheatCount = this.porkArrangeService.getCheatCount(queryParam);
        this.setQoqReportCheatInfo(qaqArrangeCount, qaqCheatCount, suspicion);
        return suspicion;
    }

    /**
     * 三维批次作弊分页
     *
     * @param queryParam 查询参数
     *
     * @return 理货详情信息
     */
    @GetMapping("/pork-bim-stat/suspicion/page")
    public Pagination<PorkArrangeSuspicionVO> pageReportSuspicion(@Valid StatQueryParam queryParam) {
        // 获取理货批次信息
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        PorkArrangeQueryParam porkArrangeQueryParam = new PorkArrangeQueryParam();
        porkArrangeQueryParam.setPeriod(queryParam.getPeriod());
        porkArrangeQueryParam.setTenant(tenant);
        Page<PorkArrange> page = this.porkArrangeService.pageBimPorkArrange(porkArrangeQueryParam);

        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }

        // 获取报备信息
        Set<Long> porkReportIds = page.getRecords().stream().map(PorkArrange::getPorkReportId).collect(Collectors.toSet());
        List<PorkReport> porkReports = this.porkReportService.listByIds(porkReportIds);
        Map<Long, List<PorkReport>> porkReportMap = porkReports.stream().collect(Collectors.groupingBy(PorkReport::getId));

        // 获取理货详情信息
        Set<String> porkReportCodes = porkReports.stream().map(PorkReport::getCode).collect(Collectors.toSet());
        List<PorkArrangeDetail> porkArrangeDetails = this.porkArrangeDetailService.listCheatPorkArrangeDetails(porkReportCodes, tenant);
        Map<Long, List<PorkArrangeDetail>> porkArrangeDetailMap = porkArrangeDetails.stream()
                .collect(Collectors.groupingBy(PorkArrangeDetail::getPorkReportId));

        // 获取批发商信息
        Set<Long> supplierIds = porkReports.stream().map(PorkReport::getSupplierId).collect(Collectors.toSet());
        List<SupplierDTO> suppliers = this.supplierClient.listByIds(supplierIds);
        Map<Long, List<SupplierDTO>> supplierMap = suppliers.stream().collect(Collectors.groupingBy(SupplierDTO::getId));

        // 构建信息
        List<PorkArrangeSuspicionVO> records = ObjectUtils.copying(page.getRecords(), PorkArrangeSuspicionVO.class);
        records.forEach(record -> {
            if (porkArrangeDetailMap.containsKey(record.getPorkReportId())) {
                record.setCheatSlices(porkArrangeDetailMap.get(record.getPorkReportId()).size());
            } else {
                record.setCheatSlices(0);
            }
            if (porkReportMap.containsKey(record.getPorkReportId())) {
                PorkReport porkReport = porkReportMap.get(record.getPorkReportId()).get(0);
                record.setPorkReportCode(porkReport.getCode());
                if (supplierMap.containsKey(porkReport.getSupplierId())) {
                    SupplierDTO supplier = supplierMap.get(porkReport.getSupplierId()).get(0);
                    record.setSupplierName(supplier.getName());
                    record.setSupplierId(supplier.getId());
                }
            }
        });
        return PagingUtils.convert(page, records);
    }

    /**
     * 查看理货中作弊信息
     *
     * @param code 批次号
     *
     * @return 作弊信息
     */
    @GetMapping("/pork-bim-stat/suspicion-detail/{code}/page")
    public Pagination<PorkArrangeDetailVO> pageReportSuspicionDetail(@PathVariable("code") String code) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        // 获取作弊理货详情信息
        PorkArrangeQueryParam queryParam = new PorkArrangeQueryParam();
        queryParam.setPorkReportCode(code);
        queryParam.setTenant(tenant);
        Page<PorkArrangeDetail> page = this.porkArrangeDetailService.pageCheatPorkArrangeDetails(queryParam);
        return PagingUtils.convert(page, PorkArrangeDetailVO.class);
    }

    /**
     * 三维登记检疫票分页信息
     *
     * @param queryParam 查询参数
     *
     * @return
     */
    @GetMapping("/pork-bim-stat/pratique-register/page")
    public Pagination<PorkPratiqueRegisterVO> pageBimPratiqueRegister(@Valid StatQueryParam queryParam) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        // 获取不合格检疫票信息
        queryParam.setTenant(tenant);
        List<PorkReport> porkReports = this.porkReportService.getStatPorkReports(queryParam);
        if (CollectionUtils.isEmpty(porkReports)) {
            return Pagination.empty();
        }

        Set<Long> porkReportIds = porkReports.stream().map(PorkReport::getId).collect(Collectors.toSet());
        Page<PratiqueRegister> page = this.pratiqueRegisterService.pageFailedPratiqueRegister(new Paging(queryParam.getPageNumber(), queryParam.getPageSize()), porkReportIds, tenant);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }

        // 获取理货信息
        porkReportIds = page.getRecords().stream().map(PratiqueRegister::getPorkReportId).collect(Collectors.toSet());
        List<PorkArrange> porkArranges = this.porkArrangeService.listByPorkReportIds(porkReportIds, tenant);
        Map<Long, List<PorkArrange>> porkArrangeMap = porkArranges.stream().collect(Collectors.groupingBy(PorkArrange::getPorkReportId));

        // 获取批发商信息
        Set<Long> supplierIds = porkReports.stream().map(PorkReport::getSupplierId).collect(Collectors.toSet());
        List<SupplierDTO> suppliers = this.supplierClient.listByIds(supplierIds);
        Map<Long, List<SupplierDTO>> supplierMap = suppliers.stream().collect(Collectors.groupingBy(SupplierDTO::getId));

        // 获取登记人员信息
        Set<Long> registrantIds = page.getRecords().stream().map(PratiqueRegister::getRegistrantId).collect(Collectors.toSet());
        List<UserDTO> users = this.userClient.listUserByIds(registrantIds);
        Map<Long, List<UserDTO>> userMap = users.stream().collect(Collectors.groupingBy(UserDTO::getId));

        // 获取来货报备映射关系
        Map<Long, List<PorkReport>> porkReportMap = porkReports.stream().collect(Collectors.groupingBy(PorkReport::getId));

        // 构建信息
        List<PorkPratiqueRegisterVO> records = ObjectUtils.copying(page.getRecords(), PorkPratiqueRegisterVO.class);
        records.forEach(record -> {
            if (porkReportMap.containsKey(record.getPorkReportId())) {
                PorkReport porkReport = porkReportMap.get(record.getPorkReportId()).get(0);
                record.setPorkReportCode(porkReport.getCode());
                record.setGoodsCategoryId(porkReport.getGoodsCategoryId());
                record.setGoodsCategoryName(porkReport.getGoodsCategoryName());
                if (supplierMap.containsKey(porkReport.getSupplierId())) {
                    SupplierDTO supplier = supplierMap.get(porkReport.getSupplierId()).get(0);
                    record.setSupplierId(supplier.getId());
                    record.setSupplierName(supplier.getName());
                }
            }
            if (porkArrangeMap.containsKey(record.getPorkReportId())) {
                PorkArrange porkArrange = porkArrangeMap.get(record.getPorkReportId()).get(0);
                record.setSlices(porkArrange.getSlices());
            }
            if (userMap.containsKey(record.getRegistrantId())) {
                UserDTO user = userMap.get(record.getRegistrantId()).get(0);
                record.setRegisterName(user.getName());
            }
        });
        return PagingUtils.convert(page, records);
    }

    /**
     * 三维瘦肉精分页信息
     *
     * @param queryParam 查询信息
     *
     * @return
     */
    @GetMapping("/pork-bim-stat/spot-check-clenbuterol/page")
    public Pagination<PorkSpotCheckVO> pageBimSpotCheckClenbuterol(@Valid StatQueryParam queryParam) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        // 获取报备审核过的
        queryParam.setTenant(tenant);
        List<PorkReport> porkReports = this.porkReportService.getStatPorkReports(queryParam);
        if (CollectionUtils.isEmpty(porkReports)) {
            return Pagination.empty();
        }
        // 获取瘦肉精为阳性的批次号
        Set<Long> porkReportIds = porkReports.stream().map(PorkReport::getId).collect(Collectors.toSet());
        SpotCheckQueryParam spotCheckQueryParam = new SpotCheckQueryParam();
        spotCheckQueryParam.setClenbuterol(SpotCheckEnum.POSITIVE);
        spotCheckQueryParam.setPorkReportIds(porkReportIds);
        spotCheckQueryParam.setTenant(tenant);
        spotCheckQueryParam.setPeriod(queryParam.getPeriod());
        Page<SpotCheck> page = this.spotCheckService.pageSpotCheck(spotCheckQueryParam);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }

        // 获取理货详情信息
        List<PorkArrange> porkArranges = this.porkArrangeService.listByPorkReportIds(porkReportIds, tenant);
        Map<Long, List<PorkArrange>> porkArrangeMap = porkArranges.stream().collect(Collectors.groupingBy(PorkArrange::getPorkReportId));

        // 获取批发商信息
        Set<Long> supplierIds = porkReports.stream().map(PorkReport::getSupplierId).collect(Collectors.toSet());
        List<SupplierDTO> suppliers = this.supplierClient.listByIds(supplierIds);
        Map<Long, List<SupplierDTO>> supplierMap = suppliers.stream().collect(Collectors.groupingBy(SupplierDTO::getId));

        // 获取登记员信息
        Set<Long> inspectorIds = page.getRecords().stream().map(SpotCheck::getInspectorId).collect(Collectors.toSet());
        List<Inspector> inspectors = this.inspectorService.listByIds(inspectorIds);
        Map<Long, List<Inspector>> inspectorMap = inspectors.stream().collect(Collectors.groupingBy(Inspector::getId));

        // 获取来货报备映射关系
        Map<Long, List<PorkReport>> porkReportMap = porkReports.stream().collect(Collectors.groupingBy(PorkReport::getId));

        // 构建信息
        List<PorkSpotCheckVO> records = ObjectUtils.copying(page.getRecords(), PorkSpotCheckVO.class);
        records.forEach(record -> {
            if (porkArrangeMap.containsKey(record.getPorkReportId())) {
                PorkArrange porkArrange = porkArrangeMap.get(record.getPorkReportId()).get(0);
                record.setSlices(porkArrange.getSlices());
            }
            if (inspectorMap.containsKey(record.getInspectorId())) {
                Inspector inspector = inspectorMap.get(record.getInspectorId()).get(0);
                record.setInspectorName(inspector.getName());
            }
            if (porkReportMap.containsKey(record.getPorkReportId())) {
                PorkReport porkReport = porkReportMap.get(record.getPorkReportId()).get(0);
                record.setPorkReportCode(porkReport.getCode());
                if (supplierMap.containsKey(porkReport.getSupplierId())) {
                    SupplierDTO supplier = supplierMap.get(porkReport.getSupplierId()).get(0);
                    record.setSupplierId(supplier.getId());
                    record.setSupplierName(supplier.getName());
                }
            }
        });
        return PagingUtils.convert(page, records);
    }

    /**
     * 三维含水量分页信息
     *
     * @param queryParam 查询信息
     *
     * @return
     */
    @GetMapping("/pork-bim-stat/spot-check-watered/page")
    public Pagination<PorkSpotCheckVO> pageBimSpotCheckWatered(@Valid StatQueryParam queryParam) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        // 获取报备审核过的
        queryParam.setTenant(tenant);
        List<PorkReport> porkReports = this.porkReportService.getStatPorkReports(queryParam);
        if (CollectionUtils.isEmpty(porkReports)) {
            return Pagination.empty();
        }
        // 获取含水量为阳性的批次号
        Set<Long> porkReportIds = porkReports.stream().map(PorkReport::getId).collect(Collectors.toSet());
        SpotCheckQueryParam spotCheckQueryParam = new SpotCheckQueryParam();
        spotCheckQueryParam.setWatered(SpotCheckEnum.POSITIVE);
        spotCheckQueryParam.setPorkReportIds(porkReportIds);
        spotCheckQueryParam.setTenant(tenant);
        spotCheckQueryParam.setPeriod(queryParam.getPeriod());
        Page<SpotCheck> page = this.spotCheckService.pageSpotCheck(spotCheckQueryParam);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }

        // 获取理货详情信息
        List<PorkArrange> porkArranges = this.porkArrangeService.listByPorkReportIds(porkReportIds, tenant);
        Map<Long, List<PorkArrange>> porkArrangeMap = porkArranges.stream().collect(Collectors.groupingBy(PorkArrange::getPorkReportId));

        // 获取批发商信息
        Set<Long> supplierIds = porkReports.stream().map(PorkReport::getSupplierId).collect(Collectors.toSet());
        List<SupplierDTO> suppliers = this.supplierClient.listByIds(supplierIds);
        Map<Long, List<SupplierDTO>> supplierMap = suppliers.stream().collect(Collectors.groupingBy(SupplierDTO::getId));

        // 获取登记员信息
        Set<Long> inspectorIds = page.getRecords().stream().map(SpotCheck::getInspectorId).collect(Collectors.toSet());
        List<Inspector> inspectors = this.inspectorService.listByIds(inspectorIds);
        Map<Long, List<Inspector>> inspectorMap = inspectors.stream().collect(Collectors.groupingBy(Inspector::getId));

        // 获取来货报备映射关系
        Map<Long, List<PorkReport>> porkReportMap = porkReports.stream().collect(Collectors.groupingBy(PorkReport::getId));

        // 构建信息
        List<PorkSpotCheckVO> records = ObjectUtils.copying(page.getRecords(), PorkSpotCheckVO.class);
        records.forEach(record -> {
            if (porkArrangeMap.containsKey(record.getPorkReportId())) {
                PorkArrange porkArrange = porkArrangeMap.get(record.getPorkReportId()).get(0);
                record.setSlices(porkArrange.getSlices() == null ? 0 : porkArrange.getSlices());
            }
            if (inspectorMap.containsKey(record.getInspectorId())) {
                Inspector inspector = inspectorMap.get(record.getInspectorId()).get(0);
                record.setInspectorName(inspector.getName());
            }
            if (porkReportMap.containsKey(record.getPorkReportId())) {
                PorkReport porkReport = porkReportMap.get(record.getPorkReportId()).get(0);
                record.setPorkReportCode(porkReport.getCode());
                if (supplierMap.containsKey(porkReport.getSupplierId())) {
                    SupplierDTO supplier = supplierMap.get(porkReport.getSupplierId()).get(0);
                    record.setSupplierId(supplier.getId());
                    record.setSupplierName(supplier.getName());
                }
            }
        });
        return PagingUtils.convert(page, records);
    }

    /**
     * 理货数据审核
     *
     * @param queryParam 查询参数
     *
     * @return 统计信息
     */
    @GetMapping("/pork-bim-stat/arrange-audit")
    public PorkStatVO.PorkStatArrangeAudit getPorkStatArrangeAudit(@Valid StatQueryParam queryParam) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();

        // 获取时间
        this.setDateTime(queryParam);
        queryParam.setTenant(tenant);

        PorkStatVO.PorkStatArrangeAudit arrangeAudit = new PorkStatVO.PorkStatArrangeAudit();
        // 获取报备信息，根据报备号查询理货片数
        List<PorkReport> porkReports = this.porkReportService.getStatPorkReports(queryParam);
        List<Long> porkReportIds = porkReports.stream().map(PorkReport::getId).collect(Collectors.toList());
        List<PorkArrangeAudit> porkArrangeAudits = this.porkArrangeAuditService.listByPorkReportIds(porkReportIds, tenant);

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

        // 构建环比信息
        this.setQoqDateTime(queryParam);
        // 获取报备信息，根据报备号查询理货片数
        List<PorkReport> qoqPorkReports = this.porkReportService.getStatPorkReports(queryParam);
        List<Long> qoqPorkReportIds = qoqPorkReports.stream().map(PorkReport::getId).collect(Collectors.toList());
        List<PorkArrangeAudit> qoqPorkArrangeAudits = this.porkArrangeAuditService.listByPorkReportIds(qoqPorkReportIds, tenant);
        int qoqFailed = (int) qoqPorkArrangeAudits.stream()
                .filter(porkArrangeAudit -> PorkArrangeAuditEnum.FAILED.equals(porkArrangeAudit.getAuditStatus()))
                .count();
        int qoqPassed = (int) qoqPorkArrangeAudits.stream()
                .filter(porkArrangeAudit -> PorkArrangeAuditEnum.PASSED.equals(porkArrangeAudit.getAuditStatus()))
                .count();
        arrangeAudit.setQoqPorkArrangeAuditPassed(
                qoqPassed == 0 ? BigDecimal.ZERO : BigDecimal.valueOf(((double) passed - qoqPassed) / qoqPassed * 100));
        arrangeAudit.setQoqPorkArrangeAuditFailed(
                qoqFailed == 0 ? BigDecimal.ZERO : BigDecimal.valueOf(((double) passed - qoqFailed) / qoqFailed * 100));
        return arrangeAudit;
    }

    /**
     * 获取理货机最后一次理货信息
     *
     * @return
     */
    @GetMapping("/pork-bim-stat/device/arrange-last-info/{deviceId}")
    public DeviceLastArrangeInfoVO getDeviceLastArrangeInfo(@PathVariable("deviceId") String deviceId) {
        //最后一次理货信息
        PorkArrangeDetail detail = this.porkArrangeDetailService.getLastPorkArrangeDetailByDeviceId(deviceId);
        if (Objects.isNull(detail)) {
            return null;
        }
        PorkArrangeDetailVO porkArrangeDetailVO = ObjectUtils.copying(detail, PorkArrangeDetailVO.class);
        if (StringUtils.isNotEmpty(porkArrangeDetailVO.getTransferFiles())) {
            List<Long> fileIds = Arrays.stream(porkArrangeDetailVO.getTransferFiles().split(",")).map(fileId -> Long.valueOf(fileId))
                    .collect(Collectors.toList());
            List<FileVO> fileVOS = this.fileClient.getByIds(fileIds);
            porkArrangeDetailVO.setPicPaths(fileVOS.stream().map(FileVO::getPath).collect(Collectors.joining(",")));
            porkArrangeDetailVO.setPicFilePaths(fileVOS.stream().map(FileVO::getPath).collect(Collectors.toList()));
        }
        DeviceLastArrangeInfoVO deviceLastArrangeInfoVO = new DeviceLastArrangeInfoVO();
        //理货信息
        PorkArrange porkArrange = this.porkArrangeService.getByPorkReport(detail.getPorkReportId(), detail.getTenant());
        deviceLastArrangeInfoVO.setPorkArrangeDetail(porkArrangeDetailVO);
        //最后一次理货的商户信息
        deviceLastArrangeInfoVO.setSupplierInfo(this.getSupplierInfoVO(detail));
        deviceLastArrangeInfoVO.setPorkArrangeStatus(porkArrange.getStatus());
        deviceLastArrangeInfoVO.setPorkArrange(ObjectUtils.copying(porkArrange, PorkArrangeVO.class));
        //报备信息
        deviceLastArrangeInfoVO.setPorkReport(this.getPorkReportInfo(detail));
        //食安信息
        PorkArrangeAudit porkArrangeAudit = this.porkArrangeAuditService.getByPorkReport(detail.getPorkReportId(), detail.getTenant());
        if (Objects.isNull(porkArrangeAudit)) {
            deviceLastArrangeInfoVO.setPorkArrangeAuditStatus(porkArrangeAudit.getAuditStatus());
        }
        //报备情况
        deviceLastArrangeInfoVO.setPorkReportStatAudit(this.porkReportService.getSupplierReportAudit(detail.getSupplierId(), detail.getTenant()));
        //理货情况
        deviceLastArrangeInfoVO.setPorkStatArrangeAudit(this.porkArrangeAuditService.getSupplierArrangeAudit(detail.getSupplierId(), detail.getTenant()));
        return deviceLastArrangeInfoVO;
    }

    /**
     * 设备理货片数统计
     *
     * @param queryParam
     *
     * @return
     */
    @GetMapping("/pork-bim-stat/device/arrange-slices/statistics")
    public List<PorkDeviceStatisticsVO> getDevicePorkArrangeStatistics(@Valid PlaceStatQueryParam queryParam) {
        var deviceVOS = meattradeDeviceQueryService.queryDevices();
        if (CollectionUtils.isEmpty(deviceVOS)) {
            return CollectionUtils.emptyList();
        }
        setDateTime(queryParam);
        queryParam.setTenant(SessionContextHolder.getRequester().getTenant());
        var deviceIds = deviceVOS.stream().map(device -> String.valueOf(device.getId())).collect(Collectors.toSet());
        queryParam.setPlaceIds(deviceIds);
        var placeStatisticsMap = porkArrangeDetailService.getDevicePorkArrangeStatistics(queryParam)
                .stream()
                .collect(Collectors.toMap(PorkDeviceStatisticsVO::getId, place -> place, (o, n) -> n));
        return deviceVOS.stream().map(device -> {
            var deviceId = String.valueOf(device.getId());
            PorkDeviceStatisticsVO porkDeviceStatisticsVO;
            if (placeStatisticsMap.containsKey(deviceId)) {
                porkDeviceStatisticsVO = placeStatisticsMap.get(deviceId);
                porkDeviceStatisticsVO.setCode(device.getCode());
                porkDeviceStatisticsVO.setName(device.getName());
            } else {
                porkDeviceStatisticsVO = new PorkDeviceStatisticsVO();
                porkDeviceStatisticsVO.setId(deviceId);
                porkDeviceStatisticsVO.setCode(device.getCode());
                porkDeviceStatisticsVO.setName(device.getName());
                porkDeviceStatisticsVO.setNumber(0);
            }
            return porkDeviceStatisticsVO;
        }).collect(Collectors.toList());
    }

    /**
     * 理货片数统计
     *
     * @param queryParam
     *
     * @return
     */
    @GetMapping("/pork-bim/stat/arrange/slices/statistics")
    public PorkArrangeSlicesStatisticsVO getPorkArrangeSlicesStatistics(@Valid StatQueryParam queryParam) {
        setDateTime(queryParam);
        queryParam.setTenant(SessionContextHolder.getRequester().getTenant());
        var currentSlices = porkArrangeDetailService.getPorkArrangeSlicesStatistics(queryParam);
        setQoqDateTime(queryParam);
        var lastSlices = porkArrangeDetailService.getPorkArrangeSlicesStatistics(queryParam);
        return new PorkArrangeSlicesStatisticsVO(currentSlices.intValue(), MathUtils.calculateRingRatio(currentSlices, lastSlices, 2));
    }

    /**
     * 三位可视化获取肉类交易标准日期
     *
     * @param queryParam
     *
     * @return
     */
    @GetMapping("/pork-bim-stat/date")
    public PorkStatDateVO getStatDateInfo(@Valid StatQueryParam queryParam) {
        // 获取时间
        this.setDateTime(queryParam);
        Period currentPeriod = queryParam.getPeriod();
        //获取上期时间
        this.setQoqDateTime(queryParam);
        Period lastCurrentPeriod = queryParam.getPeriod();
        return new PorkStatDateVO(currentPeriod, lastCurrentPeriod);
    }

    private PorkReportVO getPorkReportInfo(PorkArrangeDetail detail) {
        //最后一次的理货报备信息
        PorkReport porkReport = this.porkReportService.getById(detail.getPorkReportId());
        if (Objects.isNull(porkReport)) {
            return null;
        }
        PorkReportVO porkReportVO = ObjectUtils.copying(porkReport, PorkReportVO.class);
        List<FileVO> files = this.fileClient.getByBusinessKey(porkReport.getId());
        porkReportVO.setFiles(files);
        // 扩展检疫证号
        if (StringUtils.isNotEmpty(porkReport.getPratiqueCode())) {
            porkReportVO.setPratiqueCodes(Arrays.asList(porkReport.getPratiqueCode().split(",")));
        }
        // 扩展屠宰场信息
        Abattoir abattoir = this.abattoirService.getById(porkReportVO.getAbattoirId());
        if (Objects.nonNull(abattoir)) {
            porkReportVO.setAbattoirName(abattoir.getName());
        }
        return porkReportVO;
    }

    private SupplierInfoVO getSupplierInfoVO(PorkArrangeDetail detail) {
        SupplierDTO supplier = this.supplierClient.getSupplierById(detail.getSupplierId());
        if (Objects.isNull(supplier)) {
            return null;
        }
        SupplierInfoVO supplierInfoVO = ObjectUtils.copying(supplier, SupplierInfoVO.class);
        //获取管理用户
        UserDTO supplierAdminUser = this.userClient.getTenantAdminByTenant(supplier.getSupplierTenant());
        List<FileVO> fileVOS = new ArrayList<>();
        if (Objects.nonNull(supplierAdminUser)) {
            // 获取用户头像
            fileVOS = this.fileClient.getByBusinessKey(supplierAdminUser.getId());
        }
        if (!CollectionUtils.isEmpty(fileVOS)) {
            supplierInfoVO.setFileVO(fileVOS.get(0));
        }
        return supplierInfoVO;
    }

    private void setQoqReportCheatInfo(Integer arrangeCount, Integer cheatCount, PorkStatVO.ReportSuspicion suspicion) {
        int noDoubt = arrangeCount - cheatCount;
        suspicion.setQoqSuspicion(cheatCount == 0 ? BigDecimal.ZERO
                                                  : BigDecimal.valueOf(((double) suspicion.getSuspicionNum() - cheatCount) / cheatCount)
                                          .multiply(BigDecimal.valueOf(100)));
        suspicion.setQoqNoDoubt(noDoubt == 0 ? BigDecimal.ZERO
                                             : BigDecimal.valueOf(((double) suspicion.getNoDoubtNum() - noDoubt) / noDoubt)
                                        .multiply(BigDecimal.valueOf(100)));
    }

    private void setSupplyInfo(PorkSupplyStatisticsVO supplyStatistics, PorkStatSummaryVO summary, Integer supplierCount) {
        // 信息构建
        summary.setSupplier(supplierCount);
        summary.setSupplyWeight(supplyStatistics.getWeight());
        summary.setSupplySlices(supplyStatistics.getSlices());

    }

    private void setQoqSupplyInfo(PorkSupplyStatisticsVO supplyStatistics, Integer supplierCount, PorkStatSummaryVO summary) {
        // 供货重量、供货片数信息获取
        int supplySlices = supplyStatistics.getSlices();
        // 构建环比信息
        summary.setQoqSupplyWeight(BigDecimal.ZERO.compareTo(supplyStatistics.getWeight()) == 0 ?
                                   BigDecimal.ZERO : summary.getSupplyWeight()
                                           .subtract(supplyStatistics.getWeight())
                                           .divide(supplyStatistics.getWeight(), 4, RoundingMode.DOWN)
                                           .multiply(BigDecimal.valueOf(100)));
        summary.setQoqSupplySlices(supplySlices == 0 ? BigDecimal.ZERO :
                                   BigDecimal.valueOf((double) (summary.getSupplySlices() - supplySlices) / supplySlices)
                                           .multiply(BigDecimal.valueOf(100)));
    }

    private List<PorkStatSummaryVO> setSupplyTradeRank(List<SupplierDTO> suppliers,
                                                       Map<Long, SupplierOrderTradeStatisticsVO> supplierOrderTradeStatistics
                                                      ) {
        return suppliers.stream().map(supplier -> {
            PorkStatSummaryVO summary = new PorkStatSummaryVO();
            summary.setSupplierId(Objects.isNull(supplier) ? null : supplier.getId());
            summary.setSupplyName(Objects.isNull(supplier) ? "" : supplier.getName());
            summary.setSupplierCode(Objects.isNull(supplier) ? "" : supplier.getCode());
            if (supplierOrderTradeStatistics.containsKey(supplier.getId())) {
                SupplierOrderTradeStatisticsVO supplierOrderTradeStatisticsVO = supplierOrderTradeStatistics.get(supplier.getId());
                summary.setTradeWeight(supplierOrderTradeStatisticsVO.getWeight());
                summary.setTradeCommission(supplierOrderTradeStatisticsVO.getCommission());
                summary.setTradeAmount(supplierOrderTradeStatisticsVO.getAmount());
                summary.setTradeSlices(supplierOrderTradeStatisticsVO.getSlices());
            } else {
                summary.setTradeWeight(BigDecimal.ZERO);
                summary.setTradeAmount(BigDecimal.ZERO);
            }
            return summary;
        }).collect(Collectors.toList());

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

    private void setReportCheatInfo(Integer arrangeCount, Integer cheatCount, PorkStatVO.ReportSuspicion suspicion) {
        suspicion.setTotal(arrangeCount);
        suspicion.setSuspicion(BigDecimal.valueOf((double) cheatCount / arrangeCount).multiply(BigDecimal.valueOf(100)));
        suspicion.setSuspicionNum(cheatCount);
        suspicion.setNoDoubt(BigDecimal.valueOf(100).subtract(suspicion.getSuspicion()));
        suspicion.setNoDoubtNum(arrangeCount - cheatCount);
    }

    private void setSpotCheckQOQ(List<PorkReport> porkReports, PorkStatVO.PorkStatSpotCheck spotCheck) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        List<Long> porkReportIds = porkReports.stream().map(PorkReport::getId).collect(Collectors.toList());
        // 获取抽检信息
        List<SpotCheck> spotChecks = this.spotCheckService.listByPorkReports(porkReportIds, tenant);
        List<PratiqueRegister> failedPratoqueRegisters = this.pratiqueRegisterService.getFailedPratiqueRegister(porkReportIds, tenant);
        // 构建信息
        int clenbuterolFailed = (int) spotChecks.stream()
                .filter(sp -> Objects.nonNull(sp.getClenbuterol()) && sp.getClenbuterol().equals(SpotCheckEnum.POSITIVE)).count();
        int wateredFailed = (int) spotChecks.stream()
                .filter(sp -> Objects.nonNull(sp.getWatered()) && sp.getWatered().equals(SpotCheckEnum.POSITIVE)).count();
        // 构建环比信息
        BigDecimal qoqClenbuterolFailed = 0 == spotCheck.getClenbuterolFailed() && 0 == clenbuterolFailed ? BigDecimal.ZERO :
                                          0 == clenbuterolFailed ? BigDecimal.valueOf(100) :
                                          BigDecimal.valueOf(
                                                          (double) (spotCheck.getClenbuterolFailed() - clenbuterolFailed) / clenbuterolFailed)
                                                  .multiply(BigDecimal.valueOf(100));
        BigDecimal qoqWatered = 0 == spotCheck.getWatered() && 0 == wateredFailed ? BigDecimal.ZERO :
                                0 == wateredFailed ? BigDecimal.valueOf(100) :
                                BigDecimal.valueOf((double) (spotCheck.getWatered() - wateredFailed) / wateredFailed)
                                        .multiply(BigDecimal.valueOf(100));
        BigDecimal qoqPratiqueFailed =
                0 == spotCheck.getPratiqueFailed() && CollectionUtils.isEmpty(failedPratoqueRegisters) ? BigDecimal.ZERO :
                CollectionUtils.isEmpty(failedPratoqueRegisters) ? BigDecimal.valueOf(100) :
                BigDecimal
                        .valueOf((double) (spotCheck.getPratiqueFailed() - failedPratoqueRegisters.size()) / failedPratoqueRegisters.size())
                        .multiply(BigDecimal.valueOf(100));
        spotCheck.setQoqClenbuterolFailed(qoqClenbuterolFailed);
        spotCheck.setQoqWatered(qoqWatered);
        spotCheck.setQoqPratiqueFailed(qoqPratiqueFailed);
        spotCheck.setClenbuterolFailedPer(spotCheck.getSupplyReports() == 0 ? BigDecimal.ZERO : BigDecimal
                .valueOf((double) spotCheck.getClenbuterolFailed() / spotCheck.getSupplyReports()).multiply(BigDecimal.valueOf(100)));
        spotCheck.setWateredPer(spotCheck.getSupplyReports() == 0 ? BigDecimal.ZERO : BigDecimal
                .valueOf((double) spotCheck.getWatered() / spotCheck.getSupplyReports()).multiply(BigDecimal.valueOf(100)));
        spotCheck.setPratiqueFailedPer(spotCheck.getSupplyReports() == 0 ? BigDecimal.ZERO : BigDecimal
                .valueOf((double) spotCheck.getPratiqueFailed() / spotCheck.getSupplyReports()).multiply(BigDecimal.valueOf(100)));
    }

    private void setSupplyTradeRankQOQ(List<PorkStatSummaryVO> summaryVOS,
                                       Map<Long, SupplierOrderTradeStatisticsVO> supplierOrderTradeStatisticsVOMap
                                      ) {
        //        Map<Long, List<Order>> orderMap = orders.stream().collect(Collectors.groupingBy(Order::getSupplierId));
        summaryVOS.forEach(summaryVO -> {
            if (supplierOrderTradeStatisticsVOMap.containsKey(summaryVO.getSupplierId())) {
                SupplierOrderTradeStatisticsVO supplierOrder = supplierOrderTradeStatisticsVOMap.get(summaryVO.getSupplierId());
                //                BigDecimal weight = supplierOrders.stream().map(Order::getWeight).reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal qoq = summaryVO.getSupplyWeight()
                        .subtract(supplierOrder.getWeight())
                        .divide(supplierOrder.getWeight(), 4, RoundingMode.DOWN)
                        .multiply(BigDecimal.valueOf(100));
                summaryVO.setQoq(qoq);
            } else {
                summaryVO.setQoq(Objects.equals(summaryVO.getTradeWeight(), BigDecimal.ZERO) ? BigDecimal.ZERO : BigDecimal.valueOf(100));
            }
        });
    }

    private void setQoqTradeInfo(PorkStatSummaryVO summary, StatQueryParam queryParam) {
        // 构建环比信息
        this.setQoqDateTime(queryParam);
        // 获取交易完成订单数据
        OrderTradeStatisticsVO order = this.orderService.getOrderTradeStatistics(queryParam);
        // 构建信息
        PorkStatSummaryVO qoqSummary = new PorkStatSummaryVO();
        // 获取交易数据信息
        this.setTradeInfo(order, qoqSummary);

        // 获取供货数据信息，先要获取来货报备信息;
        PorkStatSummaryVO porkStatSummaryVO = porkArrangeDetailService.statSupplyMeatInfo(queryParam);
        qoqSummary.setSupplier(porkStatSummaryVO.getSupplier() == null ? 0 : porkStatSummaryVO.getSupplier());
        qoqSummary.setSupplyWeight(
                porkStatSummaryVO.getSupplyWeight() == null ? new BigDecimal("0.00") : porkStatSummaryVO.getSupplyWeight());
        qoqSummary.setSupplySlices(porkStatSummaryVO.getSupplySlices() == null ? 0 : porkStatSummaryVO.getSupplySlices());

        // 构建环比信息
        summary.setQoqTradeAmount(BigDecimal.ZERO.compareTo(qoqSummary.getTradeAmount()) == 0 ? BigDecimal.ZERO :
                                  (summary.getTradeAmount().subtract(qoqSummary.getTradeAmount())
                                          .divide(qoqSummary.getTradeAmount(), 4, RoundingMode.DOWN))
                                          .multiply(BigDecimal.valueOf(100)));
        summary.setQoqTradeCommission(BigDecimal.ZERO.compareTo(qoqSummary.getTradeCommission()) == 0 ? BigDecimal.ZERO :
                                      (summary.getTradeCommission().subtract(qoqSummary.getTradeCommission())
                                              .divide(qoqSummary.getTradeCommission(), 4, RoundingMode.DOWN))
                                              .multiply(BigDecimal.valueOf(100)));
        summary.setQoqTradeAverageAmount(BigDecimal.ZERO.compareTo(qoqSummary.getTradeAverageAmount()) == 0 ? BigDecimal.ZERO :
                                         (summary.getTradeAverageAmount().subtract(qoqSummary.getTradeAverageAmount())
                                                 .divide(qoqSummary.getTradeAverageAmount(), 4, RoundingMode.DOWN))
                                                 .multiply(BigDecimal.valueOf(100)));
        summary.setQoqActualAverageAmount(BigDecimal.ZERO.compareTo(qoqSummary.getActualAverageAmount()) == 0 ? BigDecimal.ZERO :
                                          (summary.getActualAverageAmount().subtract(qoqSummary.getActualAverageAmount())
                                                  .divide(qoqSummary.getActualAverageAmount(), 4, RoundingMode.DOWN))
                                                  .multiply(BigDecimal.valueOf(100)));
        summary.setQoqTradeWeight(BigDecimal.ZERO.compareTo(qoqSummary.getTradeWeight()) == 0 ? BigDecimal.ZERO :
                                  (summary.getTradeWeight().subtract(qoqSummary.getTradeWeight())
                                          .divide(qoqSummary.getTradeWeight(), 4, RoundingMode.DOWN))
                                          .multiply(BigDecimal.valueOf(100)));
        summary.setQoqTradeSlices(qoqSummary.getTradeSlices() == 0 ? BigDecimal.ZERO :
                                  BigDecimal.valueOf(
                                                  (double) (summary.getTradeSlices() - qoqSummary.getTradeSlices()) / qoqSummary.getTradeSlices())
                                          .multiply(BigDecimal.valueOf(100)));
    }

    private void setTradeInfo(OrderTradeStatisticsVO order, PorkStatSummaryVO summary) {
        // 交易重量，交易金额，交易佣金信息获取
        BigDecimal tradeWeight = order.getWeight();
        BigDecimal tradeAmount = order.getAmount();
        BigDecimal tradeCommission = order.getCommission();
        int tradeSlices = order.getSlices();
        BigDecimal tradeAverageAmount = tradeSlices == 0 ? BigDecimal.ZERO : tradeAmount.divide(tradeWeight, 2, RoundingMode.DOWN);
        // 实际均价信息获取，先获取该市场的最低价格
        //去除最低限价交易的 总销售额
        BigDecimal actualAmount = order.getActualAmount();
        //去除最低限价交易的 总重量
        BigDecimal actualWeight = order.getActualWeight();
        if (Objects.nonNull(actualWeight) && actualWeight.intValue() != 0) {
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

    private void setQoqDateTime(StatQueryParam queryParam) {
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        if (Objects.isNull(queryParam.getPeriod()) && Objects.isNull(queryParam.getType())) {
            // 时间为空默认为昨天
            LocalDateTime yesterday = DateUtils.addDays(LocalDateTime.now(), -1);
            if (DateUtils.toTimestamp(yesterday) < DateUtils.toTimestamp(DateUtils.getMidTime(yesterday))) {
                startTime = DateUtils.addDays(DateUtils.getMidTime(yesterday), -1);
                endTime = DateUtils.getMidTime(yesterday);
            } else {
                startTime = DateUtils.getMidTime(yesterday);
                endTime = DateUtils.addDays(DateUtils.getMidTime(yesterday), 1);
            }
        } else if (DateEnum.TODAY.equals(queryParam.getType())) {
            // 昨天
            LocalDateTime yesterday = DateUtils.addDays(LocalDateTime.now(), -1);
            if (DateUtils.toTimestamp(yesterday) < DateUtils.toTimestamp(DateUtils.getMidTime(yesterday))) {
                startTime = DateUtils.addDays(DateUtils.getMidTime(yesterday), -1);
                endTime = DateUtils.getMidTime(yesterday);
            } else {
                startTime = DateUtils.getMidTime(yesterday);
                endTime = DateUtils.addDays(DateUtils.getMidTime(yesterday), 1);
            }
        } else if (DateEnum.WEEK.equals(queryParam.getType())) {
            // 近7天
            startTime = DateUtils.addDays(DateUtils.getMidTime(LocalDateTime.now()), -13);
            endTime = DateUtils.addDays(DateUtils.getMidTime(LocalDateTime.now()), -6);
        } else if (DateEnum.MONTH.equals(queryParam.getType())) {
            // 近30天
            LocalDateTime now = LocalDateTime.now();
            if (DateUtils.toTimestamp(now) < DateUtils.toTimestamp(DateUtils.getMidTime(LocalDateTime.now()))) {
                startTime = DateUtils.addDays(DateUtils.getMidTime(now), -60);
                endTime = DateUtils.addDays(DateUtils.getMidTime(now), -30);
            } else {
                startTime = DateUtils.addDays(DateUtils.getMidTime(now), -59);
                endTime = DateUtils.addDays(DateUtils.getMidTime(now), -29);
            }
        } else if (DateEnum.YEAR.equals(queryParam.getType())) {
            // 近一年
            LocalDateTime now = LocalDateTime.now();
            if (DateUtils.toTimestamp(now) < DateUtils.toTimestamp(DateUtils.getMidTime(LocalDateTime.now()))) {
                startTime = DateUtils.addDays(DateUtils.getMidTime(now), -730);
                endTime = DateUtils.addDays(DateUtils.getMidTime(now), -365);
            } else {
                startTime = DateUtils.addDays(DateUtils.getMidTime(now), -729);
                endTime = DateUtils.addDays(DateUtils.getMidTime(now), -364);
            }
        } else if (queryParam.getPeriod().getBegin().equals(queryParam.getPeriod().getEnd())) {
            startTime = DateUtils.getMidTime(queryParam.getPeriod().getBegin());
            endTime = DateUtils.addDays(startTime, 1);
        } else {
            startTime = DateUtils.getMidTime(queryParam.getPeriod().getBegin());
            endTime = DateUtils.addDays(DateUtils.getMidTime(queryParam.getPeriod().getEnd()), 1);
        }
        Period period = new Period(startTime, endTime);
        queryParam.setPeriod(period);
    }

    private void setDateTime(StatQueryParam queryParam) {
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        if (Objects.isNull(queryParam.getPeriod()) && Objects.isNull(queryParam.getType())) {
            // 时间为空默认为今天
            LocalDateTime now = LocalDateTime.now();
            if (DateUtils.toTimestamp(now) < DateUtils.toTimestamp(DateUtils.getMidTime(LocalDateTime.now()))) {
                startTime = DateUtils.addDays(DateUtils.getMidTime(LocalDateTime.now()), -1);
                endTime = DateUtils.getMidTime(LocalDateTime.now());
            } else {
                startTime = DateUtils.getMidTime(LocalDateTime.now());
                endTime = DateUtils.addDays(DateUtils.getMidTime(LocalDateTime.now()), 1);
            }
        } else if (DateEnum.TODAY.equals(queryParam.getType())) {
            // 今天
            LocalDateTime now = LocalDateTime.now();
            if (DateUtils.toTimestamp(now) < DateUtils.toTimestamp(DateUtils.getMidTime(LocalDateTime.now()))) {
                startTime = DateUtils.addDays(DateUtils.getMidTime(LocalDateTime.now()), -1);
                endTime = DateUtils.getMidTime(LocalDateTime.now());
            } else {
                startTime = DateUtils.getMidTime(LocalDateTime.now());
                endTime = DateUtils.addDays(DateUtils.getMidTime(LocalDateTime.now()), 1);
            }
        } else if (DateEnum.YESTERDAY.equals(queryParam.getType())) {
            // 昨天
            LocalDateTime now = LocalDateTime.now();
            if (DateUtils.toTimestamp(now) < DateUtils.toTimestamp(DateUtils.getMidTime(LocalDateTime.now()))) {
                startTime = DateUtils.addDays(DateUtils.getMidTime(LocalDateTime.now()), -2);
                endTime = DateUtils.addDays(DateUtils.getMidTime(LocalDateTime.now()), -1);
            } else {
                startTime = DateUtils.addDays(DateUtils.getMidTime(LocalDateTime.now()), -1);
                endTime = DateUtils.getMidTime(LocalDateTime.now());
            }
        } else if (DateEnum.THIS_WEEK.equals(queryParam.getType())) {
            // 本周
            LocalDateTime now = LocalDateTime.now();
            if (DateUtils.toTimestamp(now) < DateUtils.toTimestamp(DateUtils.getMidTime(LocalDateTime.now()))) {
                startTime = DateUtils.addDays(DateUtils.getMidTime(DateUtils.getBackDateTime(LocalDateTime.now(), TIMERANGE_WEEK)), -1);
                endTime = DateUtils.getMidTime(now);
            } else {
                startTime = DateUtils.addDays(DateUtils.getMidTime(DateUtils.getBackDateTime(LocalDateTime.now(), TIMERANGE_WEEK)), -1);
                endTime = DateUtils.addDays(DateUtils.getMidTime(now), 1);
            }
        } else if (DateEnum.LAST_WEEK.equals(queryParam.getType())) {
            // 上周
            LocalDateTime now = LocalDateTime.now();
            if (DateUtils.toTimestamp(now) < DateUtils.toTimestamp(DateUtils.getMidTime(LocalDateTime.now()))) {
                startTime = DateUtils.getMidTime(DateUtils.addDays(DateUtils.getBackDateTime(DateUtils.addDays(LocalDateTime.now(), -7), TIMERANGE_WEEK), -1));
                endTime = DateUtils.getMidTime(DateUtils.addDays(LocalDateTime.now(), -7));
            } else {
                startTime = DateUtils.getMidTime(DateUtils.addDays(DateUtils.getBackDateTime(DateUtils.addDays(LocalDateTime.now(), -7), TIMERANGE_WEEK), -1));
                endTime = DateUtils.getMidTime(DateUtils.addDays(LocalDateTime.now(), -6));
            }
        } else if (DateEnum.WEEK.equals(queryParam.getType())) {
            // 近7天
            startTime = DateUtils.addDays(DateUtils.getMidTime(LocalDateTime.now()), -6);
            endTime = DateUtils.addDays(DateUtils.getMidTime(LocalDateTime.now()), 1);
        } else if (DateEnum.THIS_MONTH.equals(queryParam.getType())) {
            // 本月
            LocalDateTime now = LocalDateTime.now();
            if (DateUtils.toTimestamp(now) < DateUtils.toTimestamp(DateUtils.getMidTime(LocalDateTime.now()))) {
                startTime = DateUtils.getMidTime(DateUtils.getBackDateTime(LocalDateTime.now(), TIMERANGE_MONTH));
                endTime = DateUtils.getMidTime(LocalDateTime.now());
            } else {
                startTime = DateUtils.getMidTime(DateUtils.getBackDateTime(DateUtils.addDays(LocalDateTime.now(), 1), TIMERANGE_MONTH));
                endTime = DateUtils.addDays(DateUtils.getMidTime(DateUtils.addDays(LocalDateTime.now(), 1)), 1);
            }
        } else if (DateEnum.LAST_MONTH.equals(queryParam.getType())) {
            // 上月
            LocalDateTime now = LocalDateTime.now();
            if (DateUtils.toTimestamp(now) < DateUtils.toTimestamp(DateUtils.getMidTime(LocalDateTime.now()))) {
                startTime = DateUtils.addDays(DateUtils.getMidTime(DateUtils.getBackDateTime(LocalDateTime.now(), TIMERANGE_MONTH)), -30);
                endTime = DateUtils.addDays(DateUtils.getMidTime(LocalDateTime.now()), -30);
            } else {
                startTime = DateUtils.addDays(DateUtils.getMidTime(DateUtils.getBackDateTime(DateUtils.addDays(LocalDateTime.now(), 1), TIMERANGE_MONTH)), -30);
                endTime = DateUtils.addDays(DateUtils.addDays(DateUtils.getMidTime(DateUtils.addDays(LocalDateTime.now(), 1)), 1), -30);
            }
        } else if (DateEnum.MONTH.equals(queryParam.getType())) {
            // 近30天
            LocalDateTime now = LocalDateTime.now();
            if (DateUtils.toTimestamp(now) < DateUtils.toTimestamp(DateUtils.getMidTime(LocalDateTime.now()))) {
                startTime = DateUtils.addDays(DateUtils.getMidTime(now), -30);
                endTime = DateUtils.getMidTime(LocalDateTime.now());
            } else {
                startTime = DateUtils.addDays(DateUtils.addDays(DateUtils.getMidTime(now), 1), -30);
                endTime = DateUtils.addDays(DateUtils.getMidTime(LocalDateTime.now()), 1);
            }
        } else if (DateEnum.YEAR.equals(queryParam.getType())) {
            // 近一年
            LocalDateTime now = LocalDateTime.now();
            if (DateUtils.toTimestamp(now) < DateUtils.toTimestamp(DateUtils.getMidTime(LocalDateTime.now()))) {
                startTime = DateUtils.addDays(DateUtils.getMidTime(now), -365);
                endTime = DateUtils.getMidTime(LocalDateTime.now());
            } else {
                startTime = DateUtils.addDays(DateUtils.addDays(DateUtils.getMidTime(now), 1), -365);
                endTime = DateUtils.addDays(DateUtils.getMidTime(LocalDateTime.now()), 1);
            }
        } else if (queryParam.getPeriod().getBegin().equals(queryParam.getPeriod().getEnd())) {
            startTime = DateUtils.getMidTime(queryParam.getPeriod().getBegin());
            endTime = DateUtils.addDays(startTime, 1);
        } else {
            startTime = DateUtils.getMidTime(queryParam.getPeriod().getBegin());
            endTime = DateUtils.addDays(DateUtils.getMidTime(queryParam.getPeriod().getEnd()), 1);
        }
        Period period = new Period(startTime, endTime);
        queryParam.setPeriod(period);
    }

}
