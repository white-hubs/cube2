package com.guhecloud.cube.biz.linkage.server.record.reconfirmation.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EventJudgmentDelayTask {

    /**
     * 事件记录主键
     */
    private Long id;

    /**
     * 延迟时间:秒
     */
    private Integer delaySeconds;

    /**
     * 超时回调方法
     */
    private Runnable timeoutCallback;
}
