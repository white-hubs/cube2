package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import lombok.Data;

import java.util.List;

/**
 * @author lrj
 * @Description 支路分项配置
 * @date 2022-07-04 15:12:36
 */
@Data
public class RouteProjectVO {

    //
    private Long id;
    //区域名称
    private String routeName;
    //备注
    private String remark;
    //仪表类型
    private String instrumentType;
    /**
     * 仪表数
     */
    private Long metersNum;
    /**
     * 绑定设备类型
     */
    private List<EnergyProjectItemVO> energyProjectItemVOS;

}