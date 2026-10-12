package com.guhecloud.cube.biz.door.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.door.server.entity.DoorDevice;
import com.guhecloud.cube.biz.door.server.queryparam.DoorDeviceQueryParam;
import com.guhecloud.cube.biz.door.server.vo.DoorDeviceVo;
import com.guhecloud.subsystem.requester.sdk.vo.accesscontrol.AuthDoorStatusVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface DoorDeviceMapper extends BaseMapper<DoorDevice> {
    Set<Long> getDeviceIds();

    Page<DoorDeviceVo> getDoorDevicePage(Page page, @Param("doorParam") DoorDeviceQueryParam doorParam);

    List<DoorDeviceVo> getDoorDeviceList(@Param("doorParam") DoorDeviceQueryParam doorParam);

    void updateStatus(@Param("list") List<AuthDoorStatusVo> list);

    void deleteDoorDeviceList(@Param("ids") List<Long> ids);

    void deleteDoorDeviceByBusinessIds(@Param("businessIds") List<Long> ids);

    List<DoorDeviceVo> getDoorDevicePage(@Param("doorParam") DoorDeviceQueryParam doorParam);
}
