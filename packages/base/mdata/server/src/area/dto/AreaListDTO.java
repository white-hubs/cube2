package com.guhecloud.cube.biz.mdata.server.area.dto;

import com.guhecloud.platform.base.model.Tree;
import lombok.Data;

/**
 * 区域信息
 */
@Data
public class AreaListDTO extends Tree.Simple<Long, AreaListDTO> {

    /**
     * 区域名称
     */
    private String name;

    /**
     * 区域编码
     */
    private String code;

    /**
     * 序列号
     */
    private String sequence;
}
