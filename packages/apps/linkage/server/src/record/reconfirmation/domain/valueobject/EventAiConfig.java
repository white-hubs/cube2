package com.guhecloud.cube.biz.linkage.server.record.reconfirmation.domain.valueobject;

import lombok.Data;

import java.util.Set;

@Data
public class EventAiConfig {

    /**
     * 是否开启
     */
    private Boolean enabled;

    /**
     * 报警事件分类
     */
    private Set<String> alarmEventTypeCodes;

    /**
     * 指定的识别设备id集合
     */
    private Set<String> appointDevices;

    /**
     * 提示词
     */
    private String cueWord;

    /**
     * 图片主机访问地址
     */
    private String imageHostAddress;
}
