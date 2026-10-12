package com.guhecloud.cube.base.message2.server.sat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.message2.server.sat.entity.SatMsgSceneReceiveUser;

import java.util.Collection;
import java.util.List;

public interface SatMsgSceneReceiveUserService extends IService<SatMsgSceneReceiveUser> {

    List<SatMsgSceneReceiveUser> getUsersBySatMsgSceneReceiveIds(Collection<Long> satMsgSceneReceiveIds);
}
