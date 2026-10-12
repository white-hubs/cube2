package com.guhecloud.cube.biz.ouaa.server.behavior.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.server.behavior.dao.UserBehaviorMapper;
import com.guhecloud.cube.biz.ouaa.server.behavior.entity.UserBehavior;
import com.guhecloud.cube.biz.ouaa.server.behavior.param.UserBehaviorModifyParam;
import com.guhecloud.cube.biz.ouaa.server.behavior.service.UserBehaviorService;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class UserBehaviorServiceImpl extends ServiceImpl<UserBehaviorMapper, UserBehavior> implements UserBehaviorService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull UserBehaviorModifyParam param) {
        UserBehavior userBehavior = this.getUserBehavior(param.getUserId(), param.getTerminal());
        if (Objects.nonNull(userBehavior)) {
            userBehavior.setBehaviorConfig(param.getBehaviorConfig());
            this.baseMapper.updateById(userBehavior);
        } else {
            userBehavior = new UserBehavior();
            userBehavior.setUserId(param.getUserId());
            userBehavior.setTerminal(param.getTerminal());
            userBehavior.setBehaviorConfig(param.getBehaviorConfig());
            this.baseMapper.insert(userBehavior);
        }
        return userBehavior.getId();
    }

    @Override
    public UserBehavior getUserBehavior(@NonNull Long userId, @NonNull String terminal) {
        List<UserBehavior> userBehaviors = this.list(Wrappers.<UserBehavior>lambdaQuery()
                                                             .eq(UserBehavior::getUserId, userId)
                                                             .eq(UserBehavior::getTerminal, terminal)
                                                    );
        return CollectionUtils.isEmpty(userBehaviors) ? null : userBehaviors.get(0);
    }
}
