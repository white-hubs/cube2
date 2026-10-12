package com.guhecloud.cube.srv.gen.safety.personnel.server.common.facade;

import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.TenantClient;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.TenantDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.param.UserQueryParam;
import com.guhecloud.cube.biz.ouaa.client.queryrier.UserQuery;
import com.guhecloud.cube.srv.gen.safety.personnel.server.common.facade.model.OuaaUserSnapshot;
import com.guhecloud.cube.srv.gen.safety.personnel.server.org.util.OuaaUserUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DesensitizeUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * OUAA 用户与组织访问门面。
 */
@Slf4j
@Service("safetyPersonnelOuaaFacade")
@RequiredArgsConstructor
public class SafetyPersonnelOuaaFacadeService {

    private final UserClient userClient;
    private final OrganizationClient organizationClient;
    private final TenantClient tenantClient;

    /**
     * 按 ID 查询 OUAA 用户。
     *
     * @param userId 用户ID
     *
     * @return 用户信息，不存在时返回 null
     */
    public UserDTO getUser(Long userId) {
        if (userId == null) {
            return null;
        }
        try {
            return userClient.getUser(userId);
        } catch (Exception e) {
            log.warn("查询OUAA用户失败，userId={}", userId, e);
            return null;
        }
    }

    /**
     * 批量查询 OUAA 用户。
     *
     * @param userIds 用户ID集合
     *
     * @return 用户列表
     */
    public List<UserDTO> listUsersByIds(Collection<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return Collections.emptyList();
        }
        return userClient.postList(UserQuery.builder()
                                           .userIds(new HashSet<>(userIds))
                                           .build());
    }

    /**
     * 分页查询 OUAA 用户。
     *
     * @param query 查询参数
     *
     * @return 用户分页
     */
    public Pagination<UserDTO> pageUsers(UserQueryParam query) {
        return userClient.postListUsers(query);
    }

    /**
     * 查询 OUAA 组织。
     *
     * @param organizationId 组织ID
     *
     * @return 组织信息
     */
    public OrganizationDTO getOrganization(Long organizationId) {
        Assert.isParameterError(organizationId == null, "组织ID不能为空");
        return organizationClient.getOrganizationsById(organizationId);
    }

    /**
     * 解析当前请求租户的企业/单位名称。
     *
     * @return 租户名称，无法解析时为空
     */
    public String resolveCurrentTenantCompanyName() {
        try {
            if (SessionContextHolder.getRequester() == null || SessionContextHolder.getRequester().getTenant() == null) {
                return null;
            }
            Integer tenantCode = SessionContextHolder.getRequester().getTenant();
            TenantDTO tenant = tenantClient.getByCode(tenantCode);
            return tenant == null ? null : tenant.getName();
        } catch (Exception e) {
            log.warn("解析当前租户名称失败", e);
            return null;
        }
    }

    /**
     * 构建 OUAA 用户展示快照。
     *
     * @param user OUAA用户
     *
     * @return 展示快照
     */
    public OuaaUserSnapshot buildSnapshot(UserDTO user) {
        if (user == null) {
            return null;
        }
        return OuaaUserSnapshot.builder()
                .userName(user.getName())
                .phone(user.getMobile())
                .deptName(OuaaUserUtils.resolveDeptName(user))
                .postName(OuaaUserUtils.resolvePostName(user))
                .companyName(resolveCurrentTenantCompanyName())
                .build();
    }

    /**
     * 按用户 ID 构建展示快照。
     *
     * @param userId 用户ID
     *
     * @return 展示快照，用户不存在时返回 null
     */
    public OuaaUserSnapshot buildSnapshot(Long userId) {
        return buildSnapshot(getUser(userId));
    }

    /**
     * 手机号脱敏，用于首页等只读展示。
     *
     * @param mobile 原始手机号
     *
     * @return 脱敏手机号
     */
    public String desensitizeMobile(String mobile) {
        if (StringUtils.isBlank(mobile)) {
            return mobile;
        }
        return DesensitizeUtils.desensitize(mobile, DesensitizeUtils.DESENSITIZE_MODE_MOBILE);
    }
}
