package com.guhecloud.cube.srv.gen.conferencemanage.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.AppointmentRecord;
import com.guhecloud.cube.srv.gen.conferencemanage.server.param.AppointmentParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.AppointmentRecordVo;

import java.util.List;

public interface AppointmentRecordService extends IService<AppointmentRecord> {
    Page<AppointmentRecordVo> selectAppointRecord(AppointmentParam param);

    List<AppointmentRecordVo> selectAppointRecordList(AppointmentParam param);
}
