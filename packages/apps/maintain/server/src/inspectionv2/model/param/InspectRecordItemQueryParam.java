package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

@Data
public class InspectRecordItemQueryParam {
    /**
     * 时间段条件(创建时间就是巡检时间)
     */
    private Period period;

    /**
     * 项是否异常
     */
    private Boolean itemErrorValueFlag;

    /**
     * 巡检记录id(对应InspectRecordPO的id)
     */
    private Long recordId;
}
