package com.guhecloud.cube.srv.electron.guide.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-08  13:57
 * @Description:
 * @Version: 1.0
 */
@Data
public class LayerManagePageParam extends Paging {

    /**
     * 区域主键
     */
    private String areaId;
}
