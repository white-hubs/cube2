package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import lombok.Data;

/**
 * @author lrj
 * @Description 报警规则设备绑定
 * @date 2022-07-07 15:12:36
 */
@Data
public class EnergyAlarmRuleVO {

    //
    private Long id;
    //报警规则id
    private Long rulesId;
    //设备id
    private Long deviceId;

}