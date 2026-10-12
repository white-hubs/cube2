package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param;

import com.guhecloud.cube.srv.gen.maintain.server.common.param.StatisticsQueryParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wql
 * @describe 部门巡检统计
 * @date 2024/1/24 19:39
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InspectDepStatisticsParam extends StatisticsQueryParam {
    private String status;
}
