package com.guhecloud.cube.deliver.project.merchant.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 设备通知具体方式配置
 * </p>
 *
 * @author bq
 * @since 2022-09-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("device_notice_config")
public class DeviceNoticeConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 通知ID
     */
    private String noticeId;

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

}
