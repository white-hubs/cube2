package com.guhecloud.cube.base.device.center.core.rule.tag;

import com.guhecloud.cube.base.device.center.core.param.DeviceCustomerPageParam;
import com.guhecloud.cube.base.device.center.core.rule.entity.RuleGroup;
import com.guhecloud.cube.base.device.center.core.service.CenterDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
public class DeviceTagRuleChangeDiff extends BaseTagRuleChangeDiff {

    private final CenterDeviceService centerDeviceService;

    public DeviceTagRuleChangeDiff(CenterDeviceService centerDeviceService) {this.centerDeviceService = centerDeviceService;}

    @Override
    protected Set<Long> getStayOldDeviceIds(String tagCode) {
        return centerDeviceService.deviceIdsByTagCode(tagCode);
    }

    @Override
    protected Set<Long> getStayNewDeviceIds(String tagCode, RuleGroup ruleGroup) {
        DeviceCustomerPageParam param = new DeviceCustomerPageParam();
        param.setRuleGroup(ruleGroup);
        param.setPageNumber(1);
        param.setPageSize(50000);
        return centerDeviceService.deviceIdsByRuleCondition(param);
    }
}
