package com.guhecloud.cube.biz.meattrade.server.base.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 理货机信息展示
 *
 * @author lzy
 * @date 2021/9/9
 */
@Data
public class TallyOperationLogVO {

    /**
     * 操作
     */
    private String operation;

    /**
     * 时间
     */
    private LocalDateTime time;
}
