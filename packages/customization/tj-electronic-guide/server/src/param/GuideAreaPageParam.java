package com.guhecloud.cube.srv.tj.electronic.guide.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-08  13:35
 * @Description:
 * @Version: 1.0
 */
@Data
public class GuideAreaPageParam extends Paging {
    /**
     * 是否为正序
     */
    private Boolean sequence;
    /**
     * 状态，默认查询已启用
     */
    private String state;
}
