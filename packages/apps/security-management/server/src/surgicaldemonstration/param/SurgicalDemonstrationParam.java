package com.guhecloud.cube.srv.gen.securitymanagement.server.surgicaldemonstration.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

@Data
public class SurgicalDemonstrationParam extends Paging {
    /**
     * 时间类型 TODAY:'今日',THIS_WEEK:'本周', THIS_MONTH:'本月',THIS_YEAR:'今年'
     */
    private String type;

    /**
     * 场景类型
     */
    private Long sceneType;

    /**
     * 区域主键
     */
    private Long areaId;

    /**
     * 时间范围
     */
    private Period period;

    private String keyword;

}
