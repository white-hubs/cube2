package com.guhecloud.cube.srv.gen.business.info.sync.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 访客表
 *
 * @TableName provisional_user
 */
@Data
@TableName("provisional_user")
public class ProvisionalUser implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 移动电话
     */
    private String mobile;

    /**
     * 用户性别
     */
    private String gender;

    /**
     * 证件类型
     */
    private Long paperType;

    /**
     * 证件号码
     */
    private String paperNumber;

    /**
     * 是否已删除
     */
    private Long deleted;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 租户id
     */
    private Integer tenant;

    private static final long serialVersionUID = -5316865116880221315L;
}