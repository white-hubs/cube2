package com.guhecloud.cube.biz.mdata.server.area.param;

import lombok.Data;

import jakarta.validation.constraints.Size;

/**
 * 区域查询信息
 */
@Data
public class AreaGroupQueryParam {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;
}
