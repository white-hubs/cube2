package com.guhecloud.cube.customization.xialing.server.liangcang.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 粮仓绑定状态分页查询。
 * Query 参数；继承 {@link com.guhecloud.platform.base.model.Paging} 的 pageNumber、pageSize。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RadarBoundPageParam extends Paging {
    /** 透传智慧仓 warehousePage 的 search，用于仓名等模糊匹配。 */
    private String radarName;
    /** 绑定状态：true 已创建，false 未创建；默认 true。 */
    private Boolean bindingStatus;
}
