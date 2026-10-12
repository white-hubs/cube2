package com.guhecloud.cube.deliver.project.merchant.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "user")
public class UserDeliver {

    private String id;

    private String username;

    private String password;

    private Boolean isPwdRandom;

    private String nickname;

    private String pinyin;

    private Integer gender;

    private String mobile;

    private String email;

    private String identityCard;

    private String avatar;

    private Boolean enabled;

    private String type;

    private String remark;

    private String loginIp;

    private LocalDateTime loginTime;

    private String theme;

    private Boolean isExternal;

    private String source;

    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Boolean deleted;

}
