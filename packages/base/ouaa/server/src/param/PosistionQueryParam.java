package com.guhecloud.cube.biz.ouaa.server.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import jakarta.validation.constraints.Size;

/**
 * 岗位查询参数
 *
 * @author Woody
 * @date 2021/7/16
 */
@Data
public class PosistionQueryParam extends Paging {
    /**
     * 岗位名称
     */
    @Size(max = 100, message = "职位名称长度不能大于100")
    private String name;

    /**
     * 是否启用
     */
    private Boolean enabled;
}
