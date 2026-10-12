package com.guhecloud.cube.biz.mdata.server.purchaser.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.mdata.server.purchaser.dao.PurchaserMapper;
import com.guhecloud.cube.biz.mdata.server.purchaser.entity.Purchaser;
import com.guhecloud.cube.biz.mdata.server.purchaser.enumeration.PurchaserAuthenticationEnum;
import com.guhecloud.cube.biz.mdata.server.purchaser.param.PurchaserAuthenticationQueryParam;
import com.guhecloud.cube.biz.mdata.server.purchaser.param.PurchaserInnerModifyParam;
import com.guhecloud.cube.biz.mdata.server.purchaser.param.PurchaserModifyParam;
import com.guhecloud.cube.biz.mdata.server.purchaser.service.PurchaserService;
import com.guhecloud.cube.biz.mdata.server.purchaser.vo.PurchaserAuthenticationVO;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.param.UserModifyParam;
import com.guhecloud.cube.biz.ouaa.metadata.enumeration.Gender;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author lzy
 * @date 2021/12/24
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PurchaserServiceImpl extends ServiceImpl<PurchaserMapper, Purchaser> implements PurchaserService {
    private final PurchaserMapper purchaserMapper;
    private final UserClient userClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addPurchaser(@NonNull PurchaserModifyParam param) {
        Long userId = SecurityContextHolder.getPrincipal().getId();
        UserDTO user = this.userClient.getUser(userId);
        try {
            Purchaser p = this.getPurchaser(userId, param.getTenant());
            if (Objects.isNull(p)) {
                Purchaser purchaser = new Purchaser();
                purchaser.setId(IdWorker.getId());
                purchaser.setTenant(param.getTenant());
                purchaser.setUserId(userId);
                purchaser.setUserName(user == null ? "" : user.getName());
                this.baseMapper.insert(purchaser);
            }
        } catch (DuplicateKeyException e) {

        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePurchaserName(@NonNull Long userId, @NonNull String userName) {
        LambdaUpdateWrapper<Purchaser> updateWrapper = Wrappers.<Purchaser>lambdaUpdate()
                .set(Purchaser::getUserName, userName)
                .eq(Purchaser::getUserId, userId);
        this.update(updateWrapper);
    }

    @Override
    public Purchaser getPurchaser(@NonNull Long userId, @NonNull Integer tenant) {
        return this.baseMapper.selectOne(Wrappers.<Purchaser>lambdaQuery()
                                                 .eq(Purchaser::getTenant, tenant)
                                                 .eq(Purchaser::getUserId, userId));
    }

    @Override
    public List<Purchaser> listByIds(@NonNull Collection<? extends Serializable> ids) {
        return CollectionUtils.isEmpty(ids) ? Collections.emptyList() :
               this.lambdaQuery().in(Purchaser::getId, ids).list();
    }

    @Override
    public List<Purchaser> listByUserId(@NonNull Serializable userId) {
        return this.baseMapper.selectList(Wrappers.<Purchaser>lambdaQuery().eq(Purchaser::getUserId, userId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addInnerPurchaser(@NonNull PurchaserInnerModifyParam param) {
        // 判断当前手机号是否已注册用户
        UserDTO user = this.userClient.getUserByMobile(param.getMobile());
        if (user == null) {
            UserModifyParam userModifyParam = new UserModifyParam();
            userModifyParam.setName(param.getName());
            userModifyParam.setUsername(param.getMobile());
            userModifyParam.setMobile(param.getMobile());
            userModifyParam.setGender(Gender.NONE);
            userModifyParam.setEnabled(true);
            user = userClient.save(userModifyParam);
        }

        // 构建信息
        Purchaser purchaser = this.getPurchaser(user.getId(), param.getTenant());
        if (purchaser == null) {
            purchaser = new Purchaser();
            purchaser.setUserId(user.getId());
            purchaser.setTenant(param.getTenant());
            purchaser.setUserName(user.getName());
            this.save(purchaser);
        }
        return user.getId();
    }

    @Override
    public Page<PurchaserAuthenticationVO> pageForPurchaser(PurchaserAuthenticationQueryParam param) {
        return purchaserMapper.pageForPurchaser(PagingUtils.convert(param), Wrappers.query()
                .and(StringUtils.isNotBlank(param.getKeyword()), q -> q.like("p.user_name", param.getKeyword())
                        .or().like("pa.delegate_name", param.getKeyword()))
                .eq(param.getStatus() != null &&
                    !param.getStatus().equals(PurchaserAuthenticationEnum.UNAUTHORIZED), "pa.status", param.getStatus())
                .isNull(param.getStatus() != null && param.getStatus().equals(PurchaserAuthenticationEnum.UNAUTHORIZED), "pa.status")
                .eq(param.getType() != null, "pa.type", param.getType())
                .eq("p.tenant", param.getTenant()));
    }

    @Override
    public Page<PurchaserAuthenticationVO> pageForPurchaserVerify(PurchaserAuthenticationQueryParam param) {
        return purchaserMapper.pageForPurchaser(PagingUtils.convert(param), Wrappers.query()
                .and(StringUtils.isNotBlank(param.getKeyword()), q -> q.like("p.user_name", param.getKeyword())
                        .or().like("pa.delegate_name", param.getKeyword()))
                .eq(param.getStatus() != null, "pa.status", param.getStatus())
                .eq(param.getType() != null, "pa.type", param.getType())
                .eq("p.tenant", param.getTenant())
                .isNotNull("pa.status"));
    }
}
