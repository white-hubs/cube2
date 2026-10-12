package com.guhecloud.cube.biz.mdata.server.area.vo;

import com.guhecloud.platform.base.model.Tree;
import lombok.Data;

/**
 * 区域信息
 */
@Data
public class AreaGroupVO extends Tree.Simple<Long, AreaGroupVO> {

    /**
     * 主键
     */
    private Long id;

    /**
     * 区域名称
     */
    private String name;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 区域数量
     */
    private Long areaCount;

    /**
     * 序列号
     */
    private String sequence;
}
