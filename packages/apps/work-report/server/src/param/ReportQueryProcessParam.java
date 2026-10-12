package com.guhecloud.cube.srv.gen.reportmanagement.server.param;

import com.guhecloud.cube.base.flow.client.param.query.ProcessInstancePageQuery;
import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/6/26 16:01
 * @Description: 工作汇报查询参数
 */

@Data
public class ReportQueryProcessParam extends ProcessInstancePageQuery {
    /**
     * 汇报类型id
     */
    private Long reportTypeId;
    /**
     * 空类型
     */
    private Boolean isNoType = false;
}
