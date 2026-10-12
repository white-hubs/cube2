package com.guhecloud.cube.biz.vmonitor.server.common.infrastruceture.facade.user.service;

import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.param.UserQueryParam;
import com.guhecloud.cube.biz.ouaa.client.queryrier.UserQuery;
import com.guhecloud.cube.biz.vmonitor.server.common.infrastruceture.facade.user.request.CameraUserQueryRequest;
import com.guhecloud.cube.biz.vmonitor.server.common.infrastruceture.facade.user.response.CameraUserPageResponse;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CameraUserQueryService {

    private final UserClient userClient;

    public Pagination<CameraUserPageResponse> queryCameraUserPage(CameraUserQueryRequest request) {
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setIds(request.getUserIds());
        userQueryParam.setFilterUserIds(request.getFilterUserIds());
        userQueryParam.setPageNumber(request.getPageNumber());
        userQueryParam.setPageSize(request.getPageSize());
        userQueryParam.setKeyword(request.getKeyword());
        userQueryParam.setOrganizationId(request.getOrganizationId());
        Pagination<UserDTO> userDTOPagination = this.userClient.postListUsers(userQueryParam);
        List<CameraUserPageResponse> cameraUserPageResponses = ObjectUtils.copying(userDTOPagination.getRecords(), CameraUserPageResponse.class);
        return Pagination.<CameraUserPageResponse>builder()
                .records(cameraUserPageResponses)
                .total(userDTOPagination.getTotal())
                .pages(userDTOPagination.getPages())
                .build();
    }

    public Set<Long> queryCameraUserIds(CameraUserQueryRequest request) {
        UserQuery userQuery = new UserQuery();
        userQuery.setKeyword(request.getKeyword());
        userQuery.setOrganizationIds(request.getOrganizationIds());
        if (!CollectionUtils.isEmpty(request.getUserIds())) {
            userQuery.setUserIds(request.getUserIds().stream().collect(Collectors.toSet()));
        }
        List<UserDTO> userDTOS = this.userClient.list(userQuery);
        if (CollectionUtils.isEmpty(userDTOS)) {
            return Collections.emptySet();
        }
        return userDTOS.stream().map(UserDTO::getId).collect(Collectors.toSet());
    }
}
