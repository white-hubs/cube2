package com.guhecloud.cube.biz.collection.analysis.collection;

/**
 * 采集接口组合
 *
 * @author lanchun
 * @date 2024/12/9 10:01
 */
public interface ICollectionProcessor<REQ, REP> extends ICollection<REQ>, ICleaner<REQ, REP> {

}
