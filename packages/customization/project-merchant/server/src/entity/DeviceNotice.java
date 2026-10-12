package com.guhecloud.cube.deliver.project.merchant.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 设备相关通知（目前只有到期通知）
 * </p>
 *
 * @author bq
 * @since 2022-08-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("device_notice")
public class DeviceNotice implements Serializable {

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
     * 还有多少天到期通知，负数就表示过期多少天
     */
    private int days;

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
//    private boolean pop;

    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
