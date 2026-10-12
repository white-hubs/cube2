package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import lombok.Data;

/**
 * @author lrj
 * @Description 能耗开关配置
 * @date 2022-07-04 15:12:36
 */
@Data
public class EnergyGateVO {
    //
    private Long id;
    //能耗类型
    private String energyType;
    //开关
    private Boolean open;

}