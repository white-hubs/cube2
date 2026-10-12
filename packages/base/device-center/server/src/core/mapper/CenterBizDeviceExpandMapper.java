package com.guhecloud.cube.base.device.center.core.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.base.device.center.core.model.BizDeviceExpand;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceExpandInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.std.vo.DeviceExpandVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Mapper
public interface CenterBizDeviceExpandMapper extends BaseMapper<BizDeviceExpand> {

    @InterceptorIgnore(tenantLine = "true")
    void updateBySelective(@Param("bizDeviceExpand") BizDeviceExpand bizDeviceExpand);

    List<DeviceExpandVO> selectDeviceExpand(@Param("deviceIds") List<Long> deviceIds);

    @InterceptorIgnore(tenantLine = "true")
    BizDeviceExpand selectByIdNoTenant(@Param("deviceId") Long deviceId);

    void logicDeleteBatchIds(@Param("userId") String userId, @Param("userType") String userType,
                             @Param("deviceIds") List<Long> deviceIds, @Param("updateTime") LocalDateTime updateTime
                            );

    List<DeviceExpandInfoDTO> getExpandListByDeviceId(@Param("deviceIds") Set<Long> deviceIds);

    DeviceExpandInfoDTO getExpandByDeviceId(@Param("deviceId") Long deviceId);
}