package com.guhecloud.cube.srv.gen.maintain.server.electron.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.PatrolAlarm;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatrolAlarmMapper extends BaseMapper<PatrolAlarm> {

    void insertOrUpdateBatch(List<PatrolAlarm> list);
}
