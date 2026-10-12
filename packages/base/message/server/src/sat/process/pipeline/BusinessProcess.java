package com.guhecloud.cube.base.message2.server.sat.process.pipeline;

/**
 * 业务执行器
 *
 * @author lanchun
 * @date 2023/5/8 11:42
 */
public interface BusinessProcess<T extends ProcessModel> {

    /**
     * 真正处理逻辑
     *
     * @param context
     */
    void process(ProcessContext<T> context);
}
