package com.guhecloud.cube.srv.gen.device.supervise.server.analysis.domain;

/**
 * @author pengjian
 * @date 2022/7/25 18:44
 * @description
 */
public interface Callback {
    /**
     * 执行成功回调
     */
    void success(CallbackResult callbackResult);

    /**
     * 执行失败回调
     */
    void fail(CallbackResult callbackResult);
}
