package com.guhecloud.platform.dcm.support;

import com.guhecloud.platform.dcm.entity.RequestedConfigItem;

import java.util.List;

/**
 * 差异比较接口
 */
public interface IRciComparator {

    /**
     * 以指定模式比较目标状态和当前状态，生成差异列表
     * 前置要求， 源状态和目标状态的配置项，均为可对比对象(即: 相同的Provider/ConfigType)。
     */
    List<RequestedConfigItem> compare(List<RequestedConfigItem> toState, List<RequestedConfigItem> fromState, boolean isPartial);
}
