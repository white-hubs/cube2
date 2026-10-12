package com.guhecloud.cube.srv.gen.visitor.management.server.vo.result;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 访客状态变更记录返回对象
 */
@Data
public class VisitorStatusChangeRecordResult {
    private Long id;
    private Long reservationId;
    private String phone;
    private String status;
    private LocalDateTime createTime;

    private Integer hourStr;
    private Integer count;
}
