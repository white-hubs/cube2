package com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.controller;

import com.guhecloud.cube.base.device.center.metadata.std.param.DevicePropWriteCmd;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterAttrCtlStdClient;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.entity.GroupControlLog;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.param.GroupControlLogQueryParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.param.GroupControlOrderParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.param.TimeControlLogQueryParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.service.GroupControlLogService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.service.TimeControlLogService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.vo.GroupControlLogVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.vo.TimeControlLogVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.service.GroupBaseDetailsService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.GroupBaseDetailsVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.HlInGroupStatVo;
import com.guhecloud.platform.base.context.ExecutorContextHolder;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IntelligentLightingController {

    private final GroupControlLogService groupControlLogService;

    private final TimeControlLogService timeControlLogService;

    private final GroupBaseDetailsService groupBaseDetailsService;

    private final DeviceCenterAttrCtlStdClient deviceCenterAttrCtlClient;

    /**
     * @Description 群控日志
     * @Author lrj
     */
    @GetMapping("/group-control/{id}/logs")
    public Pagination<GroupControlLogVo> getGroupControlLogs(@PathVariable Long id, GroupControlLogQueryParam param) {
        return groupControlLogService.getGroupControlLogs(id, param);
    }

    /**
     * @Description 时控计划
     * @Author lrj
     */
    @GetMapping("/time-control/logs")
    public Pagination<TimeControlLogVo> getTimeControlPlan(TimeControlLogQueryParam param) {
        return timeControlLogService.getTimeControlPlan(param);
    }

    /**
     * @Description 群控命令下达
     * @Author lrj
     */
    @PostMapping("/group-control/control")
    public Boolean groupControl(@RequestBody GroupControlOrderParam param) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        List<GroupBaseDetailsVo> groupBaseDetailsList = groupBaseDetailsService.getGroupBaseDetails(param);

        LocalDateTime optTime = LocalDateTime.now();
        for (GroupBaseDetailsVo groupBaseDetails : groupBaseDetailsList) {
            ExecutorContextHolder.run(() -> {
                String expectedValue = "";
                String originalValue = "";
                if (param.getOpenGate().equals("1")) {
                    expectedValue = param.getOpenGate();
                    originalValue = "0";
                } else if (param.getOpenGate().equals("0")) {
                    expectedValue = param.getOpenGate();
                    originalValue = "1";
                }
                SessionContextHolder.requestTenant(tenant);
                GroupControlLog groupControlLog = new GroupControlLog();
                Set<DevicePropWriteCmd.PropWriteValue> keyValues = new HashSet<>();
                DevicePropWriteCmd deviceAttrCtlCommand = new DevicePropWriteCmd();
                DevicePropWriteCmd.PropWriteValue keyValue = new DevicePropWriteCmd.PropWriteValue();
                keyValue.setCode("disjunctor");
                keyValue.setExpectedValue(expectedValue);
                keyValue.setOriginalValue(originalValue);
                keyValues.add(keyValue);
                deviceAttrCtlCommand.setDeviceId(groupBaseDetails.getDeviceId());
                deviceAttrCtlCommand.setPropWriteValues(keyValues);

                groupControlLog.setGroupControlId(groupBaseDetails.getGroupControlId());
                groupControlLog.setDeviceId(groupBaseDetails.getDeviceId());
                groupControlLog.setDeviceName(groupBaseDetails.getDeviceName());
                groupControlLog.setAction(param.getOpenGate().equals("1") ? "开" : "关");
                groupControlLog.setTenant(tenant);
                groupControlLog.setOptTime(optTime);
                try {
                    deviceCenterAttrCtlClient.controlAttribute(deviceAttrCtlCommand);
                    groupControlLog.setResult("成功");
                } catch (Exception e) {
                    groupControlLog.setResult("失败");
                }
                groupControlLogService.save(groupControlLog);

            });

        }
        return true;

    }

    /**
     * @Description
     * @Author lrj
     */
    @GetMapping("/group-control/stat")
    public List<HlInGroupStatVo> getGroupControlLogs(GroupControlLogQueryParam param) {
        return groupControlLogService.getGroupControlStat(param);
    }

    /**
     * @Description
     * @Author lrj
     */
    @GetMapping("/group-control/deviceInGroup/stat")
    public HlInGroupStatVo getHlInGroupStat(GroupControlOrderParam param) {
        return groupBaseDetailsService.getHlInGroupStat(param);
    }

}
