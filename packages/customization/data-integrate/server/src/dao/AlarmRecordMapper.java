package com.guhecloud.cube.base.dataintegrate.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.base.dataintegrate.server.entity.SafetyAlarmRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：采集市场数据配置 Repository接口
 *
 * @author Appet
 * @date 2023-09-09 15:08:36
 */
@Repository("di-alarm-mapper")
public interface AlarmRecordMapper extends BaseMapper<SafetyAlarmRecord> {

    void insertOrUpdateAlarmBatch(List<SafetyAlarmRecord> datas);

    List<Long> getAllIds(@Param("ids") List<Long> ids);
}