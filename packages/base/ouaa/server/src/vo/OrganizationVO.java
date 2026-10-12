package com.guhecloud.cube.biz.ouaa.server.vo;

import com.guhecloud.platform.base.model.Tree;
import lombok.Data;

/**
 * 组织机构信息
 *
 * @author Woody
 * @date 2021/6/25
 */
@Data
public class OrganizationVO extends Tree.Simple<Long, OrganizationVO> {
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
