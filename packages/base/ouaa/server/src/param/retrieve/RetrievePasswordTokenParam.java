package com.guhecloud.cube.biz.ouaa.server.param.retrieve;

import com.guhecloud.cube.biz.ouaa.server.enumeration.AuthTypeEnum;

public record RetrievePasswordTokenParam(AuthTypeEnum type, String tel, String code) {

}
