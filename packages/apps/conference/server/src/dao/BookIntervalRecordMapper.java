package com.guhecloud.cube.srv.gen.conferencemanage.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.BookIntervalRecord;
import com.guhecloud.cube.srv.gen.conferencemanage.server.param.AppointmentParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.BookIntervalStatisticsVo;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.BookRoomAppointmentRecordVo;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.FilterIntervalRecordVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface BookIntervalRecordMapper extends BaseMapper<BookIntervalRecord> {
    int getCount(Long conferenceId);

    int getBookCount(@Param("conferenceId") Long conferenceId, @Param("startTime") String startTime, @Param("endTime") String endTime);

    Page<BookRoomAppointmentRecordVo> getBookRoomAppointmentInfo(Page page, @Param("param") AppointmentParam param);

    List<BookIntervalStatisticsVo> getBookIntervalStatisticsPeriod(@Param("periodTime") List<String> periodTime);

    List<BookIntervalStatisticsVo> getReservedStatistics(@Param("appointmentDates") List<String> appointmentDates,
                                                         @Param("periodTime") List<String> periodTime
                                                        );

    List<BookIntervalStatisticsVo> getPeriodCount();

    List<BookIntervalStatisticsVo> getAllInterval();

    List<BookIntervalStatisticsVo> getAllIntervalUsage(@Param("st") LocalDateTime st, @Param("et") LocalDateTime et);

    List<FilterIntervalRecordVo> getFilterIntervalRecords(@Param("list") List<Long> ids, @Param("date") String date);

    List<BookRoomAppointmentRecordVo> getAppointmentRecordList(@Param("date") String date, @Param("conferenceId") Long conferenceId);
}
