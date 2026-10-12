package com.guhecloud.cube.biz.emergency.server.param;

import lombok.Data;

import jakarta.validation.constraints.Size;

/**
 * 预案类型查询参数
 *
 * @author lanchun
 * @date 2022/9/2 10:40
 */
@Data
public class EmergencyTypeQueryParam {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字长度不能大于100")
    private String keyword;

    /**
     * 应用编号
     */
    @Size(max = 100, message = "应用编号长度不能大于100")
    private String applicationCode;
}
