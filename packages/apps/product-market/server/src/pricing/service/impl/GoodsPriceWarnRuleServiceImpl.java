package com.guhecloud.cube.srv.gen.product.market.server.pricing.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.product.market.server.common.dao.MarketGoodsCategoryMapper;
import com.guhecloud.cube.srv.gen.product.market.server.common.entity.MarketGoodsCategory;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.dao.GoodsPriceWarnCategoryMapper;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.dao.GoodsPriceWarnRuleMapper;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.dao.GoodsPricingRecordMapper;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPriceWarnCategory;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPriceWarnRule;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPricingRecord;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.enumeration.PriceTypeEnum;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPriceWarnRuleParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPriceWarnRuleQueryParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPricingRecordParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.service.GoodsPriceWarnRuleService;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.util.ExpressionEvaluator;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPriceWarnRuleCheckVO;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPriceWarnRuleVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 描述：采价预警规则服务层
 *
 * @author Appet
 * @date 2023-05-16 15:08:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GoodsPriceWarnRuleServiceImpl extends ServiceImpl<GoodsPriceWarnRuleMapper, GoodsPriceWarnRule> implements GoodsPriceWarnRuleService {
    private final MarketGoodsCategoryMapper marketGoodsCategoryMapper;
    private final GoodsPriceWarnCategoryMapper goodsPriceWarnCategoryMapper;
    private final GoodsPriceWarnRuleMapper goodsPriceWarnRuleMapper;
    private final GoodsPricingRecordMapper goodsPricingRecordMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(GoodsPriceWarnRuleParam param) {
        GoodsPriceWarnRule goodsPriceWarnRule = ObjectUtils.copying(param, GoodsPriceWarnRule.class);
        this.baseMapper.insert(goodsPriceWarnRule);
        //保存告警涉及的品类
        this.batchAddWarnCategory(goodsPriceWarnRule, param.getGoodsCategoryIds());
        return goodsPriceWarnRule.getId();
    }

    private void batchAddWarnCategory(GoodsPriceWarnRule rule, List<Long> goodsCategoryIds) {
        //保存数据
        if (CollectionUtils.isNotEmpty(goodsCategoryIds)) {
            goodsCategoryIds.stream().forEach(goodsCategoryId -> {
                MarketGoodsCategory category = marketGoodsCategoryMapper.selectById(goodsCategoryId);
                String parentIds = category != null ? category.getSequence() : "";
                String goodsCategoryName = category != null ? category.getName() : "";
                try {
                    goodsPriceWarnCategoryMapper.insert(new GoodsPriceWarnCategory(rule.getGroupId(), rule.getId(), rule.getPriceType(), goodsCategoryId, goodsCategoryName, parentIds));
                } catch (DuplicateKeyException e) {
                    log.error("采价预警股则重复：", e);
                    //查询出重复的规则
                    List<GoodsPriceWarnCategory> warnCatgory = goodsPriceWarnCategoryMapper.selectList(Wrappers
                                                                                                               .<GoodsPriceWarnCategory>lambdaQuery()
                                                                                                               .eq(GoodsPriceWarnCategory::getGoodsCategoryId, goodsCategoryId)
                                                                                                               .eq(GoodsPriceWarnCategory::getPriceType, rule.getPriceType()));
                    GoodsPriceWarnCategory warnCategory = warnCatgory.get(0);
                    //查询出对应的规则
                    GoodsPriceWarnRule warnRule = goodsPriceWarnRuleMapper.selectById(warnCategory.getRuleId());

                    String errorInfo = warnCategory.getGoodsCategoryName() + " 在 " + warnRule.getName() + " 中已经设置了" +
                                       warnCategory.getPriceType().getLabel();
                    Assert.isConflict(CollectionUtils.isNotEmpty(warnCatgory), errorInfo);
                }
            });
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, GoodsPriceWarnRuleParam param) {
        GoodsPriceWarnRule goodsPriceWarnRule = this.baseMapper.selectById(id);
        Assert.isNotFound(goodsPriceWarnRule, "未查询到相关信息");
        ObjectUtils.copying(param, goodsPriceWarnRule);
        this.baseMapper.updateById(goodsPriceWarnRule);
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
    public GoodsPriceWarnRuleVO getById(Long id) {
        GoodsPriceWarnRule goodsPriceWarnRule = this.baseMapper.selectById(id);
        return ObjectUtils.copying(goodsPriceWarnRule, GoodsPriceWarnRuleVO.class);
    }

    @Override
    public List<GoodsPriceWarnRuleVO> getByGroupId(Long groupId) {
        List<GoodsPriceWarnRule> rules = this.baseMapper.selectList(Wrappers.<GoodsPriceWarnRule>lambdaQuery()
                                                                            .eq(GoodsPriceWarnRule::getGroupId, groupId));
        if (CollectionUtils.isNotEmpty(rules)) {
            return rules.stream().map(r -> {
                GoodsPriceWarnRuleVO ruleVO = ObjectUtils.copying(r, GoodsPriceWarnRuleVO.class);
                //查询规则适用的品类
                List<GoodsPriceWarnCategory> categories = goodsPriceWarnCategoryMapper.selectList(Wrappers
                                                                                                          .<GoodsPriceWarnCategory>lambdaQuery()
                                                                                                          .eq(GoodsPriceWarnCategory::getRuleId, r.getId()));
                if (CollectionUtils.isNotEmpty(categories)) {
                    ruleVO.setGoodsCategoryIds(categories.stream().map(GoodsPriceWarnCategory::getGoodsCategoryId)
                                                       .collect(Collectors.toList()));
                    ruleVO.setGoodsCategoryNames(categories.stream().map(GoodsPriceWarnCategory::getGoodsCategoryName)
                                                         .filter(Objects::nonNull).collect(Collectors.toList()));
                }
                return ruleVO;
            }).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public Pagination<GoodsPriceWarnRuleVO> getPage(GoodsPriceWarnRuleQueryParam param) {
        //请自行添加查询条件
        Page<GoodsPriceWarnRule> page = this.lambdaQuery()
                .page(PagingUtils.convert(param));
        return PagingUtils.convert(page, GoodsPriceWarnRuleVO.class);
    }

    @Override
    public List<GoodsPriceWarnRuleCheckVO> checkWarnRule(GoodsPricingRecordParam pricingRecordParam) {
        List<GoodsPriceWarnCategory> warnCategories = goodsPriceWarnCategoryMapper.getEnableRule(pricingRecordParam.getGoodsCategoryId());
        if (CollectionUtils.isNotEmpty(warnCategories)) {
            List<Long> ruleIds = warnCategories.stream().map(w -> w.getRuleId()).collect(Collectors.toList());
            //查询所有的适用规则
            List<GoodsPriceWarnRule> goodsPriceWarnRules = goodsPriceWarnRuleMapper.selectBatchIds(ruleIds);
            if (CollectionUtils.isNotEmpty(goodsPriceWarnRules)) {
                return goodsPriceWarnRules.stream().map(r -> {
                    BigDecimal currentVal = this.getValue(r.getPriceType(), ObjectUtils.copying(pricingRecordParam, GoodsPricingRecord.class));
                    if (currentVal != null) {
                        String expression = "false";
                        if (r.getPriceType().name().endsWith("CREASE")) {//增幅或降幅
                            //获取该品类的上一次报价
                            GoodsPricingRecord pricingRecord = goodsPricingRecordMapper.selectOne(Wrappers.<GoodsPricingRecord>lambdaQuery()
                                                                                                          .eq(GoodsPricingRecord::getGoodsCategoryId, pricingRecordParam.getGoodsCategoryId())
                                                                                                          .ne(pricingRecordParam.getId() !=
                                                                                                              null, GoodsPricingRecord::getId, pricingRecordParam.getId())
                                                                                                          .orderByDesc(GoodsPricingRecord::getCreateTime)
                                                                                                          .last("limit 0,1"));
                            if (pricingRecord != null) {
                                BigDecimal preVal = this.getValue(r.getPriceType(), pricingRecord);
                                if (preVal != null) {
                                    //涨跌幅的百分比
                                    BigDecimal pert = currentVal.subtract(preVal).multiply(BigDecimal.valueOf(100)).divide(preVal);
                                    if (r.getPriceType().name().endsWith("INCREASE") && pert.compareTo(BigDecimal.ZERO) >= 0) {
                                        expression = pert.setScale(2) + r.getCompareCondition() + r.getCompareValue();
                                    } else if (r.getPriceType().name().endsWith("DECREASE") && pert.compareTo(BigDecimal.ZERO) < 0) {
                                        expression = pert.abs().setScale(2) + r.getCompareCondition() + r.getCompareValue();
                                    }
                                }
                            }
                        } else {
                            expression = currentVal.setScale(2) + r.getCompareCondition() + r.getCompareValue();
                        }
                        try {
                            boolean evaluate = ExpressionEvaluator.evaluate(expression);
                            if (evaluate) {
                                return GoodsPriceWarnRuleCheckVO.builder()
                                        .groupId(r.getGroupId())
                                        .ruleId(r.getId())
                                        .ruleName(r.getName())
                                        .priceType(r.getPriceType())
                                        .warnDesc(r.getPriceType().getLabel() + ":" + expression)
                                        .build();
                            }
                        } catch (Exception e) {
                            log.error("表达式异常：" + expression + " 无法计算结果");
                        }
                    }
                    return null;
                }).filter(Objects::nonNull).collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }

    private BigDecimal getValue(PriceTypeEnum priceType, GoodsPricingRecord param) {
        BigDecimal val = null;
        if (priceType.name().startsWith("MAX")) {
            val = param.getMaxPrice();
        } else if (priceType.name().startsWith("MID")) {
            val = param.getMidPrice();
        } else if (priceType.name().startsWith("MIN")) {
            val = param.getMinPrice();
        }
        return val;
    }
}