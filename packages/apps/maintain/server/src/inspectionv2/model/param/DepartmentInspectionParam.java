package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wql
 * @describe 部门巡检统计请求参数
 * @date 2023/4/17 11:33
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DepartmentInspectionParam extends Paging {
    /**
     * 查询时间
     */
    private Period period;
    /**
     * 部门名称
     */
    private String depName;

    /**
     * 人员/部门名称模糊匹配
     */
    private String name;
}
