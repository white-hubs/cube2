package com.guhecloud.cube.biz.todo.server.vo;

import lombok.Data;

/**
 * 待办业务类型返回参数
 *
 * @author lanchun
 * @date 2022/7/22 11:42
 */
@Data
public class TodoBusinessTypeVO {

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 业务类型名称
     */
    private String businessTypeName;
}
