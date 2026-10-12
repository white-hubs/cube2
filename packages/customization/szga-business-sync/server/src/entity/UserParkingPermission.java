package com.guhecloud.cube.srv.gen.business.info.sync.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 停车权限
 *
 * @TableName user_parking_permission
 */
@Data
@TableName("user_parking_permission")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserParkingPermission implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 车牌号
     */
    private String vehicleNo;

    /**
     * 状态（OPEN开卡、CANCEL注销）
     */
    private String status;

    /**
     * 厂区编号
     */
    private String parkinglotCode;

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

    private static final long serialVersionUID = -3954193324678370828L;
}