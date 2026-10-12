package com.guhecloud.platform.redis.authority.message;

import lombok.Data;

import java.util.Set;

@Data
public class AuthorityMessage {

    /**
     * 角色主键
     */
    private Long roleId;

    /**
     * 资源列表
     */
    private Set<String> resources;
}
