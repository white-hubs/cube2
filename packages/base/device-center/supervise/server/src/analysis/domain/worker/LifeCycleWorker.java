package com.guhecloud.cube.srv.gen.device.supervise.server.analysis.domain.worker;

import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.application.dto.DeviceLifeCycleDTO;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.application.service.DeviceLifeCycleService;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.infrastructure.dao.DeviceHealthProblemMapper;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.infrastructure.po.DeviceHealthProblemPO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Requester;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;

import java.text.DecimalFormat;
import java.time.LocalDateTime;

/**
 * @author pengjian
 * @date 2022/8/1 13:27
 * @description 生命周期
 */
public class LifeCycleWorker extends HealthyWorker {

    private float number;

    public LifeCycleWorker(Long deviceId, Long healthValueId, Integer tenantId, Requester requester, Principal principal) {
        super(deviceId, healthValueId, tenantId, requester, principal);
    }

    @Override
    protected Integer run() {
        SessionContextHolder.setRequester(requester);
        SecurityContextHolder.setPrincipal(principal);
        SessionContextHolder.requestTenant(tenantId);
        /**
         *1. 读取设备的报废时间和创建时间，并计算出使用寿命
         *2. 计算当前时间和设备创建时间的差值 占 使用寿命的百分之几
         *3. 再计算分数
         */

        DeviceLifeCycleService deviceLifeCycleService = ApplicationContextHolder.getBean(DeviceLifeCycleService.class);
        DeviceLifeCycleDTO deviceLifeCycleDTO = deviceLifeCycleService.get(this.deviceId);

        var planScrappedTime = deviceLifeCycleDTO.getStopTime();
        var createTime = deviceLifeCycleDTO.getCreateTime();
        long nh = 1000 * 60 * 60;
        long lifeHour = (DateUtils.toTimestamp(planScrappedTime) - DateUtils.toTimestamp(createTime)) / nh;
        var date = LocalDateTime.now();
        long usedHour = Math.abs((DateUtils.toTimestamp(date) - DateUtils.toTimestamp(createTime)) / nh);
        Float rate = (usedHour * 1.0F) / lifeHour;
        DecimalFormat b = new DecimalFormat("0.00");
        String format = b.format(rate);
        float formatRate = Float.parseFloat(format);

        // 每递进百分之 ？ 扣 ？ 分
        float number = (formatRate * 100) / 10;
        this.number = number;

        float score = number * 3;

        return (int) score;
    }

    @Override
    protected void remind() {
        if (number > 1) {
            DeviceHealthProblemPO deviceHealthProblemPO = new DeviceHealthProblemPO();
            deviceHealthProblemPO.setDeviceHealthValueId(healthValueId);
            deviceHealthProblemPO.setDesc("设备运行时间已经超过生命有效周期10%");

            DeviceHealthProblemMapper problemMapper = ApplicationContextHolder.getBean(DeviceHealthProblemMapper.class);
            problemMapper.insert(deviceHealthProblemPO);
        }

    }
}
