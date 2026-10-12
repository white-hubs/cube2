package com.guhecloud.cube.biz.ouaa.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.ouaa.server.entity.TenantApplication;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface TenantApplicationService extends IService<TenantApplication> {

    Long save(Integer tenant, Long productId, Long applicationId);

    void remove(Integer tenant, Long productId, Long applicationId);

    void removeByTenant(Integer tenant);

    void removeByProductApplication(Long productId, Long applicationId);

    List<TenantApplication> listByTenant(Integer tenant);

    List<TenantApplication> listByTenantProduct(Integer tenant);

    List<TenantApplication> listByTenantProductApplications(Integer tenant, Long productId, Collection<Long> applicationIds);

    List<TenantApplication> listByProductApplication(Long productId, Long applicationId);

    List<TenantApplication> listByApplication(Long applicationId);

    List<TenantApplication> listByTenantAndApplicationId(Integer tenant, Set<Long> applicationIds);
}
