package com.guhecloud.cube.base.visualization.server.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2023-08-29  10:58
 * @Description:
 * @Version: 1.0
 */
@Data
public class ModelManagePageParam extends Paging {
    /**
     * 模型名称
     */
    private String name;
    /**
     * 模型id
     */
    private String id;
    /**
     * 模型类型
     */
    private String modelType;
}
