package com.guhecloud.cube.biz.todo.server.vo;

import lombok.Data;

/**
 * 标签返回参数
 *
 * @author lanchun
 * @date 2023/2/27 11:44
 */
@Data
public class TodoTagVO {

    /**
     * 标签编号
     */
    private String code;

    /**
     * 标签名称
     */
    private String name;
}
