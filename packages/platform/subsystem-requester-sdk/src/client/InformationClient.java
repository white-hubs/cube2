package com.guhecloud.subsystem.requester.sdk.client;

import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.requester.sdk.config.DynamicConfiguration;
import com.guhecloud.subsystem.requester.sdk.param.information.InformationParam;
import com.guhecloud.subsystem.requester.sdk.param.information.MaterialParam;
import com.guhecloud.subsystem.requester.sdk.param.information.ProgramScheduleParam;
import com.guhecloud.subsystem.requester.sdk.param.information.SendProgramToTerminalParam;
import com.guhecloud.subsystem.requester.sdk.vo.information.PLayRecordVo;
import com.guhecloud.subsystem.requester.sdk.vo.information.ProgramScheduleVo;
import com.guhecloud.subsystem.requester.sdk.vo.information.ProgramVo;
import com.guhecloud.subsystem.requester.sdk.vo.information.SendProgramToTerminalVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = SubsystemTypeConstants.INFORMATION_SERVER, configuration = DynamicConfiguration.class)
public interface InformationClient {
    /**
     * 播放记录
     *
     * @param informationParam
     *
     * @return
     */
    @PostMapping("/getPayLogs")
    List<PLayRecordVo> getPayLogs(@RequestBody InformationParam informationParam);

    /**
     * 节目单列表
     *
     * @param request
     *
     * @return
     */
    @PostMapping("/getProgramScheduleList")
    List<ProgramScheduleVo> getProgramScheduleList(@RequestBody ProgramScheduleParam request);

    /**
     * 节目列表
     *
     * @param request
     *
     * @return
     */
    @PostMapping("/getProgramList")
    List<ProgramVo> getProgramList(@RequestBody ProgramScheduleParam request);

    /**
     * 根据节目id集合批量获取节目对应的素材下载地址
     */
    @PostMapping("/getMaterialsByProgramIds")
    List<ProgramVo> getMaterialsByProgramIds(@RequestBody MaterialParam request);

    /**
     * @Description 发送节目到指定终端
     */
    @PostMapping("/program/sendProgramToTerminal")
    List<SendProgramToTerminalVo> sendProgramToTerminal(@RequestBody SendProgramToTerminalParam param);
}
