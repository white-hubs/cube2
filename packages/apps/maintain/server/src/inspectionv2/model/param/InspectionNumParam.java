package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 巡检次数查询参数
 * @date 2023/4/17 19:36
 */
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InspectionNumParam extends Paging {
    /**
     * 查询时间
     */
    private Period period;
    /**
     * 部门id
     */
    private Long depId;
    /**
     * 人员id
     */
    private Long staffId;
    /**
     * 对象id
     */
    private Long objId;
}
