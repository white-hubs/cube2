package com.guhecloud.cube.srv.gen.device.supervise.server.analysis.domain;

/**
 * @author pengjian
 * @date 2022/8/1 17:30
 * @description
 */
public class HealthCallBackResult extends CallbackResult {
    public HealthCallBackResult(Integer score, Long healthValueId, Long deviceId, Integer tenantId) {
        super(score, healthValueId, deviceId, tenantId);
    }

}
