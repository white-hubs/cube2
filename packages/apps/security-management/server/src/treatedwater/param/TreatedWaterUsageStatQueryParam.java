package com.guhecloud.cube.srv.gen.securitymanagement.server.treatedwater.param;

import com.guhecloud.cube.srv.gen.securitymanagement.server.enumeration.PeriodStatTypeEnum;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import com.guhecloud.platform.base.util.StringUtils;

/**
 * @author Appet
 * @Description 净化水使用量-小时表
 * @date 2023-10-30 15:08:36
 */
@Data
public class TreatedWaterUsageStatQueryParam {
    //净化水的类型
    private String waterType;
    //日期类型：TODAY:'今日',THIS_WEEK:'本周', THIS_MONTH:'本月',THIS_YEAR:'今年'
    private String type;
    //时间区间
    private Period period;
    //时间格式
    private String dateFormat;

    public void setDateFormat() {
        if (StringUtils.isNotEmpty(type)) {
            this.dateFormat = PeriodStatTypeEnum.valueOf(type).dateFormat;
        }
    }

    public void setType(String type) {
        this.type = type;
        this.setDateFormat();
    }
}