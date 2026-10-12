package com.guhecloud.cube.iot.mgt.device.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.iot.mgt.core.data.vo.cache.DeviceDigestVo;
import com.guhecloud.cube.iot.mgt.device.model.DevDevice;
import com.guhecloud.cube.iot.mgt.device.param.BatchSyncDeviceParam;
import com.guhecloud.cube.iot.mgt.device.param.BatchUpdateDeviceParam;
import com.guhecloud.cube.iot.mgt.device.vo.DeviceThinPairVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface DevDeviceMapper extends BaseMapper<DevDevice> {
    List<DeviceThinPairVo> listThinDeviceByProductId(@Param("productId") Long productId);

    List<DevDevice> selectVisibleGatewayDevice();

    DeviceDigestVo queryDeviceCache(@Param("deviceId") Long deviceId);

    @InterceptorIgnore(tenantLine = "true")
    void batchUpdate(@Param("infos") List<BatchUpdateDeviceParam.UpdateInfo> updateInfos);

    Set<String> queryDeviceCode(@Param("devices") List<BatchSyncDeviceParam.SynDeviceInfo> synDevices);
}