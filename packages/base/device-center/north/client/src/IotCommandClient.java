package com.guhecloud.cube.iot.north.bridge.client;

import com.guhecloud.cube.iot.north.bridge.client.pj.dto.CommandResponseDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.CommandParam;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.CommandRestParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

import static com.guhecloud.cube.iot.north.bridge.client.Profile.CLIENT_NAME;

@FeignClient(name = CLIENT_NAME)
public interface IotCommandClient {
    /**
     * 命令下发
     * @param commandRequest commandRequest
     * @return com.guheyun.common.web.result.Result<com.guheyun.common.bo.v2.LiveStreamSubscribeResponse>
     */
    @PostMapping("/north/device/command/issue")
    CommandResponseDTO projectCommandIssue(@RequestBody CommandParam commandRequest);

    /**
     * 批量命令下发
     * @param commandRequests commandRequests
     * @return com.guheyun.common.web.result.Result<com.guheyun.common.bo.v2.LiveStreamSubscribeResponse>
     */
    @PostMapping("/north/device/command/issue/batch")
    List<CommandResponseDTO> projectCommandIssue(@RequestBody Set<CommandParam> commandRequests);

    /**
     * 命令下发重置
     *
     * @param commandDeviceRestParam commandDeviceRestParam
     * @return CommandResponseDTO
     */
    @PostMapping("/north/device/command/reset/issues")
    CommandResponseDTO projectCommandResetIssue(@RequestBody CommandRestParam commandDeviceRestParam);

    /**
     * 查询命令状态
     *
     * @param instanceId instanceId
     * @return com.guheyun.common.web.result.Result
     * @author Fl
     **/
    @GetMapping("/north/device/command/issue-status")
    CommandResponseDTO projectCommandStatus(@RequestParam("instanceId") String instanceId);
}
