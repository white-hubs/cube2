package com.guhecloud.cube.srv.gen.product.market.server.common.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.product.market.server.common.dao.MarketGoodsCategoryMapper;
import com.guhecloud.cube.srv.gen.product.market.server.common.entity.MarketGoodsCategory;
import com.guhecloud.cube.srv.gen.product.market.server.common.param.MarketGoodsCategoryParam;
import com.guhecloud.cube.srv.gen.product.market.server.common.param.MarketGoodsCategoryQueryParam;
import com.guhecloud.cube.srv.gen.product.market.server.common.service.MarketGoodsCategoryService;
import com.guhecloud.cube.srv.gen.product.market.server.common.vo.MarketGoodsCategoryVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
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
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 描述：商品品类服务层
 *
 * @author Appet
 * @date 2023-05-10 15:08:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MarketGoodsCategoryServiceImpl extends ServiceImpl<MarketGoodsCategoryMapper, MarketGoodsCategory> implements MarketGoodsCategoryService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(MarketGoodsCategoryParam param) {
        MarketGoodsCategory marketGoodsCategory = ObjectUtils.copying(param, MarketGoodsCategory.class);
        this.baseMapper.insert(marketGoodsCategory);
        return marketGoodsCategory.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, MarketGoodsCategoryParam param) {
        MarketGoodsCategory marketGoodsCategory = this.baseMapper.selectById(id);
        Assert.isNotFound(marketGoodsCategory, "未查询到相关信息");
        ObjectUtils.copying(param, marketGoodsCategory);
        this.baseMapper.updateById(marketGoodsCategory);
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
    public MarketGoodsCategoryVO getById(Long id) {
        MarketGoodsCategory marketGoodsCategory = this.baseMapper.selectById(id);
        return ObjectUtils.copying(marketGoodsCategory, MarketGoodsCategoryVO.class);
    }

    @Override
    public Pagination<MarketGoodsCategoryVO> getPage(MarketGoodsCategoryQueryParam param) {
        //请自行添加查询条件
        Page<MarketGoodsCategory> page = this.lambdaQuery()
                .page(PagingUtils.convert(param));
        return PagingUtils.convert(page, MarketGoodsCategoryVO.class);
    }

    @Override
    public List<MarketGoodsCategoryVO> getListByIds(String ids) {
        List<MarketGoodsCategory> parentCategoryList = this.baseMapper.selectList(Wrappers.<MarketGoodsCategory>lambdaQuery()
                                                                                          .eq(MarketGoodsCategory::getLevel, 1));

        List<Long> categoryIds = Arrays.asList(ids.split(",")).stream().map(Long::parseLong).collect(Collectors.toList());
        List<MarketGoodsCategory> categoryList = this.baseMapper.selectList(Wrappers.<MarketGoodsCategory>lambdaQuery()
                                                                                    .in(MarketGoodsCategory::getId, categoryIds));
        List<MarketGoodsCategoryVO> categoryVOList = categoryList.stream().map(c -> {
            MarketGoodsCategoryVO vo = ObjectUtils.copying(c, MarketGoodsCategoryVO.class);
            if (vo != null && c.getParentId() != null) {
                MarketGoodsCategory category = parentCategoryList.stream()
                        .filter(p -> p.getId().toString().equals(c.getParentId().toString())).findAny().orElse(null);
                vo.setParentName(category != null ? category.getName() : "");
            }
            return vo;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        return categoryVOList;
    }
}