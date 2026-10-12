package com.guhecloud.cube.biz.linkage.server.linkage.strategy.action;

import com.guhecloud.cube.biz.linkage.server.linkage.strategy.action.result.CallbackResult;

import java.util.List;

public interface Callback {

    boolean success(List<CallbackResult> callbackResults);

    boolean fail(List<CallbackResult> callbackResults);
}
