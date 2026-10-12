package com.guhecloud.cube.biz.ouaa.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.ouaa.server.entity.UserRole;
import com.guhecloud.cube.biz.ouaa.server.entity.UserTenant;
import com.guhecloud.cube.biz.ouaa.server.entity.UserWechat;

import java.util.List;

/**
 * 用户微信关联业务操作接口
 *
 * @author Woody
 * @date 2021/7/26
 */
public interface UserWechatService extends IService<UserWechat> {
    /**
     * 初始化微信用户配置信息
     *
     * @param userWechat 用户微信关联信息
     * @param userTenant 用户租户关联信息
     * @param userRoles  用户角色关联信息列表
     */
    void initializeWechatUserConfiguration(UserWechat userWechat, UserTenant userTenant, List<UserRole> userRoles);

    UserWechat getWechatInfo(Long userId);

    UserWechat getUserWechatByUnionid(String unionId);

    UserWechat getUserWechatByOpenid(String openId);
}
