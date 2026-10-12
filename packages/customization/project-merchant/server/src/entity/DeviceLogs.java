package com.guhecloud.cube.deliver.project.merchant.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 设备修改日志
 * </p>
 *
 * @author lzy
 * @since 2021-04-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("device_logs")
public class DeviceLogs implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 部门id
     */
    private String deptId;

    /**
     * 部门名
     */
    private String deptName;

    /**
     * 时间
     */
    private LocalDateTime time;

    /**
     * 备注
     */
    private String remark;


}
