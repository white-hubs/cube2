package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttClockInRecordsPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result.ClockInInfoClassesResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttClassesRecordPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2023-09-22  15:25
 * @Description:
 * @Version: 1.0
 */
@Mapper
public interface AttClockInRecordsMapper extends BaseMapper<AttClockInRecordsPO> {
    List<ClockInInfoClassesResult> getClockInInfoByUser(@Param("clockInDate") String clockInDate, @Param("userId") Long userId);

    AttClassesRecordPO getRecordsByClock(@Param("clockInId") Long clockInId);

    AttClockInRecordsPO getClockInInAbnormalfoByUser(@Param("clockInDate") String clockInDate, @Param("userId") Long userId);

    AttClockInRecordsPO getClockInInNormalByUser(@Param("clockInDate") String clockInDate, @Param("userId") Long userId);

    List<AttClockInRecordsPO> getAllByCorrect();

    AttClockInRecordsPO getClockInInByUserIdAndTime(@Param("userId") Long userId, @Param("clockInTime") LocalDateTime clockInTime);
}
