package com.guhecloud.cube.srv.gen.business.info.sync.server.hotle.entity;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/5/27 17:23
 * @Description:
 */

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.business.info.sync.server.enumeration.FlowResult;
import com.guhecloud.cube.srv.gen.business.info.sync.server.hotle.enumeration.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("hotel_reservation_order")
public class HotelReservationOrder {

    private String id;

    /**
     * 预定人
     */
    private String submitUser;

    /**
     * 电话号码
     */
    private String phone;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 单位
     */
    private String workUnit;

    /**
     * 预计入住时间
     */
    private LocalDateTime stayTime;

    /**
     * 预计退房时间
     */
    private LocalDateTime leaveTime;

    /**
     * 审批结果
     */
    private FlowResult approvalResult;

    /**
     * 租户ID
     */
    private Integer tenant;

    /**
     * 删除标记 (0-未删除，1-已删除)
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
     * 订单状态
     */
    private OrderStatus status;
}

