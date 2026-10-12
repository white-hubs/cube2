package com.guhecloud.cube.srv.gen.reportmanagement.server.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

/**
 * @author Appet
 * @Description 工作安排
 * @date 2024-01-04 10:08:36
 */
@Data
public class WorkPlanQueryParam extends Paging {

    //关键字
    private String keyword;
    //类型
    private Long type;
    //发布时间
    private Period period;
    //发布部门ID
    private Long deptId;
    //状态
    private String status;
    //发布人ID
    private Long publisherId;

}