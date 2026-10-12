package com.guhecloud.cube.srv.gen.product.market.server.pricing.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.dao.GoodsPriceWarnCategoryMapper;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPriceWarnCategory;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPriceWarnCategoryParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPriceWarnCategoryQueryParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.service.GoodsPriceWarnCategoryService;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPriceWarnCategoryVO;
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

import java.util.List;

/**
 * 描述：采价预警规则-适用类型服务层
 *
 * @author Appet
 * @date 2023-05-16 15:08:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GoodsPriceWarnCategoryServiceImpl extends ServiceImpl<GoodsPriceWarnCategoryMapper, GoodsPriceWarnCategory> implements GoodsPriceWarnCategoryService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(GoodsPriceWarnCategoryParam param) {
        GoodsPriceWarnCategory goodsPriceWarnCategory = ObjectUtils.copying(param, GoodsPriceWarnCategory.class);
        this.baseMapper.insert(goodsPriceWarnCategory);
        return goodsPriceWarnCategory.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, GoodsPriceWarnCategoryParam param) {
        GoodsPriceWarnCategory goodsPriceWarnCategory = this.baseMapper.selectById(id);
        Assert.isNotFound(goodsPriceWarnCategory, "未查询到相关信息");
        ObjectUtils.copying(param, goodsPriceWarnCategory);
        this.baseMapper.updateById(goodsPriceWarnCategory);
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
    public GoodsPriceWarnCategoryVO getById(Long id) {
        GoodsPriceWarnCategory goodsPriceWarnCategory = this.baseMapper.selectById(id);
        return ObjectUtils.copying(goodsPriceWarnCategory, GoodsPriceWarnCategoryVO.class);
    }

    @Override
    public Pagination<GoodsPriceWarnCategoryVO> getPage(GoodsPriceWarnCategoryQueryParam param) {
        //请自行添加查询条件
        Page<GoodsPriceWarnCategory> page = this.lambdaQuery()
                .page(PagingUtils.convert(param));
        return PagingUtils.convert(page, GoodsPriceWarnCategoryVO.class);
    }
}