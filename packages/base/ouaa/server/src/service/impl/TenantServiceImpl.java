package com.guhecloud.cube.biz.ouaa.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.param.FileRefCreateParam;
import com.guhecloud.cube.base.file.client.param.FileRefRemoveParam;
import com.guhecloud.cube.biz.ouaa.metadata.enumeration.OuaaFileSceneCodeEnum;
import com.guhecloud.cube.biz.ouaa.metadata.enumeration.TenantCategory;
import com.guhecloud.cube.biz.ouaa.server.dao.TenantMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.Tenant;
import com.guhecloud.cube.biz.ouaa.server.entity.TenantApplication;
import com.guhecloud.cube.biz.ouaa.server.entity.TenantProduct;
import com.guhecloud.cube.biz.ouaa.server.entity.UserTenant;
import com.guhecloud.cube.biz.ouaa.server.param.TenantModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.TenantProductModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.TenantQueryParam;
import com.guhecloud.cube.biz.ouaa.server.param.TenantUpdateParam;
import com.guhecloud.cube.biz.ouaa.server.rocketmq.message.TenantInitMessage;
import com.guhecloud.cube.biz.ouaa.server.service.TenantApplicationService;
import com.guhecloud.cube.biz.ouaa.server.service.TenantProductService;
import com.guhecloud.cube.biz.ouaa.server.service.TenantService;
import com.guhecloud.cube.biz.ouaa.server.service.UserTenantService;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 租户业务操作实现
 *
 * @author Woody
 * @date 2021/7/1
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TenantServiceImpl extends ServiceImpl<TenantMapper, Tenant> implements TenantService {

    private final UserTenantService userTenantService;

    private final FileRefClient fileRefClient;

    private final TenantProductService tenantProductService;

    private final TenantApplicationService tenantApplicationService;

    @Override
    public boolean deleteTenant(@NonNull Long id) {
        return DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Tenant addTenant(@NonNull TenantModifyParam param) {
        Tenant tenant = ObjectUtils.copying(param, Tenant.class);
        tenant.setCode(new Random().nextInt(99999999));//随机生成8位编号
        tenant.setCategory(param.getCategory());//市场租户
        tenant.setFile(CollectionUtils.isEmpty(param.getFileIds()) ? null : String.join("-", param.getFileIds()));
        tenant.setLocation(CollectionUtils.isEmpty(param.getLocations()) ? null : String.join("-", param.getLocations()));
        boolean successful = this.save(tenant);
        // 生成文件关联
        this.setFileRef(tenant.getId(), this.getFileIds(tenant.getLogoId(), param.getFileIds()));
        UserTenant userTenant = UserTenant.builder()
                .tenant(tenant.getCode())
                .userId(param.getUserAdminId())
                .admin(true)
                .build();
        this.userTenantService.save(userTenant);
        if (TenantCategory.MARKET == tenant.getCategory()) {
            // 发送租户初始化消息
            TenantInitMessage message = TenantInitMessage.builder().tenant(tenant.getCode()).build();
            RocketMQContextHolder.sendTransactionalMessage(TenantInitMessage.TOPIC, message, () -> {
                return successful ? RocketMQLocalTransactionState.COMMIT : RocketMQLocalTransactionState.ROLLBACK;
            });
        }
        return tenant;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Tenant updateTenant(@NonNull Long id, @NonNull TenantModifyParam param) {
        Tenant tenant = this.getById(id);
        Assert.isNotFound(tenant, "租户不存在");
        // 获取该租户用户管理员
        UserTenant userTenant = this.userTenantService.getUserTenantAdmin(tenant.getCode());
        if (Objects.isNull(userTenant)) {
            userTenant = new UserTenant();
            userTenant.setTenant(tenant.getCode());
            userTenant.setUserId(param.getUserAdminId());
            userTenant.setAdmin(true);
            this.userTenantService.save(userTenant);
        } else if (!Objects.equals(userTenant.getUserId(), param.getUserAdminId())) {
            userTenant.setUserId(param.getUserAdminId());
            this.userTenantService.updateById(userTenant);
        }
        ObjectUtils.copying(param, tenant);
        if (!CollectionUtils.isEmpty(param.getFileIds())) {
            tenant.setFile(String.join("-", param.getFileIds()));
        }
        if (!CollectionUtils.isEmpty(param.getLocations())) {
            tenant.setLocation(String.join("-", param.getLocations()));
        }
        this.updateById(tenant);
        return tenant;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProductTenant(@NonNull Integer tenant, @NonNull TenantProductModifyParam param) {
        //删除该租户的产品
        this.tenantProductService.removeByTenant(tenant);
        //删除该租户的应用
        this.tenantApplicationService.removeByTenant(tenant);
        if (!CollectionUtils.isEmpty(param.getProducts())) {
            List<TenantApplication> tenantApplications = new ArrayList<>();
            List<TenantProduct> tenantProducts = param.getProducts().stream().map(x -> {
                if (!CollectionUtils.isEmpty(x.getApplicationIds())) {
                    x.getApplicationIds().stream().forEach(a -> {
                        tenantApplications.add(TenantApplication.builder().productId(x.getId()).tenant(tenant).applicationId(a).build());
                    });
                }
                return TenantProduct.builder().productId(x.getId()).tenant(tenant).build();
            }).collect(Collectors.toList());
            //新增产品关联
            this.tenantProductService.saveBatch(tenantProducts);
            //新增应用关联
            this.tenantApplicationService.saveBatch(tenantApplications);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@NonNull TenantUpdateParam param) {
        Tenant tenant = this.getById(param.getId());
        Set<Long> removeFileIds = new HashSet<>();
        if (tenant.getLogoId() != null) {
            removeFileIds.add(tenant.getLogoId());
        }
        if (!StringUtils.isEmpty(tenant.getFile())) {
            removeFileIds.addAll(Arrays.stream(tenant.getFile().split("-")).map(Long::parseLong).collect(Collectors.toSet()));
        }
        tenant.setRemark(param.getRemark());
        tenant.setLogoId(param.getFileId());
        tenant.setAddress(param.getAddress());
        tenant.setBuilding(param.getBuilding());
        tenant.setAreaCovered(param.getAreaCovered());
        tenant.setFloorArea(param.getFloorArea());
        tenant.setLocation(param.getLocation());
        tenant.setName(param.getName());
        tenant.setServiceHotLine(param.getServiceHotLine());
        tenant.setFile(CollectionUtils.isEmpty(param.getFileIds()) ? null : String.join("-", param.getFileIds()));
        tenant.setLntAndLat(param.getLntAndLat());
        tenant.setLntAndLatScope(param.getLntAndLatScope());
        tenant.setLntAndLatScopeName(param.getLntAndLatScopeName());
        tenant.setComplaintHotLine(param.getComplaintHotLine());
        this.updateById(tenant);
        //根据业务id移除关联
        if (!CollectionUtils.isEmpty(removeFileIds)) {
            FileRefRemoveParam fileRefRemoveParam = new FileRefRemoveParam();
            fileRefRemoveParam.setBusinessKey(tenant.getId());
            fileRefRemoveParam.setFileIdList(new ArrayList<>(removeFileIds));
            fileRefRemoveParam.setSceneCode(OuaaFileSceneCodeEnum.OUAA_TENANT.getCode());
            this.fileRefClient.removeByBusinessKeyAndIds(fileRefRemoveParam);
        }
        this.setFileRef(tenant.getId(), this.getFileIds(param.getFileId(), param.getFileIds()));
    }

    @Override
    public Tenant getByCode(@NonNull Integer code) {
        return this.baseMapper.selectOne(Wrappers.<Tenant>lambdaQuery().eq(Tenant::getCode, code));
    }

    @Override
    public List<Tenant> listByCodes(@NonNull Collection<? extends Serializable> codes) {
        return CollectionUtils.isEmpty(codes) ? Collections.emptyList() :
               this.lambdaQuery().in(Tenant::getCode, codes).list();
    }

    @Override
    public Page<Tenant> listTenants(@NonNull TenantQueryParam param) {
        return this.lambdaQuery().eq(Objects.nonNull(param.getCode()), Tenant::getCode, param.getCode())
                .like(!StringUtils.isEmpty(param.getName()), Tenant::getName, param.getName())
                .eq(Objects.nonNull(param.getEnabled()), Tenant::getEnabled, param.getEnabled())
                .notIn(!CollectionUtils.isEmpty(param.getCodes()), Tenant::getCode, param.getCodes())
                .orderByDesc(Tenant::getId).page(PagingUtils.convert(param));
    }

    @Override
    public Page<Tenant> listTenantsWithMarket(TenantQueryParam param) {
        return this.lambdaQuery().eq(Objects.nonNull(param.getCode()), Tenant::getCode, param.getCode())
                .like(!StringUtils.isEmpty(param.getName()), Tenant::getName, param.getName())
                .eq(Objects.nonNull(param.getEnabled()), Tenant::getEnabled, param.getEnabled())
                .eq(Tenant::getCategory, TenantCategory.MARKET)
                .orderByDesc(Tenant::getId).page(PagingUtils.convert(param));
    }

    @Override
    public List<Tenant> listTenants(String keyword, String category) {
        return this.lambdaQuery()
                .like(!StringUtils.isEmpty(keyword), Tenant::getName, keyword)
                .eq(!StringUtils.isEmpty(category), Tenant::getCategory, category)
                .list();
    }

    @Override
    public List<Tenant> listTenantsByNameOrCode(@NonNull String name, @NonNull Integer code) {
        return this.lambdaQuery()
                .and(q -> q
                        .eq(Tenant::getName, name)
                        .or()
                        .eq(Tenant::getCode, code)
                    ).list();
    }

    @Override
    public List<Tenant> listTenants(@NonNull List<Long> ids) {
        return CollectionUtils.isEmpty(ids) ? Collections.emptyList() :
               this.lambdaQuery().in(Tenant::getId, ids).list();
    }

    @Override
    public List<Tenant> listTenantsByCode(@NonNull List<Integer> tenants) {
        return CollectionUtils.isEmpty(tenants) ? Collections.emptyList() :
               this.lambdaQuery().in(Tenant::getCode, tenants).list();
    }

    @Override
    public void updateRecentLoginTenant(@NonNull Integer tenant) {
        Principal principal = SecurityContextHolder.getPrincipal();
        List<UserTenant> userTenants = this.userTenantService.listUserTenants(principal.getId());
        userTenants.forEach(userTenant -> {
            if (Objects.equals(userTenant.getRecentLogin(), true) && !Objects.equals(userTenant.getTenant(), tenant)) {
                userTenant.setRecentLogin(false);
            } else if (Objects.equals(userTenant.getTenant(), tenant)) {
                userTenant.setRecentLogin(true);
            }
        });
        this.userTenantService.updateBatchById(userTenants);
    }

    @Override
    public void updateMainEnterpriseTenant(@NonNull Integer tenant) {
        Principal principal = SecurityContextHolder.getPrincipal();
        List<UserTenant> userTenants = this.userTenantService.listUserTenants(principal.getId());
        userTenants.forEach(userTenant -> {
            if (Objects.equals(userTenant.getMainEnterprise(), true) && !Objects.equals(userTenant.getTenant(), tenant)) {
                userTenant.setMainEnterprise(false);
            } else if (Objects.equals(userTenant.getTenant(), tenant)) {
                userTenant.setMainEnterprise(true);
            }
        });
        this.userTenantService.updateBatchById(userTenants);
    }

    @Override
    public String getComplaintPhone(Integer tenant) {
        Tenant t = this.lambdaQuery().eq(Tenant::getCode, tenant).one();
        if (t != null) {
            return t.getComplaintHotLine();
        }
        return "";
    }

    private void setFileRef(Long businessKey, Set<Long> fileIds) {
        if (CollectionUtils.isEmpty(fileIds)) {
            return;
        }
        FileRefCreateParam fileRefCreateParam = new FileRefCreateParam();
        fileRefCreateParam.setFileIdList(new ArrayList<>(fileIds));
        fileRefCreateParam.setBusinessKey(businessKey);
        fileRefCreateParam.setSceneCode(OuaaFileSceneCodeEnum.OUAA_TENANT.getCode());
        this.fileRefClient.refInduce(fileRefCreateParam);
    }

    private Set<Long> getFileIds(Long logoId, List<String> fileIds) {
        Set<Long> ids = new HashSet<>();
        if (!CollectionUtils.isEmpty(fileIds)) {
            ids.addAll(fileIds.stream().map(Long::parseLong).collect(Collectors.toSet()));
        }
        if (Objects.nonNull(logoId)) {
            ids.add(logoId);
        }
        return ids;
    }
}
