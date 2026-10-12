package com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.vo;

import lombok.Data;

import java.util.List;

@Data
public class AreaOptionVO {
    /**
     * mdata 区域主键。
     */
    private Long id;

    /**
     * 父级区域主键。
     */
    private Long parentId;

    /**
     * 区域层级。
     */
    private Integer level;

    /**
     * 区域名称。
     */
    private String name;

    /**
     * 区域编码。
     */
    private String code;

    /**
     * 区域 sequence，新增或编辑重点人员时提交该值。
     */
    private String sequence;

    /**
     * 下级区域列表。
     */
    private List<AreaOptionVO> children;
}
