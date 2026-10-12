package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.BigDecimalSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author lrj
 * @Description 能耗分项
 * @date 2022-07-04 15:12:36
 */
@Data
public class EnergyProjectVO {

    private Long id;
    //分项名称
    private String projectName;
    //仪表类型
    private String instrumentType;
    private String instrumentTypeName;
    //备注
    private String remark;
    //父级ID
    private Long parentId;
    private String parentName;

    //编号
    private String code;
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
}