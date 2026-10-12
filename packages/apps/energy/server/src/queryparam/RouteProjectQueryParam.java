package com.guhecloud.cube.srv.gen.energymanagement.server.queryparam;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;

/**
 * @author lrj
 * @Description 支路分项配置
 * @date 2022-07-04 15:12:36
 */
@Data
public class RouteProjectQueryParam extends Paging {

    //区域名称
    private String routeName;
    //备注
    private String remark;
    //仪表类型
    @NotEmpty(message = "仪表类型不能为空")
    private String instrumentType;
    //父级ID
    private Long parentId;

}