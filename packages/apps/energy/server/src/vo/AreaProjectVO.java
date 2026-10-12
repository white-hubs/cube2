package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.BigDecimalSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author lrj
 * @Description 区域分项配置
 * @date 2022-07-04 15:12:36
 */
@Data
public class AreaProjectVO {

    //
    private Long id;
    //区域名称
    private String areaName;
    //备注
    private String remark;
    //仪表类型
    private String instrumentType;
    /**
     * 仪表数
     */
    private Long metersNum;
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal value;
    /**
     * 绑定设备类型
     */
    private List<EnergyProjectItemVO> energyProjectItemVOS;

    private List<AreaProjectVO> children;

}