package com.guhecloud.cube.srv.gen.device.supervise.server.analysis.interfaces.vo;

import lombok.Data;

/**
 * @author pengjian
 * @date 2022/8/10 14:31
 * @description
 */
@Data
public class HealthLevelVO {
    private Integer bestNumber = 0;
    private Integer normalNumber = 0;
    private Integer subNormalNumber = 0;
    private Integer unhealthyNumber = 0;

    private Float bestRate;
    private Float normalRate;
    private Float subNormalRate;
    private Float unhealthyRate;
}
