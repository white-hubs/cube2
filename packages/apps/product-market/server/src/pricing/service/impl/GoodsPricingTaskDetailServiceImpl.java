package com.guhecloud.cube.srv.gen.product.market.server.pricing.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.product.market.server.common.dao.MarketGoodsCategoryMapper;
import com.guhecloud.cube.srv.gen.product.market.server.common.entity.MarketGoodsCategory;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.dao.GoodsPricingTaskDetailMapper;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPricingTask;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPricingTaskDetail;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.enumeration.PricingNoticeCodeEnum;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.enumeration.UrgencyEnum;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.message.event.PricingTaskEvent;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPricingTaskDetailParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPricingTaskDetailQueryParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.service.GoodsPricingTaskDetailService;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPricingTaskDetailVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 描述：采价任务详情服务层
 *
 * @author Appet
 * @date 2023-05-16 15:08:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GoodsPricingTaskDetailServiceImpl extends ServiceImpl<GoodsPricingTaskDetailMapper, GoodsPricingTaskDetail> implements GoodsPricingTaskDetailService {
    private final UserClient userClient;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final MarketGoodsCategoryMapper marketGoodsCategoryMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(GoodsPricingTaskDetailParam param) {
        GoodsPricingTaskDetail goodsPricingTaskDetail = ObjectUtils.copying(param, GoodsPricingTaskDetail.class);
        this.baseMapper.insert(goodsPricingTaskDetail);
        return goodsPricingTaskDetail.getId();
    }

    @Override
    public void addAndNotice(GoodsPricingTask task, GoodsPricingTaskDetail taskDetail) {
        //插入采价详情
        //扩展品类和用户姓名
        this.baseMapper.insert(extend(taskDetail));
        //消息通知
        try {
            //参数信息
            Map<String, String> msgParams = new HashMap<>();
            msgParams.put("pricingTaskName", task.getName());
            msgParams.put("pricingTaskUrgency", UrgencyEnum.valueOf(task.getUrgency()).getLabel());
            Set<Long> userIds = Arrays.stream(taskDetail.getUserIds().split(","))
                    .map(Long::parseLong).collect(Collectors.toSet());
            applicationEventPublisher.publishEvent(new PricingTaskEvent(this, task, userIds, PricingNoticeCodeEnum.PRICING_TASK_PUBLISHED_NOTIFY.name(), msgParams));
        } catch (Exception e) {
            log.error("采价任务，消息推送异常：", e);
        }
    }

    private GoodsPricingTaskDetail extend(GoodsPricingTaskDetail taskDetail) {
        //人员
        String userNames = Arrays.stream(taskDetail.getUserIds().split(",")).map(u -> {
            UserDTO user = userClient.getUser(Long.valueOf(u));
            if (user != null) {
                return user.getName();
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.joining(","));
        taskDetail.setUserNames(userNames);
        //品类
        List<Long> categoryIds = Arrays.asList(taskDetail.getGoodsCategoryIds().split(",")).stream().map(Long::parseLong)
                .collect(Collectors.toList());
        List<MarketGoodsCategory> categoryList = marketGoodsCategoryMapper.selectList(Wrappers.<MarketGoodsCategory>lambdaQuery()
                                                                                              .in(MarketGoodsCategory::getId, categoryIds));
        String categoryNames = Optional.ofNullable(categoryList).orElse(Collections.emptyList()).stream()
                .map(MarketGoodsCategory::getName).collect(Collectors.joining(","));
        taskDetail.setGoodsCategoryNames(categoryNames);
        return taskDetail;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, GoodsPricingTaskDetailParam param) {
        GoodsPricingTaskDetail goodsPricingTaskDetail = this.baseMapper.selectById(id);
        Assert.isNotFound(goodsPricingTaskDetail, "未查询到相关信息");
        ObjectUtils.copying(param, goodsPricingTaskDetail);
        this.baseMapper.updateById(goodsPricingTaskDetail);
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
    public GoodsPricingTaskDetailVO getById(Long id) {
        GoodsPricingTaskDetail goodsPricingTaskDetail = this.baseMapper.selectById(id);
        return ObjectUtils.copying(goodsPricingTaskDetail, GoodsPricingTaskDetailVO.class);
    }

    @Override
    public Pagination<GoodsPricingTaskDetailVO> getPage(GoodsPricingTaskDetailQueryParam param) {
        //请自行添加查询条件
        Page<GoodsPricingTaskDetail> page = this.lambdaQuery()
                .page(PagingUtils.convert(param));
        return PagingUtils.convert(page, GoodsPricingTaskDetailVO.class);
    }
}