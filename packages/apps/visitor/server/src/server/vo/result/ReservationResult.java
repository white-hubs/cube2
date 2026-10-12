package com.guhecloud.cube.srv.gen.visitor.management.server.vo.result;

import lombok.Data;

/**
 * 预约操作返回对象
 */
@Data
public class ReservationResult {
    private Long id; // 预约id（通行记录的通行人id）
    private String appointmentCode; // 访客预约码
    private String barCode; // 二维码加密串,有效期与预约时间一致
}
