package com.guhecloud.cube.biz.fusion.communicate.server.third.tencent.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 移除用户响应信息
 *
 * @author lanchun
 * @date 2025/2/11 14:52
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RemoveUserResponse {

    /**
     * 请求id
     */
    @JsonProperty(value = "RequestId")
    private String requestId;
}
