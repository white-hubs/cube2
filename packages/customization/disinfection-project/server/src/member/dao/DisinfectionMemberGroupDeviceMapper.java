package com.guhecloud.cube.srv.gen.disinfection.server.member.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.disinfection.server.member.entity.MemberGroupDevice;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DisinfectionMemberGroupDeviceMapper extends BaseMapper<MemberGroupDevice> {
    @InterceptorIgnore(tenantLine = "on")
    List<Integer> getTenantsByDeviceId(long iotDeviceId);
}
