package com.guhecloud.cube.base.device.center.open.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.device.center.metadata.param.QueryDeviceGroupParam;
import com.guhecloud.cube.base.device.center.open.interfaces.web.query.DeviceGroupDetailQueryParam;
import com.guhecloud.cube.base.device.center.open.interfaces.web.query.DeviceGroupQueryParam;
import com.guhecloud.cube.base.device.center.open.interfaces.web.query.TagDeviceGroupDeviceQueryParam;
import com.guhecloud.cube.base.device.center.open.interfaces.web.vo.DeviceGroupPageVO;
import com.guhecloud.cube.base.device.center.open.interfaces.web.vo.DeviceGroupVO;
import com.guhecloud.cube.base.device.center.open.interfaces.web.vo.DevicePageInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeviceGroupMapper {

    Page<DevicePageInfoVO> deviceInfoPageByTag(Page page,
                                               @Param("param") DeviceGroupDetailQueryParam param,
                                               @Param("tagId") Long tagId
                                              );

    Page<DevicePageInfoVO> groupDevicesPageByTagCode(Page page, @Param("param") TagDeviceGroupDeviceQueryParam param);

    Page<DeviceGroupPageVO> deviceGroupPageByTag(Page page,
                                                 @Param("param") DeviceGroupQueryParam param,
                                                 @Param("tagId") Long tagId
                                                );

    List<DeviceGroupVO> deviceGroupsByTag(@Param("ids") List<Long> groupIds,
                                          @Param("tagId") Long tagId,
                                          @Param("param") QueryDeviceGroupParam param
                                         );

}
