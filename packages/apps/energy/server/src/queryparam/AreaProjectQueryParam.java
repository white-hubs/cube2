package com.guhecloud.cube.srv.gen.energymanagement.server.queryparam;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

/**
 * @author lrj
 * @Description 区域分项配置
 * @date 2022-07-04 15:12:36
 */
@Data
public class AreaProjectQueryParam extends Paging {

    //区域名称
    private String areaName;
    //备注
    private String remark;
    //仪表类型
    //    @NotEmpty(message = "仪表类型不能为空")
    private String instrumentType;
    //父级ID
    private Long parentId;

}