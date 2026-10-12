package com.guhecloud.cube.biz.smartcard.server.queryparam;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

@Data
public class CardQueryParam extends Paging {
    //关键字
    private String keyword;

    //卡状态 NORMAL正常 LOSS挂失
    private String status;
}
