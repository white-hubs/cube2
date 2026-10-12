package com.guhecloud.cube.biz.mdata.server.purchaser.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.mdata.server.purchaser.dao.PurchaserAuthenticationMapper;
import com.guhecloud.cube.biz.mdata.server.purchaser.dao.PurchaserMapper;
import com.guhecloud.cube.biz.mdata.server.purchaser.entity.Purchaser;
import com.guhecloud.cube.biz.mdata.server.purchaser.entity.PurchaserAuthentication;
import com.guhecloud.cube.biz.mdata.server.purchaser.enumeration.AuthenticationSourceEnum;
import com.guhecloud.cube.biz.mdata.server.purchaser.enumeration.PurchaserAuthenticationEnum;
import com.guhecloud.cube.biz.mdata.server.purchaser.param.PurchaserAuthenticationModifyParam;
import com.guhecloud.cube.biz.mdata.server.purchaser.param.PurchaserAuthenticationQueryParam;
import com.guhecloud.cube.biz.mdata.server.purchaser.service.PurchaserAuthenticationService;
import com.guhecloud.cube.biz.mdata.server.supplier.enumeration.SupplierAuthenticatedEnum;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.entity.BasicEntity;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @author lzy
 * @date 2021/12/24
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PurchaserAuthenticationServiceImpl extends ServiceImpl<PurchaserAuthenticationMapper, PurchaserAuthentication> implements PurchaserAuthenticationService {

    private final PurchaserMapper purchaserMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addPurchaserAuthentication(@NonNull PurchaserAuthenticationModifyParam param) {
        // 新增采购商信息
        Long userId = SecurityContextHolder.getPrincipal().getId();
        Purchaser purchaser = this.purchaserMapper.selectOne(Wrappers.<Purchaser>lambdaQuery().eq(Purchaser::getUserId, userId)
                                                                     .eq(Purchaser::getTenant, param.getTenant()));
        if (purchaser == null) {
            purchaser = new Purchaser();
            purchaser.setTenant(param.getTenant());
            purchaser.setUserId(SecurityContextHolder.getPrincipal().getId());
            this.purchaserMapper.insert(purchaser);
        }
        param.setStatus(PurchaserAuthenticationEnum.AUDITED);
        param.setSource(AuthenticationSourceEnum.MP);
        this.addPurchaserAuthentication(purchaser.getId(), param);
    }

    private void addPurchaserAuthentication(Long purchaserId, PurchaserAuthenticationModifyParam param) {
        // 查询当前采购商认证信息
        PurchaserAuthentication pa = this.baseMapper.selectOne(Wrappers.<PurchaserAuthentication>lambdaQuery()
                                                                       .eq(PurchaserAuthentication::getPurchaserId, purchaserId)
                                                                       .eq(PurchaserAuthentication::getTenant, param.getTenant())
                                                                       .ne(PurchaserAuthentication::getStatus, PurchaserAuthenticationEnum.INVALID));
        if (Objects.nonNull(pa)) {
            pa.setStatus(PurchaserAuthenticationEnum.INVALID);
            this.baseMapper.updateById(pa);
        }
        PurchaserAuthentication purchaserAuthentication = ObjectUtils.copying(param, PurchaserAuthentication.class);
        purchaserAuthentication.setPurchaserId(purchaserId);
        purchaserAuthentication.setStatus(param.getStatus());
        purchaserAuthentication.setSubmitTime(DateUtils.now());
        this.baseMapper.insert(purchaserAuthentication);
    }

    @Override
    public void addPurchaserAuthenticationForPC(Long purchaserId, PurchaserAuthenticationModifyParam param) {
        Purchaser purchaser = this.purchaserMapper.selectOne(Wrappers.<Purchaser>lambdaQuery().eq(Purchaser::getId, purchaserId)
                                                                     .eq(Purchaser::getTenant, param.getTenant()));
        Assert.isNotFound(purchaser, "采购商信息不存在，无法完成认证");
        param.setStatus(PurchaserAuthenticationEnum.CERTIFIED);
        param.setSource(AuthenticationSourceEnum.PC);
        this.addPurchaserAuthentication(purchaser.getId(), param);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditPurchaserAuthentication(@NonNull Long id, @NonNull PurchaserAuthenticationEnum status, String remark) {
        Purchaser purchaser = this.purchaserMapper.selectById(id);
        Assert.isNotFound(purchaser, "采购商信息不存在");

        PurchaserAuthentication purchaserAuthentication = this.getByPurchaser(purchaser.getId(), purchaser.getTenant());
        Assert.isNotFound(purchaserAuthentication, "不存在采购商认证信息!");
        if (PurchaserAuthenticationEnum.FAILED == status) {
            purchaserAuthentication.setRemark(remark);
        }
        purchaserAuthentication.setStatus(status);
        purchaserAuthentication.setAuditTime(DateUtils.now());
        this.baseMapper.updateById(purchaserAuthentication);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPurchaserAuthentication(@NonNull Long id) {
        PurchaserAuthentication purchaserAuthentication = this.baseMapper.selectById(id);
        Assert.isNotFound(purchaserAuthentication, "不存在采购商认证信息!");
        purchaserAuthentication.setStatus(PurchaserAuthenticationEnum.INVALID);
        this.baseMapper.updateById(purchaserAuthentication);
    }

    @Override
    public PurchaserAuthentication getByPurchaser(@NonNull Long purchaserId, @NonNull Integer tenant) {
        return this.baseMapper.selectOne(Wrappers.<PurchaserAuthentication>lambdaQuery()
                                                 .eq(PurchaserAuthentication::getTenant, tenant)
                                                 .eq(PurchaserAuthentication::getPurchaserId, purchaserId)
                                                 .ne(PurchaserAuthentication::getStatus, PurchaserAuthenticationEnum.INVALID));
    }

    @Override
    public PurchaserAuthentication getById(@NonNull Serializable id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public List<PurchaserAuthentication> getLastByPurchaser(@NonNull Long purchaserId, @NonNull Integer tenant) {
        return this.baseMapper.selectList(Wrappers.<PurchaserAuthentication>lambdaQuery()
                                                  .eq(PurchaserAuthentication::getStatus, SupplierAuthenticatedEnum.INVALID)
                                                  .eq(PurchaserAuthentication::getPurchaserId, purchaserId)
                                                  .eq(PurchaserAuthentication::getTenant, tenant)
                                                  .orderByDesc(BasicEntity::getCreateTime));
    }

    @Override
    public List<PurchaserAuthentication> listByTenant(@NonNull Integer tenant) {
        return this.lambdaQuery()
                .eq(PurchaserAuthentication::getTenant, tenant)
                .eq(PurchaserAuthentication::getStatus, PurchaserAuthenticationEnum.CERTIFIED)
                .ne(PurchaserAuthentication::getStatus, PurchaserAuthenticationEnum.INVALID)
                .list();
    }

    @Override
    public Page<PurchaserAuthentication> pageAuthentication(@NonNull PurchaserAuthenticationQueryParam queryParam) {
        return this.lambdaQuery()
                .eq(Objects.nonNull(queryParam.getStatus()), PurchaserAuthentication::getStatus, queryParam.getStatus())
                .eq(Objects.nonNull(queryParam.getType()), PurchaserAuthentication::getType, queryParam.getType())
                .eq(PurchaserAuthentication::getTenant, queryParam.getTenant())
                .ne(PurchaserAuthentication::getStatus, PurchaserAuthenticationEnum.INVALID)
                .and(StringUtils.isNotEmpty(queryParam.getKeyword()), q -> q
                        .like(PurchaserAuthentication::getTypeName, queryParam.getKeyword())
                    ).page(PagingUtils.convert(queryParam));
    }
}
