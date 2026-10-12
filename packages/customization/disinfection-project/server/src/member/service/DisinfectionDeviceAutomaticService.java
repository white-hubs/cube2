package com.guhecloud.cube.srv.gen.disinfection.server.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.disinfection.server.member.entity.MemberDeviceAutomatic;
import com.guhecloud.cube.srv.gen.disinfection.server.member.param.DeviceAutomaticCommandParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.param.DisinfectionDeviceAutomaticQueryParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.MemberDeviceAutomaticPageResult;
import com.guhecloud.platform.base.model.Pagination;

public interface DisinfectionDeviceAutomaticService extends IService<MemberDeviceAutomatic> {
    Pagination<MemberDeviceAutomaticPageResult> deviceAutomaticPage(DisinfectionDeviceAutomaticQueryParam param);

    Boolean addDeviceAutomatic(DeviceAutomaticCommandParam param);

    Boolean editDeviceAutomatic(DeviceAutomaticCommandParam param);

    Boolean removeDeviceAutomatic(Long id);
}
