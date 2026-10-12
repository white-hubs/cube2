package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.BigDecimalSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class AreaStatisticsVo {
    private Long id;
    //区域名称
    private String areaName;
    //备注
    private String remark;
    //仪表类型
    private String instrumentType;
    //父级
    private String parentId;
    //层级
    private String sequence;

    private String areaId;

    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal value;

    private Integer tenant;

    private Integer level;
    //子集
    private List<AreaStatisticsVo> children;

}
