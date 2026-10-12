package com.guhecloud.cube.biz.collection.analysis.gm.enumeration;

import lombok.Getter;

/**
 * 光明采集统计
 *
 * @author lanchun
 * @date 2024/12/9 9:30
 */
@Getter
public enum CollectionSceneEnum {

    /**
     * 光明统计采集
     */
    GM_STATISTICS_COLLECTION,

    /**
     * 日统计
     */
    GM_DAY_COLLECTION,
    /**
     * 周统计
     */
    GM_WEEK_COLLECTION;
}
