package com.guhecloud.cube.srv.gen.energymanagement.server.queryparam;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

/**
 * @author lrj
 * @Description 报警规则设备绑定
 * @date 2022-07-07 15:12:36
 */
@Data
public class EnergyAlarmRuleQueryParam extends Paging {

    //报警规则id
    private String rulesId;
    //设备id
    private String deviceId;

}