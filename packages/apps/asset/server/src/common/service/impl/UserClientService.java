package com.guhecloud.cube.srv.gen.property.management.server.common.service.impl;

import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import com.guhecloud.platform.base.model.ActorTypeEnum;

import java.util.List;

public interface UserClientService {
    String getUserByUserName(String username);

    String getUser(Long userId);

    String getUser(String userId);

    List<UserDTO> getUserByIds(List<Long> longs);

    String getCreateByName(BasicEnhancedEntity<Long> entity);

    String getCreateByName(String createBy, String creatorId, ActorTypeEnum creatorType);
}
