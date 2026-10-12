package com.guhecloud.cube.biz.mdata.server.merchant.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.param.FileRefCreateParam;
import com.guhecloud.cube.biz.mdata.server.merchant.dao.AuthenticationMapper;
import com.guhecloud.cube.biz.mdata.server.merchant.dto.AuthenticationDTO;
import com.guhecloud.cube.biz.mdata.server.merchant.entity.Authentication;
import com.guhecloud.cube.biz.mdata.server.merchant.entity.Merchant;
import com.guhecloud.cube.biz.mdata.server.merchant.entity.MerchantUser;
import com.guhecloud.cube.biz.mdata.server.merchant.entity.SellerAuthentication;
import com.guhecloud.cube.biz.mdata.server.merchant.enumeration.MerchantEnum;
import com.guhecloud.cube.biz.mdata.server.merchant.param.AuthenticationCreateParam;
import com.guhecloud.cube.biz.mdata.server.merchant.param.AuthenticationQueryParam;
import com.guhecloud.cube.biz.mdata.server.merchant.param.AuthenticationReviewParam;
import com.guhecloud.cube.biz.mdata.server.merchant.param.AuthenticationUpdateCreate;
import com.guhecloud.cube.biz.mdata.server.merchant.service.AuthenticationService;
import com.guhecloud.cube.biz.mdata.server.merchant.service.MerchantService;
import com.guhecloud.cube.biz.mdata.server.merchant.service.MerchantUserService;
import com.guhecloud.cube.biz.mdata.server.merchant.service.SellerAuthenticationService;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticationServiceImpl extends ServiceImpl<AuthenticationMapper, Authentication> implements AuthenticationService {

    private final MerchantService merchantService;
    private final SellerAuthenticationService sellerAuthenticationService;
    private final MerchantUserService merchantUserService;
    private final FileRefClient fileRefClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(@NonNull AuthenticationCreateParam param) {
        // 获取当前用户信息
        Principal principal = SecurityContextHolder.getPrincipal();
        // 商户资料认证状态修改为待审核
        List<MerchantUser> merchantUsers = this.merchantUserService.listMerchantUsers(principal.getId());
        Assert.isNotFound(CollectionUtils.isEmpty(merchantUsers), "不存在商户资料信息");
        List<Long> merchantIds = merchantUsers.stream().map(MerchantUser::getMerchantId).collect(Collectors.toList());

        List<Merchant> merchants = this.merchantService.getByIds(merchantIds);
        Assert.isNotFound(CollectionUtils.isEmpty(merchants), "不存在商户资料信息!");
        Merchant merchant = merchants.getFirst();
        merchant.setStatus(MerchantEnum.AUTH_AUDIT);
        this.merchantService.updateById(merchant);

        // 判断是否有通过或不通过数据
        List<Authentication> authentications = this.getListByMerchantId(merchant.getId());
        if (!CollectionUtils.isEmpty(authentications)) {
            List<Long> authenticationIds = authentications.stream().map(Authentication::getId).collect(Collectors.toList());
            this.lambdaUpdate()
                    .set(Authentication::getStatus, MerchantEnum.AUTH_INVALID)
                    .in(Authentication::getId, authenticationIds)
                    .update();
        }
        // 商户认证状态修改为待审核
        Authentication authentication = ObjectUtils.copying(param, Authentication.class);
        authentication.setMerchantId(merchant.getId());
        authentication.setStatus(MerchantEnum.AUTH_AUDIT);
        authentication.setSubmitTime(DateUtils.now());
        this.save(authentication);
        // 身份证正反面图片上传，可能会有营业执照上传
        this.fileUpload(param.getFileIds(), authentication.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@NonNull AuthenticationUpdateCreate param) {
        Principal principal = SecurityContextHolder.getPrincipal();
        // 获取商户信息
        List<MerchantUser> merchantUsers = this.merchantUserService.listMerchantUsers(principal.getId());
        Assert.isNotFound(CollectionUtils.isEmpty(merchantUsers), "不存在商户资料信息");
        List<Merchant> merchants = this.merchantService.getByIds(merchantUsers.stream().map(MerchantUser::getMerchantId)
                                                                         .collect(Collectors.toList()));
        merchants = merchants.stream().filter(m -> m.getUserId().equals(principal.getId())).collect(Collectors.toList());
        Assert.isNotFound(CollectionUtils.isEmpty(merchants), "不存在商户资料信息");
        Merchant merchant = merchants.getFirst();
        // 获取认证信息
        Authentication authentication = this.getByMerchantId(param.getId());
        // 重新认证
        if (MerchantEnum.AUTH_INVALID.equals(param.getAuditStatus())) {
            merchant.setBuyer(false);
            merchant.setSeller(false);
            authentication.setStatus(MerchantEnum.AUTH_INVALID);
            // 卖家认证变为失效
            SellerAuthentication sellerAuthentication = sellerAuthenticationService.getByMerchantId(merchant.getId());
            if (Objects.nonNull(sellerAuthentication)) {
                sellerAuthentication.setStatus(MerchantEnum.AUTH_INVALID);
                this.sellerAuthenticationService.updateById(sellerAuthentication);
            }
            // 商户变为未认证
            merchant.setStatus(MerchantEnum.AUTH_UNAUTHORIZED);
        }
        this.baseMapper.updateById(authentication);
        merchantService.updateById(merchant);
    }

    @Override
    public Authentication getById(@NonNull Long id) {
        Authentication authentication = this.baseMapper.selectById(id);
        Assert.isNotFound(authentication, "不存在商户认证信息");
        return authentication;
    }

    @Override
    public Authentication getByUserId(@NonNull Long userId) {
        Merchant merchant = merchantService.getByUserId(userId);
        return this.getByMerchantId(merchant.getId());
    }

    @Override
    public Authentication getByMerchantId(@NonNull Long merchantId) {
        Authentication authentication = this.lambdaQuery()
                .eq(Authentication::getMerchantId, merchantId)
                .ne(Authentication::getStatus, MerchantEnum.AUTH_INVALID)
                .one();
        if (Objects.nonNull(authentication)) {
            return authentication;
        }
        List<Authentication> authentications = this.lambdaQuery()
                .eq(Authentication::getMerchantId, merchantId)
                .eq(Authentication::getStatus, MerchantEnum.AUTH_INVALID)
                .orderByDesc(Authentication::getSubmitTime)
                .list();
        return CollectionUtils.isEmpty(authentications) ? null : authentications.get(0);
    }

    @Override
    public Authentication getByMerchantIds(@NonNull List<Long> merchantIds) {
        return CollectionUtils.isEmpty(merchantIds) ? null :
               this.lambdaQuery().ne(Authentication::getStatus, MerchantEnum.AUTH_INVALID)
                       .in(Authentication::getMerchantId, merchantIds).one();
    }

    private List<Authentication> getListByMerchantId(@NonNull Long merchantId) {
        return this.lambdaQuery()
                .eq(Authentication::getMerchantId, merchantId)
                .and(q -> q
                        .eq(Authentication::getStatus, MerchantEnum.AUTH_PASSED)
                        .or()
                        .eq(Authentication::getStatus, MerchantEnum.AUTH_FAILED)

                    )
                .list();

    }

    @Override
    public Page<AuthenticationDTO> page(@NonNull AuthenticationQueryParam queryParam) {
        return this.baseMapper.page(PagingUtils.convert(queryParam), queryParam);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void review(AuthenticationReviewParam param) {
        // 获取认证信息
        Authentication authentication = this.getById(param.getId());
        Assert.isNotFound(authentication, "不存在认证信息");
        // 获取商户信息
        Merchant merchant = this.merchantService.getById(authentication.getMerchantId());
        if (MerchantEnum.AUTH_FAILED.equals(param.getAuditStatus())) {
            // 实名认证信息失败
            authentication.setReason(param.getAuditReason());
        } else if (MerchantEnum.AUTH_PASSED.equals(param.getAuditStatus())) {
            merchant.setBuyer(true);
        }
        if (Objects.nonNull(param.getAuditStatus())) {
            authentication.setStatus(param.getAuditStatus());
        }
        // 获取卖家认证信息
        SellerAuthentication sellerAuthentication = sellerAuthenticationService.getByMerchantId(merchant.getId());
        if (Objects.nonNull(sellerAuthentication)) {
            if (MerchantEnum.AUTH_FAILED.equals(param.getSellerStatus())) {
                // 卖家认证信息失败
                sellerAuthentication.setReason(param.getSellerReason());
            } else if (MerchantEnum.AUTH_PASSED.equals(param.getSellerStatus())) {
                merchant.setSeller(true);
            }
            if (Objects.nonNull(param.getSellerStatus())) {
                sellerAuthentication.setStatus(param.getSellerStatus());
            }
            sellerAuthentication.setStatus(param.getSellerStatus());
            this.sellerAuthenticationService.updateById(sellerAuthentication);
        }
        if (!MerchantEnum.AUTH_PASSED.equals(merchant.getStatus())) {
            merchant.setStatus(param.getAuditStatus());
        }
        // 认证时间
        authentication.setReviewTime(DateUtils.now());
        // 更新数据
        this.baseMapper.updateById(authentication);
        this.merchantService.updateById(merchant);

    }

    private void fileUpload(List<Long> fileIds, Long businessKey) {
        // 文件批量上传
        List<FileRefCreateParam> fileRefs = fileIds.stream().map(fileId -> {
            FileRefCreateParam param = new FileRefCreateParam();
            param.setBusinessKey(businessKey);
            param.setFileId(fileId);
            return param;
        }).collect(Collectors.toList());
        this.fileRefClient.saveBatch(fileRefs);
    }

}
