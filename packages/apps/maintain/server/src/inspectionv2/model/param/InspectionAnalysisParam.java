package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param;

import com.guhecloud.platform.base.model.Period;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wql
 * @describe 巡检分析请求参数
 * @date 2023/4/18 9:51
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InspectionAnalysisParam {
    /**
     * 查询时间
     */
    private Period period;
    /**
     * 时间类型
     */
    private String timeFlag;
    /**
     * 对象类型
     */
    private String objType;

}
