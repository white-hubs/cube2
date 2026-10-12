package com.guhecloud.cube.biz.ouaa.server.dto;

import com.guhecloud.cube.biz.ouaa.server.enumeration.AuthTypeEnum;

public record AuthInfoCacheDTO(Long userId, AuthTypeEnum authType, String payload) {
}
