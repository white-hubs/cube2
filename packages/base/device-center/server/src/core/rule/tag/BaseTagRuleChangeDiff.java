package com.guhecloud.cube.base.device.center.core.rule.tag;

import com.guhecloud.cube.base.device.center.core.rule.entity.RuleGroup;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.Collections;
import java.util.Set;

public abstract class BaseTagRuleChangeDiff {

    protected abstract Set<Long> getStayOldDeviceIds(String tagCode);

    protected abstract Set<Long> getStayNewDeviceIds(String tagCode, RuleGroup ruleGroup);

    public Set<Long> removeDeviceIds(String tagCode, RuleGroup ruleGroup) {
        Set<Long> stayOldDeviceIds = getStayOldDeviceIds(tagCode);
        Set<Long> stayNewDeviceIds = getStayNewDeviceIds(tagCode, ruleGroup);
        if (CollectionUtils.isEmpty(stayNewDeviceIds)) {
            return stayOldDeviceIds;
        }
        if (CollectionUtils.isEmpty(stayOldDeviceIds)) {
            return Collections.emptySet();
        }
        stayOldDeviceIds.removeAll(stayNewDeviceIds);
        return stayOldDeviceIds;
    }

    public Set<Long> newDeviceIds(String tagCode, RuleGroup ruleGroup) {
        Set<Long> stayOldDeviceIds = getStayOldDeviceIds(tagCode);
        Set<Long> stayNewDeviceIds = getStayNewDeviceIds(tagCode, ruleGroup);
        if (CollectionUtils.isEmpty(stayOldDeviceIds)) {
            return stayNewDeviceIds;
        }
        if (CollectionUtils.isEmpty(stayNewDeviceIds)) {
            return Collections.emptySet();
        }
        stayNewDeviceIds.removeAll(stayOldDeviceIds);
        return stayNewDeviceIds;
    }
}
