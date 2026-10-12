package com.guhecloud.cube.base.alarm.server.flow.infrastructure.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmRecordHandlerUserPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AlarmRecordHandlerUserMapper extends BaseMapper<AlarmRecordHandlerUserPO> {

    void insertOrUpdateBatch(@Param("handlerUsers") List<AlarmRecordHandlerUserPO> alarmRecordHandlerUserPOS);
}
