package com.guhecloud.cube.biz.dispatch.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Appet
 * @Description 通话记录分页
 * @date 2022-07-26 10:08:36
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DispatchGPSVO {
    private String code;//设备编号
    private LocalDateTime uploadTime;//上传时间
    private Integer direction;//方向
    private double lng;//经度
    private double lat;//纬度
    private float height;//高度
    private float speed;//速度
    private String address;//位置信息
}