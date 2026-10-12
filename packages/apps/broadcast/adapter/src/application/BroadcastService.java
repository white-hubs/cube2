package com.guhecloud.cube.biz.adapter.broadcast.server.application;

import com.guhecloud.cube.biz.adapter.broadcast.server.param.DevicePlayTextParam;

public interface BroadcastService {

    String devicePlayText(DevicePlayTextParam playTextParam);

    Integer devicePlayTextState(String playTextId);
}
