package com.guhecloud.cube.biz.ouaa.client.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import java.util.Collection;

@Data
public class TenantQueryParam extends Paging {

    /**
     * 租住编号
     */
    private Integer code;

    /**
     * 搜索名称
     */
    private String name;

    /**
     * 租户编号集合
     */
    private Collection<Integer> codes;

}