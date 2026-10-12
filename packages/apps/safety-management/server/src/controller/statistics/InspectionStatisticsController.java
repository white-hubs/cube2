package com.guhecloud.cube.srv.gen.safety.management.controller.statistics;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.InspectionItemStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.InspectionListQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.MarketInspectionQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.MarketInspectionRecordQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.SafetyRecordQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.inspect.InspectBaseQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.InspectTaskVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.GroupCountVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.InspectionStatisticsVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.MarketStatisticsInfo;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.SafetyInspectItemVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.SafetyInspectRecordVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.SafetyInspectStandardVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.StackedBarChartVO;
import com.guhecloud.cube.srv.gen.safety.management.enumeration.InspectionItemTagEnum;
import com.guhecloud.cube.srv.gen.safety.management.po.MarketConfigPO;
import com.guhecloud.cube.srv.gen.safety.management.po.SafetyInspectRecordItemPO;
import com.guhecloud.cube.srv.gen.safety.management.po.SafetyInspectRecordPO;
import com.guhecloud.cube.srv.gen.safety.management.service.InspectionStatisticsService;
import com.guhecloud.cube.srv.gen.safety.management.service.MarketConfigService;
import com.guhecloud.cube.srv.gen.safety.management.service.SafetyInspectRecordItemService;
import com.guhecloud.cube.srv.gen.safety.management.service.SafetyInspectRecordService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 巡检统计
 *
 * @author wql
 * @describe 巡检统计
 * @date 2023/9/8 10:47
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectionStatisticsController {

    private final InspectionStatisticsService inspectionStatisticsService;

    private final MarketConfigService marketConfigService;

    private final SafetyInspectRecordItemService safetyInspectRecordItemService;

    private final SafetyInspectRecordService safetyInspectRecordService;

    /**
     * 安全巡检统计
     *
     * @Author wql
     * @Description 安全巡检统计
     * @Date 19:59 2023/9/7
     **/
    @GetMapping("/safety/inspection")
    public InspectionStatisticsVO getInspectionStatistics(BaseQuery query) {
        Assert.isParameterError(query.getPeriod() == null, "查询范围不允许为空!");
        int defaultNumber = BigDecimal.ZERO.intValue();
        if (!this.setConfigMarket(query)) {
            return InspectionStatisticsVO.builder().inspectionTaskNum(defaultNumber).tempInspectionNum((long) defaultNumber)
                    .errorInspectionNum((long) defaultNumber).build();
        }
        // 总任务数量
        InspectionStatisticsVO result = inspectionStatisticsService.getInspectionStatistics(query);
        // 临时巡检数量
        Period period = query.getPeriod();
        // 临时巡检数量
        long tempNumber = safetyInspectRecordService.lambdaQuery()
                .between(period != null && period.getBegin() != null && period.getEnd() !=
                                                                        null, SafetyInspectRecordPO::getTime, ObjectUtils.ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, Period::getEnd))
                .eq(query.getMarketTenant() != null, SafetyInspectRecordPO::getMarketTenant, query.getMarketTenant())
                .in(!CollectionUtils.isEmpty(query.getMarketTenants()), SafetyInspectRecordPO::getMarketTenant, query.getMarketTenants())
                .isNull(SafetyInspectRecordPO::getTaskId).count();
        result.setTempInspectionNum(tempNumber);
        // 异常数量
        long errorNumber = safetyInspectRecordService.lambdaQuery()
                .between(period != null && period.getBegin() != null && period.getEnd() !=
                                                                        null, SafetyInspectRecordPO::getTime, ObjectUtils.ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, Period::getEnd))
                .eq(SafetyInspectRecordPO::getResult, "ABNORMAL")
                .eq(query.getMarketTenant() != null, SafetyInspectRecordPO::getMarketTenant, query.getMarketTenant())
                .in(!CollectionUtils.isEmpty(query.getMarketTenants()), SafetyInspectRecordPO::getMarketTenant, query.getMarketTenants())
                .count();
        result.setErrorInspectionNum(errorNumber);
        return result;
    }

    /**
     * 巡检任务排名
     *
     * @Author wql
     * @Description 巡检任务排名
     * @Date 16:54 2023/9/20
     **/
    @GetMapping("/safety/marketInspection")
    public Pagination<MarketStatisticsInfo> getInspectionMarketStatistics(MarketInspectionQuery query) {
        Assert.isParameterError(query.getPeriod() == null, "查询范围不允许为空!");
        return inspectionStatisticsService.getInspectionMarketStatistics(query);
    }

    /**
     * 安全巡检列表详情
     *
     * @Author wql
     * @Description 安全巡检列表详情
     * @Date 20:07 2023/9/7
     **/
    @GetMapping("/safety/inspectionList")
    public Pagination<InspectTaskVO> getInspectionList(InspectionListQuery inspectionListParam) {
        Assert.isParameterError(
                inspectionListParam.getPeriod() == null && inspectionListParam.getExecPeriod() == null, "查询范围不允许为空!");
        Pagination<InspectTaskVO> inspectionList = inspectionStatisticsService.getInspectionPage(inspectionListParam);
        if (CollectionUtils.isEmpty(inspectionList.getRecords())) {
            return inspectionList;
        }
        Set<Integer> marketTenants = inspectionList.getRecords().stream()
                .filter(inspectTaskVO -> Objects.nonNull(inspectTaskVO.getMarketTenant()))
                .map(inspectTaskVO -> inspectTaskVO.getMarketTenant())
                .collect(Collectors.toSet());
        if (!CollectionUtils.isEmpty(marketTenants)) {
            List<MarketConfigPO> marketConfigPOS = this.marketConfigService.list(Wrappers.<MarketConfigPO>lambdaQuery()
                                                                                         .in(MarketConfigPO::getMarketTenant, marketTenants));
            inspectionList.getRecords().stream().forEach(inspectTaskVO -> {
                Optional<MarketConfigPO> marketConfigOptional = marketConfigPOS
                        .stream()
                        .filter(marketConfigPO -> marketConfigPO.getMarketTenant().intValue() == inspectTaskVO.getMarketTenant().intValue())
                        .findFirst();
                marketConfigOptional.ifPresent(marketConfigPO ->
                                                       inspectTaskVO.setMarketName(marketConfigPO.getMarketName())
                                              );
            });
        }
        return inspectionList;
    }

    /**
     * 查询巡检类型列表
     *
     * @Author wql
     * @Description 查询巡检类型列表
     * @Date 16:01 2023/9/11
     **/
    @GetMapping("/safety/inspectionTypes")
    public List<String> getInspectionTypes() {
        return inspectionStatisticsService.getInspectionTypes();
    }

    /**
     * 统计查询范围内的记录数
     *
     * @return
     */
    @GetMapping("/safety/statistics/inspect/count")
    public Integer countInspect(InspectBaseQuery query) {
        return inspectionStatisticsService.count(query);
    }

    /**
     * 巡检车辆违规统计
     *
     * @param query 条件
     *
     * @return 违规统计数据
     */
    @GetMapping("/safety/statistics/inspect/vehicle-violation")
    public Pagination<StackedBarChartVO> inspectVehicleViolationStat(@Valid InspectionItemStatisticsQuery query) {
        if (CollectionUtils.isEmpty(query.getMarketTenantList())) {
            List<MarketConfigPO> configPOList = marketConfigService.list(Wrappers.<MarketConfigPO>lambdaQuery()
                                                                                 .eq(MarketConfigPO::getStatus, true));
            if (CollectionUtils.isEmpty(configPOList)) {
                return Pagination.empty();
            }
            List<Integer> tenantList = configPOList.stream().map(MarketConfigPO::getMarketTenant).collect(Collectors.toList());
            query.setMarketTenantList(tenantList);
        }
        query.setTagList(Arrays.asList(InspectionItemTagEnum.CHARGE.name(), InspectionItemTagEnum.PARKING.name()));
        return safetyInspectRecordItemService.inspectVehicleViolationStat(query);
    }

    /**
     * 巡检车辆违规统计
     *
     * @param query 条件
     *
     * @return 违规统计数据
     */
    @GetMapping("/safety/statistics/inspect/violation-total")
    public Integer vehicleViolationTotal(@Valid InspectionItemStatisticsQuery query) {
        query.setPageNumber(BigDecimal.ONE.intValue());
        query.setPageSize(BigDecimal.ONE.intValue());
        query.setTagList(Arrays.stream(InspectionItemTagEnum.values()).map(InspectionItemTagEnum::name).collect(Collectors.toList()));
        int total = BigDecimal.ZERO.intValue();
        for (InspectionItemTagEnum value : InspectionItemTagEnum.values()) {
            query.setTagList(Collections.singletonList(value.name()));
            total += safetyInspectRecordItemService.getInspectRecordCount(query);
        }
        return total;
    }

    /**
     * 临时巡检排名环比
     *
     * @param query 条件
     *
     * @return 零时巡检排名环比
     */
    @GetMapping("/safety/statistics/record/ranking")
    public Pagination<MarketStatisticsInfo> getTempRecordMarketStatistics(MarketInspectionRecordQuery query) {
        Assert.isParameterError(query.getPeriod() == null, "查询范围不能为空!");
        this.handlerErrorTagList(query);
        return safetyInspectRecordService.getInspectionRecordMarketStatistics(query);
    }

    /**
     * 巡检记录分页列表
     *
     * @param query 条件
     *
     * @return 巡检记录
     */
    @GetMapping("/safety/statistics/record/page")
    public Pagination<SafetyInspectRecordVO> safetyRecordPage(MarketInspectionRecordQuery query) {
        this.handlerErrorTagList(query);
        return safetyInspectRecordService.safetyRecordPage(query);
    }

    /**
     * 异常记录分页列表
     *
     * @param query 条件
     *
     * @return 巡检记录
     */
    @GetMapping("/safety/statistics/record/error-page")
    public Pagination<SafetyInspectRecordVO> safetyErrorRecordPage(MarketInspectionRecordQuery query) {
        this.handlerErrorTagList(query);
        return safetyInspectRecordService.safetyErrorRecordPage(query);
    }

    /**
     * 查询巡检记录下的巡检项(分页)
     * 可用条件：是否查询异常项, 异常tag列表, 市场id集合
     *
     * @param query 条件
     *
     * @return 巡检记录项分页列表
     */
    @GetMapping("/safety/statistics/item/page")
    public List<SafetyInspectStandardVO> getSafetyRecordItemPage(SafetyRecordQuery query) {
        if (!this.setConfigMarket(query)) {
            return Collections.emptyList();
        }
        List<SafetyInspectRecordItemPO> list = safetyInspectRecordItemService.list(Wrappers.<SafetyInspectRecordItemPO>lambdaQuery()
                                                                                           .eq(SafetyInspectRecordItemPO::getRecordId, query.getRecordId())
                                                                                           .in(CollectionUtils.isNotEmpty(query.getErrorTagList()), SafetyInspectRecordItemPO::getItemTag, query.getErrorTagList())
                                                                                           .isNotNull(Boolean.TRUE.equals(query.getErrorTagBool()), SafetyInspectRecordItemPO::getItemTag)
                                                                                           .in(CollectionUtils.isNotEmpty(query.getMarketTenants()), SafetyInspectRecordItemPO::getMarketTenant, query.getMarketTenants())

                                                                                  );
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        List<SafetyInspectStandardVO> result = new ArrayList<>();
        list.stream().collect(Collectors.groupingBy(SafetyInspectRecordItemPO::getStandardId)).forEach((k, ls) -> {
            SafetyInspectStandardVO vo = new SafetyInspectStandardVO();
            vo.setStandardId(k);
            vo.setStandardName(list.get(0).getStandardName());
            vo.setItemVOList(list.stream()
                                     .map(v -> SafetyInspectItemVO.builder().id(v.getId()).itemId(v.getItemId()).itemName(v.getItemName())
                                             .errorDesc(v.getErrorDesc()).build()).collect(Collectors.toList()));
            result.add(vo);
        });
        return result;
    }

    /**
     * 市场违规排名
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/safety/statistics/record/error-ranking")
    public Pagination<MarketStatisticsInfo> getInspectionMarketErrorRecordStatistics(MarketInspectionQuery query) {
        this.handlerErrorTagList(query);
        return inspectionStatisticsService.getInspectionMarketErrorRecordStatistics(query);
    }

    /**
     * 设置开启的市场条件
     *
     * @param query 条件
     *
     * @return 是否设置成功
     */
    private Boolean setConfigMarket(BaseQuery query) {
        if (CollectionUtils.isEmpty(query.getMarketTenants())) {
            List<MarketConfigPO> configPOList = marketConfigService.list(Wrappers.<MarketConfigPO>lambdaQuery()
                                                                                 .eq(MarketConfigPO::getStatus, true));
            if (CollectionUtils.isEmpty(configPOList)) {
                return false;
            }
            List<Integer> tenantList = configPOList.stream().map(MarketConfigPO::getMarketTenant).collect(Collectors.toList());
            query.setMarketTenants(tenantList);
        }
        return true;
    }

    /**
     * 小散工程巡查记录
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/safety/statistics/inspect/record/page")
    public Pagination<SafetyInspectRecordVO> getSafetyInspectRecordPage(MarketInspectionRecordQuery query) {
        this.handlerErrorTagList(query);
        return safetyInspectRecordService.getSafetyInspectRecordPage(query);
    }

    /**
     * 巡检任务状态分布
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/safety/statistics/inspect/task/count/status")
    public List<GroupCountVO> inspectTaskCountStatus(BaseStatisticsQuery query) {
        return inspectionStatisticsService.countStatus(query);
    }

    private void handlerErrorTagList(BaseQuery query) {
        if (!StringUtils.isEmpty(query.getErrorTags())) {
            List<String> errorTagList = Stream.of(query.getErrorTags().split(","))
                    .collect(Collectors.toList());
            if (query.getErrorTagList() != null) {
                query.getErrorTagList().addAll(errorTagList);
            } else {
                query.setErrorTagList(errorTagList);
            }
        }
    }
}
