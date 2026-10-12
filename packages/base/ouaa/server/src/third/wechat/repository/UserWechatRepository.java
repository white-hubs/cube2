package com.guhecloud.cube.biz.ouaa.server.third.wechat.repository;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.ouaa.server.dao.UserWechatMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.UserWechat;
import com.guhecloud.cube.biz.ouaa.server.third.common.entity.ThirdUserRef;
import com.guhecloud.cube.biz.ouaa.server.third.common.enumeration.ThirdTypeEnum;
import com.guhecloud.cube.biz.ouaa.server.third.common.repository.ThirdUserRefRepository;
import com.guhecloud.cube.biz.ouaa.server.third.wechat.entity.UserRefWechat;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserWechatRepository {

    private final ThirdUserRefRepository thirdUserRefRepository;

    private final UserWechatMapper userWechatMapper;

    @Transactional(rollbackFor = Exception.class)
    public Long save(UserRefWechat userRefWechat) {
        Optional<UserRefWechat> wechatUser = getWechatUser(userRefWechat.getUserId(), userRefWechat.getOpenId());
        UserWechat user;
        if (wechatUser.isPresent()) {
            UserRefWechat refWechat = wechatUser.get();
            user = convertEntityToPO(refWechat);
            this.userWechatMapper.updateById(user);
        } else {
            user = convertEntityToPO(userRefWechat);
            this.userWechatMapper.insert(user);
        }
        ThirdUserRef thirdUserRef = new ThirdUserRef(IdWorker.getId(),
                                                     ThirdTypeEnum.WECHAT,
                                                     userRefWechat.getConfigId(),
                                                     userRefWechat.getUserId(),
                                                     user.getId(),
                                                     userRefWechat.getTenant());
        this.thirdUserRefRepository.saveThirdUserRef(thirdUserRef);
        return user.getId();
    }

    public Optional<UserRefWechat> getWechatUser(Long userId, String openId) {
        List<UserWechat> userWechatList = this.userWechatMapper
                .selectList(Wrappers.<UserWechat>lambdaQuery()
                                    .eq(UserWechat::getUserId, userId)
                                    .eq(UserWechat::getOpenId, openId)
                           );
        if (CollectionUtils.isEmpty(userWechatList)) {
            return Optional.empty();
        }
        return Optional.of(convertPOTOEntity(userWechatList.get(0)));
    }

    private UserRefWechat convertPOTOEntity(UserWechat userWechat) {
        return new UserRefWechat(userWechat.getId(), userWechat.getUserId(), userWechat.getOpenId(), userWechat.getUnionId());
    }

    private UserWechat convertEntityToPO(UserRefWechat userRefWechat) {
        return UserWechat.builder()
                .id(userRefWechat.getId())
                .userId(userRefWechat.getUserId())
                .openId(userRefWechat.getOpenId())
                .unionId(userRefWechat.getUnionId())
                .build();
    }
}
