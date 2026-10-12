package com.guhecloud.cube.srv.gen.device.supervise.server.analysis.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author pengjian
 * @date 2022/9/19 16:17
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DegradationVO {
    private Float value;
    private String time;
}
