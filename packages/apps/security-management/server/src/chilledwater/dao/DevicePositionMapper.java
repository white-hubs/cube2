package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.entity.DevicePosition;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.WaterDeviceMonitorParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.WholesalerDetailParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.DevicePositionPageVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.PositionInfoVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.WholesalerDeviceVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DevicePositionMapper extends BaseMapper<DevicePosition> {

    Page<DevicePositionPageVo> getDeviceMonitor(Page page, @Param("param") WaterDeviceMonitorParam param);

    Page<WholesalerDeviceVo> getWholesalerDetail(Page page, @Param("param") WholesalerDetailParam param);

    List<WholesalerDeviceVo> wholesalerDetailExcel(@Param("param") WholesalerDetailParam param);

    List<Long> getDeviceIds(@Param("keyword") String keyword);

    List<PositionInfoVo> getPositionInfo(@Param("ids") List<Long> ids);

    List<WholesalerDeviceVo> getDeviceList(@Param("id") Long id);

}
