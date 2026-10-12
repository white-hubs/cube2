package com.guhecloud.cube.biz.ouaa.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.server.dao.ProductApplicationMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.ProductApplication;
import com.guhecloud.cube.biz.ouaa.server.service.ProductApplicationService;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Service
public class ProductApplicationServiceImpl extends ServiceImpl<ProductApplicationMapper, ProductApplication> implements ProductApplicationService {

    @Override
    public Long save(Long productId, Long applicationId) {
        ProductApplication saved = this.baseMapper.selectOne(Wrappers.<ProductApplication>lambdaQuery()
                                                                     .eq(ProductApplication::getProductId, productId)
                                                                     .eq(ProductApplication::getApplicationId, applicationId));
        if (Objects.isNull(saved)) {
            saved = ProductApplication.builder().productId(productId).applicationId(applicationId).build();
            this.baseMapper.insert(saved);
        }
        return saved.getId();
    }

    @Override
    public void remove(Long productId, Long applicationId) {
        this.baseMapper.delete(Wrappers.<ProductApplication>lambdaQuery().eq(ProductApplication::getProductId, productId)
                                       .eq(ProductApplication::getApplicationId, applicationId));
    }

    @Override
    public void removeByProduct(@NonNull Serializable productId) {
        this.baseMapper.delete(Wrappers.<ProductApplication>lambdaQuery().eq(ProductApplication::getProductId, productId));
    }

    @Override
    public void removeByApplication(Serializable applicationId) {
        this.baseMapper.delete(Wrappers.<ProductApplication>lambdaQuery().eq(ProductApplication::getApplicationId, applicationId));
    }

    @Override
    public List<ProductApplication> listByProduct(Long productId) {
        return this.baseMapper.selectList(Wrappers.<ProductApplication>lambdaQuery().eq(ProductApplication::getProductId, productId));
    }

    @Override
    public List<ProductApplication> listByApplication(Long applicationId) {
        return this.baseMapper.selectList(Wrappers.<ProductApplication>lambdaQuery()
                                                  .eq(ProductApplication::getApplicationId, applicationId));
    }

}
