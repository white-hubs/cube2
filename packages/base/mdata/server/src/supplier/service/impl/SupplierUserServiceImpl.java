package com.guhecloud.cube.biz.mdata.server.supplier.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.mdata.server.stall.entity.Stall;
import com.guhecloud.cube.biz.mdata.server.stall.service.StallService;
import com.guhecloud.cube.biz.mdata.server.supplier.dao.SupplierMapper;
import com.guhecloud.cube.biz.mdata.server.supplier.dao.SupplierUserMapper;
import com.guhecloud.cube.biz.mdata.server.supplier.entity.Supplier;
import com.guhecloud.cube.biz.mdata.server.supplier.entity.SupplierUser;
import com.guhecloud.cube.biz.mdata.server.supplier.enumeration.SupplierUserEnum;
import com.guhecloud.cube.biz.mdata.server.supplier.param.SupplierUserCreateParam;
import com.guhecloud.cube.biz.mdata.server.supplier.param.SupplierUserQueryParam;
import com.guhecloud.cube.biz.mdata.server.supplier.param.SupplierUserUpdateParam;
import com.guhecloud.cube.biz.mdata.server.supplier.service.SupplierUserService;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.UserTenantClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.param.UserModifyParam;
import com.guhecloud.cube.biz.ouaa.metadata.enumeration.Gender;
import com.guhecloud.cube.biz.wallet.client.WalletClient;
import com.guhecloud.cube.biz.wallet.client.dto.QuickPaymentDTO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author lzy
 * @date 2021/12/14
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SupplierUserServiceImpl extends ServiceImpl<SupplierUserMapper, SupplierUser> implements SupplierUserService {

    private final StallService stallService;
    private final SupplierMapper supplierMapper;

    private final UserClient userClient;
    private final UserTenantClient userTenantClient;
    private final WalletClient walletClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(@NonNull SupplierUserCreateParam param) {
        // 判断用户是否存在
        UserModifyParam userModifyParam = new UserModifyParam();
        userModifyParam.setName(param.getSupplierCategoryName());
        userModifyParam.setMobile(param.getSupplierCategoryPhone());
        userModifyParam.setUsername(param.getSupplierCategoryPhone());
        userModifyParam.setGender(Gender.NONE);
        userModifyParam.setEnabled(true);
        UserDTO userDTO = userClient.save(userModifyParam);

        // 获取批发商信息
        Supplier supplier = this.supplierMapper.selectById(param.getSupplierId());
        Assert.isNotFound(supplier, "不存在管理员店铺信息");
        // 获取管理员信息
        SupplierUser supplierUserAdmin = this.lambdaQuery().eq(SupplierUser::getSupplierCategory, SupplierUserEnum.ADMIN)
                .eq(SupplierUser::getUserId, supplier.getAdminUserId())
                .eq(SupplierUser::getSupplierId, param.getSupplierId()).one();
        if (Boolean.TRUE.equals(param.getAuthWalletPay())) {
            // 判断管理员是否开启了免密支付
            QuickPaymentDTO quickPayment = this.walletClient.getQuickPayment(supplierUserAdmin.getUserId(), supplier.getTenant());
            Assert.isNotAcceptable(quickPayment.getEnabled() == null || !quickPayment.getEnabled(), "管理员未开通免密支付!");
        }

        // 添加相关商户角色的关联关系
        SupplierUser user = this.lambdaQuery().eq(SupplierUser::getSupplierId, param.getSupplierId())
                .eq(SupplierUser::getUserId, userDTO.getId())
                .eq(SupplierUser::getSupplierCategory, param.getSupplierCategory()).one();
        Assert.isNotAcceptable(Objects.isNull(user) && param.getSupplierCategory() == SupplierUserEnum.MANAGER &&
                               Objects.equals(supplierUserAdmin.getUserId(), userDTO.getId()),
                               "不能添加管理员为经营者!");
        Assert.isConflict(Objects.nonNull(user), "已存在相同用户!");

        // 添加用户与商户的关联关系
        this.userTenantClient.addUserTenant(SessionContextHolder.getRequester().getTenant(), userDTO.getId());

        SupplierUser supplierUser = ObjectUtils.copying(param, SupplierUser.class);
        supplierUser.setUserId(userDTO.getId());
        if (!CollectionUtils.isEmpty(param.getSupplierStalls())) {
            supplierUser.setSupplierStallId(String.join("-", param.getSupplierStalls()));
            SessionContextHolder.requestTenant(supplier.getTenant());
            List<Stall> stalls = this.stallService.listStalls(param.getSupplierStalls().stream().map(Long::parseLong)
                                                                      .collect(Collectors.toList()));
            supplierUser.setSupplierStallName(stalls.stream().map(Stall::getName).collect(Collectors.joining(",")));
        }
        this.save(supplierUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@NonNull SupplierUserUpdateParam param) {
        SupplierUser supplierUser = this.getById(param.getId());
        Assert.isNotFound(supplierUser, "不存在对应信息!");
        // 获取管理员信息
        Integer supplierTenant = SessionContextHolder.getRequester().getTenant();
        Supplier supplier = this.supplierMapper.selectOne(Wrappers.<Supplier>lambdaQuery()
                                                                  .eq(Supplier::getSupplierTenant, supplierTenant));
        Assert.isNotFound(supplier, "不存在当前批发商信息!");
        if (Boolean.TRUE.equals(param.getAuthWalletPay())) {
            // 判断管理员是否开启了免密支付
            QuickPaymentDTO quickPayment = this.walletClient.getQuickPayment(supplier.getAdminUserId(), supplier.getTenant());
            Assert.isNotAcceptable(quickPayment.getEnabled() == null || !quickPayment.getEnabled(), "管理员未开通免密支付!");
        }

        // 判断新输入的手机号是否存在
        if (!supplierUser.getSupplierCategoryPhone().equals(param.getSupplierCategoryPhone())) {
            UserModifyParam userModifyParam = new UserModifyParam();
            userModifyParam.setName(param.getSupplierCategoryName());
            userModifyParam.setMobile(param.getSupplierCategoryPhone());
            userModifyParam.setUsername(param.getSupplierCategoryPhone());
            userModifyParam.setGender(Gender.NONE);
            userModifyParam.setEnabled(true);
            UserDTO userDTO = this.userClient.save(userModifyParam);

            // 删除原有关联关系
            this.userTenantClient.removeUserTenant(SessionContextHolder.getRequester().getTenant(), supplierUser.getUserId());

            // 新增关联关系
            this.userTenantClient.addUserTenant(SessionContextHolder.getRequester().getTenant(), userDTO.getId());
            supplierUser.setUserId(userDTO.getId());
        }

        ObjectUtils.copying(param, supplierUser);
        if (!CollectionUtils.isEmpty(param.getSupplierStalls())) {
            supplierUser.setSupplierStallId(String.join("-", param.getSupplierStalls()));
            SessionContextHolder.requestTenant(supplier.getTenant());
            List<Stall> stalls = this.stallService.listStalls(param.getSupplierStalls().stream().map(Long::parseLong)
                                                                      .collect(Collectors.toList()));
            supplierUser.setSupplierStallName(stalls.stream().map(Stall::getName).collect(Collectors.joining(",")));
        }
        this.updateById(supplierUser);
    }

    @Override
    public void remove(@NonNull Serializable id) {
        SupplierUser supplierUser = this.baseMapper.selectById(id);
        if (Objects.isNull(supplierUser)) {
            return;
        }
        Assert.isPermissionDenied(SupplierUserEnum.ADMIN == supplierUser.getSupplierCategory(), "不能删除管理员信息!");
        Integer supplierTenant = SessionContextHolder.getRequester().getTenant();
        Supplier supplier = this.supplierMapper.selectOne(Wrappers.<Supplier>lambdaQuery().eq(Supplier::getSupplierTenant, supplierTenant));
        Assert.isNotFound(supplier, "不存在批发商信息!");
        Assert.isPermissionDenied(!Objects.equals(supplier.getId(), supplierUser.getSupplierId()), "不能操作其他店铺信息!");

        // 删除
        this.baseMapper.deleteById(id);
    }

    @Override
    public List<SupplierUser> listSupplierUser(@NonNull Long supplierId, @NonNull Long userId) {
        return this.lambdaQuery().eq(SupplierUser::getUserId, userId).eq(SupplierUser::getSupplierId, supplierId).list();
    }

    @Override
    public List<SupplierUser> listSupplierUser(@NonNull Long userId) {
        return this.lambdaQuery().eq(SupplierUser::getUserId, userId).list();
    }

    @Override
    public Page<SupplierUser> page(@NonNull SupplierUserQueryParam queryParam) {
        return this.lambdaQuery()
                .eq(SupplierUser::getSupplierCategory, queryParam.getSupplierCategory())
                .eq(SupplierUser::getSupplierId, queryParam.getSupplierId())
                .and(StringUtils.isNotEmpty(queryParam.getKeyword()), q -> q
                        .like(SupplierUser::getSupplierCategoryName, queryParam.getKeyword())
                        .or()
                        .like(SupplierUser::getSupplierStallName, queryParam.getKeyword())
                        .or()
                        .like(SupplierUser::getSupplierCategoryPhone, queryParam.getKeyword())
                    )
                .orderByDesc(SupplierUser::getId)
                .page(PagingUtils.convert(queryParam));
    }
}
