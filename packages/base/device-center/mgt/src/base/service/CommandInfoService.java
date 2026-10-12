package com.guhecloud.cube.iot.mgt.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.iot.mgt.base.model.CommandInfo;
import com.guhecloud.cube.iot.mgt.core.data.lang.cmd.DownCmd;

public interface CommandInfoService extends IService<CommandInfo> {

    /**
     * 开始记录命令
     *
     * @param cmdSeq               命令序列号
     * @param devInfoSync
     * @param translatedCmdContent 翻译后的命令内容
     */
    void startCmd(String cmdSeq, DownCmd devInfoSync, String translatedCmdContent);
}
