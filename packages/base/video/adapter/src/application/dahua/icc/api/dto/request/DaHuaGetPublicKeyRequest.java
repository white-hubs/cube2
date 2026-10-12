package com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.constants.DaHuaApiConstant.PUBLIC_TOKEN;

/**
 * 获取公钥请求
 *
 * @author lanchun
 * @date 2025/5/16 14:00
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DaHuaGetPublicKeyRequest extends AbstractDaHuaRequest {

    @Override
    public String getRequestUrl() {
        return getRequestUrl(PUBLIC_TOKEN).build();
    }
}
