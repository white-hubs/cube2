package com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.repository.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.po.ParkingInfoDevicePO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.po.ParkingInfoPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

@Mapper
public interface ParkingInfoMapper extends BaseMapper<ParkingInfoPO> {

    Page<ParkingInfoPO> selectDockConfigParkingInfoPage(Page<ParkingInfoPO> page,
                                                        @Param("source") String source,
                                                        @Param("keyword") String keyword,
                                                        @Param("providerCode") String providerCode
                                                       );

    List<ParkingInfoPO> selectDockConfigsParkingInfoByProviderCode(@Param("providerCode") String providerCode);

    List<ParkingInfoPO> selectParkingInfosByDeviceIds(@Param("deviceIds") Collection<Long> deviceIds);

    List<ParkingInfoDevicePO> selectParkingInfoDevicesByProviderCode(@Param("providerCode") String providerCode);
}
