package com.guhecloud.cube.iot.mgt.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.iot.mgt.base.model.CommandLog;

public interface CommandLogService extends IService<CommandLog> {

    String issueToIotPlatform = "消息准备投递至物联平台";

    void logCmd(String cmdSeq, String instanceId, String translateDev);
}
