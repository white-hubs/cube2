package com.guhecloud.cube.sn.meattrade.plugin.dbc.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class StatQueryParam extends Paging {

    /**
     * 检测项编号
     */
    private String itemCode;

    /**
     * 时间类别
     */
    private String type;

    /**
     * 时间范围
     */
    private Period period;

    public Integer getDay() {
        if ("TODAY".equals(type)) {
            return 1;
        } else if ("WEEK".equals(type)) {
            return 7;
        } else if ("MONTH".equals(type)) {
            return 30;
        } else if ("YEAR".equals(type)) {
            return 365;
        }
        return 1;
    }

    public String getTimeRange() {
        return "近" + getDay() + "天";
    }
}
