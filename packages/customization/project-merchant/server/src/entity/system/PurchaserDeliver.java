package com.guhecloud.cube.deliver.project.merchant.entity.system;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 商户端-采购商
 * </p>
 *
 * @author bq
 * @since 2021-12-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("purchaser")
public class PurchaserDeliver implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 拼音首字母
     */
    private String pinyin;

    /**
     * 性别（男：1，女：2，未知：9）
     */
    private Integer gender;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 用户头像路径
     */
    private String avatar;

    /**
     * 状态（启用，停用）
     */
    private Boolean enabled;

    /**
     * 用户类型
     */
    private String type;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 删除标志（0代表存在，1代表删除）
     */
    @TableField(fill = FieldFill.INSERT)
    private Boolean deleted;


}
