package com.guhecloud.cube.srv.gen.device.supervise.server.analysis.domain.worker;

import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.domain.CallbackResult;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.domain.Worker;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Requester;
import com.guhecloud.platform.security.model.Principal;
import lombok.AllArgsConstructor;

/**
 * @author pengjian
 * @date 2022/8/1 13:39
 * @description
 */
@AllArgsConstructor
public abstract class HealthyWorker extends Worker {
    protected Long deviceId;
    protected Long healthValueId;
    protected Integer tenantId;
    protected Requester requester;
    protected Principal principal;

    @Override
    protected Integer computingScore() {
        SessionContextHolder.requestTenant(tenantId);
        Integer score = 0;
        try {
            score = run();
            this.callbackResult = new CallbackResult();
            this.callbackResult.setDeviceId(deviceId);
            this.callbackResult.setHealthValueId(healthValueId);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        remind();
        return score;
    }

    protected abstract Integer run();

}
