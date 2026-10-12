package com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 去重信息
 *
 * @author lanchun
 * @date 2022/9/3 11:11
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RepeatTimeInfo {

    /**
     * 超时时间
     */
    private Integer repeatTime;

    /**
     * 超时时间单位
     */
    private String repeatTimeUnit;

}
