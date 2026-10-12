package com.guhecloud.cube.biz.meattrade.server.pork.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.mdata.client.SupplierClient;
import com.guhecloud.cube.biz.mdata.client.dto.SupplierDTO;
import com.guhecloud.cube.biz.meattrade.server.pork.dto.StatisticsDTO;
import com.guhecloud.cube.biz.meattrade.server.pork.param.StatisticsMpQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.StatisticsQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.service.StatisticsService;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.StatisticsPageVO;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.StatisticsVO;
import com.guhecloud.cube.biz.meattrade.server.utlis.DateUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 统计接口
 */
@Slf4j
@RestController("meattrade-statisticsController")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StatisticsController {

    private final StatisticsService statisticsService;

    private final SupplierClient supplierClient;

    @GetMapping("/statistics")
    public StatisticsVO pageV2(@Valid StatisticsQueryParam queryParam) {
        List<SupplierDTO> suppliers = this.supplierClient.listSuppliers(queryParam.getKeyword(), SessionContextHolder.getRequester()
                .getTenant());
        Set<Long> supplierIds = suppliers.stream().map(SupplierDTO::getId).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(supplierIds)) {
            return new StatisticsVO();
        }
        queryParam.setSupplierIds(supplierIds);
        //格式化时间
        if (ObjectUtils.isNotNull(queryParam.getPeriod())) {
            queryParam.setPeriod(DateUtils.formatDateForType(null, queryParam.getPeriod()));
        }
        Page<StatisticsDTO> page = this.statisticsService.pageGoodsCategoryStatistics(queryParam);
        List<StatisticsPageVO> pageVOS = ObjectUtils.copying(page.getRecords(), StatisticsPageVO.class);
        StatisticsVO statisticsVO = new StatisticsVO();
        if (CollectionUtils.isEmpty(pageVOS)) {
            return statisticsVO;
        }

        // 获取批发商主键/批发商信息映射关系
        Map<Long, SupplierDTO> supplierMap = suppliers.stream().collect(Collectors.toMap(SupplierDTO::getId, s -> s));
        pageVOS = pageVOS.stream().map(stat -> {
            if (supplierMap.containsKey(stat.getSupplierId())) {
                SupplierDTO supplier = supplierMap.get(stat.getSupplierId());
                stat.setSupplierName(supplier.getName());
                stat.setSupplierCode(supplier.getCode());
            }
            return stat;
        }).collect(Collectors.toList());
        statisticsVO.setRecords(pageVOS);
        statisticsVO.setTotal((int) page.getTotal());
        return statisticsVO;
    }

    /**
     * 小程序销售统计
     *
     * @param queryParam 查询参数
     *
     * @return 销售统计信息
     */
    @GetMapping("/statistics/mp/page")
    public StatisticsVO pageMpStatistics(@Valid StatisticsMpQueryParam queryParam) {
        // 获取当前商户
        Integer supplierTenant = SessionContextHolder.getRequester().getTenant();
        SupplierDTO supplier = this.supplierClient.getBySupplierTenant(supplierTenant);
        Assert.isNotFound(supplier, "不存在批发商信息!");
        queryParam.setSupplierId(supplier.getId());
        // 确定时间
        queryParam.setPeriod(DateUtils.formatDateForType(queryParam.getType(), queryParam.getPeriod()));
        StatisticsQueryParam param = ObjectUtils.copying(queryParam, StatisticsQueryParam.class);
        Page<StatisticsDTO> statisticsDTOPage = this.statisticsService.pageStatisticsV2(param);
        if (CollectionUtils.isEmpty(statisticsDTOPage.getRecords())) {
            // 构建信息
            StatisticsVO statisticsVO = new StatisticsVO();
            StatisticsPageVO statisticsPageVO = buildNullStatisticsVO(supplier);
            statisticsVO.setCurrentStatistics(statisticsPageVO);
            return statisticsVO;
        }
        // 获取当前商户的销售统计
        Page<StatisticsDTO> dtos = this.statisticsService.pageMpStatistics(queryParam);
        dtos.getRecords().forEach(statisticsPageVO -> {
            statisticsPageVO.setActualAmount(statisticsPageVO.getAmount() - statisticsPageVO.getCommission());
            statisticsPageVO.setAveragePrice(BigDecimal.valueOf(statisticsPageVO.getAmount())
                                                     .divide(BigDecimal.valueOf(statisticsPageVO.getDealWeight()), 2, BigDecimal.ROUND_HALF_UP)
                                                     .doubleValue());
        });
        List<StatisticsPageVO> pageVOS = ObjectUtils.copying(dtos.getRecords(), StatisticsPageVO.class);
        // 构建信息
        StatisticsVO statisticsVO = new StatisticsVO();
        statisticsVO.setRecords(pageVOS);
        // 获取当前时间端的销售统计
        statisticsVO.setCurrentStatistics(ObjectUtils.copying(statisticsDTOPage.getRecords().get(0), StatisticsPageVO.class));
        return statisticsVO;
    }

    private StatisticsPageVO buildNullStatisticsVO(SupplierDTO supplier) {
        StatisticsPageVO pageVO = new StatisticsPageVO();
        pageVO.setSupplierId(Objects.isNull(supplier) ? null : supplier.getId());
        pageVO.setSupplierName(Objects.isNull(supplier) ? "" : supplier.getName());
        pageVO.setSupplierCode(Objects.isNull(supplier) ? "" : supplier.getCode());
        pageVO.setAmount(BigDecimal.ZERO);
        pageVO.setAveragePrice(BigDecimal.ZERO);
        pageVO.setHighestPrice(BigDecimal.ZERO);
        pageVO.setLowerPrice(BigDecimal.ZERO);
        pageVO.setActualAmount(BigDecimal.ZERO);
        pageVO.setCommission(BigDecimal.ZERO);
        pageVO.setPorkArrangeSlices(0);
        pageVO.setDealSlices(0);
        pageVO.setDealWeight(BigDecimal.ZERO);
        pageVO.setPorkArrangeWeight(BigDecimal.ZERO);
        pageVO.setStatisticsDate(LocalDateTime.now());
        return pageVO;
    }

}