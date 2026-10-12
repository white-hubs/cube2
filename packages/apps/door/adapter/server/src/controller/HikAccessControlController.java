package com.guhecloud.cube.srv.gen.adapter.access.control.server.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.AccessDevicesParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.AccessPointsParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.DoorControlParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.DoorEventsParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.KSDJRecordParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.TKRecordParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.VisitingRecordParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.service.impl.HikAccessControlServiceImpl;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.AccessPointVo;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.AccessRecordVO;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.AuthDoorStatusVo;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.CardVO;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.DoorControlResultVo;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.DoorDevicesVo;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.DoorEventsVo;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.OrgVO;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.VisitingRecordVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description 海康接口管理
 */
@Slf4j
@RestController
@RequestMapping("/hik")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HikAccessControlController {
    private final HikAccessControlServiceImpl accessControlService;

    /**
     * @Description 获取门禁点位分页列表
     * @Author lrj
     */
    @PostMapping("/getDoorPoints")
    public Page<AccessPointVo> getDoorPoints(@RequestBody AccessPointsParam accessPointsParam) {
        return accessControlService.getDoorPoints(accessPointsParam);
    }

    /**
     * @Description 查询门禁设备列表
     * @Author lrj
     */
    @PostMapping("/getDoorDevices")
    public Page<DoorDevicesVo> getDoorDevices(@RequestBody AccessDevicesParam accessDevicesParam) {
        return accessControlService.getDoorDevices(accessDevicesParam);
    }

    /**
     * @Description 查询门禁点事件
     * @Author lrj
     */
    @PostMapping("/getDoorEvents")
    public Page<DoorEventsVo> getDoorEvents(@RequestBody DoorEventsParam doorEventsParam) throws Exception {
        return accessControlService.getDoorEvents(doorEventsParam);
    }

    /**
     * @Description 门禁点反控
     * @Author lrj
     */
    @PostMapping("/doorControl")
    public List<DoorControlResultVo> doorControl(@RequestBody DoorControlParam param) {
        return accessControlService.doorControl(param);
    }

    /**
     * @Description 获取门禁状态
     * @Author lrj
     */
    @PostMapping("/getDoorStates")
    public List<AuthDoorStatusVo> getDoorStates(@RequestBody List<String> ids) {
        return accessControlService.getDoorStates(ids);
    }

    /**
     * 获取所有的门禁卡（也可以是一卡通的卡）
     *
     * @return
     */
    @PostMapping("/getDoorCard")
    public List<CardVO> getDoorCard() {
        return accessControlService.getDoorCard();
    }

    /**
     * 获取门禁系统的组织
     *
     * @return
     */
    @PostMapping("/getOrg")
    public List<OrgVO> getOrg() {
        return accessControlService.getOrg();
    }

    /**
     * 获取门禁系统的访客记录
     *
     * @return
     */
    @PostMapping("/getVisitingRecord")
    public List<VisitingRecordVO> getVisitingRecord(@RequestBody VisitingRecordParam param) {
        return accessControlService.getVisitingRecord(param);
    }

    /**
     * 获取门禁系统的梯控记录
     *
     * @return
     */
    @PostMapping("/getTKRecord")
    public List<AccessRecordVO> getTKRecord(@RequestBody TKRecordParam param) {
        return accessControlService.getTKRecord(param);
    }

    /**
     * 获取门禁系统的可视对讲记录
     *
     * @return
     */
    @PostMapping("/getKSDJRecord")
    public List<AccessRecordVO> getKSDJRecord(@RequestBody KSDJRecordParam param) {
        return accessControlService.getKSDJRecord(param);
    }
}
