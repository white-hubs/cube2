package com.guhecloud.cube.biz.ouaa.server.param;

import com.guhecloud.cube.biz.ouaa.server.enumeration.AuthTypeEnum;
import com.guhecloud.cube.biz.ouaa.server.enumeration.BindTypeEnum;

import java.util.Map;

public record AuthBindParam(AuthTypeEnum authType, BindTypeEnum bindType, String tokenId, Map<String, String> authParam) {

}
