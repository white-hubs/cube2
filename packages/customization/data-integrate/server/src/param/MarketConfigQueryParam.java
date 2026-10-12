package com.guhecloud.cube.base.dataintegrate.server.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

/**
 * @author Appet
 * @Description 采集市场配置
 * @date 2023-09-09 15:08:36
 */
@Data
public class MarketConfigQueryParam extends Paging {

    //市场名称
    private String keyword;

    /**
     * 是否按照状态排序
     */
    private Boolean statusOrder;
}