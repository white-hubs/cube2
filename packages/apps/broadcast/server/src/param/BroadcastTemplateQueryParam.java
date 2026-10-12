package com.guhecloud.cube.biz.subsystem.broadcast.server.param;

import lombok.Data;

import jakarta.validation.constraints.Size;

/**
 * 广播模板列表查询参数
 *
 * @author lanchun
 * @date 2022/12/12 15:32
 */
@Data
public class BroadcastTemplateQueryParam {

    /**
     * 关键字
     * 可通过模板名称搜索
     */
    @Size(max = 100, message = "关键字长度不能大于100")
    private String keyword;
}
