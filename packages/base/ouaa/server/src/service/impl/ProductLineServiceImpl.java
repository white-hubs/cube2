package com.guhecloud.cube.biz.ouaa.server.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.server.dao.ProductLineMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.ProductLine;
import com.guhecloud.cube.biz.ouaa.server.param.ProductLineModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.ProductLineQueryParam;
import com.guhecloud.cube.biz.ouaa.server.service.ProductLineService;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class ProductLineServiceImpl extends ServiceImpl<ProductLineMapper, ProductLine> implements ProductLineService {

    @Override
    public Long save(@NonNull ProductLineModifyParam param) {
        ProductLine productLine = ObjectUtils.copying(param, ProductLine.class);
        this.baseMapper.insert(productLine);
        return productLine.getId();
    }

    @Override
    public void update(@NonNull Long id, @NonNull ProductLineModifyParam param) {
        ProductLine productLine = this.getById(id);
        Assert.isNotFound(productLine, "产品线不存在");
        ObjectUtils.copying(param, productLine);
        this.baseMapper.updateById(productLine);
    }

    @Override
    public void remove(@NonNull Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    public ProductLine getById(@NonNull Serializable id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public List<ProductLine> listByIds(@NonNull Collection<? extends Serializable> idList) {
        return idList.isEmpty() ? Collections.emptyList() : this.baseMapper.selectBatchIds(idList);
    }

    @Override
    public Page<ProductLine> listByPage(@NonNull ProductLineQueryParam param) {
        return this.lambdaQuery()
                .like(!StringUtils.isEmpty(param.getKeyword()), ProductLine::getName, param.getKeyword())
                .eq(Objects.nonNull(param.getEnabled()), ProductLine::getEnabled, param.getEnabled())
                .orderByDesc(ProductLine::getCreateTime)
                .page(PagingUtils.convert(param));
    }

}
