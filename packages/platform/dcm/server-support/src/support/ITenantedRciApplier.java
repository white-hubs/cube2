package com.guhecloud.platform.dcm.support;

import com.guhecloud.platform.dcm.entity.RequestedConfigItem;

import java.util.List;

public interface ITenantedRciApplier {
    List<RequestedConfigItem> apply(List<RequestedConfigItem> rcis, Long tenant);
}
