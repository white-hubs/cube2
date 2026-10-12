package com.guhecloud.cube.srv.gen.business.info.sync.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统用户表
 *
 * @TableName user
 */
@Data
@TableName("user_info")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
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
     * 是否启用
     */
    private Integer enabled;

    /**
     * 员工状态
     */
    private String status;

    /**
     * 证件类型
     */
    private String paperType;

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

    /**
     * 短号
     */
    private String cornet;

    private static final long serialVersionUID = 7819390613160157641L;
}