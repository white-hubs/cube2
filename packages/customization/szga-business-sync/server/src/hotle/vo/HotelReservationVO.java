package com.guhecloud.cube.srv.gen.business.info.sync.server.hotle.vo;

import annotation.FormField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/5/27 17:35
 * @Description: 酒店预定vo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HotelReservationVO {
    @FormField(label = "预定人")
    private String submitUser;
    @FormField(label = "身份证号")
    private String idCard;
    @FormField(label = "手机号码")
    private String phone;
    @FormField(label = "单位名称")
    private String workUnit;
    @FormField(label = "预计入住时间")
    private LocalDateTime stayTime;
    @FormField(label = "预计退房时间")
    private LocalDateTime leaveTime;
}
