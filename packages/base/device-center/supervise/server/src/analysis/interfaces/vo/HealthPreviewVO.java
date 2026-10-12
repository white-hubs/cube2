package com.guhecloud.cube.srv.gen.device.supervise.server.analysis.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author pengjian
 * @date 2022/8/10 14:43
 * @description
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthPreviewVO {
    private Integer totalNumber = 0;
    private Integer bestNumber;
    private Integer normalNumber;
    private Integer subNormalNumber;
    private Integer unhealthyNumber;

    private Float bestRate;
    private Float normalRate;
    private Float subNormalRate;
    private Float unhealthyRate;

    private Integer healthValue;
}
