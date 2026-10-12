package com.guhecloud.cube.biz.ouaa.server.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import jakarta.validation.constraints.Size;

/**
 * 应用组查询参数
 *
 * @author lanchun
 * @date 2022/5/9 11:09
 */
@Data
public class ApplicationGroupQueryParam extends Paging {
    /**
     * 关键字名称
     */
    @Size(max = 100, message = "关键字名称长度不能大于100")
    private String keyword;
}
