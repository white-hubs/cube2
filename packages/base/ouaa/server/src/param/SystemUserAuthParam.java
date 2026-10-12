package com.guhecloud.cube.biz.ouaa.server.param;

import com.guhecloud.cube.biz.ouaa.server.enumeration.AuthTypeEnum;

import java.util.Map;

public record SystemUserAuthParam(AuthTypeEnum authType, Map<String, String> authParam) {

}
