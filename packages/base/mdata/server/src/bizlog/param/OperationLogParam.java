package com.guhecloud.cube.biz.mdata.server.bizlog.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2024-11-27  17:29
 * @Description:
 * @Version: 1.0
 */
@Data
public class OperationLogParam extends Paging {
    /**
     * 请求结果
     */
    private String result;
    /**
     * 请求时间
     */
    private Period period;
    /**
     * 请求用户
     */
    private String userName;
    /**
     * 请求方式
     */
    private String method;
}
