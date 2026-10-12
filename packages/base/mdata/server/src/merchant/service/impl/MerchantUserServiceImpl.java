package com.guhecloud.cube.biz.mdata.server.merchant.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.mdata.server.merchant.dao.MerchantUserMapper;
import com.guhecloud.cube.biz.mdata.server.merchant.entity.MerchantUser;
import com.guhecloud.cube.biz.mdata.server.merchant.param.MerchantUserCreateParam;
import com.guhecloud.cube.biz.mdata.server.merchant.param.MerchantUserQueryParam;
import com.guhecloud.cube.biz.mdata.server.merchant.param.MerchantUserUpdateParam;
import com.guhecloud.cube.biz.mdata.server.merchant.service.MerchantUserService;
import com.guhecloud.cube.biz.mdata.server.stall.entity.Stall;
import com.guhecloud.cube.biz.mdata.server.stall.service.StallService;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.param.UserModifyParam;
import com.guhecloud.cube.biz.ouaa.metadata.enumeration.Gender;
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

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lzy
 * @date 2021/10/11
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MerchantUserServiceImpl extends ServiceImpl<MerchantUserMapper, MerchantUser> implements MerchantUserService {

    private final UserClient userClient;
    private final StallService stallService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(@NonNull MerchantUserCreateParam param) {
        // 判断用户是否存在
        UserModifyParam userModifyParam = new UserModifyParam();
        userModifyParam.setName(param.getMerchantCategoryName());
        userModifyParam.setMobile(param.getMerchantCategoryPhone());
        userModifyParam.setUsername(param.getMerchantCategoryPhone());
        userModifyParam.setGender(Gender.NONE);
        UserDTO userDTO = userClient.save(userModifyParam);

        // 添加相关商户角色的关联关系
        MerchantUser user
                = this.lambdaQuery().eq(MerchantUser::getMerchantId, param.getMerchantId()).eq(MerchantUser::getUserId, userDTO.getId())
                .one();
        Assert.isConflict(user != null, "该用户已存在商户中!");
        MerchantUser merchantUser = ObjectUtils.copying(param, MerchantUser.class);
        merchantUser.setUserId(userDTO.getId());
        if (!CollectionUtils.isEmpty(param.getMerchantStalls())) {
            merchantUser.setMerchantStallId(String.join(",", param.getMerchantStalls()));
            List<Stall> stalls = this.stallService.listStalls(param.getMerchantStalls().stream().map(Long::parseLong)
                                                                      .collect(Collectors.toList()));
            merchantUser.setMerchantStallName(stalls.stream().map(Stall::getName).collect(Collectors.joining(",")));
        }
        this.save(merchantUser);
    }

    @Override
    public void update(@NonNull MerchantUserUpdateParam param) {
        MerchantUser merchantUser = this.getById(param.getId());
        Assert.isNotFound(merchantUser, "不存在对应信息!");
        ObjectUtils.copying(param, merchantUser);
        this.updateById(merchantUser);
    }

    @Override
    public MerchantUser getMerchantUser(@NonNull Long merchantId, @NonNull Long userId) {
        return this.lambdaQuery().eq(MerchantUser::getMerchantId, merchantId).eq(MerchantUser::getUserId, userId).one();
    }

    @Override
    public List<MerchantUser> listMerchantUsers(@NonNull Long userId) {
        return this.lambdaQuery().eq(MerchantUser::getUserId, userId).list();
    }

    @Override
    public List<MerchantUser> listMerchantUsersByUserIds(@NonNull Collection<Long> userIds) {
        return CollectionUtils.isEmpty(userIds) ? Collections.emptyList() :
               this.lambdaQuery().in(MerchantUser::getUserId, userIds).list();
    }

    @Override
    public List<MerchantUser> listMerchantUsers(@NonNull List<Long> merchantIds) {
        return CollectionUtils.isEmpty(merchantIds) ? Collections.emptyList() :
               this.lambdaQuery().in(MerchantUser::getMerchantId, merchantIds).list();
    }

    @Override
    public Page<MerchantUser> page(@NonNull MerchantUserQueryParam queryParam) {
        return this.lambdaQuery()
                .eq(MerchantUser::getMerchantCategory, queryParam.getMerchantCategory())
                .eq(MerchantUser::getMerchantId, queryParam.getMerchantId())
                .and(StringUtils.isNotEmpty(queryParam.getKeyword()), q -> q
                        .like(MerchantUser::getMerchantCategoryName, queryParam.getKeyword())
                        .or()
                        .like(MerchantUser::getMerchantCategoryPhone, queryParam.getKeyword())
                    )
                .orderByDesc(MerchantUser::getId)
                .page(PagingUtils.convert(queryParam));
    }
}
