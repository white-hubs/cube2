package com.guhecloud.cube.biz.mdata.server.supplier.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.mdata.client.dto.SupplierDTO;
import com.guhecloud.cube.biz.mdata.client.rocketmq.message.AddMallMessage;
import com.guhecloud.cube.biz.mdata.server.supplier.dao.SupplierAuthenticationMapper;
import com.guhecloud.cube.biz.mdata.server.supplier.dao.SupplierMapper;
import com.guhecloud.cube.biz.mdata.server.supplier.entity.Supplier;
import com.guhecloud.cube.biz.mdata.server.supplier.entity.SupplierAuthentication;
import com.guhecloud.cube.biz.mdata.server.supplier.enumeration.SupplierAuthenticatedEnum;
import com.guhecloud.cube.biz.mdata.server.supplier.param.SupplierAuthenticationModifyParam;
import com.guhecloud.cube.biz.mdata.server.supplier.param.SupplierAuthenticationQueryParam;
import com.guhecloud.cube.biz.mdata.server.supplier.service.SupplierAuthenticationService;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.entity.BasicEntity;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
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

/**
 * @author lzy
 * @date 2021/12/4
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SupplierAuthenticationServiceImpl extends ServiceImpl<SupplierAuthenticationMapper, SupplierAuthentication> implements SupplierAuthenticationService {

    private final SupplierMapper supplierMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addSupplierAuthentication(@NonNull SupplierAuthenticationModifyParam param) {
        // 查看当前用户是否存在认证信息
        SupplierAuthentication sa =
                this.baseMapper.selectOne(Wrappers.<SupplierAuthentication>lambdaQuery()
                                                  .eq(SupplierAuthentication::getSupplierId, param.getSupplierId())
                                                  .eq(SupplierAuthentication::getTenant, param.getTenant())
                                                  .ne(SupplierAuthentication::getStatus, SupplierAuthenticatedEnum.INVALID));
        if (Objects.nonNull(sa)) {
            // 将当前认证状态修改为已失效
            sa.setStatus(SupplierAuthenticatedEnum.INVALID);
            this.baseMapper.updateById(sa);
        }
        // 新增新的批发商认证信息
        SupplierAuthentication supplierAuthentication = ObjectUtils.copying(param, SupplierAuthentication.class);
        supplierAuthentication.setSubmitTime(DateUtils.now());
        supplierAuthentication.setStatus(param.getStatus());
        supplierAuthentication.setSource(param.getSource());
        this.baseMapper.insert(supplierAuthentication);
    }

    @Override
    public SupplierAuthentication getById(@NonNull Serializable id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public SupplierAuthentication getActiveBySupplierId(@NonNull Serializable supplierId, @NonNull Integer tenant) {
        return this.baseMapper.selectOne(Wrappers.<SupplierAuthentication>lambdaQuery()
                                                 .eq(SupplierAuthentication::getSupplierId, supplierId)
                                                 .eq(SupplierAuthentication::getTenant, tenant)
                                                 .ne(SupplierAuthentication::getStatus, SupplierAuthenticatedEnum.INVALID));
    }

    @Override
    public List<SupplierAuthentication> getLastBySupplierId(@NonNull Serializable supplierId, @NonNull Integer tenant) {
        return this.baseMapper.selectList(Wrappers.<SupplierAuthentication>lambdaQuery()
                                                  .eq(SupplierAuthentication::getStatus, SupplierAuthenticatedEnum.INVALID)
                                                  .eq(SupplierAuthentication::getSupplierId, supplierId)
                                                  .eq(SupplierAuthentication::getTenant, tenant)
                                                  .orderByDesc(BasicEntity::getCreateTime));
    }

    @Override
    public Page<SupplierAuthentication> pageSupplierAuthentication(@NonNull SupplierAuthenticationQueryParam queryParam) {
        return this.lambdaQuery()
                .ne(SupplierAuthentication::getStatus, SupplierAuthenticatedEnum.INVALID)
                .eq(Objects.nonNull(queryParam.getStatus()), SupplierAuthentication::getStatus, queryParam.getStatus())
                .eq(Objects.nonNull(queryParam.getType()), SupplierAuthentication::getType, queryParam.getType())
                .eq(SupplierAuthentication::getTenant, queryParam.getTenant())
                .like(StringUtils.isNotEmpty(queryParam.getKeyword()), SupplierAuthentication::getDelegateName, queryParam.getKeyword())
                .orderByDesc(BasicEntity::getUpdateTime)
                .page(PagingUtils.convert(queryParam));
    }

    @Override
    public List<SupplierAuthentication> listBySupplierIds(@NonNull Collection<? extends Serializable> supplierIds,
                                                          @NonNull Integer tenant
                                                         ) {
        return CollectionUtils.isEmpty(supplierIds) ? Collections.emptyList() :
               this.baseMapper.selectList(Wrappers.<SupplierAuthentication>lambdaQuery()
                                                  .eq(SupplierAuthentication::getTenant, tenant)
                                                  .ne(SupplierAuthentication::getStatus, SupplierAuthenticatedEnum.INVALID)
                                                  .in(SupplierAuthentication::getSupplierId, supplierIds));
    }

    @Override
    public List<SupplierAuthentication> listSupplierAuthentication(@NonNull SupplierAuthenticationQueryParam queryParam,
                                                                   @NonNull Integer tenant
                                                                  ) {
        return this.lambdaQuery()
                .eq(Objects.nonNull(queryParam.getStatus()), SupplierAuthentication::getStatus, queryParam.getStatus())
                .eq(Objects.nonNull(queryParam.getType()), SupplierAuthentication::getType, queryParam.getType())
                .eq(SupplierAuthentication::getTenant, tenant)
                .ne(SupplierAuthentication::getStatus, SupplierAuthenticatedEnum.INVALID)
                .list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditSupplierAuthentication(@NonNull Long id, @NonNull SupplierAuthenticatedEnum status, String remark) {
        // 获取认证信息
        SupplierAuthentication supplierAuthentication = this.baseMapper.selectById(id);
        Assert.isNotFound(supplierAuthentication, "不存在批发商认证信息!");
        Assert.isUnauthenticated(supplierAuthentication.getStatus() == SupplierAuthenticatedEnum.INVALID, "认证信息已失效!");
        Assert.isNotAcceptable(supplierAuthentication.getStatus() != SupplierAuthenticatedEnum.AUDITED, "认证信息状态不是审核中!");

        if (SupplierAuthenticatedEnum.FAILED == status) {
            supplierAuthentication.setRemark(remark);
        }
        supplierAuthentication.setStatus(status);
        this.baseMapper.updateById(supplierAuthentication);

        // 发送消息
        Supplier supplier = this.supplierMapper.selectById(supplierAuthentication.getSupplierId());
        Assert.isNotFound(supplier, "不存在批发商信息!");
        //  审核通过发送消息初始化商城店铺信息
        if (status == SupplierAuthenticatedEnum.CERTIFIED) {
            AddMallMessage message = AddMallMessage.builder().supplier(ObjectUtils.copying(supplier, SupplierDTO.class)).build();
            RocketMQContextHolder.send(AddMallMessage.TOPIC, message);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetSupplierAuthentication(@NonNull SupplierAuthentication supplierAuthentication) {
        supplierAuthentication.setStatus(SupplierAuthenticatedEnum.INVALID);
        this.updateById(supplierAuthentication);
    }

}
