package com.guhecloud.cube.biz.ouaa.client.dto;

import com.guhecloud.platform.base.model.Tree;
import lombok.Data;

/**
 * @author lzy
 * @date 2021/9/14
 */
@Data
public class OrganizationDTO extends Tree.Simple<Long, OrganizationDTO> {
    /**
     * 主键
     */
    private Long id;
    /**
     * 组织机构名称
     */
    private String name;

    /**
     * 组织机构备注
     */
    private String remark;

    /**
     * 组织机构成员数量
     */
    private Integer members;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 序列号
     */
    private String sequence;
}
