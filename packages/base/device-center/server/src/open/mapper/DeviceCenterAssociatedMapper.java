package com.guhecloud.cube.base.device.center.open.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.device.center.metadata.std.vo.AssociatedVideoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface DeviceCenterAssociatedMapper {

    List<AssociatedVideoVO> getAssociatedVideosByDeviceId(@Param("deviceInstanceId") Long deviceInstanceId, @Param("tagId") Long tagId);

    Set<Long> getAssociatedDevices(@Param("deviceId") Long deviceId);

    Set<Long> filterNotVideoDeviceIds(@Param("deviceIds") Set<Long> deviceIds);

    Page<AssociatedVideoVO> getAssociatedVideos(@Param("page") Page<?> page, @Param("tagId") Long tagId);

}
