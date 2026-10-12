package com.guhecloud.cube.base.flow.dcm.service;

import com.guhecloud.platform.dcm.entity.RequestedConfigItem;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-04-25  19:41
 * @Description:
 * @Version: 1.0
 */
public interface FlowDcmService {
    List<RequestedConfigItem> fetchRuntimeAppGroupConfig(Long tenant);

    List<RequestedConfigItem> applyAppGroupConfig(List<RequestedConfigItem> diffs2Apply, Long tenant);

}
