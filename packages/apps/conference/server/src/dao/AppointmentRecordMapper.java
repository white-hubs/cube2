package com.guhecloud.cube.srv.gen.conferencemanage.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.AppointmentRecord;
import com.guhecloud.cube.srv.gen.conferencemanage.server.param.AppointmentParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.param.ConferenceUsageParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.AppointmentIntervalLogVo;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.AppointmentRecordVo;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.AppointmentUsageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AppointmentRecordMapper extends BaseMapper<AppointmentRecord> {
    List<AppointmentUsageVo> selectConferenceUsage(@Param("param") ConferenceUsageParam param, @Param("startTime") String startTime,
                                                   @Param("endTime") String endTime
                                                  );

    AppointmentUsageVo selectConferenceCount(@Param("param") ConferenceUsageParam param, @Param("startTime") String startTime,
                                             @Param("endTime") String endTime
                                            );

    List<AppointmentIntervalLogVo> selectTimeIntervalLog(@Param("param") ConferenceUsageParam param, @Param("startTime") String startTime,
                                                         @Param("endTime") String endTime
                                                        );

    Page<AppointmentRecordVo> selectApppointRecordInfo(Page page, @Param("param") AppointmentParam param);

    List<AppointmentRecordVo> selectApppointRecordInfo(@Param("param") AppointmentParam param);
}
