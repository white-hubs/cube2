package com.guhecloud.cube.base.dataintegrate.client.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/9 16:34
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
public class AlarmMarketTimeoutCountVO extends AlarmTimeoutCountVO {
    private String marketTenantID;

    private String marketName;
}
