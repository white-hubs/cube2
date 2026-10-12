package com.guhecloud.cube.biz.todo.server.param;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/11/4 11:58
 * @Description:
 */
@Data
public class TodoHomePageParam {

    /**
     * 类型
     */
    private String type;

    /**
     * 日期
     */
    private Period period;

}
