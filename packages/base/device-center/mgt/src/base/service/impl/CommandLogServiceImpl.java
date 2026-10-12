package com.guhecloud.cube.iot.mgt.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.iot.mgt.base.mapper.CommandLogMapper;
import com.guhecloud.cube.iot.mgt.base.model.CommandLog;
import com.guhecloud.cube.iot.mgt.base.service.CommandLogService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

@Service
public class CommandLogServiceImpl extends ServiceImpl<CommandLogMapper, CommandLog> implements CommandLogService {

    @Resource
    private CommandLogMapper commandLogMapper;

    @Override
    public void logCmd(String cmdSeq, String instanceId, String translateDev) {
        CommandLog commandLog = new CommandLog();
        commandLog.setCmdSeq(cmdSeq);
        commandLog.setInstanceId(instanceId);
        commandLog.setLogContent(translateDev);

        commandLogMapper.insert(commandLog);
    }
}
