package com.guhecloud.cube.base.device.center.visualization.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import jakarta.validation.constraints.Size;

import java.util.List;

@Data
public class VisualizationQueryParam extends Paging {
    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;
    /**
     * 时间类型 TODAY:'今日',LAST_WEEK:'上周',THIS_WEEK:'本周', THIS_MONTH:'本月',THIS_YEAR:'今年'
     */
    private String type;

    /**
     * 时间范围
     */
    private Period period;

    /**
     * 设备类型主键集合
     */
    private List<Long> deviceTypes;

    /**
     * 应用编号
     */
    private String applicationCode;
    //时间标识
    private String timeFormat;
}
