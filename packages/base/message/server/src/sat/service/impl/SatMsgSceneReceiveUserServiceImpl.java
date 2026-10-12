package com.guhecloud.cube.base.message2.server.sat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.message2.server.sat.dao.SatMsgSceneReceiveUserMapper;
import com.guhecloud.cube.base.message2.server.sat.entity.SatMsgSceneReceiveUser;
import com.guhecloud.cube.base.message2.server.sat.service.SatMsgSceneReceiveUserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SatMsgSceneReceiveUserServiceImpl extends ServiceImpl<SatMsgSceneReceiveUserMapper, SatMsgSceneReceiveUser> implements SatMsgSceneReceiveUserService {

    @Override
    public List<SatMsgSceneReceiveUser> getUsersBySatMsgSceneReceiveIds(@NonNull Collection<Long> satMsgSceneReceiveIds) {
        return CollectionUtils.isEmpty(satMsgSceneReceiveIds) ? Collections.emptyList() :
               this.lambdaQuery().in(SatMsgSceneReceiveUser::getSatMsgSceneReceiveId, satMsgSceneReceiveIds).list();
    }
}
