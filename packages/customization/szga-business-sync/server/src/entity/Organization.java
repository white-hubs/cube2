package com.guhecloud.cube.srv.gen.business.info.sync.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 组织机构信息表
 *
 * @TableName organization
 */
@Data
@TableName("organization")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Organization implements Serializable {

    private static final long serialVersionUID = -5596509551814204156L;
    /**
     * 主键
     */
    private Long id;

    /**
     * 组织名称
     */
    private String name;

    /**
     * 组织备注
     */
    private String remark;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 组织层级
     */
    private Integer level;

    /**
     * 上级组织主键
     */
    private Long parentId;

    /**
     * 组织机构路径id
     */
    private String orgPathId;

    /**
     * 组织机构路径名称
     */
    private String orgPathName;

    /**
     * 是否已删除
     */
    private Long deleted;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 租户id
     */
    private Integer tenant;

}