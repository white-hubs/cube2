package com.guhecloud.cube.biz.linkage.server.record.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EventAssistConfirmVO {

    /**
     * 分析结果(确认报警、疑似报警)
     */
    private String result;
}
