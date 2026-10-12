package com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo;

import lombok.Data;

/**
 * 安全设备应用分组统计数量
 *
 * @author lanchun
 * @date 2023/5/12 17:42
 */
@Data
public class SecurityDeviceApplicationCodeStatisticsVO {

    private String applicationCode;

    private String applicationName;

    //数量
    private Integer num;
}
