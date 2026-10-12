package com.guhecloud.cube.srv.gen.business.info.sync.server.controller.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/1/20 17:54
 * @Description:
 */
@Data
public class OrganizationInfoVO {
    /**
     * 部门主键，唯一标识一个部门
     */
    private Long id;

    /**
     * 部门名称，表示部门的名称
     */
    private String name;

    /**
     * 所在层级，表示部门在组织结构中的层级
     */
    private Integer level;

    /**
     * 父级部门id，表示该部门的直接上级部门的id
     */
    private Long parentId;

    /**
     * 部门全路径id，表示从根部门到当前部门的完整路径的id组合
     */
    private String orgPathId;

    /**
     * 部门全路径名称，表示从根部门到当前部门的完整路径的名称组合
     */
    private String orgPathName;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
