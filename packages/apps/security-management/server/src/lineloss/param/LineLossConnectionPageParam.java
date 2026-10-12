package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 线损线路分页查询参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LineLossConnectionPageParam extends Paging {

    /**
     * 线路名称（支持模糊查询）
     */
    private String lineName;
}
