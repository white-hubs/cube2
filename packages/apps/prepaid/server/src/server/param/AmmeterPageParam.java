package com.guhecloud.cube.srv.prepaid.server.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 电表分页入参
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AmmeterPageParam extends Paging {
    /**
     * 电表编号
     */
    private String meterId;
    /**
     * 是否失联
     */
    private Boolean unConnect;
    /**
     * 是否欠费（true表示欠费）
     */
    private Boolean oweMoney;
}
