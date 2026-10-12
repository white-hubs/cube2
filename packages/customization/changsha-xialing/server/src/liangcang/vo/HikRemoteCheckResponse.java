package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 海康门禁远程核验响应。
 *
 * @param remoteCheck 远程核验结果
 */
public record HikRemoteCheckResponse(
        @JsonProperty("RemoteCheck") HikRemoteCheckResultResponse remoteCheck
) {
}
