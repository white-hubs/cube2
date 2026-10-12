package com.guhecloud.cube.srv.gen.visitor.management.server.vo.result;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 通行记录
 */
@Data
public class PassRecordResult {
    private Long id;
    private String deviceCode;
    private String deviceName;
    private String channelCode;
    private String channelName;
    private String swingTime;
    private LocalDateTime time;
    private Long personId;
    private String personCode;
    private String personName;
    private Integer enterOrExit; //进出门类型：0 - 开门，1 - 进门，2 - 出门
    private Integer openResult; //开门结果：0 - 失败，1 - 成功
}
