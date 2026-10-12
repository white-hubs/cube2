package com.guhecloud.cube.sn.meattrade.plugin.dbc.controller;

import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.response.statistics.GetMeatTestingPageResponse;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.param.StatQueryParam;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.repository.po.MeatFarmSupplyRankingStatPO;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.repository.po.MeatTestingRankingStatPO;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.repository.po.SellerSupplySumStatPO;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.repository.po.SellersComparisonTopStatPO;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.repository.po.TransactionComparisonStatDetailPO;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.repository.po.TransactionComparisonStatPO;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.repository.service.MeatFarmSupplyRankingStatService;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.repository.service.MeatTestingRankingStatService;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.repository.service.SellerSupplySumStatService;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.repository.service.SellersComparisonTopStatService;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.repository.service.TransactionComparisonStatDetailService;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.repository.service.TransactionComparisonStatService;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.vo.statistics.MeatFarmSupplyRankingStatVO;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.vo.statistics.MeatTestingRankingPageVO;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.vo.statistics.MeatTestingRankingStatVO;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.vo.statistics.SellerSupplySumStatVO;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.vo.statistics.SellersComparisonTopStatVO;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.vo.statistics.SummaryAmountStatVO;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.vo.statistics.SummaryAveragePriceStatVO;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.vo.statistics.SummaryCommissionStatVO;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.vo.statistics.SummarySlicesStatVO;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.vo.statistics.SummaryWeightStatVO;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.vo.statistics.TransactionComparisonStatVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DbcMeatTradeStatisticsController {

    private final MeatFarmSupplyRankingStatService meatFarmSupplyRankingStatService;

    private final MeatTestingRankingStatService meatTestingRankingStatService;

    private final SellersComparisonTopStatService sellersComparisonTopStatService;

    private final SellerSupplySumStatService sellersSupplySumStatService;

    private final TransactionComparisonStatService transactionComparisonStatService;

    private final TransactionComparisonStatDetailService transactionComparisonStatDetailService;

    /**
     * 白条来货数据统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/dbc/meat-trade/stat/farm/supply-ranking")
    public Pagination<MeatFarmSupplyRankingStatVO> getMeatFarmSupplyRankingStat(@Valid StatQueryParam param) {
        Pagination<MeatFarmSupplyRankingStatPO> page = meatFarmSupplyRankingStatService.getMeatFarmSupplyRankingStatList(param);
        List<MeatFarmSupplyRankingStatVO> meatFarmSupplyRankingStatVOList = page.getRecords()
                .stream()
                .map(po ->
                             MeatFarmSupplyRankingStatVO
                                     .builder()
                                     .abattoirName(po.getMeatFarmName())
                                     .supplySlices(po.getTotalTallyQuantity())
                                     .supplyWeight(po.getTotalTallyWeight())
                                     .qoq(StringUtils.isBlank(po.getTallyWeightRate()) ?
                                          null : new BigDecimal(po.getTallyWeightRate()))
                                     .build()
                    ).collect(Collectors.toList());
        return PagingUtils.convert(page, meatFarmSupplyRankingStatVOList);
    }

    /**
     * 食安数据统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/dbc/meat-trade/stat/testing-ranking")
    public List<MeatTestingRankingStatVO> getMeatTestingRankingStat(@Valid StatQueryParam param) {
        List<MeatTestingRankingStatPO> meatTestingRankingTest = meatTestingRankingStatService.getMeatTestingRankingTest(param);
        return ObjectUtils.copying(meatTestingRankingTest, MeatTestingRankingStatVO.class);
    }

    /**
     * 白条交易大户
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/dbc/meat-trade/stat/sellers/comparison-top")
    public Pagination<SellersComparisonTopStatVO> getSellersComparisonTopStatPage(@Valid StatQueryParam param) {
        Pagination<SellersComparisonTopStatPO> page = sellersComparisonTopStatService.getSellersComparisonTopStat(param);
        List<SellersComparisonTopStatVO> sellersComparisonTopStatVOS = page.getRecords()
                .stream()
                .map(po -> {
                    SellersComparisonTopStatVO statVO = new SellersComparisonTopStatVO();
                    statVO.setSupplyName(po.getSellerName());
                    statVO.setSupplierCode(po.getSeller());
                    statVO.setTradeWeight(po.getTotalNet());
                    statVO.setTradeAmount(po.getTotalPayable());
                    statVO.setQoq(StringUtils.isBlank(po.getNetRate()) ?
                                  null : new BigDecimal(po.getNetRate()));
                    return statVO;
                })
                .collect(Collectors.toList());
        return PagingUtils.convert(page, sellersComparisonTopStatVOS);
    }

    /**
     * 供货数据
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/dbc/meat-trade/stat/sellers/supply-sum")
    public SellerSupplySumStatVO getSellersSupplySumStat(@Valid StatQueryParam param) {
        SellerSupplySumStatPO sellerSupplySumStat = sellersSupplySumStatService.getSellerSupplySumStat(param);
        if (Objects.isNull(sellerSupplySumStat)) {
            return null;
        }
        SellerSupplySumStatVO statVO = new SellerSupplySumStatVO();
        statVO.setSupplier(sellerSupplySumStat.getTotalSellerCount());
        statVO.setSupplyWeight(sellerSupplySumStat.getTotalTallyWeight());
        statVO.setSupplySlices(sellerSupplySumStat.getTotalTallyQuantity());
        statVO.setQoqSupplier(StringUtils.isBlank(sellerSupplySumStat.getSellerCountRate()) ?
                              null : new BigDecimal(sellerSupplySumStat.getSellerCountRate()));
        statVO.setQoqSupplyWeight(StringUtils.isBlank(sellerSupplySumStat.getTallyWeightRate()) ?
                                  null : new BigDecimal(sellerSupplySumStat.getTallyWeightRate()));
        statVO.setQoqSupplySlices(StringUtils.isBlank(sellerSupplySumStat.getTallyQuantityRate()) ?
                                  null : new BigDecimal(sellerSupplySumStat.getTallyQuantityRate()));
        return statVO;
    }

    /**
     * 交易数据
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/dbc/meat-trade/stat/transaction-comparison")
    public TransactionComparisonStatVO getTransactionComparisonStat(@Valid StatQueryParam param) {
        TransactionComparisonStatPO transactionComparisonStat = transactionComparisonStatService.getTransactionComparisonStat(param);
        if (Objects.isNull(transactionComparisonStat)) {
            return null;
        }
        TransactionComparisonStatVO statVO = new TransactionComparisonStatVO();
        statVO.setActualAverageAmount(transactionComparisonStat.getAvgPayable());
        statVO.setTradeAmount(transactionComparisonStat.getTotalAmount());
        statVO.setTradeAverageAmount(transactionComparisonStat.getAvgAmount());
        statVO.setTradeWeight(transactionComparisonStat.getTotalWeight());
        statVO.setTradeCommission(transactionComparisonStat.getTotalSellerFee());
        statVO.setTradeSlices(transactionComparisonStat.getTotalQuantity());
        statVO.setQoqTradeAmount(StringUtils.isBlank(transactionComparisonStat.getAmountRate()) ?
                                 null : new BigDecimal(transactionComparisonStat.getAmountRate()));
        statVO.setQoqTradeCommission(StringUtils.isBlank(transactionComparisonStat.getSellerFeeRate()) ?
                                     null : new BigDecimal(transactionComparisonStat.getSellerFeeRate()));
        statVO.setQoqTradeAverageAmount(StringUtils.isBlank(transactionComparisonStat.getAvgAmountRate()) ?
                                        null : new BigDecimal(transactionComparisonStat.getAvgAmountRate()));
        statVO.setQoqActualAverageAmount(StringUtils.isBlank(transactionComparisonStat.getAvgPayableRate()) ?
                                         null : new BigDecimal(transactionComparisonStat.getAvgPayableRate()));
        statVO.setQoqTradeWeight(StringUtils.isBlank(transactionComparisonStat.getWeightRate()) ?
                                 null : new BigDecimal(transactionComparisonStat.getWeightRate()));
        statVO.setQoqTradeSlices(StringUtils.isBlank(transactionComparisonStat.getQuantityRate()) ?
                                 null : new BigDecimal(transactionComparisonStat.getQuantityRate()));
        return statVO;
    }

    /**
     * 交易金额详细信息
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/dbc/meat-trade/stat/transaction-comparison/amount")
    public SummaryAmountStatVO getStatSummaryAmount(@Valid StatQueryParam param) {
        List<TransactionComparisonStatDetailPO> detail = transactionComparisonStatDetailService.getTransactionComparisonStatDetails(param);
        List<SummaryAmountStatVO.AmountDate> currentDateAmountList = new ArrayList<>();
        List<SummaryAmountStatVO.AmountDate> lastDateAmountList = new ArrayList<>();
        detail.forEach(item -> {
            currentDateAmountList.add(SummaryAmountStatVO.AmountDate.builder()
                                              .summaryDate(DateUtils.format(item.getStartDate()))
                                              .summaryAmount(item.getTotalAmount())
                                              .build());
            lastDateAmountList.add(SummaryAmountStatVO.AmountDate.builder()
                                           .summaryDate(DateUtils.format(item.getPrevStartDate()))
                                           .summaryAmount(item.getPrevTotalAmount())
                                           .build());
        });
        TransactionComparisonStatPO transactionComparisonStat = transactionComparisonStatService.getTransactionComparisonStat(param);
        return SummaryAmountStatVO.builder()
                .currentAmount(transactionComparisonStat.getTotalAmount())
                .lastAmount(transactionComparisonStat.getPrevTotalAmount())
                .qoqTradeAmount(StringUtils.isBlank(transactionComparisonStat.getAmountRate()) ?
                                null : new BigDecimal(transactionComparisonStat.getAmountRate()))
                .currentDateAmountList(currentDateAmountList)
                .lastDateAmountList(lastDateAmountList)
                .build();
    }

    /**
     * 交易佣金详细信息
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/dbc/meat-trade/stat/transaction-comparison/commission")
    public SummaryCommissionStatVO getStatSummaryCommission(@Valid StatQueryParam param) {
        List<TransactionComparisonStatDetailPO> detail = transactionComparisonStatDetailService.getTransactionComparisonStatDetails(param);
        List<SummaryCommissionStatVO.CommissionDate> currentCommissionList = new ArrayList<>();
        List<SummaryCommissionStatVO.CommissionDate> lastCommissionList = new ArrayList<>();
        detail.forEach(item -> {
            currentCommissionList.add(SummaryCommissionStatVO.CommissionDate.builder()
                                              .summaryDate(DateUtils.format(item.getStartDate()))
                                              .summaryCommission(item.getTotalSellerFee())
                                              .build());
            lastCommissionList.add(SummaryCommissionStatVO.CommissionDate.builder()
                                           .summaryDate(DateUtils.format(item.getPrevStartDate()))
                                           .summaryCommission(item.getTotalSellerFee())
                                           .build());
        });
        TransactionComparisonStatPO transactionComparisonStat = transactionComparisonStatService.getTransactionComparisonStat(param);
        return SummaryCommissionStatVO.builder()
                .currentCommission(transactionComparisonStat.getTotalSellerFee())
                .lastCommission(transactionComparisonStat.getPrevTotalSellerFee())
                .qoqTradeCommission(StringUtils.isBlank(transactionComparisonStat.getSellerFeeRate()) ?
                                    null : new BigDecimal(transactionComparisonStat.getSellerFeeRate()))
                .currentDateCommissionList(currentCommissionList)
                .lastDateCommissionList(lastCommissionList)
                .build();
    }

    /**
     * 交易均价详细信息
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/dbc/meat-trade/stat/transaction-comparison/average-price")
    public SummaryAveragePriceStatVO getStatSummaryAveragePrice(@Valid StatQueryParam param) {
        List<TransactionComparisonStatDetailPO> detail = transactionComparisonStatDetailService.getTransactionComparisonStatDetails(param);
        List<SummaryAveragePriceStatVO.AveragePriceDate> currentList = new ArrayList<>();
        List<SummaryAveragePriceStatVO.AveragePriceDate> lastList = new ArrayList<>();
        detail.forEach(item -> {
            currentList.add(SummaryAveragePriceStatVO.AveragePriceDate.builder()
                                    .summaryDate(DateUtils.format(item.getStartDate()))
                                    .summaryAveragePrice(item.getAvgAmount())
                                    .build());
            lastList.add(SummaryAveragePriceStatVO.AveragePriceDate.builder()
                                 .summaryDate(DateUtils.format(item.getPrevStartDate()))
                                 .summaryAveragePrice(item.getPrevAvgAmount())
                                 .build());
        });
        TransactionComparisonStatPO transactionComparisonStat = transactionComparisonStatService.getTransactionComparisonStat(param);
        return SummaryAveragePriceStatVO.builder()
                .currentAveragePrice(transactionComparisonStat.getAvgAmount())
                .lastAveragePrice(transactionComparisonStat.getPrevAvgAmount())
                .qoqTradeAveragePrice(StringUtils.isBlank(transactionComparisonStat.getAvgAmountRate()) ?
                                      null : new BigDecimal(transactionComparisonStat.getAvgAmountRate()))
                .currentDateAveragePriceList(currentList)
                .lastDateAveragePriceList(lastList)
                .build();
    }

    /**
     * 实际均价详细信息
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/dbc/meat-trade/stat/transaction-comparison/actual-average-price")
    public SummaryAveragePriceStatVO getStatSummaryActualAveragePrice(@Valid StatQueryParam param) {
        List<TransactionComparisonStatDetailPO> detail = transactionComparisonStatDetailService.getTransactionComparisonStatDetails(param);
        List<SummaryAveragePriceStatVO.AveragePriceDate> currentList = new ArrayList<>();
        List<SummaryAveragePriceStatVO.AveragePriceDate> lastList = new ArrayList<>();
        detail.forEach(item -> {
            currentList.add(SummaryAveragePriceStatVO.AveragePriceDate.builder()
                                    .summaryDate(DateUtils.format(item.getStartDate()))
                                    .summaryAveragePrice(item.getAvgPayable())
                                    .build());
            lastList.add(SummaryAveragePriceStatVO.AveragePriceDate.builder()
                                 .summaryDate(DateUtils.format(item.getPrevStartDate()))
                                 .summaryAveragePrice(item.getPrevAvgPayable())
                                 .build());
        });
        TransactionComparisonStatPO transactionComparisonStat = transactionComparisonStatService.getTransactionComparisonStat(param);
        return SummaryAveragePriceStatVO.builder()
                .currentAveragePrice(transactionComparisonStat.getAvgAmount())
                .lastAveragePrice(transactionComparisonStat.getPrevAvgAmount())
                .qoqTradeAveragePrice(StringUtils.isBlank(transactionComparisonStat.getAvgAmountRate()) ?
                                      null : new BigDecimal(transactionComparisonStat.getAvgAmountRate()))
                .currentDateAveragePriceList(currentList)
                .lastDateAveragePriceList(lastList)
                .build();
    }

    /**
     * 交易重量详细信息
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/dbc/meat-trade/stat/transaction-comparison/weight")
    public SummaryWeightStatVO getStatSummaryWeightDetail(@Valid StatQueryParam param) {
        List<TransactionComparisonStatDetailPO> detail = transactionComparisonStatDetailService.getTransactionComparisonStatDetails(param);
        List<SummaryWeightStatVO.WeightDate> currentList = new ArrayList<>();
        List<SummaryWeightStatVO.WeightDate> lastList = new ArrayList<>();
        detail.forEach(item -> {
            currentList.add(SummaryWeightStatVO.WeightDate.builder()
                                    .summaryDate(DateUtils.format(item.getStartDate()))
                                    .summaryWeight(item.getTotalWeight())
                                    .build());
            lastList.add(SummaryWeightStatVO.WeightDate.builder()
                                 .summaryDate(DateUtils.format(item.getPrevStartDate()))
                                 .summaryWeight(item.getPrevTotalWeight())
                                 .build());
        });
        TransactionComparisonStatPO transactionComparisonStat = transactionComparisonStatService.getTransactionComparisonStat(param);
        return SummaryWeightStatVO.builder()
                .currentWeight(transactionComparisonStat.getTotalWeight())
                .lastWeight(transactionComparisonStat.getPrevTotalWeight())
                .qoqTradeWeight(StringUtils.isBlank(transactionComparisonStat.getWeightRate()) ?
                                null : new BigDecimal(transactionComparisonStat.getWeightRate()))
                .currentDateWeightList(currentList)
                .lastDateWeightList(lastList)
                .build();
    }

    /**
     * 交易片数详细信息
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/dbc/meat-trade/stat/transaction-comparison/slice")
    public SummarySlicesStatVO getStatSummarySlices(@Valid StatQueryParam param) {
        List<TransactionComparisonStatDetailPO> detail = transactionComparisonStatDetailService.getTransactionComparisonStatDetails(param);
        List<SummarySlicesStatVO.SlicesDate> currentList = new ArrayList<>();
        List<SummarySlicesStatVO.SlicesDate> lastList = new ArrayList<>();
        detail.forEach(item -> {
            currentList.add(SummarySlicesStatVO.SlicesDate.builder()
                                    .summaryDate(DateUtils.format(item.getStartDate()))
                                    .summarySlices(item.getTotalQuantity())
                                    .build());
            lastList.add(SummarySlicesStatVO.SlicesDate.builder()
                                 .summaryDate(DateUtils.format(item.getPrevStartDate()))
                                 .summarySlices(item.getPrevTotalQuantity())
                                 .build());
        });
        TransactionComparisonStatPO transactionComparisonStat = transactionComparisonStatService.getTransactionComparisonStat(param);
        return SummarySlicesStatVO.builder()
                .currentSlices(transactionComparisonStat.getTotalQuantity())
                .lastSlices(transactionComparisonStat.getPrevTotalQuantity())
                .qoqTradeSlices(StringUtils.isBlank(transactionComparisonStat.getQuantityRate()) ?
                                null : new BigDecimal(transactionComparisonStat.getQuantityRate()))
                .currentDateSlicesList(currentList)
                .lastDateSlicesList(lastList)
                .build();
    }

    /**
     * 获取检测项详细分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/dbc/meat-trade/stat/testing/page")
    public Pagination<MeatTestingRankingPageVO> getMeatTestingPage(@Valid StatQueryParam param) {
        Pagination<GetMeatTestingPageResponse> page = meatTestingRankingStatService.getMeatTestingRankingPage(param);
        List<MeatTestingRankingPageVO> vos = page.getRecords()
                .stream()
                .map(res ->
                             MeatTestingRankingPageVO.builder()
                                     .id(res.getOid())
                                     .supplierId(res.getSellerId())
                                     .supplierName(res.getSellerName())
                                     .porkReportCode(res.getMeatTallyBatchNumber())
                                     .checkResult(res.getStatus())
                                     .inspectorName(res.getCreateName())
                                     .build())
                .collect(Collectors.toList());
        return PagingUtils.convert(page, vos);
    }
}
