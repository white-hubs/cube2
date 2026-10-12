package com.guhecloud.cube.biz.meattrade.server.base.param;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 理货机运行日志新增信息
 *
 * @author lzy
 * @date 2021/9/9
 */
@Data
public class TallyOperationLogCreateParam {

    /**
     * 设备主键
     */
    private Long deviceId;

    /**
     * 操作
     */
    private String operation;

    /**
     * 操作时间
     */
    private LocalDateTime time;
}
