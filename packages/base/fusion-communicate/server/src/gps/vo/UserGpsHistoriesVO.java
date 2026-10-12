package com.guhecloud.cube.biz.fusion.communicate.server.gps.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGpsHistoriesVO {

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
}
