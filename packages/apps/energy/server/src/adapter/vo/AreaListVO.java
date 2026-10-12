package com.guhecloud.cube.srv.gen.energymanagement.server.adapter.vo;

import lombok.Data;

import java.util.List;

/**
 * 区域信息
 */
@Data
public class AreaListVO {

    private String id;
    /**
     * 区域名称
     */
    private String areaName;

    /**
     * 区域编码
     */
    private String code;

    private String parentId;

    /**
     * 序号
     */
    private Integer sort;
    //标识是否是设备节点 true 是 其他为否
    private Boolean deviceFlag;

    private List<AreaListVO> children;

}
