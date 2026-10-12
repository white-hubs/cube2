package com.guhecloud.cube.deliver.project.merchant.entity.storeprofile;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 商户相关功能通知市场方（目前合同、认证）
 * </p>
 *
 * @author bq
 * @since 2022-03-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("wholesaler_notice")
public class WholesalerNotice implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 通知对象类型（用户、部门、岗位、部门岗位）
     */
    private String noticeType;

    /**
     * 通知的用户IDS
     */
    private String userIds;

    /**
     * 通知的部门IDS
     */
    private String deptIds;

    /**
     * 通知的岗位IDS
     */
    private String postIds;

    /**
     * 站内信
     */
    private boolean site;

    /**
     * 短信
     */
    private boolean sms;

    /**
     * 微信
     */
    private boolean wx;

    /**
     * 弹窗
     */
    private boolean pop;

    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}
