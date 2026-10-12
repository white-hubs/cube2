package com.guhecloud.cube.biz.mdata.server.merchant.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.biz.mdata.server.merchant.dao.MerchantMapper;
import com.guhecloud.cube.biz.mdata.server.merchant.dto.MerchantDTO;
import com.guhecloud.cube.biz.mdata.server.merchant.entity.Merchant;
import com.guhecloud.cube.biz.mdata.server.merchant.entity.MerchantUser;
import com.guhecloud.cube.biz.mdata.server.merchant.enumeration.MerchantEnum;
import com.guhecloud.cube.biz.mdata.server.merchant.enumeration.MerchantUserEnum;
import com.guhecloud.cube.biz.mdata.server.merchant.enumeration.RisklevelEnum;
import com.guhecloud.cube.biz.mdata.server.merchant.param.MerchantCreateParam;
import com.guhecloud.cube.biz.mdata.server.merchant.param.MerchantQueryParam;
import com.guhecloud.cube.biz.mdata.server.merchant.param.MerchantStatQueryParam;
import com.guhecloud.cube.biz.mdata.server.merchant.param.MerchantUpdateParam;
import com.guhecloud.cube.biz.mdata.server.merchant.service.MerchantService;
import com.guhecloud.cube.biz.mdata.server.merchant.service.MerchantUserService;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.UserTenantClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MerchantServiceImpl extends ServiceImpl<MerchantMapper, Merchant> implements MerchantService {

    private final UserTenantClient userTenantClient;
    private final FileRefClient fileRefClient;
    private final UserClient userClient;
    private final MerchantUserService merchantUserService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(@NotNull MerchantCreateParam param) {
        // 获取当前用户信息
        Principal principal = SecurityContextHolder.getPrincipal();
        // 判断是否为重新认证，若存在商户资料，则为重新认证的信息，否则为第一次加入的商户
        Merchant merchant = this.getById(param.getId());
        if (merchant == null) {
            this.saveMerchant(param, principal.getId());
        } else {
            Long id = merchant.getId();
            ObjectUtils.copying(param, merchant);
            merchant.setId(id);
            // 构建区域、品类信息
            merchant.setArea(String.join(",", param.getAreaIds()));
            merchant.setRiskLevel(RisklevelEnum.RISK_LEVEL_LOW);
            this.updateById(merchant);
        }
    }

    private void saveMerchant(MerchantCreateParam param, Long userId) {
        UserDTO userDTO = this.userClient.getUser(userId);
        Merchant merchant = ObjectUtils.copying(param, Merchant.class);
        // 构建商户信息
        merchant.setId(IdWorker.getId());
        merchant.setStatus(MerchantEnum.AUTH_UNAUTHORIZED);
        merchant.setCode(Objects.isNull(userDTO) ? merchant.getPhone() : userDTO.getMobile());
        merchant.setOwner(userId);
        merchant.setUserId(userId);
        // 构建区域、品类信息
        merchant.setArea(String.join(",", param.getAreaIds()));
        merchant.setRiskLevel(RisklevelEnum.RISK_LEVEL_LOW);
        this.save(merchant);
        // 构建用户租户绑定关系
        // 构建商户用户绑定关系
        MerchantUser merchantUser = MerchantUser.builder()
                .merchantId(merchant.getId()).userId(userId)
                .merchantCategory(MerchantUserEnum.ADMIN)
                .merchantCategoryName(userDTO.getName())
                .merchantCategoryPhone(userDTO.getMobile())
                .build();
        this.merchantUserService.save(merchantUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@NonNull MerchantUpdateParam param) {
        // 商户信息编辑
        Merchant merchant = this.getById(param.getId());
        Assert.isNotFound(merchant, "不存在商户资料信息");
        ObjectUtils.copying(param, merchant);
        merchant.setArea(String.join(",", param.getAreaIds()));
        this.baseMapper.updateById(merchant);
        if (Objects.nonNull(param.getFileId())) {
            // 删除相关文件
            this.fileRefClient.removeByBusinessKey(param.getId());
            // 新增文件
            this.fileRefClient.save(param.getId(), param.getFileId());
        }
    }

    @Override
    public Page<Merchant> getMerchantsByBiz(@NonNull MerchantStatQueryParam queryParam) {
        return this.lambdaQuery()
                .and(StringUtils.isNotEmpty(queryParam.getKeyword()), q -> q
                        .like(Merchant::getName, queryParam.getKeyword())
                        .or()
                        .like(Merchant::getCode, queryParam.getKeyword())
                    )
                .eq(Objects.nonNull(queryParam.getSeller()), Merchant::getSeller, queryParam.getSeller())
                .eq(Objects.nonNull(queryParam.getBuyer()), Merchant::getBuyer, queryParam.getBuyer())
                .page(PagingUtils.convert(queryParam));
    }

    @Override
    public Merchant getById(@NotNull Long id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public List<Merchant> getByIds(@NonNull List<Long> ids) {
        return CollectionUtils.isEmpty(ids) ? Collections.emptyList() :
               this.lambdaQuery().in(Merchant::getId, ids).list();
    }

    @Override
    public Merchant getByUserId(@NonNull Long userId) {
        return this.lambdaQuery().eq(Merchant::getUserId, userId).one();
    }

    @Override
    public List<Merchant> listByUserId(@NonNull Long userId) {
        List<MerchantUser> merchantUsers = this.merchantUserService.listMerchantUsers(userId);
        return CollectionUtils.isEmpty(merchantUsers) ? Collections.emptyList() :
               this.lambdaQuery().in(Merchant::getId,
                                     merchantUsers.stream().map(MerchantUser::getMerchantId).collect(Collectors.toList()))
                       .list();
    }

    @Override
    public Page<MerchantDTO> page(@NonNull MerchantQueryParam queryParam) {
        return this.baseMapper.page(PagingUtils.convert(queryParam), queryParam);
    }

    @Override
    public List<Merchant> listMerchantByIds(Collection<Long> merchantIds) {
        return CollectionUtils.isEmpty(merchantIds) ? Collections.emptyList() :
               this.lambdaQuery().in(Merchant::getId, merchantIds).list();
    }

    @Override
    public Page<Merchant> getBuyer(String keyword, Paging paging) {
        return this.lambdaQuery()
                .eq(Merchant::getBuyer, true)
                .and(StringUtils.isNotEmpty(keyword), q -> q
                        .like(Merchant::getName, keyword)
                        .or()
                        .like(Merchant::getCode, keyword)
                    )
                .page(PagingUtils.convert(paging));
    }

    @Override
    public List<Merchant> lookupMerchants(@NonNull String keyword) {
        return this.baseMapper.lookupMerchants(keyword);
    }

    @Override
    public List<Merchant> listUserMerchants(@NonNull Collection<Long> userIds) {
        return userIds.isEmpty() ? Collections.emptyList() : this.lambdaQuery().in(Merchant::getUserId, userIds).list();
    }

    @Override
    public List<Long> getMerchantIds(@NonNull MerchantQueryParam queryParam) {
        List<Merchant> list = this.lambdaQuery()
                .and(StringUtils.isNotEmpty(queryParam.getKeyword()), q -> q
                        .like(Merchant::getCode, queryParam.getKeyword())
                        .or()
                        .like(Merchant::getName, queryParam.getKeyword())
                        .or()
                        .like(Merchant::getPhone, queryParam.getKeyword())
                    )
                .list();
        List<Long> merchantIds = list.stream().map(Merchant::getId).collect(Collectors.toList());
        return merchantIds;
    }

    @Override
    public Merchant getByMidType(String midType, String midValue) {
        return this.lambdaQuery()
                .eq("merchantId".equals(midType), Merchant::getCode, midValue)
                .eq("phone".equals(midType), Merchant::getCode, midValue)
                .one();
    }

    private void fileUpload(List<Long> fileIds, Long businessKey) {
        fileIds.forEach(fileId -> {
            fileRefClient.save(businessKey, fileId);
        });
    }

    @Override
    public Merchant getActiveMerchant(@NonNull Long id) {
        Merchant merchant = this.getById(id);
        Assert.isNotFound(merchant, "商户不存在");
        Assert.isNotFound(merchant.getUserId(), "用户不存在");
        return merchant;
    }

    @Override
    public Merchant getUserActiveMerchant(@NonNull Long userId) {
        Merchant merchant = this.getByUserId(userId);
        Assert.isNotFound(merchant, "商户不存在");
        return merchant;
    }

    @Override
    public Page<Merchant> lookupMerchantPage(MerchantQueryParam queryParam) {
        return this.baseMapper.lookupMerchantPage(PagingUtils.convert(queryParam), queryParam.getKeyword(), queryParam.getMerchantIds());
    }
}
