package com.guhecloud.cube.iot.mgt.core.data.vo.linkage;

import com.guhecloud.cube.iot.mgt.core.data.vo.linkage.action.LinkActionTriggerMeta;
import lombok.Data;

import java.util.List;

@Data
public class LinkageConfig {
    /**
     * 主键
     */
    private Long id;

    /**
     * 联动名称
     */
    private String name;

    /**
     * 联动唯一编号
     */
    private String number;

    /**
     * 联动配置
     */
    private String config;

    private String triggerType;

    /**
     * 联动状态:0关闭;1开启
     */
    private int status;

    private List<SingleLinkConditionMeta> linkConditionMetas;

    private List<LinkActionTriggerMeta> linkActionTriggerMetaList;
}