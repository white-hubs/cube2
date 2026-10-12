package com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo;

import com.guhecloud.platform.base.model.Tree;
import lombok.Data;

@Data
public class AreaListVo extends Tree.Simple<Long, AreaListVo> {
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

    /**
     * 区域组态绑定ID
     */
    private Long roomScadaResourcesId;

    /**
     * 区域ID
     */
    private Long areaId;

    /**
     * 园区管理区域序列号
     */
    private String areaSequence;

    /**
     * 二维组态地址
     */
    private String url;

    //是否绑定二位组态标识
    private Boolean enabled = false;

}
