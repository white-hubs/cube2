package com.guhecloud.cube.biz.adapter.person.rollcall.server.application.hikspcc.service;

import com.guhecloud.cube.biz.adapter.person.rollcall.server.param.PersonRollCallRecordParam;
import com.guhecloud.cube.biz.adapter.person.rollcall.server.vo.PersonRollCallRecordVO;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemAdapterConfig;

import java.util.List;

public interface HikSpccRollCallService {
    /**
     * 重连mq
     */
    void reConnect(SubsystemAdapterConfig subscribeInitMessage);

    /**
     * 查询点名任务记录
     *
     * @return 点名记录
     */
    List<PersonRollCallRecordVO> getRollCallRecord(PersonRollCallRecordParam param);
}
