package com.guhecloud.cube.srv.electron.guide.param;

import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-08  13:35
 * @Description:
 * @Version: 1.0
 */
@Data
public class GuideAreaQueryParam {
    /**
     * 是否为正序
     */
    private Boolean sequence;

    /**
     * 状态，默认查询已启用
     */
    private String state;
}
