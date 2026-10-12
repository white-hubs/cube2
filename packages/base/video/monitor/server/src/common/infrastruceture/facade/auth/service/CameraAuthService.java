package com.guhecloud.cube.biz.vmonitor.server.common.infrastruceture.facade.auth.service;

import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.vmonitor.server.auth.enumeration.ScopeTypeEnum;
import com.guhecloud.cube.biz.vmonitor.server.auth.service.AuthScopeService;
import com.guhecloud.cube.biz.vmonitor.server.auth.vo.AuthScopeVO;
import com.guhecloud.cube.biz.vmonitor.server.common.infrastruceture.facade.securitymanage.service.CameraSecurityDeviceQueryService;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CameraAuthService {

    private final AuthScopeService authScopeService;

    private final CameraSecurityDeviceQueryService cameraSecurityDeviceQueryService;

    private final UserClient userClient;

    public CameraAuthService(AuthScopeService authScopeService, CameraSecurityDeviceQueryService cameraSecurityDeviceQueryService,
                             UserClient userClient
                            ) {
        this.authScopeService = authScopeService;
        this.cameraSecurityDeviceQueryService = cameraSecurityDeviceQueryService;
        this.userClient = userClient;
    }

    public List<Long> getUserAuthScopeDeviceIds(Long userId) {
        var user = userClient.getUser(userId);
        if (Objects.isNull(user)) {
            return Collections.emptyList();
        }
        //根据用户、用户组织架构、用户角色匹配权限范围
        var targetIds = new HashSet<Long>();
        targetIds.add(user.getId());
        if (Objects.nonNull(user.getOrganization())) {
            targetIds.add(user.getOrganization().getId());
        }
        if (CollectionUtils.isNotEmpty(user.getRoles())) {
            targetIds.addAll(user.getRoles().stream().map(UserDTO.Role::getId).collect(Collectors.toSet()));
        }
        var authScopeAuthMap = authScopeService.getAuthScopeVOSByAuthTargetIds(targetIds).stream()
                .collect(Collectors.groupingBy(AuthScopeVO::getScopeType, Collectors.mapping(AuthScopeVO::getScopeId, Collectors.toSet())));
        var deviceIds = cameraSecurityDeviceQueryService.queryCameraDeviceIdsByAreasOrGroups(authScopeAuthMap.get(ScopeTypeEnum.DEVICE_AREA),
                                                                                             authScopeAuthMap.get(ScopeTypeEnum.DEVICE_GROUP),
                                                                                             authScopeAuthMap.get(ScopeTypeEnum.DEVICE));

        return new ArrayList<>(deviceIds);
    }
}
