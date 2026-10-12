package com.guhecloud.cube.srv.gen.device.supervise.server.analysis.application.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

/**
 * @author pengjian
 * @date 2022/8/5 14:17
 * @description
 */
@Data
public class DeviceInstanceHealthAlarmRulePreviewQuery extends Paging {
    private String keyword;
}
