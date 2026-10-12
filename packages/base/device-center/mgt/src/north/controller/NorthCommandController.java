package com.guhecloud.cube.iot.mgt.north.controller;

import com.guhecloud.cube.iot.mgt.north.param.CommandRestParam;
import com.guhecloud.cube.iot.mgt.north.server.impl.NorthCommandService;
import com.guhecloud.cube.iot.mgt.north.vo.CommandRequest;
import com.guhecloud.cube.iot.mgt.north.vo.CommandResponse;
import com.guhecloud.cube.iot.mgt.core.data.lang.cmd.CommandExecStatusEnum;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/north/device/command")
public class NorthCommandController {

    @Resource
    private NorthCommandService northCommandService;

    /**
     * 命令下发
     *
     * @param commandRequest commandRequest
     *
     * @author Fl
     **/
    @PostMapping("/issue")
    public CommandResponse projectCommandIssue(@RequestBody CommandRequest commandRequest) {
        return northCommandService.commandIssue(commandRequest);
    }

    /**
     * 批量命令下发
     *
     * @param commandRequests commandRequests
     *
     * @author Fl
     **/
    @PostMapping("/issue/batch")
    public List<CommandResponse> projectCommandIssue(@RequestBody Set<CommandRequest> commandRequests) {

        if (Objects.isNull(commandRequests)) {
            return Collections.emptyList();
        }

        List<CommandResponse> result = new ArrayList<>(commandRequests.size());
        for (CommandRequest commandRequest : commandRequests) {
            try {
                result.add(northCommandService.commandIssue(commandRequest));
            } catch (Exception e) {
                CommandResponse commandResponse = new CommandResponse();
                commandResponse.setCommandHandle(commandRequest.getCommandSeq());
                commandResponse.setCommandSeq(commandRequest.getCommandSeq());
                commandResponse.setMessage(e.getMessage());
                commandResponse.setStatusCode(CommandExecStatusEnum.REJECT.code);
                result.add(new CommandResponse());
                log.error("command issue error", e);
            }
        }

        return result;
    }

    /**
     * 命令下发重置
     *
     * @param param commandDeviceRestParam
     *
     * @return CommandResponseDTO
     */
    @PostMapping("/reset/issues")
    public CommandResponse projectCommandResetIssue(@RequestBody CommandRestParam param) {
        if (Objects.isNull(param)) {
            CommandResponse response = new CommandResponse();
            response.setStatusCode(CommandExecStatusEnum.REJECT.code);
            response.setMessage("commandDeviceRestParam is null");
            return response;
        }
        if (Objects.isNull(param.getCommandResetEnum())) {
            CommandResponse response = new CommandResponse();
            response.setStatusCode(CommandExecStatusEnum.REJECT.code);
            response.setMessage("commandResetEnum is null");
            return response;
        }

        return northCommandService.commandDeviceResetIssue(param);
    }

    /**
     * 查询命令状态
     *
     * @param instanceId instanceId
     *
     * @author Fl
     **/
    @GetMapping("/issue-status")
    public CommandResponse projectCommandStatus(@RequestParam("instanceId") String instanceId) {
        return northCommandService.commandStatus(instanceId);
    }
}
