package com.guhecloud.cube.srv.gen.device.supervise.server.analysis.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author pengjian
 * @date 2022/7/25 18:54
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CallbackResult {
    private Integer score;
    private Long healthValueId;
    private Long deviceId;
    private Integer tenantId;

    public static final CallbackResult FAIL = new CallbackResult(
            null, null, null, null
    );
}
