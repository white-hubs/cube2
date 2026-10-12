package com.guhecloud.cube.biz.ouaa.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.ouaa.server.entity.UserTel;

import java.util.List;
import java.util.Map;

public interface UserTelService extends IService<UserTel> {
    /**
     * 设置用户电话号码是否可用
     */
    void relationUserTelIsBind(Long userId, String tel);

    boolean checkUserBindExist(Long userId);

    boolean checkUserBindTel(Long userId, String tel);

    Map<Long, UserTel> getBindMapByUserIds(List<Long> userIds);

    boolean checkTelBindExist(String tel);

    UserTel getBindUserTel(String tel);

    UserTel getBindByUserId(Long userId);
}
