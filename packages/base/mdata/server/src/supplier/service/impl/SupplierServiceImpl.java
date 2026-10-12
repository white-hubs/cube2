package com.guhecloud.cube.biz.mdata.server.supplier.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.param.FileRefCreateParam;
import com.guhecloud.cube.biz.mdata.server.purchaser.enumeration.PurchaserAuthenticationEnum;
import com.guhecloud.cube.biz.mdata.server.rocketmq.message.SupplierMessage;
import com.guhecloud.cube.biz.mdata.server.stall.service.StallService;
import com.guhecloud.cube.biz.mdata.server.supplier.dao.SupplierMapper;
import com.guhecloud.cube.biz.mdata.server.supplier.dao.SupplierUserMapper;
import com.guhecloud.cube.biz.mdata.server.supplier.entity.Supplier;
import com.guhecloud.cube.biz.mdata.server.supplier.entity.SupplierUser;
import com.guhecloud.cube.biz.mdata.server.supplier.enumeration.PurchaserTypeEnum;
import com.guhecloud.cube.biz.mdata.server.supplier.enumeration.RiskLevelEnum;
import com.guhecloud.cube.biz.mdata.server.supplier.enumeration.SupplierAuthenticatedEnum;
import com.guhecloud.cube.biz.mdata.server.supplier.enumeration.SupplierUserEnum;
import com.guhecloud.cube.biz.mdata.server.supplier.param.SupplierAuthenticationModifyParam;
import com.guhecloud.cube.biz.mdata.server.supplier.param.SupplierModifyMpParam;
import com.guhecloud.cube.biz.mdata.server.supplier.param.SupplierModifyParam;
import com.guhecloud.cube.biz.mdata.server.supplier.param.SupplierQueryParam;
import com.guhecloud.cube.biz.mdata.server.supplier.service.SupplierAuthenticationService;
import com.guhecloud.cube.biz.mdata.server.supplier.service.SupplierService;
import com.guhecloud.cube.biz.mdata.server.supplier.vo.SupplierPageVO;
import com.guhecloud.cube.biz.mdata.server.supplier.vo.SupplierVO;
import com.guhecloud.cube.biz.ouaa.client.TenantClient;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.TenantDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.param.TenantModifyParam;
import com.guhecloud.cube.biz.ouaa.client.param.UserModifyParam;
import com.guhecloud.cube.biz.ouaa.metadata.enumeration.Gender;
import com.guhecloud.cube.biz.ouaa.metadata.enumeration.TenantCategory;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.entity.BasicEntity;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.guhecloud.cube.biz.mdata.server.constants.FileSceneConstants.SUPPLIER_INFO;
import static com.guhecloud.cube.biz.mdata.server.constants.FileSceneConstants.TENANT_LOGO;

/**
 * @author lzy
 * @date 2021/12/3
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SupplierServiceImpl extends ServiceImpl<SupplierMapper, Supplier> implements SupplierService {
    private final TenantClient tenantClient;
    private final UserClient userClient;
    private final FileRefClient fileRefClient;
    private final SupplierUserMapper supplierUserMapper;
    private final SupplierMapper supplierMapper;
    private final SupplierAuthenticationService supplierAuthenticationService;
    private final StallService stallService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addSupplier(@NonNull SupplierModifyParam param) {
        // 获取当前管理员的用户信息
        UserModifyParam userModifyParam = new UserModifyParam();
        userModifyParam.setName(param.getAdminPhone());
        userModifyParam.setMobile(param.getAdminPhone());
        userModifyParam.setUsername(param.getAdminPhone());
        userModifyParam.setGender(Gender.NONE);
        userModifyParam.setEnabled(true);
        UserDTO userDTO = this.userClient.save(userModifyParam);

        // 新增租户信息
        TenantModifyParam saveTenant = new TenantModifyParam();
        saveTenant.setName(param.getName());
        saveTenant.setUserAdminId(userDTO.getId());
        saveTenant.setLocation(param.getAreaIds());
        saveTenant.setAddress(param.getAddress());
        saveTenant.setCategory(TenantCategory.SUPPLIER);
        TenantDTO tenantDTO = this.tenantClient.addTenant(saveTenant);

        // 新增批发商基础信息
        Supplier supplier = ObjectUtils.copying(param, Supplier.class);
        supplier.setAdminUserId(userDTO.getId());
        supplier.setAdminUserName(userDTO.getName());
        supplier.setCode(param.getAdminPhone());
        supplier.setArea(String.join("-", param.getAreaIds()));
        supplier.setCategory(String.join("-", param.getCategoryIds()));
        supplier.setSupplierTenant(tenantDTO.getCode());
        supplier.setRiskLevel(RiskLevelEnum.LOW);

        // 市场租户编号
        supplier.setTenant(SessionContextHolder.getRequester().getTenant());
        this.baseMapper.insert(supplier);

        this.addSupplierUser(supplier, userDTO, SupplierUserEnum.ADMIN);

        if (Objects.nonNull(param.getFileId())) {
            // 新增logo信息
            this.saveFileInfo(param.getFileId(), tenantDTO.getId(), supplier.getId());
        }
        if (!CollectionUtils.isEmpty(param.getStallIds())) {
            this.stallService.saveSupplierStall(param.getStallIds(), supplier.getId());
        }
        return supplier.getId();
    }

    @Override
    public void importSupplierTemplate(List<SupplierModifyParam> params) {
        if (CollectionUtils.isNotEmpty(params)) {
            for (SupplierModifyParam param : params) {
                // 获取当前管理员的用户信息
                UserModifyParam userModifyParam = new UserModifyParam();
                userModifyParam.setName(param.getAdminPhone());
                userModifyParam.setMobile(param.getAdminPhone());
                userModifyParam.setUsername(param.getAdminPhone());
                userModifyParam.setGender(Gender.NONE);
                userModifyParam.setEnabled(true);
                UserDTO userDTO = this.userClient.save(userModifyParam);

                // 新增租户信息
                TenantModifyParam saveTenant = new TenantModifyParam();
                saveTenant.setName(param.getName());
                saveTenant.setUserAdminId(userDTO.getId());
                saveTenant.setAddress(param.getAddress());
                saveTenant.setCategory(TenantCategory.SUPPLIER);
                TenantDTO tenantDTO = this.tenantClient.addTenant(saveTenant);

                // 新增批发商基础信息
                Supplier supplier = ObjectUtils.copying(param, Supplier.class);
                supplier.setAdminUserId(userDTO.getId());
                supplier.setAdminUserName(userDTO.getName());
                supplier.setCode(param.getAdminPhone());
                supplier.setSupplierTenant(tenantDTO.getCode());
                supplier.setRiskLevel(RiskLevelEnum.LOW);

                // 市场租户编号
                supplier.setTenant(SessionContextHolder.getRequester().getTenant());
                this.baseMapper.insert(supplier);
                this.addSupplierUser(supplier, userDTO, SupplierUserEnum.ADMIN);

            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addSupplierAndAuthentication(@NonNull SupplierModifyParam param) {
        Long supplierId = this.addSupplier(param);
        SupplierAuthenticationModifyParam supplierAuthenticationModifyParam = new SupplierAuthenticationModifyParam();
        supplierAuthenticationModifyParam.setSupplierId(supplierId);
        supplierAuthenticationModifyParam.setStatus(SupplierAuthenticatedEnum.CERTIFIED);
        supplierAuthenticationModifyParam.setType(PurchaserTypeEnum.PERSONAL);
        this.supplierAuthenticationService.addSupplierAuthentication(supplierAuthenticationModifyParam);
        return supplierId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Supplier addSupplierByMp(@NonNull SupplierModifyMpParam param) {
        // 获取当前管理员的用户信息
        UserModifyParam userModifyParam = new UserModifyParam();
        userModifyParam.setName(param.getAdminPhone());
        userModifyParam.setMobile(param.getAdminPhone());
        userModifyParam.setUsername(param.getAdminPhone());
        userModifyParam.setGender(Gender.NONE);
        UserDTO userDTO = this.userClient.save(userModifyParam);

        // 新增租户信息
        TenantModifyParam saveTenant = new TenantModifyParam();
        saveTenant.setName(param.getName());
        saveTenant.setUserAdminId(userDTO.getId());
        saveTenant.setLocation(param.getAreaIds());
        saveTenant.setAddress(param.getAddress());
        saveTenant.setCategory(TenantCategory.SUPPLIER);
        TenantDTO tenantDTO = this.tenantClient.addTenant(saveTenant);

        // 新增批发商基础信息
        Supplier supplier = ObjectUtils.copying(param, Supplier.class);
        supplier.setAdminUserId(userDTO.getId());
        supplier.setAdminUserName(userDTO.getName());
        supplier.setCode(param.getAdminPhone());
        supplier.setArea(String.join("-", param.getAreaIds()));
        supplier.setSupplierTenant(tenantDTO.getCode());
        supplier.setTenant(param.getMarketTenant());
        supplier.setRiskLevel(RiskLevelEnum.LOW);

        if (!CollectionUtils.isEmpty(param.getCategoryIds())) {
            supplier.setCategory(String.join("-", param.getCategoryIds()));
        }
        this.baseMapper.insert(supplier);

        this.addSupplierUser(supplier, userDTO, SupplierUserEnum.ADMIN);

        if (Objects.nonNull(param.getLogoId())) {
            // 新增logo信息
            this.saveFileInfo(param.getLogoId(), tenantDTO.getId(), supplier.getId());
        }
        if (!CollectionUtils.isEmpty(param.getStallIds())) {
            this.stallService.saveSupplierStall(param.getStallIds(), supplier.getId());
        }
        return supplier;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSupplierAdminUser(@NonNull Long adminUserId, @NonNull String adminUserName) {
        LambdaUpdateWrapper<Supplier> updateWrapper = Wrappers.<Supplier>lambdaUpdate()
                .set(Supplier::getAdminUserName, adminUserName)
                .eq(Supplier::getAdminUserId, adminUserId);
        this.update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSupplier(@NonNull Long id, @NonNull SupplierModifyParam param) {
        Supplier supplier = this.baseMapper.selectById(id);
        Assert.isNotFound(supplier, "不存在该批发商资料信息!");

        // 获取当前管理员的用户信息
        UserDTO userDTO = this.userClient.getUserByMobile(param.getAdminPhone());
        Assert.isNotFound(userDTO, "不存在管理员用户信息!");

        // 修改租户信息
        TenantModifyParam tenantModifyParam = new TenantModifyParam();
        tenantModifyParam.setUserAdminId(userDTO.getId());
        tenantModifyParam.setName(param.getName());
        tenantModifyParam.setCategory(TenantCategory.SUPPLIER);
        TenantDTO tenantDTO = this.tenantClient.updateTenant(supplier.getSupplierTenant(), tenantModifyParam);

        // 更新批发商信息
        if (!CollectionUtils.isEmpty(param.getAreaIds())) {
            supplier.setArea(String.join("-", param.getAreaIds()));
        }
        if (!CollectionUtils.isEmpty(param.getCategoryIds())) {
            supplier.setCategory(String.join("-", param.getCategoryIds()));
        }
        Integer riskAmount = supplier.getRiskAmount();
        ObjectUtils.copying(param, supplier);
        supplier.setRiskAmount(riskAmount);
        supplier.setAdminUserName(userDTO.getName());
        this.baseMapper.updateById(supplier);
        this.stallService.removeSupplierStall(supplier.getId());
        if (!CollectionUtils.isEmpty(param.getStallIds())) {
            this.stallService.saveSupplierStall(param.getStallIds(), supplier.getId());
        }
        // 修改logo图片
        if (Objects.nonNull(param.getFileId())) {
            // 删除原有logo
            this.fileRefClient.removeByBusinessKeyAndScene(supplier.getId(), SUPPLIER_INFO);
            this.fileRefClient.removeByBusinessKeyAndScene(tenantDTO.getId(), TENANT_LOGO);
            // 新增logo信息
            this.saveFileInfo(param.getFileId(), tenantDTO.getId(), supplier.getId());
        }

        //数据同步
        SupplierMessage copying = ObjectUtils.copying(supplier, SupplierMessage.class);
        RocketMQContextHolder.send(SupplierMessage.TOPIC, copying);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateInnerSupplier(@NonNull Long id, @NonNull SupplierModifyParam param) {
        Supplier supplier = this.getById(id);
        Assert.isNotFound(supplier, "不存在批发商信息!");
        supplier.setName(param.getName());
        if (StringUtils.isNotBlank(param.getPhone())) {
            supplier.setPhone(param.getPhone());
        }
        if (StringUtils.isNotBlank(param.getAdminPhone())) {
            supplier.setAdminPhone(param.getAdminPhone());
        }
        // 判断当前批发商的风险级别
        supplier.setRiskAmount(param.getRiskAmount());
        supplier.setRiskLevel(param.getRiskLevel());
        this.baseMapper.updateById(supplier);
    }

    @Override
    public Supplier getById(@NonNull Serializable id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public Supplier getByName(@NonNull Serializable name) {
        return this.lambdaQuery().eq(Supplier::getName, name).one();
    }

    @Override
    public Page<Supplier> pageSupplier(@NonNull SupplierQueryParam queryParam) {
        return this.baseMapper.pageSupplier(PagingUtils.convert(queryParam), queryParam);
    }

    @Override
    public Page<UserDTO> getCustomers(@NonNull SupplierQueryParam queryParam) {
        return this.baseMapper.getCustomers(PagingUtils.convert(queryParam), queryParam);
    }

    @Override
    public Page<SupplierVO> getCustomersV2(SupplierQueryParam queryParam) {
        Page<SupplierVO> customersV2 = this.baseMapper.getCustomersV2(PagingUtils.convert(queryParam), queryParam);
        Optional.ofNullable(customersV2.getRecords()).orElse(Collections.emptyList()).stream().forEach(c -> {
            c.setLiaison(StringUtils.isNotEmpty(c.getLiaison()) ? c.getLiaison() : c.getAdminName());
            c.setPhone(StringUtils.isNotEmpty(c.getPhone()) ? c.getPhone() : c.getAdminPhone());
            c.setUseForSelect(c.getAdminUserId() + "_" + c.getId());
        });
        return customersV2;
    }

    @Override
    public List<SupplierVO> listCustomersV2(SupplierQueryParam queryParam) {
        List<SupplierVO> customersV2 = this.baseMapper.getCustomersV2(queryParam);
        Optional.ofNullable(customersV2).orElse(Collections.emptyList()).stream().forEach(c -> {
            c.setLiaison(StringUtils.isNotEmpty(c.getLiaison()) ? c.getLiaison() : c.getAdminName());
            c.setPhone(StringUtils.isNotEmpty(c.getPhone()) ? c.getPhone() : c.getAdminPhone());
            c.setUseForSelect(c.getAdminUserId() + "_" + c.getId());
        });
        return customersV2;
    }

    @Override
    public List<Supplier> listMySupplierTenants(@NonNull Long adminUserId) {
        return this.baseMapper.selectList(Wrappers.<Supplier>lambdaQuery().eq(Supplier::getAdminUserId, adminUserId));
    }

    @Override
    public List<Supplier> listSuppliersTenants() {
        return this.lambdaQuery().eq(Supplier::getAdminUserId, SecurityContextHolder.getPrincipal().getId()).list();
    }

    @Override
    public List<Supplier> listSuppliersByUserAdminId(@NonNull Long userAdminId, @NonNull Integer tenant) {
        return this.lambdaQuery().eq(Supplier::getAdminUserId, userAdminId).eq(Supplier::getTenant, tenant).list();
    }

    @Override
    public List<Supplier> listSupplier(@NonNull String keyword, @NonNull Integer tenant) {
        return this.lambdaQuery()
                .and(StringUtils.isNotEmpty(keyword), q -> q
                        .like(Supplier::getName, keyword)
                        .or()
                        .like(Supplier::getCode, keyword)
                        .or()
                        .like(Supplier::getLiaison, keyword)
                        .or()
                        .like(Supplier::getPhone, keyword))
                .eq(Supplier::getTenant, tenant)
                .list();
    }

    @Override
    public List<Supplier> listOutSupplier(String keyword, Integer tenant) {
        return this.lambdaQuery()
                .and(StringUtils.isNotEmpty(keyword), q -> q
                        .like(Supplier::getName, keyword)
                        .or()
                        .like(Supplier::getCode, keyword)
                        .or()
                        .like(Supplier::getLiaison, keyword)
                        .or()
                        .like(Supplier::getPhone, keyword))
                .eq(Supplier::getTenant, tenant)
                .list();
    }

    @Override
    public List<Supplier> listByUserIds(@NonNull Collection<? extends Serializable> userIds, @NonNull Integer tenant) {
        return CollectionUtils.isEmpty(userIds) ? Collections.emptyList() :
               this.baseMapper.selectList(Wrappers.<Supplier>lambdaQuery()
                                                  .in(Supplier::getAdminUserId, userIds)
                                                  .eq(Supplier::getTenant, tenant));
    }

    @Override
    public Supplier getByAdminPhone(@NonNull String adminPhone) {
        return this.baseMapper.selectOne(Wrappers.<Supplier>lambdaQuery().eq(Supplier::getAdminPhone, adminPhone));
    }

    @Override
    public Supplier getBySupplierTenant(@NonNull Integer supplierTenant) {
        return this.baseMapper.selectOne(Wrappers.<Supplier>lambdaQuery().eq(Supplier::getSupplierTenant, supplierTenant));
    }

    @Override
    public List<Supplier> getByMidType(@NonNull String midType, @NonNull String midValue, @NonNull Integer tenant) {
        return this.lambdaQuery()
                .eq(Objects.equals("merchantId", midType), Supplier::getId, midValue)
                .eq(Objects.equals("phone", midType), Supplier::getCode, midValue)
                .list();
    }

    @Override
    public Page<Supplier> pageSuppliers(@NonNull SupplierQueryParam queryParam) {
        return this.lambdaQuery()
                .eq(Supplier::getTenant, queryParam.getTenant())
                .like(StringUtils.isNotEmpty(queryParam.getKeyword()), Supplier::getName, queryParam.getKeyword())
                .in(Objects.nonNull(queryParam.getStatus()) &&
                    !CollectionUtils.isEmpty(queryParam.getIds()), Supplier::getId, queryParam.getIds())
                .in(Objects.nonNull(queryParam.getType()) &&
                    !CollectionUtils.isEmpty(queryParam.getIds()), Supplier::getId, queryParam.getIds())
                .orderByDesc(BasicEntity::getUpdateTime)
                .page(PagingUtils.convert(queryParam));
    }

    private void saveFileInfo(Long fileId, Long tenantId, Long supplierId) {
        FileRefCreateParam supllierLogo = new FileRefCreateParam();
        supllierLogo.setBusinessKey(supplierId);
        supllierLogo.setSceneCode(SUPPLIER_INFO);
        supllierLogo.setFileIdList(Collections.singletonList(fileId));

        FileRefCreateParam tenantLogo = new FileRefCreateParam();
        tenantLogo.setBusinessKey(tenantId);
        tenantLogo.setSceneCode(TENANT_LOGO);
        tenantLogo.setFileIdList(Collections.singletonList(fileId));

        List<FileRefCreateParam> saveBatch = Stream.of(supllierLogo, tenantLogo).collect(Collectors.toList());
        this.fileRefClient.saveBatch(saveBatch);
    }

    @Override
    public List<Supplier> listByIds(@NonNull Collection<? extends Serializable> ids) {
        return CollectionUtils.isEmpty(ids) ? Collections.emptyList() :
               this.baseMapper.selectBatchIds(ids);
    }

    private void addSupplierUser(Supplier supplier, UserDTO user, SupplierUserEnum supplierUserCategory) {
        // 新增supplier-user关联关系
        SupplierUser supplierUser = new SupplierUser();
        supplierUser.setSupplierId(supplier.getId());
        supplierUser.setUserId(supplier.getAdminUserId());
        supplierUser.setSupplierCategoryName(user.getName());
        supplierUser.setSupplierCategoryPhone(user.getMobile());
        supplierUser.setSupplierCategory(supplierUserCategory);
        supplierUser.setAuthWalletPay(true);
        supplierUser.setAuthPurchaseReport(true);
        this.supplierUserMapper.insert(supplierUser);
    }

    @Override
    public Page<SupplierPageVO> pageSupplierDetail(SupplierQueryParam param) {
        Page<SupplierPageVO> page = this.supplierMapper.pageSupplierDetail(PagingUtils.convert(param), Wrappers.query()
                .and(com.guhecloud.platform.base.util.StringUtils.isNotBlank(param.getKeyword()), q -> q
                        .like("s.name", param.getKeyword())
                        .or().like("s.liaison", param.getKeyword()))
                .eq(param.getStatus() != null &&
                    !param.getStatus().name().equals(PurchaserAuthenticationEnum.UNAUTHORIZED.name()), "sa.status", param.getStatus())
                .isNull(param.getStatus() != null &&
                        param.getStatus().name().equals(PurchaserAuthenticationEnum.UNAUTHORIZED.name()), "sa.status")
                .eq(param.getType() != null, "sa.type", param.getType())
                .eq("s.tenant", param.getTenant()));
        Optional.ofNullable(page.getRecords()).orElse(Collections.emptyList()).stream().forEach(s -> {
            s.setStatus(s.getStatus() != null ? s.getStatus() : SupplierAuthenticatedEnum.UNAUTHORIZED);
        });
        return page;
    }

}
