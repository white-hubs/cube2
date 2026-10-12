package com.guhecloud.cube.biz.todo.server.param;

import lombok.Data;

/**
 * 待办标签关联业务类型保存参数
 *
 * @author lanchun
 * @date 2023/2/27 13:55
 */
@Data
public class TodoTagReferBusinessTypeModifyParam {

    /**
     * 业务类型
     */
    private String businessType;

}
