package com.guhecloud.cube.biz.ouaa.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.server.dao.UserWechatMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.UserRole;
import com.guhecloud.cube.biz.ouaa.server.entity.UserTenant;
import com.guhecloud.cube.biz.ouaa.server.entity.UserWechat;
import com.guhecloud.cube.biz.ouaa.server.enumeration.TerminalEnum;
import com.guhecloud.cube.biz.ouaa.server.service.UserRoleService;
import com.guhecloud.cube.biz.ouaa.server.service.UserTenantService;
import com.guhecloud.cube.biz.ouaa.server.service.UserWechatService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 用户微信关联业务操作实现
 *
 * @author Woody
 * @date 2021/7/26
 */
@Service
@RequiredArgsConstructor
public class UserWechatServiceImpl extends ServiceImpl<UserWechatMapper, UserWechat> implements UserWechatService {
    private final UserRoleService roleService;
    private final UserTenantService userTenantService;

    @Override
    public void initializeWechatUserConfiguration(UserWechat userWechat, UserTenant userTenant,
                                                  List<UserRole> userRoles
                                                 ) {
        if (Objects.nonNull(userTenant)) {
            userTenantService.save(userTenant);
        }
        if (Objects.nonNull(userRoles)) {
            roleService.saveOrUpdateBatch(userRoles);
        }
        if (Objects.nonNull(userWechat)) {
            this.save(userWechat);
        }
    }

    @Override
    public UserWechat getWechatInfo(Long userId) {
        return Optional
                .ofNullable(userId)
                .map(userid -> lambdaQuery().eq(UserWechat::getUserId, userid).last("limit 1").one())
                .orElse(null);
    }

    @Override
    public UserWechat getUserWechatByUnionid(String unionId) {
        return Optional.ofNullable(unionId)
                .map(unionid -> lambdaQuery().eq(UserWechat::getUnionId, unionid).one())
                .orElse(null);
    }

    @Override
    public UserWechat getUserWechatByOpenid(String openId) {
        if (StringUtils.isEmpty(openId)) {
            log.error("openId is empty");
            return null;
        }

        return Optional.of(openId)
                .map(openid -> {
                    String terminal = SessionContextHolder.getRequester().getTerminal();
                    return lambdaQuery()
                            .eq(TerminalEnum.MP.getTerminal().equals(terminal), UserWechat::getMpOpenid, openid)
                            .eq(TerminalEnum.APP.getTerminal().equals(terminal), UserWechat::getAppOpenid, openid)
                            .eq(TerminalEnum.WEB.getTerminal().equals(terminal), UserWechat::getWebOpenid, openid)
                            .last("limit 1")
                            .one();
                })
                .orElse(null);
    }
}
