package com.guhecloud.cube.srv.gen.energymanagement.server.param;

import lombok.Data;

import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * @author lrj
 * @Description 能耗分项
 * @date 2022-07-04 15:12:36
 */
@Data
public class EnergyProjectParam {

    /**
     * 分项名称
     */
    @Size(max = 20, message = "类型名称不能大于20")
    private String projectName;
    /**
     * 仪表类型
     */
    private String instrumentType;

    /**
     * 备注
     */
    @Size(max = 200, message = "备注不能大于200")
    private String remark;

    //父级ID
    private Long parentId;

    //编号
    private String code;

    /**
     * 绑定计量设备信息
     */
    private List<EnergyProjectItemParam> energyProjectItemParams;

}