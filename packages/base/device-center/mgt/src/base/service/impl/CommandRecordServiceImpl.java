package com.guhecloud.cube.iot.mgt.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.iot.mgt.base.mapper.CommandRecordMapper;
import com.guhecloud.cube.iot.mgt.base.model.CommandRecord;
import com.guhecloud.cube.iot.mgt.base.service.CommandRecordService;
import com.guhecloud.cube.iot.mgt.core.data.lang.cmd.CommandExecStatusEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommandRecordServiceImpl extends ServiceImpl<CommandRecordMapper, CommandRecord> implements CommandRecordService {

    @Resource
    private CommandRecordMapper commandRecordMapper;

    @Override
    public void startCmdRecord(String cmdSeq, String instanceId, String translated, String rawCmdContent, Long deviceId, String targetIdentifier) {
        CommandRecord commandRecord = new CommandRecord();
        commandRecord.setCmdSeq(cmdSeq);
        commandRecord.setInstanceId(instanceId);
        commandRecord.setRawCmdContent(rawCmdContent);
        commandRecord.setTranslatedCmdContent(translated);
        commandRecord.setDeviceId(deviceId);
        commandRecord.setTargetIdentifier(targetIdentifier);
        commandRecord.setSingleCmdStatus(CommandExecStatusEnum.INIT.code);
        commandRecord.setStartTime(LocalDateTime.now());
        commandRecordMapper.insert(commandRecord);
    }

    @Override
    public CommandRecord queryCommandRecordByInstanceId(String instanceId) {
        return commandRecordMapper.selectOne(new QueryWrapper<CommandRecord>()
                                                     .eq("instance_id", instanceId));
    }

}
