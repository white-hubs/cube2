package com.guhecloud.cube.srv.gen.energymanagement.server.param;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * @author lrj
 * @Description 支路分项配置
 * @date 2022-07-04 15:12:36
 */
@Data
public class RouteProjectParam {

    //区域名称
    @Size(max = 20, message = "名称不能大于20")
    private String routeName;
    //备注
    @Size(max = 200, message = "备注不能大于200")
    private String remark;
    //仪表类型
    @NotEmpty(message = "仪表类型不能为空")
    private String instrumentType;
    //父级ID
    private Long parentId;
    /**
     * 绑定计量设备信息
     */
    private List<EnergyProjectItemParam> energyProjectItemParams;
}