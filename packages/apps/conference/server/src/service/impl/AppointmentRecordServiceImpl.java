package com.guhecloud.cube.srv.gen.conferencemanage.server.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.conferencemanage.server.dao.AppointmentRecordMapper;
import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.AppointmentRecord;
import com.guhecloud.cube.srv.gen.conferencemanage.server.param.AppointmentParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.service.AppointmentRecordService;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.AppointmentRecordVo;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AppointmentRecordServiceImpl extends ServiceImpl<AppointmentRecordMapper, AppointmentRecord> implements AppointmentRecordService {

    private final AppointmentRecordMapper appointmentRecordMapper;

    @Override
    public Page<AppointmentRecordVo> selectAppointRecord(AppointmentParam param) {
        return appointmentRecordMapper.selectApppointRecordInfo(PagingUtils.convert(param), param);
    }

    @Override
    public List<AppointmentRecordVo> selectAppointRecordList(AppointmentParam param) {
        return appointmentRecordMapper.selectApppointRecordInfo(param);
    }
}
