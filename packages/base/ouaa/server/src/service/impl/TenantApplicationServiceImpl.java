package com.guhecloud.cube.biz.ouaa.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.server.dao.TenantApplicationMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.Application;
import com.guhecloud.cube.biz.ouaa.server.entity.TenantApplication;
import com.guhecloud.cube.biz.ouaa.server.rocketmq.message.TenantAddApplicationMessage;
import com.guhecloud.cube.biz.ouaa.server.service.ApplicationService;
import com.guhecloud.cube.biz.ouaa.server.service.TenantApplicationService;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TenantApplicationServiceImpl extends ServiceImpl<TenantApplicationMapper, TenantApplication> implements TenantApplicationService {

    private final ApplicationService applicationService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(Integer tenant, Long productId, Long applicationId) {
        TenantApplication saved = this.baseMapper.selectOne(Wrappers.<TenantApplication>lambdaQuery()
                                                                    .eq(TenantApplication::getTenant, tenant)
                                                                    .eq(Objects.nonNull(productId), TenantApplication::getProductId, productId)
                                                                    .eq(TenantApplication::getApplicationId, applicationId));
        if (Objects.isNull(saved)) {
            saved = TenantApplication.builder().tenant(tenant).productId(productId).applicationId(applicationId).build();
            this.baseMapper.insert(saved);
        }
        Application application = this.applicationService.getById(applicationId);
        RocketMQContextHolder.sendTransactionalMessage(TenantAddApplicationMessage.TOPIC, TenantAddApplicationMessage.builder()
                .id(saved.getId())
                .applicationCodes(Collections.singletonList(application.getCode()))
                .tenant(tenant)
                .build(), () -> RocketMQLocalTransactionState.COMMIT);
        return saved.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(Integer tenant, Long productId, Long applicationId) {
        this.baseMapper.delete(Wrappers.<TenantApplication>lambdaQuery().eq(TenantApplication::getTenant, tenant)
                                       .eq(Objects.nonNull(productId), TenantApplication::getProductId, productId)
                                       .eq(TenantApplication::getApplicationId, applicationId));
    }

    @Override
    public void removeByTenant(Integer tenant) {
        this.baseMapper.delete(Wrappers.<TenantApplication>lambdaQuery().eq(TenantApplication::getTenant, tenant));
    }

    @Override
    public void removeByProductApplication(Long productId, Long applicationId) {
        this.baseMapper.delete(Wrappers.<TenantApplication>lambdaQuery().eq(TenantApplication::getProductId, productId)
                                       .eq(TenantApplication::getApplicationId, applicationId));
    }

    @Override
    public List<TenantApplication> listByTenant(Integer tenant) {
        return this.lambdaQuery().eq(TenantApplication::getTenant, tenant).list();
    }

    @Override
    public List<TenantApplication> listByTenantProduct(Integer tenant) {
        return this.baseMapper.selectListByTenantProductApplications(tenant, null, null);
    }

    @Override
    public List<TenantApplication> listByTenantProductApplications(Integer tenant, Long productId, Collection<Long> applicationIds) {
        return this.baseMapper.selectListByTenantProductApplications(tenant, productId, applicationIds);
    }

    @Override
    public List<TenantApplication> listByProductApplication(Long productId, Long applicationId) {
        return this.baseMapper.selectListByTenantProductApplications(null, productId, Collections.singleton(applicationId));
    }

    @Override
    public List<TenantApplication> listByApplication(Long applicationId) {
        return this.baseMapper.selectListByTenantProductApplications(null, null, Collections.singleton(applicationId));
    }

    @Override
    public List<TenantApplication> listByTenantAndApplicationId(Integer tenant, Set<Long> applicationIds) {
        return this.baseMapper.selectList(Wrappers.<TenantApplication>lambdaQuery()
                                                  .eq(TenantApplication::getTenant, tenant)
                                                  .in(TenantApplication::getApplicationId, applicationIds));
    }

}