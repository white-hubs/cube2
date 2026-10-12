package com.guhecloud.cube.srv.gen.disinfection.server.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.disinfection.server.member.entity.MemberGroupDeviceBindingLog;
import com.guhecloud.platform.base.model.Period;

import java.util.List;

public interface DisinfectionMemberGroupDeviceBindingLogService extends IService<MemberGroupDeviceBindingLog> {

    List<MemberGroupDeviceBindingLog> queryByPeriod(Period period);
}
