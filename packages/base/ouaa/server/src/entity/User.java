package com.guhecloud.cube.biz.ouaa.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.biz.ouaa.metadata.enumeration.Gender;
import com.guhecloud.platform.database.annotation.Unique;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 用户数据模型
 *
 * @author Woody
 * @date 2021/6/25
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("`user`")
public class User extends BasicEnhancedEntity<Long> {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 固定电话
     */
    private String phone;

    /**
     * 移动电话
     */
    @Unique(message = "移动电话已存在")
    private String mobile;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户性别
     */
    private Gender gender;

    /**
     * 工作状态
     */
    private String workStatus;

    /**
     * 用户备注
     */
    private String remark;

    /**
     * 登录账号
     */
    @Unique(message = "登录账号已存在")
    private String username;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 是否是管理员
     */
    private Boolean admin;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 入职日期
     */
    private LocalDateTime entryDate;

    /**
     * 人员照片业务主键
     */
    private Long photographKey;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 最后登录地址
     */
    private String lastLoginAddress;

    /**
     * 最后更改密码时间
     */
    private LocalDateTime lastUpdatePasswordTime;

    /**
     * 扩展字段（JSON）
     */
    private String extension;

    private String validity = "FOLLOW";

    /**
     * 用户来源
     */
    private String source;

    /**
     * 身份证号
     */
    private String idCard;
}
