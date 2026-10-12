package com.guhecloud.cube.biz.mdata.client.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Size;

/**
 * 档位分类查询信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StallCategoryQueryParam {

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