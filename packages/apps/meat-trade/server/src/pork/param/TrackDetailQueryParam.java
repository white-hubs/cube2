package com.guhecloud.cube.biz.meattrade.server.pork.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品明细查询
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TrackDetailQueryParam extends Paging {

    /**
     * 批次号
     */
    private String code;

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 是否售卖
     */
    private Boolean sell;

    /**
     * 识别结果
     */
    private Boolean result;

    /**
     * 审核是否通过
     */
    private Boolean passed;

}
