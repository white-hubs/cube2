package com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.interfaces.controller;

import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.application.querygateway.DeviceNotifyRuleTriggerRecordQueryService;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.application.querygateway.query.DeviceNotifyRuleTriggerRecordQuery;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.interfaces.vo.DeviceNotifyRuleTriggerRecordVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceNotifyRuleTriggerRecordController {

    private final DeviceNotifyRuleTriggerRecordQueryService deviceNotifyRuleTriggerRecordQueryService;

    /**
     * 查询设备通知规则触发记录分页
     *
     * @param query 查询参数
     *
     * @return 设备通知规则触发记录分页
     */
    @GetMapping("/notify-rule/trigger-records")
    public Pagination<DeviceNotifyRuleTriggerRecordVO> page(DeviceNotifyRuleTriggerRecordQuery query) {
        return deviceNotifyRuleTriggerRecordQueryService.getPage(query);
    }

}
