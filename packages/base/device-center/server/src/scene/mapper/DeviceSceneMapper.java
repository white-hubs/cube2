package com.guhecloud.cube.base.device.center.scene.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.device.center.metadata.std.dto.DeviceStateCountDTO;
import com.guhecloud.cube.base.device.center.metadata.std.vo.DeviceOfflineCountVO;
import com.guhecloud.cube.base.device.center.metadata.vo.DevicesVO;
import com.guhecloud.cube.base.device.center.metadata.vo.GenreVO;
import com.guhecloud.cube.base.device.center.open.vo.DeviceStatusVo;
import com.guhecloud.cube.base.device.center.scene.param.DeviceSceneQueryParam;
import com.guhecloud.cube.base.device.center.scene.vo.DeviceVisualCollectionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface DeviceSceneMapper {

    Page<DeviceVisualCollectionVO> getVisualDeviceList(Page page, @Param("query") DeviceSceneQueryParam deviceSceneQueryParam,
                                                       @Param("tagId") Long tagId
                                                      );

    Set<Long> getDeviceIds(@Param("tagId") Long tagId, @Param("areaId") Long areaId);

    DeviceVisualCollectionVO getVisualDevice(@Param("id") Long id);

    DeviceStateCountDTO selectNumberOfGroupByStatus(@Param("tagId") Long tagId, @Param("areaId") Long areaId);

    Integer selectEquipmentOverview(@Param("tagId") Long tagId, @Param("areaId") Long areaId);

    List<GenreVO> selectGenreDevicesNum(@Param("tagId") Long tagId, @Param("areaId") Long areaId, @Param("tenant") Integer tenant);

    List<DevicesVO> selectDevicesVo(@Param("tagId") Long tagId, @Param("areaId") Long areaId, @Param("tenant") Integer tenant);

    List<DeviceStatusVo> selectApplicationCodeNum(@Param("tagId") Long tagId, @Param("areaId") Long areaId);

    List<DeviceOfflineCountVO> selectDeviceOffline(@Param("tagId") Long tagId, @Param("areaId") Long areaId);
}

