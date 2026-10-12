package com.guhecloud.cube.biz.ouaa.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.server.dao.UserTelMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.UserTel;
import com.guhecloud.cube.biz.ouaa.server.enumeration.UserTelBindEnableEnum;
import com.guhecloud.cube.biz.ouaa.server.service.UserTelService;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserTelServiceImpl extends ServiceImpl<UserTelMapper, UserTel> implements UserTelService {
    @Override
    public void relationUserTelIsBind(Long userId, String tel) {
        if (Objects.isNull(userId)) {
            return;
        }
        if (Objects.isNull(tel)) {
            return;
        }
        UserTel userTel = lambdaQuery().eq(UserTel::getUserId, userId).eq(UserTel::getIsBind, UserTelBindEnableEnum.UNBIND)
                .eq(UserTel::getTel, tel).last("limit 1").one();
        if (Objects.isNull(userTel)) {
            userTel = UserTel.builder().userId(userId).tel(tel).id(IdWorker.getId()).build();
            save(userTel);
            return;
        }
        userTel.setIsBind(UserTelBindEnableEnum.BIND);
        updateById(userTel);

    }

    @Override
    public boolean checkUserBindExist(Long userId) {

        return Optional.ofNullable(userId)
                .map(id -> {
                    UserTel userTel = lambdaQuery()
                            .eq(UserTel::getUserId, id)
                            .eq(UserTel::getIsBind, UserTelBindEnableEnum.BIND)
                            .last("limit 1").one();
                    return Objects.nonNull(userTel);
                })
                .orElse(false);
    }

    @Override
    public boolean checkUserBindTel(Long userId, String tel) {
        if (Objects.isNull(userId) || Objects.isNull(tel)) {
            return false;
        }

        return lambdaQuery()
                       .eq(UserTel::getUserId, userId)
                       .eq(UserTel::getTel, tel)
                       .eq(UserTel::getIsBind, UserTelBindEnableEnum.BIND)
                       .count() > 0;
    }

    @Override
    public Map<Long, UserTel> getBindMapByUserIds(List<Long> userIds) {
        if (ListUtils.isEmpty(userIds)) {
            return Collections.emptyMap();
        }
        List<UserTel> list = lambdaQuery()
                .in(UserTel::getUserId, userIds)
                .eq(UserTel::getIsBind, UserTelBindEnableEnum.BIND)
                .list();
        return MapUtils.listToMap(list, UserTel::getUserId, userTel -> userTel);
    }

    @Override
    public boolean checkTelBindExist(String tel) {
        if (StringUtils.isBlank(tel)) {
            return false;
        }
        return lambdaQuery()
                       .eq(UserTel::getTel, tel)
                       .eq(UserTel::getIsBind, UserTelBindEnableEnum.BIND)
                       .count() > 0;
    }

    @Override
    public UserTel getBindUserTel(String tel) {
        return Optional
                .ofNullable(tel)
                .map(t -> lambdaQuery()
                        .eq(UserTel::getTel, tel)
                        .eq(UserTel::getIsBind, UserTelBindEnableEnum.BIND)
                        .last("limit 1").one())
                .orElse(null);
    }

    @Override
    public UserTel getBindByUserId(Long userId) {
        return Optional.ofNullable(userId)
                .map(id -> lambdaQuery()
                        .eq(UserTel::getUserId, id)
                        .eq(UserTel::getIsBind, UserTelBindEnableEnum.BIND)
                        .last("limit 1")
                        .one())
                .orElse(null);
    }

}
