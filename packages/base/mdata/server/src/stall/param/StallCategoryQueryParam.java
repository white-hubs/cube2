package com.guhecloud.cube.biz.mdata.server.stall.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.Size;

/**
 * 档位分类查询信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StallCategoryQueryParam extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "档位分类不能大于100")
    private String keyword;

    /**
     * 是否启用
     */
    private Boolean enabled;

}
