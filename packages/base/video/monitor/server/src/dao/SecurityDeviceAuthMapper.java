package com.guhecloud.cube.biz.vmonitor.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.vmonitor.server.entity.SecurityDeviceAuth;
import com.guhecloud.cube.biz.vmonitor.server.param.AuthUserParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SecurityDeviceAuthMapper extends BaseMapper<SecurityDeviceAuth> {
    List<Long> getSecurityDeviceAuthByUserId(@Param("userId") Long userId);

    List<AuthUserParam> getAllUserByDeviceId(@Param("deviceId") Long id);

    List<SecurityDeviceAuth> getUserIdsByDeviceIds(@Param("deviceIds") List<Long> deviceIds);

    Integer insertAuth(@Param("ids") List<SecurityDeviceAuth> ids, @Param("securityDeviceId") Long securityDeviceId,
                       @Param("createBy") Long createBy
                      );

    List<Long> getSecurityUserIds(@Param("deviceIds") List<Long> deviceIds);

    List<Long> getSecurityDeviceIds(@Param("userIds") List<Long> userIds);
}
