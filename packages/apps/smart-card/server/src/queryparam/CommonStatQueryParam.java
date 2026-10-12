package com.guhecloud.cube.biz.smartcard.server.queryparam;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

@Data
public class CommonStatQueryParam {

    private String type;//时间类型

    private Period period;
    //统计的时间按格式，例如：%Y-%m-%d
    private String groupFormat;

}
