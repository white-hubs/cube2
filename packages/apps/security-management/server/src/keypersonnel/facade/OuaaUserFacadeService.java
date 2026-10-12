package com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.facade;

import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.param.UserQueryParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.vo.SupervisorOptionVO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DesensitizeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OuaaUserFacadeService {
    private final UserClient userClient;

    public UserDTO getTenantUser(Long userId) {
        Assert.isParameterError(userId == null, "监管人员不能为空");
        List<UserDTO> users = userClient.listUserByIds(Collections.singletonList(userId));
        Assert.isParameterError(CollectionUtils.isEmpty(users), "监管人员不存在");
        return users.get(0);
    }

    public List<UserDTO> listByIds(Collection<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return Collections.emptyList();
        }
        return userClient.listUserByIds(userIds);
    }

    public List<SupervisorOptionVO> listOptions(String keyword) {
        UserQueryParam queryParam = new UserQueryParam();
        queryParam.setKeyword(keyword);
        List<UserDTO> users = userClient.listAllUsers(queryParam);
        if (CollectionUtils.isEmpty(users)) {
            return Collections.emptyList();
        }
        return users.stream().map(this::toOption).collect(Collectors.toList());
    }

    private SupervisorOptionVO toOption(UserDTO user) {
        SupervisorOptionVO vo = new SupervisorOptionVO();
        vo.setUserId(user.getId());
        vo.setName(user.getName());
        vo.setMobileMasked(DesensitizeUtils.desensitize(user.getMobile(), DesensitizeUtils.DESENSITIZE_MODE_MOBILE));
        if (user.getOrganization() != null) {
            vo.setOrganizationName(user.getOrganization().getName());
        }
        return vo;
    }
}
