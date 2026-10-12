package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.entity.DevicePosition;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.WaterDeviceMonitorParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.WholesalerDetailParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.DeviceAttributeCodeVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.DeviceAttributeVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.DeviceDetailVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.DevicePositionPageVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.PositionInfoVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.WholesalerDeviceVo;
import com.guhecloud.platform.base.model.Pagination;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface DevicePositionService extends IService<DevicePosition> {

    Pagination<DevicePositionPageVo> getDeviceMonitor(WaterDeviceMonitorParam param);

    DeviceDetailVo getDetail(@PathVariable Long id);

    Pagination<WholesalerDeviceVo> getWholesalerDetail(WholesalerDetailParam param);

    List<WholesalerDeviceVo> wholesalerDetailExcel(WholesalerDetailParam param);

    DeviceAttributeVo getAttribute(@PathVariable Long id);

    List<Long> getDeviceIds(String keyword);

    List<PositionInfoVo> getPositionInfo(List<Long> ids);

    List<DeviceAttributeCodeVo> getDeviceAttributeCode(Long deviceId);

    List<Long> getGearPositionIds();
}
