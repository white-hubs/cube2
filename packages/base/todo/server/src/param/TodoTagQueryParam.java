package com.guhecloud.cube.biz.todo.server.param;

import lombok.Data;

/**
 * 待办标签查询参数
 *
 * @author lanchun
 * @date 2023/2/27 11:47
 */
@Data
public class TodoTagQueryParam {

    /**
     * 关键字
     */
    private String keyword;
}
