package com.guhecloud.cube.iot.north.bridge.client;

import com.guhecloud.cube.iot.north.bridge.client.pj.dto.CommandResponseDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.DeviceCommandIssuesDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

import static com.guhecloud.cube.iot.north.bridge.client.Profile.CLIENT_NAME;

@FeignClient(name = CLIENT_NAME)
public interface IotDeviceEventExtClient {

    /**
     * 命令下发事件阈值设定
     *
     * @param deviceCommandIssues deviceCommandIssues
     * @return CommandResponse
     */
    @PostMapping("/north/device/event/ext/command/issues")
    CommandResponseDTO thresholdSet(@RequestBody DeviceCommandIssuesDTO deviceCommandIssues);

    /**
     * 批量命令下发
     *
     * @param deviceCommandIssues deviceCommandIssues
     * @author Fl
     **/
    @PostMapping("/north/device/event/ext/command/issue/batch")
    List<CommandResponseDTO> projectCommandIssue(@RequestBody Set<DeviceCommandIssuesDTO> deviceCommandIssues);

    /**
     * 查询命令状态
     *
     * @param instanceId instanceId
     * @return com.guheyun.common.web.result.Result
     * @author Fl
     **/
    @GetMapping("/north/device/event/ext/command/issue/status")
    CommandResponseDTO projectCommandStatus(@RequestParam("instanceId") String instanceId);
}
