package com.guhecloud.cube.srv.gen.safety.management.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.FireFightingDeviceQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.device.DeviceCategoryCountVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.device.DeviceCountStatusVO;
import com.guhecloud.cube.srv.gen.safety.management.po.DeviceManagePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeviceManageMapper extends BaseMapper<DeviceManagePO> {
    List<DeviceCountStatusVO> countStatusByApplication(@Param("baseQuery") BaseStatisticsQuery baseQuery,
                                                       @Param("appCodes") List<String> appCodes
                                                      );

    List<DeviceManagePO> getApplicationDevices(@Param("query") BaseQuery query, @Param("appCodes") List<String> appCodes);

    Page<DeviceManagePO> getApplicationDevicePage(Page page, @Param("query") FireFightingDeviceQuery query,
                                                  @Param("appCodes") List<String> appCodes
                                                 );

    List<String> getApplicationDeviceTypes(@Param("appCodes") List<String> appCodes);

    List<DeviceCategoryCountVO> getDeviceCategoryCount(@Param("query") FireFightingDeviceQuery query,
                                                       @Param("appCodes") List<String> appCodes
                                                      );
}
