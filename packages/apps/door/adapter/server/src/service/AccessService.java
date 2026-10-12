package com.guhecloud.cube.srv.gen.adapter.access.control.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.AccessDevicesParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.AccessPointsParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.DoorControlParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.DoorEventsParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.AccessPointVo;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.AuthDoorStatusVo;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.DoorControlResultVo;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.DoorDevicesVo;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.DoorEventsVo;

import java.util.List;

public interface AccessService {
    /**
     * @Description 点位
     * @Author lrj
     */
    Page<AccessPointVo> getDoorPoints(AccessPointsParam accessPointsParam);

    /**
     * @Description 获取门禁点设备位分页列表
     * @Author lrj
     */
    Page<DoorDevicesVo> getDoorDevices(AccessDevicesParam accessDevicesParam);

    /**
     * @Description 查询门禁点事件
     * @Author lrj
     */
    Page<DoorEventsVo> getDoorEvents(DoorEventsParam doorEventsParam) throws Exception;

    /**
     * @Description 门禁控制
     * @Author lrj
     */
    List<DoorControlResultVo> doorControl(DoorControlParam param);

    /**
     * @Description 获取门禁状态
     * @Author lrj
     */
    List<AuthDoorStatusVo> getDoorStates(List<String> ids);
}
