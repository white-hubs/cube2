package com.guhecloud.cube.biz.fusion.communicate.server.gps.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "user_gps_history")
@AllArgsConstructor
@NoArgsConstructor
public class UserGpsHistoryPO extends BasicEnhancedTenantEntity<Long> {

    private static final long serialVersionUID = 1L;
    private Long id;

    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 上传时间
     */
    private LocalDateTime uploadTime;

    /**
     * 方向
     */
    private Integer direction;

    /**
     * 经度
     */
    private Double lng;

    /**
     * 纬度
     */
    private Double lat;

    /**
     * 位置信息
     */
    private String address;

    /**
     * 高度
     */
    private Float height;

    /**
     * 速度
     */
    private Float speed;

    /**
     * 最后上报时间(当位置信息无变化时,只更新该字段)
     */
    private LocalDateTime lastUploadTime;
}
