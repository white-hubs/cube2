package com.guhecloud.cube.property.management.client.property.param.property;

import lombok.Data;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-04-07  09:54
 * @Description:
 * @Version: 1.0
 */
@Data
public class PropertyBimConfigDeleteParam {

    /**
     * 业务主键
     */
    private List<Long> ids;
}
