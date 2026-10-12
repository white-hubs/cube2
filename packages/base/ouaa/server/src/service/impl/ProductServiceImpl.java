package com.guhecloud.cube.biz.ouaa.server.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.server.dao.ProductMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.Product;
import com.guhecloud.cube.biz.ouaa.server.param.ProductModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.ProductQueryParam;
import com.guhecloud.cube.biz.ouaa.server.service.ProductApplicationService;
import com.guhecloud.cube.biz.ouaa.server.service.ProductService;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    private final ProductApplicationService productApplicationService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull ProductModifyParam param) {
        Product product = ObjectUtils.copying(param, Product.class);
        this.baseMapper.insert(product);
        return product.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@NonNull Long id, @NonNull ProductModifyParam param) {
        Product product = this.getById(id);
        Assert.isNotFound(product, "产品不存在");
        ObjectUtils.copying(param, product);
        this.baseMapper.updateById(product);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull Long id) {
        DatabaseContextHolder.softDelete(this, id);
        // 删除应用关联
        this.productApplicationService.removeByProduct(id);
    }

    @Override
    public Product getById(@NonNull Serializable id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public List<Product> listByIds(@NonNull Collection<? extends Serializable> idList) {
        return idList.isEmpty() ? Collections.emptyList() : this.baseMapper.selectBatchIds(idList);
    }

    @Override
    public Page<Product> listByPage(@NonNull ProductQueryParam param) {
        return this.lambdaQuery()
                .and(!StringUtils.isEmpty(param.getKeyword()), q -> q
                        .like(Product::getCode, param.getKeyword())
                        .or()
                        .like(Product::getName, param.getKeyword()))
                .eq(Objects.nonNull(param.getEnabled()), Product::getEnabled, param.getEnabled())
                .orderByDesc(Product::getId).page(PagingUtils.convert(param));
    }
}
