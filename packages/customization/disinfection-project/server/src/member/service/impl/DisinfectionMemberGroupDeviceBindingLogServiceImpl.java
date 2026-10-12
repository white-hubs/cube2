package com.guhecloud.cube.srv.gen.disinfection.server.member.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.disinfection.server.member.dao.DisinfectionMemberGroupDeviceBindingLogMapper;
import com.guhecloud.cube.srv.gen.disinfection.server.member.entity.MemberGroupDeviceBindingLog;
import com.guhecloud.cube.srv.gen.disinfection.server.member.service.DisinfectionMemberGroupDeviceBindingLogService;
import com.guhecloud.platform.base.model.Period;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DisinfectionMemberGroupDeviceBindingLogServiceImpl extends ServiceImpl<DisinfectionMemberGroupDeviceBindingLogMapper, MemberGroupDeviceBindingLog> implements DisinfectionMemberGroupDeviceBindingLogService {

    @Override
    public List<MemberGroupDeviceBindingLog> queryByPeriod(Period period) {

        return lambdaQuery().between(MemberGroupDeviceBindingLog::getCreateTime, period.getBegin(), period.getEnd()).list();
    }
}
