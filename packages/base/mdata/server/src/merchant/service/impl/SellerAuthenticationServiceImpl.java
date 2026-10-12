package com.guhecloud.cube.biz.mdata.server.merchant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.mdata.server.merchant.dao.SellerAuthenticationMapper;
import com.guhecloud.cube.biz.mdata.server.merchant.entity.Merchant;
import com.guhecloud.cube.biz.mdata.server.merchant.entity.MerchantUser;
import com.guhecloud.cube.biz.mdata.server.merchant.entity.SellerAuthentication;
import com.guhecloud.cube.biz.mdata.server.merchant.enumeration.MerchantEnum;
import com.guhecloud.cube.biz.mdata.server.merchant.param.SellerAuthenticationCreateParam;
import com.guhecloud.cube.biz.mdata.server.merchant.service.MerchantService;
import com.guhecloud.cube.biz.mdata.server.merchant.service.MerchantUserService;
import com.guhecloud.cube.biz.mdata.server.merchant.service.SellerAuthenticationService;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SellerAuthenticationServiceImpl extends ServiceImpl<SellerAuthenticationMapper, SellerAuthentication> implements SellerAuthenticationService {

    private final MerchantService merchantService;
    private final MerchantUserService merchantUserService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(@NonNull SellerAuthenticationCreateParam param) {
        // 获取当前用户
        Principal principal = SecurityContextHolder.getPrincipal();
        List<MerchantUser> merchantUsers = this.merchantUserService.listMerchantUsers(principal.getId());
        Assert.isNotFound(CollectionUtils.isEmpty(merchantUsers), "不存在商户资料信息");
        List<Long> merchantIds = merchantUsers.stream().map(MerchantUser::getMerchantId).collect(Collectors.toList());
        List<Merchant> merchants = this.merchantService.getByIds(merchantIds);
        Merchant merchant = merchants.getFirst();
        // 判断是否有通过或不通过数据
        List<SellerAuthentication> sellerAuthentications = this.getListByMerchantId(merchant.getId());
        if (!CollectionUtils.isEmpty(sellerAuthentications)) {
            List<Long> sellerAuthenticationIds = sellerAuthentications.stream().map(SellerAuthentication::getId)
                    .collect(Collectors.toList());
            this.lambdaUpdate()
                    .set(SellerAuthentication::getStatus, MerchantEnum.AUTH_INVALID)
                    .in(SellerAuthentication::getId, sellerAuthenticationIds)
                    .update();
        }
        // 构建卖家认证信息
        SellerAuthentication sellerAuthentication = ObjectUtils.copying(param, SellerAuthentication.class);
        sellerAuthentication.setMerchantId(merchant.getId());
        sellerAuthentication.setStatus(MerchantEnum.AUTH_AUDIT);
        sellerAuthentication.setGoodsCategory(String.join(",", param.getGoodsCategoryIds()));
        sellerAuthentication.setSubmitTime(DateUtils.now());
        this.save(sellerAuthentication);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sellerReset() {
        Principal principal = SecurityContextHolder.getPrincipal();
        Merchant merchant = merchantService.getByUserId(principal.getId());
        Assert.isNotFound(merchant, "不存在商户信息");
        SellerAuthentication sellerAuthentication = this.getByMerchantId(merchant.getId());
        sellerAuthentication.setStatus(MerchantEnum.AUTH_INVALID);
        merchant.setSeller(false);

        this.updateById(sellerAuthentication);
        this.merchantService.updateById(merchant);
    }

    @Override
    public SellerAuthentication getByMerchantId(@NonNull Long merchantId) {
        return this.lambdaQuery()
                .eq(SellerAuthentication::getMerchantId, merchantId)
                .ne(SellerAuthentication::getStatus, MerchantEnum.AUTH_INVALID)
                .one();
    }

    @Override
    public List<SellerAuthentication> getByMerchantIds(@NonNull List<Long> merchantIds) {
        return CollectionUtils.isEmpty(merchantIds) ? Collections.emptyList() : this.lambdaQuery()
                .in(SellerAuthentication::getMerchantId, merchantIds)
                .ne(SellerAuthentication::getStatus, MerchantEnum.AUTH_INVALID)
                .list();
    }

    private List<SellerAuthentication> getListByMerchantId(@NonNull Long merchantId) {
        return this.lambdaQuery()
                .eq(SellerAuthentication::getMerchantId, merchantId)
                .and(q -> q
                        .eq(SellerAuthentication::getStatus, MerchantEnum.AUTH_PASSED)
                        .or()
                        .eq(SellerAuthentication::getStatus, MerchantEnum.AUTH_FAILED)
                        .or()
                        .eq(SellerAuthentication::getStatus, MerchantEnum.AUTH_AUDIT)

                    )
                .list();
    }
}
