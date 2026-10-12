package com.guhecloud.cube.biz.ouaa.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.server.dao.TenantProductMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.TenantProduct;
import com.guhecloud.cube.biz.ouaa.server.service.TenantProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 租户产品实现
 */
@Service
public class TenantProductServiceImpl extends ServiceImpl<TenantProductMapper, TenantProduct> implements TenantProductService {

    @Override
    public Long save(Integer tenant, Long productId) {
        TenantProduct saved = this.baseMapper.selectOne(Wrappers.<TenantProduct>lambdaQuery().eq(TenantProduct::getTenant, tenant)
                                                                .eq(TenantProduct::getProductId, productId));
        if (Objects.isNull(saved)) {
            saved = TenantProduct.builder().tenant(tenant).productId(productId).build();
            this.baseMapper.insert(saved);
        }
        return saved.getId();
    }

    @Override
    public void remove(Integer tenant, Long productId) {
        this.baseMapper.delete(Wrappers.<TenantProduct>lambdaQuery().eq(TenantProduct::getTenant, tenant)
                                       .eq(TenantProduct::getProductId, productId));
    }

    @Override
    public void removeByTenant(Integer tenant) {
        this.baseMapper.delete(Wrappers.<TenantProduct>lambdaQuery().eq(TenantProduct::getTenant, tenant));
    }

    @Override
    public void removeByProduct(Long productId) {
        this.baseMapper.delete(Wrappers.<TenantProduct>lambdaQuery().eq(TenantProduct::getProductId, productId));
    }

    @Override
    public List<TenantProduct> listByTenant(Integer tenant) {
        return this.baseMapper.selectList(Wrappers.<TenantProduct>lambdaQuery().eq(TenantProduct::getTenant, tenant));
    }

    @Override
    public List<TenantProduct> listByProduct(Long productId) {
        return this.baseMapper.selectList(Wrappers.<TenantProduct>lambdaQuery().eq(TenantProduct::getProductId, productId));
    }

}
