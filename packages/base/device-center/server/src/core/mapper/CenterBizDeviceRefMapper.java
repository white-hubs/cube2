package com.guhecloud.cube.base.device.center.core.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.base.device.center.core.model.BizDeviceRef;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface CenterBizDeviceRefMapper extends BaseMapper<BizDeviceRef> {
    List<String> findRefDeviceIds(@Param("deviceId") Long deviceId);

    void removeRefDevice(@Param("deviceId") Long deviceId, @Param("updateTime") LocalDateTime updateTime);

    void saveRef(@Param("deviceId") Long deviceId, @Param("refDeviceId") Long refDeviceId, @Param("username") String username);

    void logicDeleteByDeviceIds(@Param("userId") String userId, @Param("userType") String userType,
                                @Param("deviceIds") List<Long> deviceIds, @Param("updateTime") LocalDateTime updateTime
                               );

    @InterceptorIgnore(tenantLine = "true")
    void updateBySelective(@Param("deviceRef") BizDeviceRef deviceRef);
}