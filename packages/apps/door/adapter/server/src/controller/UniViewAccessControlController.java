package com.guhecloud.cube.srv.gen.adapter.access.control.server.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.AccessDevicesParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.DoorControlParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.DoorEventsParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.service.impl.UniViewAccessServiceImpl;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.AuthDoorStatusVo;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.DoorControlResultVo;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.DoorDevicesVo;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.DoorEventsVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/uniview")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UniViewAccessControlController {

    private final UniViewAccessServiceImpl uniViewAccessService;

    /**
     * @Description 查询门禁设备列表
     * @Author lrj
     */
    @PostMapping("/getDoorDevices")
    public Page<DoorDevicesVo> getDoorDevices(@RequestBody AccessDevicesParam accessDevicesParam) {
        return uniViewAccessService.getDoorDevices(accessDevicesParam);
    }

    /**
     * @Description 查询门禁点事件
     * @Author lrj
     */
    @PostMapping("/getDoorEvents")
    public Page<DoorEventsVo> getDoorEvents(@RequestBody DoorEventsParam doorEventsParam) throws Exception {
        return uniViewAccessService.getDoorEvents(doorEventsParam);
    }

    /**
     * 查询门禁设备状态
     *
     * @param ids 门禁设备编号
     *
     * @return 设备状态
     */
    @PostMapping({ "/getDoorStates" })
    List<AuthDoorStatusVo> getDoorStates(@RequestBody List<String> ids) {
        return uniViewAccessService.getDoorStates(ids);
    }

    /**
     * 门禁设备反控制
     *
     * @param param 控制信息
     *
     * @return 反控结果
     */
    @PostMapping("/doorControl")
    public List<DoorControlResultVo> doorControl(@RequestBody DoorControlParam param) {
        return uniViewAccessService.doorControl(param);
    }

}
