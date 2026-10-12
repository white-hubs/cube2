package com.guhecloud.cube.biz.ouaa.server.vo;

import com.guhecloud.cube.biz.ouaa.server.entity.User;

public record UserTenantVO(Long id, Integer tenant, Long userId, Boolean enabled, Boolean admin, User user) {
}
