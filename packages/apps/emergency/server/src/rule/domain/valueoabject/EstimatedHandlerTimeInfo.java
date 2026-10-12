package com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 预计处理时间信息
 *
 * @author lanchun
 * @date 2022/9/3 11:13
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EstimatedHandlerTimeInfo {

    /**
     * 预计处理时间
     */
    private Integer estimatedHandlerTime;

    /**
     * 预计处理时间单位
     */
    private String estimatedHandlerUnit;
}
