package com.guhecloud.cube.srv.gen.conferencemanage.server.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

@Data
public class ConferenceGoodsQueryParam extends Paging {

    /**
     * 物品名称
     */
    private String goodsName;
}
