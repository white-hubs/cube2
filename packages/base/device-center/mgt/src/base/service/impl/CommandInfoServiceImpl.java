package com.guhecloud.cube.iot.mgt.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.iot.mgt.base.mapper.CommandInfoMapper;
import com.guhecloud.cube.iot.mgt.base.model.CommandInfo;
import com.guhecloud.cube.iot.mgt.base.service.CommandInfoService;
import com.guhecloud.cube.iot.mgt.core.data.lang.cmd.CmdFlag;
import com.guhecloud.cube.iot.mgt.core.data.lang.cmd.DownCmd;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommandInfoServiceImpl extends ServiceImpl<CommandInfoMapper, CommandInfo> implements CommandInfoService {

    @Resource
    private CommandInfoMapper commandInfoMapper;

    @Override
    public void startCmd(String cmdSeq, DownCmd downCmd, String translatedCmdContent) {
        CommandInfo commandInfo = new CommandInfo();
        commandInfo.setCmdSeq(cmdSeq);
        commandInfo.setTranslatedCmdContent(translatedCmdContent);
        commandInfo.setCmdFlag(CmdFlag.UN_COMPLETED.name());
        commandInfo.setDownCmd(downCmd.name());
        commandInfo.setStartTime(LocalDateTime.now());
        commandInfoMapper.insert(commandInfo);
    }
}
