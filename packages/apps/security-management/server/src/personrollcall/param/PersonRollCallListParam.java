package com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.param;

import com.guhecloud.platform.base.model.Period;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonRollCallListParam {

    /**
     * 时间范围
     */
    private Period callTimePeriod;

    /**
     * 时间类型 TODAY:'今日',THIS_WEEK:'本周', THIS_MONTH:'本月',THIS_YEAR:'今年'
     */
    private String type;
}
