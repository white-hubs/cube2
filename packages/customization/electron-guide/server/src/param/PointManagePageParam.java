package com.guhecloud.cube.srv.electron.guide.param;

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

    /**
     * 区域主键
     */
    private Long areaId;

    /**
     * 是否精准匹配区域
     */
    private Boolean areaAccurateMatching;

    private String keyword;
    /**
     * 状态
     */
    private String state;

    /**
     * 类型主键
     */
    private Long typeId;

    private String areaIdStr;
}
