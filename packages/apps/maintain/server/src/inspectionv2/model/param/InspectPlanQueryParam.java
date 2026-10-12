package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class InspectPlanQueryParam extends Paging {

    private String keyword;

    //任务类型
    private String typeId;

    //1开启 0关闭
    private Boolean enable;

    //任务执行状态
    private String status;

    //单次循环
    private Boolean cycle;

    //创建人
    private String createBy;

}
