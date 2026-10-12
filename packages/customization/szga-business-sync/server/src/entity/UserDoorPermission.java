package com.guhecloud.cube.srv.gen.business.info.sync.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 门禁权限
 *
 * @TableName user_door_permission
 */
@Data
@TableName("user_door_permission")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDoorPermission implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 门禁通道编号
     */
    private String resourceCode;

    /**
     * 状态（OPEN开卡、CANCEL注销）
     */
    private String status;

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
    private static final long serialVersionUID = 760497799364954045L;
}