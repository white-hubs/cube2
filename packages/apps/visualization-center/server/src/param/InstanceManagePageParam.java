package com.guhecloud.cube.base.visualization.server.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

@Data
public class InstanceManagePageParam extends Paging {
    /**
     * 项目id
     */
    private String projectId;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 状态
     */
    private String status;
}
