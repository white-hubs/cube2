package com.guhecloud.cube.biz.smartcard.server.queryparam;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

@Data
public class AccessEventQueryParam extends Paging {
    //关键字
    private String keyword;
    //卡号
    private String cardNo;
    //时间段
    private Period period;
    //时间段2
    private Period period2;
    //事件类型
    private String accessType;
    //状态
    private String status;
}
