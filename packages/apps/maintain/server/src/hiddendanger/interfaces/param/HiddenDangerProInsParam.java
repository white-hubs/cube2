package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.param;

import com.guhecloud.cube.base.flow.client.param.query.ProcessInstancePageQuery;
import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/11/11 17:21
 * @Description: 隐患排查查询参数扩展
 */

@Data
public class HiddenDangerProInsParam extends ProcessInstancePageQuery {
    /**
     * 隐患对象Id
     */
    private Long hiddenObjId;
}
