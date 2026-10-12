package com.guhecloud.cube.srv.tj.electronic.guide.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-08  16:58
 * @Description:
 * @Version: 1.0
 */
@Data
public class PointManagePageParam extends Paging {
    private Long areaId;
    private String keyword;
    /**
     * 状态
     */
    private String state;
}
