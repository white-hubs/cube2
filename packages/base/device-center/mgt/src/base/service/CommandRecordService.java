package com.guhecloud.cube.iot.mgt.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.iot.mgt.base.model.CommandRecord;

public interface CommandRecordService extends IService<CommandRecord> {

    /**
     * 开始记录命令
     *
     * @param cmdSeq          命令序列号
     * @param instanceId      子命令ID
     * @param translated      翻译后的命令内容
     * @param rawCmdContent 原始命令内容
     * @param deviceId 设备ID
     * @param targetIdentifier 目标标识 (属性 Code / 方法 Code / 事件 Code 等)
     */
    void startCmdRecord(String cmdSeq, String instanceId, String translated, String rawCmdContent, Long deviceId, String targetIdentifier);

    /**
     * 根据子命令ID查询命令记录
     *
     * @param instanceId 子命令ID
     * @return 命令记录
     */
    CommandRecord queryCommandRecordByInstanceId(String instanceId);
}
