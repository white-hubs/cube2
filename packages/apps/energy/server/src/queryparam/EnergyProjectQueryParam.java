package com.guhecloud.cube.srv.gen.energymanagement.server.queryparam;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

/**
 * @author lrj
 * @Description 能耗分项
 * @date 2022-07-04 15:12:36
 */
@Data
public class EnergyProjectQueryParam extends Paging {

    //分项名称
    private String projectName;
    //仪表类型
    private String instrumentType;
    //备注
    private String remark;

    private String timeFlag;

    private String period;
    //父级节点
    private Long parentId;

}