package com.guhecloud.cube.biz.ouaa.server.third.wechat.service;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.biz.ouaa.server.third.wechat.entity.UserRefWechat;
import com.guhecloud.cube.biz.ouaa.server.third.wechat.param.command.IncreaseUserWechatCommand;
import com.guhecloud.cube.biz.ouaa.server.third.wechat.repository.UserWechatRepository;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserWechatCommandService {

    private final UserWechatRepository wechatUserRepository;

    public Long increaseWechatUser(IncreaseUserWechatCommand increaseWechatUserCommand) {
        UserRefWechat userRefWechat = new UserRefWechat(
                IdWorker.getId(),
                SecurityContextHolder.getPrincipal().getId(),
                increaseWechatUserCommand.getOpenId(),
                increaseWechatUserCommand.getUnionId(),
                increaseWechatUserCommand.getConfigId(),
                SessionContextHolder.getRequester().getTenant()
        );
        return this.wechatUserRepository.save(userRefWechat);
    }
}
