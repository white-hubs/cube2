package com.guhecloud.cube.biz.ouaa.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.ouaa.server.entity.TenantProduct;

import java.util.List;

public interface TenantProductService extends IService<TenantProduct> {

    Long save(Integer tenant, Long productId);

    void remove(Integer tenant, Long productId);

    void removeByTenant(Integer tenant);

    void removeByProduct(Long productId);

    List<TenantProduct> listByTenant(Integer tenant);

    List<TenantProduct> listByProduct(Long productId);

}
