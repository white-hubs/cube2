package com.guhecloud.cube.biz.ouaa.server.behavior.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.ouaa.server.behavior.entity.UserBehavior;
import com.guhecloud.cube.biz.ouaa.server.behavior.param.UserBehaviorModifyParam;

public interface UserBehaviorService extends IService<UserBehavior> {

    Long save(UserBehaviorModifyParam param);

    UserBehavior getUserBehavior(Long userId, String terminal);

}
