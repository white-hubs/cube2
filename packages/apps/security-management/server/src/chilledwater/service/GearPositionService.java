package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.entity.GearPosition;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.DeviceAssociationPositionParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.DevicePositionParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.DevicePositionVo;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

public interface GearPositionService extends IService<GearPosition> {
    Pagination<DevicePositionVo> devicePositions(DevicePositionParam pram);

    void deviceAssociationAssociationPositions(List<DeviceAssociationPositionParam> pram);

    void cancellationDevice(Long gearPositionId, Long deviceId);

    List<Long> getGearPositionIds();
}
