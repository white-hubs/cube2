package com.guhecloud.cube.srv.gen.property.management.server.common.service.impl;

import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserClientServiceImpl implements UserClientService {
    private final UserClient userClient;

    @Override
    public String getUserByUserName(String username) {
        String name = "";
        if (StringUtils.isNotEmpty(username)) {
            if (ObjectUtils.isEmpty(name)) {
                UserDTO user = userClient.getUserByUserName(username);
                if (ObjectUtils.isNotEmpty(user)) {
                    name = user.getName();
                }
            }
        }
        return name;
    }

    @Override
    public String getUser(Long userId) {
        String name = "";
        if (ObjectUtils.isNotEmpty(userId)) {
            if (ObjectUtils.isEmpty(name)) {
                UserDTO user = userClient.getUser(userId);
                if (ObjectUtils.isNotEmpty(user)) {
                    name = user.getName();
                }
            }
        }
        return name;
    }

    @Override
    public String getUser(String userId) {
        if (ObjectUtils.isEmpty(userId)) {
            return "";
        }
        return this.getUser(Long.parseLong(userId));
    }

    @Override
    public List<UserDTO> getUserByIds(List<Long> longs) {
        if (ObjectUtils.isEmpty(longs)) {
            return List.of();
        }
        List<UserDTO> userDTOS = userClient.listUserByIds(longs);
        return userDTOS == null ? List.of() : userDTOS;
    }

    @Override
    public String getCreateByName(BasicEnhancedEntity<Long> entity) {
        return getCreateByName(entity.getCreateBy(), entity.getCreatorId(), entity.getCreatorType());
    }

    @Override
    public String getCreateByName(String createBy, String creatorId, ActorTypeEnum creatorType) {
        //查询创建人
        String userName;
        if (StringUtils.isNotBlank(creatorId) && ActorTypeEnum.USER.name().equals(creatorType)) {
            userName = this.getUser(creatorId);
        } else {
            userName = this.getUserByUserName(createBy);
        }
        return userName;
    }
}
