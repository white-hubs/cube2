package com.guhecloud.cube.iot.mgt.north.controller;

import com.guhecloud.cube.iot.mgt.core.data.base.Pair;
import com.guhecloud.cube.iot.mgt.north.mapper.NorthDeviceMapper;
import com.guhecloud.cube.iot.mgt.north.server.impl.NorthCommandService;
import com.guhecloud.cube.iot.mgt.north.server.impl.NorthProductService;
import com.guhecloud.cube.iot.mgt.north.vo.CommandRequest;
import com.guhecloud.cube.iot.mgt.north.vo.CommandResponse;
import com.guhecloud.cube.iot.mgt.north.vo.DeviceCommandIssues;
import com.guhecloud.cube.iot.mgt.north.vo.ProductEventVo;
import com.guhecloud.cube.iot.mgt.north.vo.ProductVo;
import com.guhecloud.cube.iot.mgt.core.data.lang.cmd.CommandExecStatusEnum;
import com.guhecloud.platform.base.util.StringUtils;
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

import static com.guhecloud.cube.iot.mgt.core.data.lang.SpecialValConst.buildEventThresholdCode;
import static com.guhecloud.cube.iot.mgt.core.data.lang.SpecialValConst.isDeviceEventThreshold;

/**
 * 扩展事件定义<br/>
 * 用于支持阈值类命令
 */
@Slf4j
@RestController
@RequestMapping("/north/device/event/ext")
public class NorthDeviceEventExtController {

    @Resource
    private NorthCommandService northCommandService;

    @Resource
    private NorthProductService northProductService;

    @Resource
    private NorthDeviceMapper northDeviceMapper;

    /**
     * 命令下发事件阈值设定
     *
     * @param deviceCommandIssues deviceCommandIssues
     *
     * @return CommandResponse
     */
    @PostMapping("/command/issues")
    public CommandResponse thresholdSet(@RequestBody DeviceCommandIssues deviceCommandIssues) {//阈值
        Pair<ProductEventVo, String> pair = findThresholdEvent(String.valueOf(deviceCommandIssues.getDeviceId()),
                                                               deviceCommandIssues.getEventCode(),
                                                               deviceCommandIssues.getThresholdCode());

        if (Objects.isNull(pair)) {
            CommandResponse commandResponse = new CommandResponse();
            commandResponse.setStatusCode(CommandExecStatusEnum.REJECT.code);
            commandResponse.setMessage("指定事件不能进行值设定!请检查配置");
            return commandResponse;
        }

        return northCommandService.commandIssue(buildCommandRequest(deviceCommandIssues, pair.getVal()));
    }

    /**
     * 批量命令下发
     *
     * @param deviceCommandIssues deviceCommandIssues
     *
     * @author Fl
     **/
    @PostMapping("/command/issue/batch")
    public List<CommandResponse> projectCommandIssue(@RequestBody Set<DeviceCommandIssues> deviceCommandIssues) {
        if (Objects.isNull(deviceCommandIssues)) {
            return Collections.emptyList();
        }

        List<CommandResponse> result = new ArrayList<>(deviceCommandIssues.size());
        for (DeviceCommandIssues dci : deviceCommandIssues) {
            try {
                result.add(this.thresholdSet(dci));
            } catch (Exception e) {
                CommandResponse commandResponse = new CommandResponse();
                commandResponse.setCommandHandle(dci.getCommandSeq());
                commandResponse.setCommandSeq(dci.getCommandSeq());
                commandResponse.setMessage(e.getMessage());
                commandResponse.setStatusCode(CommandExecStatusEnum.REJECT.code);
                result.add(new CommandResponse());
                log.error("command issue error", e);
            }
        }

        return result;
    }

    /**
     * 查询命令状态
     *
     * @param instanceId instanceId
     *
     * @author Fl
     **/
    @GetMapping("/command/issue/status")
    public CommandResponse projectCommandStatus(@RequestParam("instanceId") String instanceId) {
        return northCommandService.commandStatus(instanceId);
    }

    /**
     * 构建命令请求
     *
     * @param deviceCommandIssues 设备命令
     * @param propCode            属性编码
     *
     * @return CommandRequest
     */
    private CommandRequest buildCommandRequest(DeviceCommandIssues deviceCommandIssues, String propCode) {
        CommandRequest commandRequest = new CommandRequest();
        commandRequest.setDeviceId(String.valueOf(deviceCommandIssues.getDeviceId()));
        commandRequest.setPropCode(propCode);
        commandRequest.setPropValue(deviceCommandIssues.getValue());
        commandRequest.setCommandSeq(deviceCommandIssues.getCommandSeq());
        return commandRequest;
    }

    /**
     * 查询阈值事件
     *
     * @param deviceId      设备ID
     * @param eventCode     事件编码
     * @param thresholdCode 阈值编码
     *
     * @return ProductEventVo 事件 和 对应下发的属性code 不存在返回null
     */
    private Pair<ProductEventVo, String> findThresholdEvent(String deviceId, String eventCode, String thresholdCode) {
        if (StringUtils.isBlank(deviceId) || StringUtils.isBlank(eventCode) || StringUtils.isBlank(thresholdCode)) {
            return null;
        }

        //判断是否是阈值事件
        if (!isDeviceEventThreshold(thresholdCode)) {
            return null;
        }

        Long productId = northDeviceMapper.queryProductId(Long.parseLong(deviceId));
        //查询产品详情
        List<ProductVo> productVos = northProductService.prodListDetail(Collections.singleton(productId));
        if (Objects.isNull(productVos) || productVos.isEmpty()) {
            return null;
        }

        //产品详情
        ProductVo productVo = productVos.get(0);
        List<ProductEventVo> productEventVos = productVo.getProductEventVoList();

        ProductEventVo matchEvent = null;
        //查询事件是否存在
        for (ProductEventVo productEventVo : productEventVos) {
            if (eventCode.equals(productEventVo.getEventCode())) {
                matchEvent = productEventVo;
                break;
            }
        }

        if (Objects.isNull(matchEvent)) {
            return null;
        }

        return new Pair<>(matchEvent, buildEventThresholdCode(eventCode, thresholdCode));
    }
}
