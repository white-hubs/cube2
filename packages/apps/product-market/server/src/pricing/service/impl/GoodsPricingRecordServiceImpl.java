package com.guhecloud.cube.srv.gen.product.market.server.pricing.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.product.market.server.common.dao.IncomingQuantityMapper;
import com.guhecloud.cube.srv.gen.product.market.server.common.dao.MarketGoodsCategoryMapper;
import com.guhecloud.cube.srv.gen.product.market.server.common.entity.MarketGoodsCategory;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.dao.GoodsPriceWarnRecordMapper;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.dao.GoodsPricingRecordMapper;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.dao.GoodsPricingTaskMapper;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPriceWarnRecord;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPricingRecord;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPricingTask;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.message.event.PriceWarnEvent;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsIncomingStatParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPriceStatParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPricingRecordParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPricingRecordQueryParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPricingRecordThirdParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.service.GoodsPriceWarnRuleService;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.service.GoodsPricingRecordService;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.CommonStatVO;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPriceStatVO;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPriceTrendStatVO;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPriceWarnRuleCheckVO;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPricingRecordThirdVO;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPricingRecordVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 描述：采价记录服务层
 *
 * @author Appet
 * @date 2023-05-16 15:08:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GoodsPricingRecordServiceImpl extends ServiceImpl<GoodsPricingRecordMapper, GoodsPricingRecord> implements GoodsPricingRecordService {
    private final GoodsPricingRecordMapper goodsPricingRecordMapper;
    private final GoodsPricingTaskMapper goodsPricingTaskMapper;
    private final MarketGoodsCategoryMapper marketGoodsCategoryMapper;
    private final GoodsPriceWarnRuleService goodsPriceWarnRuleService;
    private final GoodsPriceWarnRecordMapper goodsPriceWarnRecordMapper;
    private final IncomingQuantityMapper incomingQuantityMapper;
    private final UserClient userClient;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(GoodsPricingRecordParam param) {
        param = extendPricingRecordInfo(param);
        GoodsPricingRecord goodsPricingRecord = ObjectUtils.copying(param, GoodsPricingRecord.class);

        this.baseMapper.insert(goodsPricingRecord);
        //检查是否产生价格预警，如果产生价格预警，生成预警记录
        this.checkWarnRule(goodsPricingRecord, param);

        return goodsPricingRecord.getId();
    }

    private void checkWarnRule(GoodsPricingRecord goodsPricingRecord, GoodsPricingRecordParam param) {
        param.setId(goodsPricingRecord.getId());
        List<GoodsPriceWarnRuleCheckVO> findWarnEvents = goodsPriceWarnRuleService.checkWarnRule(param);
        if (CollectionUtils.isNotEmpty(findWarnEvents)) {
            findWarnEvents.forEach(event -> {
                //插入预警记录
                GoodsPriceWarnRecord warnRecord = ObjectUtils.copying(goodsPricingRecord, GoodsPriceWarnRecord.class);
                warnRecord.setId(null);
                warnRecord.setPricingRecordId(goodsPricingRecord.getId());
                warnRecord.setPricingTime(goodsPricingRecord.getCreateTime());
                warnRecord.setGroupId(event.getGroupId());
                warnRecord.setRuleId(event.getRuleId());
                warnRecord.setRuleName(event.getRuleName());
                warnRecord.setPriceExpDesc(event.getWarnDesc());
                warnRecord.setRemark(event.getWarnDesc());
                warnRecord.setStatus(false);
                goodsPriceWarnRecordMapper.insert(warnRecord);
                //推送预警提醒
                applicationEventPublisher.publishEvent(new PriceWarnEvent(this, param.getTaskId(), warnRecord));
            });
        }
    }

    private GoodsPricingRecordParam extendPricingRecordInfo(GoodsPricingRecordParam param) {
        if (param.getUserId() != null) {
            UserDTO user = userClient.getUser(param.getUserId());
            if (user != null) {
                param.setUserName(user.getName());
                param.setUserPhone(user.getMobile());
                param.setDeptId(user.getOrganization() != null ? user.getOrganization().getId() : null);
                param.setDeptName(user.getOrganization() != null ? user.getOrganization().getName() : null);
            }
        }
        //扩展任务名称
        if (param.getTaskId() != null) {
            GoodsPricingTask goodsPricingTask = goodsPricingTaskMapper.selectById(param.getTaskId());
            param.setTaskName(goodsPricingTask != null ? goodsPricingTask.getName() : "");
        }
        //商品品类名称
        if (param.getGoodsCategoryId() != null) {
            MarketGoodsCategory category = marketGoodsCategoryMapper.selectById(param.getGoodsCategoryId());
            param.setGoodsCategoryName(category != null ? category.getName() : "");
        }
        return param;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, GoodsPricingRecordParam param) {
        param = extendPricingRecordInfo(param);
        GoodsPricingRecord goodsPricingRecord = this.baseMapper.selectById(id);
        Assert.isNotFound(goodsPricingRecord, "未查询到相关信息");
        ObjectUtils.copying(param, goodsPricingRecord);
        goodsPricingRecord.setId(id);
        this.baseMapper.updateById(goodsPricingRecord);
    }

    @Override
    public void updatePrice(Long id, GoodsPricingRecordParam param) {
        GoodsPricingRecord goodsPricingRecord = this.baseMapper.selectById(id);
        Assert.isNotFound(goodsPricingRecord, "未查询到相关信息");
        goodsPricingRecord.setMaxPrice(param.getMaxPrice());
        goodsPricingRecord.setMidPrice(param.getMidPrice());
        goodsPricingRecord.setMinPrice(param.getMinPrice());
        this.baseMapper.updateById(goodsPricingRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Long> ids) {
        ids.stream().forEach(id -> DatabaseContextHolder.softDelete(this, id));
    }

    @Override
    public GoodsPricingRecordVO getById(Long id) {
        GoodsPricingRecord goodsPricingRecord = this.baseMapper.selectById(id);
        GoodsPricingRecordVO vo = ObjectUtils.copying(goodsPricingRecord, GoodsPricingRecordVO.class);
        extendGoodsPricingRecord(vo);
        return vo;
    }

    private void extendGoodsPricingRecord(GoodsPricingRecordVO vo) {
        if (StringUtils.isBlank(vo.getGoodsCategoryName())) {
            MarketGoodsCategory category = marketGoodsCategoryMapper.selectById(vo.getGoodsCategoryId());
            vo.setGoodsCategoryName(category != null ? category.getName() : "");
        }
        //是否有报警
        List<GoodsPriceWarnRecord> goodsPriceWarnRecords = goodsPriceWarnRecordMapper.selectList(Wrappers
                                                                                                         .<GoodsPriceWarnRecord>lambdaQuery()
                                                                                                         .eq(GoodsPriceWarnRecord::getPricingRecordId, vo.getId()));
        vo.setWarned(CollectionUtils.isNotEmpty(goodsPriceWarnRecords));

    }

    @Override
    public Pagination<GoodsPricingRecordVO> getPage(GoodsPricingRecordQueryParam param) {
        Period period = param.getPeriod();
        //请自行添加查询条件
        Page<GoodsPricingRecord> page = this.lambdaQuery()
                .like(StringUtils.isNotBlank(param.getGoodsCategoryName()), GoodsPricingRecord::getGoodsCategoryName, param.getGoodsCategoryName())
                .eq(param.getTaskId() != null, GoodsPricingRecord::getTaskId, param.getTaskId())
                .eq(param.getGoodsCategoryId() != null, GoodsPricingRecord::getGoodsCategoryId, param.getGoodsCategoryId())
                .and(StringUtils.isNotBlank(param.getKeyword()), q ->
                        q.like(GoodsPricingRecord::getTaskName, param.getKeyword())
                                .or().like(GoodsPricingRecord::getUserName, param.getKeyword()))
                .between(period !=
                         null, GoodsPricingRecord::getCreateTime, ObjectUtils.ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, p -> p.getEnd()))
                .page(PagingUtils.convert(param));
        Pagination<GoodsPricingRecordVO> pagination = PagingUtils.convert(page, GoodsPricingRecordVO.class);
        if (CollectionUtils.isNotEmpty(pagination.getRecords())) {
            pagination.getRecords().stream().forEach(d -> {
                extendGoodsPricingRecord(d);
            });
        }
        return pagination;
    }

    @Override
    public List<GoodsPricingRecordVO> getList(GoodsPricingRecordQueryParam param) {
        Period period = param.getPeriod();
        List<GoodsPricingRecord> list = this.lambdaQuery()
                .like(StringUtils.isNotBlank(param.getGoodsCategoryName()), GoodsPricingRecord::getGoodsCategoryName, param.getGoodsCategoryName())
                .eq(param.getTaskId() != null, GoodsPricingRecord::getTaskId, param.getTaskId())
                .eq(param.getGoodsCategoryId() != null, GoodsPricingRecord::getGoodsCategoryId, param.getGoodsCategoryId())
                .and(StringUtils.isNotBlank(param.getKeyword()), q ->
                        q.like(GoodsPricingRecord::getTaskName, param.getKeyword())
                                .or().like(GoodsPricingRecord::getUserName, param.getKeyword()))
                .between(period !=
                         null, GoodsPricingRecord::getCreateTime, ObjectUtils.ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, p -> p.getEnd()))
                .list();
        List<GoodsPricingRecordVO> voList = ObjectUtils.copying(list, GoodsPricingRecordVO.class);
        if (CollectionUtils.isNotEmpty(voList)) {
            voList.stream().forEach(d -> {
                extendGoodsPricingRecord(d);
            });
        }
        return voList;
    }

    //计算环比时间段
    private Period calQOQPeriod(Period period) {
        var between = DateUtils.between(period.getBegin(), period.getEnd(), ChronoUnit.DAYS);
        var start = DateUtils.offsetDay(period.getBegin(), -(between + 1));
        var end = DateUtils.offsetDay(period.getEnd(), -(between + 1));
        return Period.of(start, end);
    }

    //计算同比时间段
    private Period calYOYPeriod(Period period) {
        var start = DateUtils.offsetMonth(period.getBegin(), -12);
        var end = DateUtils.offsetMonth(period.getEnd(), -12);
        return Period.of(start, end);
    }

    @Override
    public Pagination<GoodsPriceStatVO> getGoodsPriceStat(GoodsPriceStatParam param) {
        //查询商品的价格
        Page<GoodsPriceStatVO> page = goodsPricingRecordMapper.getGoodsPriceStat(PagingUtils.convert(param), param);
        //趋势比较，填充同比环比数据
        this.trendCompare(page.getRecords(), param);

        return PagingUtils.convert(page);
    }

    @Override
    public List<GoodsPriceStatVO> getGoodsPriceStatForAll(GoodsPriceStatParam param) {
        //查询商品的价格
        List<GoodsPriceStatVO> dataList = goodsPricingRecordMapper.getGoodsPriceStat(param);
        //趋势比较，填充同比环比数据
        this.trendCompare(dataList, param);

        return dataList;
    }

    private void trendCompare(List<GoodsPriceStatVO> dataList, GoodsPriceStatParam param) {
        Period period = param.getPeriod();
        if (CollectionUtils.isNotEmpty(dataList)) {
            List<Long> goodsCategoryIds = dataList.stream().map(g -> g.getGoodsCategoryId()).collect(Collectors.toList());
            param.setGoodsCategoryIds(goodsCategoryIds);
            if (period != null) {
                //获取环比的商品价格
                Period qoqPeriod = calQOQPeriod(period);
                param.setPeriod(qoqPeriod);
                List<GoodsPriceStatVO> goodsPriceStatForQoQ = goodsPricingRecordMapper.getGoodsPriceStat(param);
                Map<Long, GoodsPriceStatVO> qoqMap = goodsPriceStatForQoQ.stream()
                        .collect(Collectors.toMap(GoodsPriceStatVO::getGoodsCategoryId, g -> g));

                //获取同比的商品价格
                Period yoyPeriod = calYOYPeriod(period);
                param.setPeriod(yoyPeriod);
                List<GoodsPriceStatVO> goodsPriceStatForYOY = goodsPricingRecordMapper.getGoodsPriceStat(param);
                Map<Long, GoodsPriceStatVO> yoyMap = goodsPriceStatForYOY.stream()
                        .collect(Collectors.toMap(GoodsPriceStatVO::getGoodsCategoryId, g -> g));
                //计算同比和环比值
                dataList.stream().forEach(p -> {
                    //取出环比值
                    GoodsPriceStatVO qoq =
                            qoqMap.get(p.getGoodsCategoryId()) == null ? new GoodsPriceStatVO() : qoqMap.get(p.getGoodsCategoryId());
                    //取出同比值
                    GoodsPriceStatVO yoy =
                            yoyMap.get(p.getGoodsCategoryId()) == null ? new GoodsPriceStatVO() : yoyMap.get(p.getGoodsCategoryId());
                    //计算环比和同比
                    p.calQoQ(qoq.getMaxPriceAvg(), qoq.getMidPriceAvg(), qoq.getMinPriceAvg());
                    p.calYoY(yoy.getMaxPriceAvg(), yoy.getMidPriceAvg(), yoy.getMinPriceAvg());
                });
            }
            //查询来货量数据
            this.extendIncomingQuantity(dataList, param);
        }
    }

    //来货量
    private void extendIncomingQuantity(List<GoodsPriceStatVO> dataList, GoodsPriceStatParam param) {
        GoodsIncomingStatParam incomingStatParam = ObjectUtils.copying(param, GoodsIncomingStatParam.class);
        List<Long> goodsCategoryIds = dataList.stream().map(GoodsPriceStatVO::getGoodsCategoryId).collect(Collectors.toList());
        incomingStatParam.setGoodsCategoryIds(goodsCategoryIds);

        List<CommonStatVO> commonStatVOS = incomingQuantityMapper.incomingQuantityStatByTime(incomingStatParam);
        //通过goodsCategoryId进行匹配
        Map<String, String> incomingMap = Optional.ofNullable(commonStatVOS).orElse(Collections.emptyList()).stream()
                .collect(Collectors.toMap(CommonStatVO::getCode, s -> s.getValue()));
        dataList.stream().forEach(d -> {
            if (d.getGoodsCategoryId() != null) {
                String val = incomingMap.get(d.getGoodsCategoryId().toString());
                if (StringUtils.isNotBlank(val)) {
                    d.setIncomingQuantity(new BigDecimal(val));
                }
            }
        });
    }

    @Override
    public List<List<GoodsPriceTrendStatVO>> getGoodsPriceTrendStat(GoodsPriceStatParam param) {
        //根据统计类型，默认分组的时间格式
        if (StringUtils.isNotBlank(param.getStatType())) {
            if (param.getStatType().equals("MONTH")) {
                param.setGroupDateFormat("%Y-%m");
            } else if (param.getStatType().equals("DAY")) {
                param.setGroupDateFormat("%Y-%m-%d");
            } else if (param.getStatType().equals("WEEK")) {
                param.setGroupDateFormat("%x-%v");//按 2023-25 周的形式统计
            } else {
                param.setGroupDateFormat("%Y");
            }
        } else {
            //默认统计维度为月
            param.setGroupDateFormat("'%Y-%m'");
        }
        if (param.getPeriod() == null) {
            //默认3个月时间
            var startDate = DateUtils.offsetMonth(DateUtils.beginOfMonth(LocalDateTime.now()), -2);
            var endDate = DateUtils.endOfMonth(LocalDateTime.now());
            param.setPeriod(Period.of(startDate, endDate));
        }
        List<GoodsPriceTrendStatVO> goodsPriceTrendStat = goodsPricingRecordMapper.getGoodsPriceTrendStat(param);
        //填充没有数据的品类
        this.extendNotExistGoodsCategory(goodsPriceTrendStat, param);
        return checkTrendData(goodsPriceTrendStat, param.getStatType(), param.getPeriod());
    }

    @Override
    public List<Map<String, Object>> getGoodsPriceRToC(GoodsPriceStatParam param) {
        List<List<GoodsPriceTrendStatVO>> goodsPriceTrendStat = this.getGoodsPriceTrendStat(param);
        return convertPivotRowsToColumns(goodsPriceTrendStat);
    }

    public List<Map<String, Object>> convertPivotRowsToColumns(List<List<GoodsPriceTrendStatVO>> datas) {
        return datas.stream().map(list -> {
            List<Map<String, Object>> result = list.stream()
                    .collect(Collectors.groupingBy(GoodsPriceTrendStatVO::getGoodsCategoryName))
                    .entrySet().stream()
                    .map(entry -> {
                        Map<String, Object> map = entry.getValue().stream()
                                .collect(Collectors.toMap(
                                        GoodsPriceTrendStatVO::getDate,
                                        GoodsPriceTrendStatVO::getMidPriceAvg,
                                        (price1, price2) -> price1));
                        map.put("name", entry.getKey());
                        return map;
                    }).toList();
            return result.getFirst().entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        }).collect(Collectors.toList());
    }

    private List<List<GoodsPriceTrendStatVO>> checkTrendData(List<GoodsPriceTrendStatVO> datas, String statType, Period period) {
        LocalDate start = period.getBegin().toLocalDate();
        LocalDate end = period.getEnd().toLocalDate();
        Map<String, List<GoodsPriceTrendStatVO>> groupByNameMap = datas.stream()
                .collect(Collectors.groupingBy(GoodsPriceTrendStatVO::getGoodsCategoryName));
        return groupByNameMap.entrySet().stream().map(entry -> {
            List<GoodsPriceTrendStatVO> groupDatas = entry.getValue();
            if (statType.equals("DAY")) {
                Stream.iterate(start, day -> day.plusDays(1))
                        .limit(ChronoUnit.DAYS.between(start, end) + 1)
                        .forEach(date -> {
                            String dateStr = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                            GoodsPriceTrendStatVO trendStatVO = groupDatas.stream().filter(d -> d.getDate().equals(dateStr)).findFirst()
                                    .orElse(null);
                            if (trendStatVO == null) {
                                groupDatas.add(new GoodsPriceTrendStatVO(dateStr, entry.getKey()));
                            }
                        });
            } else if (statType.equals("WEEK")) {
                Stream.iterate(start, day -> day.plusWeeks(1))
                        .limit(ChronoUnit.WEEKS.between(start, end) + 1)
                        .forEach(date -> {
                            String dateStr = date.format(DateTimeFormatter.ofPattern("yyyy-ww"));
                            GoodsPriceTrendStatVO trendStatVO = groupDatas.stream().filter(d -> d.getDate().equals(dateStr)).findFirst()
                                    .orElse(null);
                            if (trendStatVO == null) {
                                groupDatas.add(new GoodsPriceTrendStatVO(dateStr + "周", entry.getKey()));
                            } else {
                                trendStatVO.setDate(trendStatVO.getDate() + "周");
                            }
                        });
            } else if (statType.equals("MONTH")) {
                Stream.iterate(start, day -> day.plusMonths(1))
                        .limit(ChronoUnit.MONTHS.between(start, end) + 1)
                        .forEach(date -> {
                            String dateStr = date.format(DateTimeFormatter.ofPattern("yyyy-MM"));
                            GoodsPriceTrendStatVO trendStatVO = groupDatas.stream().filter(d -> d.getDate().equals(dateStr)).findFirst()
                                    .orElse(null);
                            if (trendStatVO == null) {
                                groupDatas.add(new GoodsPriceTrendStatVO(dateStr + "月", entry.getKey()));
                            } else {
                                trendStatVO.setDate(trendStatVO.getDate() + "月");
                            }
                        });
            } else {
                Stream.iterate(start, day -> day.plusYears(1))
                        .limit(ChronoUnit.YEARS.between(start, end) + 1)
                        .forEach(date -> {
                            String dateStr = date.format(DateTimeFormatter.ofPattern("yyyy"));
                            GoodsPriceTrendStatVO trendStatVO = groupDatas.stream().filter(d -> d.getDate().equals(dateStr)).findFirst()
                                    .orElse(null);
                            if (trendStatVO == null) {
                                groupDatas.add(new GoodsPriceTrendStatVO(dateStr, entry.getKey()));
                            }
                        });
            }
            groupDatas.sort(Comparator.comparing(GoodsPriceTrendStatVO::getDate));
            return groupDatas;
        }).collect(Collectors.toList());
    }

    private void extendNotExistGoodsCategory(List<GoodsPriceTrendStatVO> dataList, GoodsPriceStatParam param) {
        List<Long> notExistCategoryIds;
        if (CollectionUtils.isNotEmpty(dataList)) {
            List<Long> existCategoryIds = dataList.stream().map(s -> s.getGoodsCategoryId()).distinct().collect(Collectors.toList());
            notExistCategoryIds = param.getGoodsCategoryIds().stream().filter(id ->
                                                                                      existCategoryIds.stream()
                                                                                              .filter(eid -> eid.longValue() ==
                                                                                                             id.longValue()).findFirst()
                                                                                              .orElse(null) == null
                                                                             ).collect(Collectors.toList());
        } else {
            notExistCategoryIds = param.getGoodsCategoryIds();
        }
        if (CollectionUtils.isNotEmpty(notExistCategoryIds)) {
            List<MarketGoodsCategory> categoryList = marketGoodsCategoryMapper.selectBatchIds(notExistCategoryIds);
            String statType = param.getStatType();
            String dateStr;
            if (statType.equals("DAY")) {
                dateStr = DateUtils.format(param.getPeriod().getBegin(), "yyyy-MM-dd");
            } else if (statType.equals("WEEK")) {
                dateStr = DateUtils.format(param.getPeriod().getBegin(), "yyyy-ww");
            } else if (statType.equals("MONTH")) {
                dateStr = DateUtils.format(param.getPeriod().getBegin(), "yyyy-MM");
            } else {
                dateStr = DateUtils.format(param.getPeriod().getBegin(), "yyyy");
            }
            List<GoodsPriceTrendStatVO> notExistList = categoryList.stream()
                    .map(s -> new GoodsPriceTrendStatVO(dateStr, s.getName()))
                    .collect(Collectors.toList());
            dataList.addAll(notExistList);
        }
    }

    /**
     * 第三方商品采价记录分页
     *
     * @param param 条件
     *
     * @return 分页
     */
    @Override
    public Pagination<GoodsPricingRecordThirdVO> getPricingRecordThirdPage(GoodsPricingRecordThirdParam param) {
        boolean pageIsNullBool = param == null || param.getPageNumber() == null || param.getPageSize() == null;
        Assert.isParameterError(pageIsNullBool, "分页参数不能为空");
        Page<GoodsPricingRecordThirdVO> page = goodsPricingRecordMapper.getGoodsPricingRecordThirdPage(PagingUtils.convert(param), param);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }

        return PagingUtils.convert(page);
    }

    /**
     * 第三方商品采价记录列表
     *
     * @param param 条件
     *
     * @return 分页
     */
    @Override
    public List<GoodsPricingRecordThirdVO> getPricingRecordThirdList(GoodsPricingRecordThirdParam param) {
        boolean periodIsNotNullBool = param != null && param.hasPeriod();
        Assert.isParameterError(!periodIsNotNullBool, "时间段不能为空！");

        List<GoodsPricingRecordThirdVO> list = goodsPricingRecordMapper.getGoodsPricingRecordThirdList(param);
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }

        return list;
    }

}