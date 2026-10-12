package com.guhecloud.cube.srv.gen.loose.works.dto;

import com.guhecloud.cube.base.flow.client.param.query.ProcessInstancePageQuery;
import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/3/15 15:52
 * @Description:
 */
@Data
public class ProjectReformQueryParam extends ProcessInstancePageQuery {

    /**
     * 整改对象id
     */
    private String projectScheduleId;
}
