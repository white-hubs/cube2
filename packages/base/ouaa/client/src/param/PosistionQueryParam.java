package com.guhecloud.cube.biz.ouaa.client.param;

import lombok.Builder;
import lombok.Data;

/**
 * 岗位查询参数
 *
 * @author Woody
 * @date 2021/7/16
 */
@Data
@Builder
public class PosistionQueryParam {
    /**
     * 岗位名称
     */
    private String name;

    /**
     * 是否启用
     */
    private Boolean enabled;
}
