package com.guhecloud.cube.srv.gen.product.market.server.pricing.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.queryrier.UserQuery;
import com.guhecloud.cube.srv.gen.product.market.server.common.dao.MarketGoodsCategoryMapper;
import com.guhecloud.cube.srv.gen.product.market.server.common.entity.MarketGoodsCategory;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.dao.GoodsPricingPlanDetailMapper;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPricingPlanDetail;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPricingPlanDetailParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPricingPlanDetailQueryParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.service.GoodsPricingPlanDetailService;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPricingPlanDetailVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 描述：采价计划详情服务层
 *
 * @author Appet
 * @date 2023-05-16 15:08:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GoodsPricingPlanDetailServiceImpl extends ServiceImpl<GoodsPricingPlanDetailMapper, GoodsPricingPlanDetail> implements GoodsPricingPlanDetailService {
    private final MarketGoodsCategoryMapper marketGoodsCategoryMapper;
    private final UserClient userClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(GoodsPricingPlanDetailParam param) {
        GoodsPricingPlanDetail goodsPricingPlanDetail = ObjectUtils.copying(param, GoodsPricingPlanDetail.class);
        this.baseMapper.insert(extend(goodsPricingPlanDetail));
        return goodsPricingPlanDetail.getId();
    }

    private GoodsPricingPlanDetail extend(GoodsPricingPlanDetail goodsPricingPlanDetail) {
        //扩展品类信息
        List<Long> categoryIds = Arrays.stream(goodsPricingPlanDetail.getGoodsCategoryIds().split(",")).map(Long::parseLong)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(categoryIds)) {
            List<MarketGoodsCategory> categoryList = marketGoodsCategoryMapper.selectBatchIds(categoryIds);
            if (!CollectionUtils.isEmpty(categoryList)) {
                String categoryNames = categoryList.stream().map(c -> c.getName()).collect(Collectors.joining(","));
                goodsPricingPlanDetail.setGoodsCategoryNames(categoryNames);
            }
        }

        //扩展人员姓名
        Set<Long> userIds = Arrays.stream(goodsPricingPlanDetail.getUserIds().split(",")).map(Long::parseLong).collect(Collectors.toSet());
        if (!CollectionUtils.isEmpty(userIds)) {
            List<UserDTO> userList = userClient.list(UserQuery.builder().userIds(userIds).build());
            if (!CollectionUtils.isEmpty(userList)) {
                String userNames = userList.stream().map(c -> c.getName()).collect(Collectors.joining(","));
                goodsPricingPlanDetail.setUserNames(userNames);
            }
        }
        return goodsPricingPlanDetail;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, GoodsPricingPlanDetailParam param) {
        GoodsPricingPlanDetail goodsPricingPlanDetail = this.baseMapper.selectById(id);
        Assert.isNotFound(goodsPricingPlanDetail, "未查询到相关信息");
        ObjectUtils.copying(param, goodsPricingPlanDetail);
        this.baseMapper.updateById(goodsPricingPlanDetail);
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
    public GoodsPricingPlanDetailVO getById(Long id) {
        GoodsPricingPlanDetail goodsPricingPlanDetail = this.baseMapper.selectById(id);
        return ObjectUtils.copying(goodsPricingPlanDetail, GoodsPricingPlanDetailVO.class);
    }

    @Override
    public Pagination<GoodsPricingPlanDetailVO> getPage(GoodsPricingPlanDetailQueryParam param) {
        //请自行添加查询条件
        Page<GoodsPricingPlanDetail> page = this.lambdaQuery()
                .page(PagingUtils.convert(param));
        return PagingUtils.convert(page, GoodsPricingPlanDetailVO.class);
    }
}