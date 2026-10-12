package com.guhecloud.subsystem.requester.sdk.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.requester.sdk.config.DynamicConfiguration;
import com.guhecloud.subsystem.requester.sdk.param.accesscontrol.AccessDevicesParam;
import com.guhecloud.subsystem.requester.sdk.param.accesscontrol.AccessEventParam;
import com.guhecloud.subsystem.requester.sdk.param.accesscontrol.AccessPointsParam;
import com.guhecloud.subsystem.requester.sdk.param.accesscontrol.DoorControlParam;
import com.guhecloud.subsystem.requester.sdk.param.accesscontrol.DoorEventsParam;
import com.guhecloud.subsystem.requester.sdk.vo.accesscontrol.AccessPointVo;
import com.guhecloud.subsystem.requester.sdk.vo.accesscontrol.AccessRecordVO;
import com.guhecloud.subsystem.requester.sdk.vo.accesscontrol.AuthDoorStatusVo;
import com.guhecloud.subsystem.requester.sdk.vo.accesscontrol.CardVO;
import com.guhecloud.subsystem.requester.sdk.vo.accesscontrol.DoorControlResultVo;
import com.guhecloud.subsystem.requester.sdk.vo.accesscontrol.DoorDevicesVo;
import com.guhecloud.subsystem.requester.sdk.vo.accesscontrol.DoorEventsVo;
import com.guhecloud.subsystem.requester.sdk.vo.accesscontrol.OrgVO;
import com.guhecloud.subsystem.requester.sdk.vo.accesscontrol.VisitingRecordVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = SubsystemTypeConstants.DOOR_SERVER, configuration = DynamicConfiguration.class)
public interface AccessControlClient {

    /**
     * @Description 获取门禁点位分页列表
     * @Author lrj
     */
    @PostMapping("/getDoorPoints")
    Page<AccessPointVo> getDoorPoints(@RequestBody AccessPointsParam accessPointsParam);

    /**
     * @Description 查询门禁设备列表
     * @Author lrj
     */
    @PostMapping("/getDoorDevices")
    Page<DoorDevicesVo> getDoorDevices(@RequestBody AccessDevicesParam accessDevicesParam);

    /**
     * @Description 查询门禁点事件
     * @Author lrj
     */
    @PostMapping("/getDoorEvents")
    Page<DoorEventsVo> getDoorEvents(@RequestBody DoorEventsParam doorEventsParam);

    /**
     * @Description 门禁点反控
     * @Author lrj
     */
    @PostMapping("/doorControl")
    List<DoorControlResultVo> doorControl(@RequestBody DoorControlParam param);

    /**
     * @Description 获取门禁状态
     * @Author lrj
     */
    @PostMapping("/getDoorStates")
    List<AuthDoorStatusVo> getDoorStates(@RequestBody List<String> ids);

    /**
     * 获取所有的门禁卡（也可以是一卡通的卡）
     *
     * @return
     */
    @PostMapping("/getDoorCard")
    List<CardVO> getDoorCard();

    /**
     * 获取门禁系统的组织
     *
     * @return
     */
    @PostMapping("/getOrg")
    List<OrgVO> getOrg();

    /**
     * 获取门禁系统的访客记录
     *
     * @return
     */
    @PostMapping("/getVisitingRecord")
    List<VisitingRecordVO> getVisitingRecord(@RequestBody AccessEventParam param);

    /**
     * 获取门禁系统的梯控记录
     *
     * @return
     */
    @PostMapping("/getTKRecord")
    List<AccessRecordVO> getTKRecord(@RequestBody AccessEventParam param);

    /**
     * 获取门禁系统的可视对讲记录
     *
     * @return
     */
    @PostMapping("/getKSDJRecord")
    List<AccessRecordVO> getKSDJRecord(@RequestBody AccessEventParam param);
}
