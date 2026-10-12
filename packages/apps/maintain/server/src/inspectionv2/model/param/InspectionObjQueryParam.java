package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wql
 * @describe 巡检对象统计查询参数
 * @date 2023/4/18 8:51
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InspectionObjQueryParam extends Paging {
    /**
     * 查询时间
     */
    private Period period;
    /**
     * 对象类型
     */
    private String objType;
    /**
     * 区域
     */
    private String area;
    /**
     * 对象名称
     */
    private String objName;
}
