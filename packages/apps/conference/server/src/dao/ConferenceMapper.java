package com.guhecloud.cube.srv.gen.conferencemanage.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.Conference;
import com.guhecloud.cube.srv.gen.conferencemanage.server.param.AppointmentParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.AppointmentVo;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.ConferenceDevicesVo;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.MobileAppointmentVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ConferenceMapper extends BaseMapper<Conference> {
    Page<AppointmentVo> getAppointmentVo(Page page, @Param("param") AppointmentParam param);

    Page<MobileAppointmentVo> getAppConference(Page page, @Param("param") AppointmentParam param);

    Page<MobileAppointmentVo> getConferencePage(Page page, @Param("param") AppointmentParam param);

    MobileAppointmentVo getConferenceDetail(@Param("id") Long id);

    List<ConferenceDevicesVo> getConferenceDevice(@Param("list") List<Long> list);

}
