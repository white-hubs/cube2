package com.guhecloud.cube.srv.gen.conferencemanage.server.third.service.imp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.BookRoom;
import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.RoomScreenConfig;
import com.guhecloud.cube.srv.gen.conferencemanage.server.third.constant.ThirdConstant;
import com.guhecloud.cube.srv.gen.conferencemanage.server.third.enumeration.ManufacturerCodeEnum;
import com.guhecloud.cube.srv.gen.conferencemanage.server.third.param.AttributeParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.third.param.InstructCommand;
import com.guhecloud.cube.srv.gen.conferencemanage.server.third.param.ProgramParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.third.param.ProgramSendParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.third.response.HuiDuResponse;
import com.guhecloud.cube.srv.gen.conferencemanage.server.third.service.DeviceProgramSendService;
import com.guhecloud.cube.srv.gen.conferencemanage.server.util.HttpClientUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/9/24 10:15
 * @Description: 设备节目发送服务实现类
 */

@Slf4j
@Service
public class DeviceProgramSendServiceImpl implements DeviceProgramSendService {

    /**
     * 会议屏展示URL
     */
    public static final String ROOM_SCREEN_URL = "/apps/@ghcloud/cube-meet-room-data-viz/#/entry?id=%s";

    public static final String REQUEST_HEADER_ORIGIN = "Origin";

    @Override
    public void newProgram(RoomScreenConfig screenConfig, HttpServletRequest httpServletRequest) {
        if (StringUtils.isBlank(screenConfig.getProgramUrl())) {
            return;
        }

        String basePreFix = HttpClientUtils.getUrlPrefix(screenConfig.getProgramUrl());
        //下发节目参数
        ProgramSendParam programSendParam = new ProgramSendParam();
        programSendParam.setSdk(true);
        programSendParam.setReset(true);
        programSendParam.setType(2);
        programSendParam.setDevices(Collections.singletonList(screenConfig.getDeviceCode()));
        //节目信息
        ProgramParam programParam = new ProgramParam();
        programParam.setUuid(UUID.randomUUID().toString());
        programParam.setVersion(1);
        programParam.setName("会议室预约情况");
        programParam.setAttributes(AttributeParam.builder()
                                           .width(1920)
                                           .language("zh-CN")
                                           .height(1080)
                                           .build());

        ProgramParam children = new ProgramParam();
        children.setUuid(UUID.randomUUID().toString());
        children.setName("会议室预约情况");
        children.setType("html");
        children.setAttributes(AttributeParam.builder()
                                       .x(0)
                                       .width(1920)
                                       .y(0)
                                       .height(1080)
                                       .build());

        ProgramParam widget = new ProgramParam();
        widget.setUuid(UUID.randomUUID().toString());
        widget.setName("html1");
        widget.setType("html");
        widget.setAttributes(AttributeParam.builder()
                                     .offsetX(0)
                                     .duration(86400)
                                     .offsetY(0)
                                     .refresh(screenConfig.getCronExpression())
                                     .style(1)
                                     .url(httpServletRequest.getHeader(REQUEST_HEADER_ORIGIN) +
                                          String.format(ROOM_SCREEN_URL, screenConfig.getConferenceId()))
                                     .build());
        log.info("下发节目url:{}", widget.getAttributes().getUrl());
        children.setWidgets(Collections.singletonList(widget));

        programParam.setChildren(Collections.singletonList(children));

        programSendParam.setPrograms(Collections.singletonList(programParam));
        String prarmJsonString = this.getPrarmJsonString(programSendParam);
        Map<String, String> headers = new HashMap<>();
        headers.put(HttpHeaders.AUTHORIZATION, screenConfig.getAuthInfo());
        String string = HttpClientUtils.doPostJson(basePreFix + ThirdConstant.PUBLISH_PROGRAM_URL, headers, prarmJsonString);

        HuiDuResponse huiDuResponse = JsonUtils.deserialize(string, HuiDuResponse.class);
        if (huiDuResponse.getCode() != 200) {
            throw new ServiceException("下发节目失败:" + huiDuResponse.getMessage());
        }
    }

    /**
     * 序列化对象为json字符串
     *
     * @param param
     *
     * @return
     */
    private String getPrarmJsonString(Object param) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String prarmJsonString;
        try {
            prarmJsonString = objectMapper.writeValueAsString(param);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return prarmJsonString;
    }

    /**
     * {
     * "command": "clear",
     * "devices": [
     * "3288S-2C4-01B75"
     * ],
     * "args": {
     * "displayId": 0
     * }
     * }
     *
     * @param screenConfig
     */
    @Override
    public void clearProgram(RoomScreenConfig screenConfig) {
        String basePreFix = HttpClientUtils.getUrlPrefix(screenConfig.getProgramUrl());
        InstructCommand.Args args = new InstructCommand.Args();
        args.setDisplayId(0);
        InstructCommand clear = InstructCommand.builder()
                .command("clear")
                .devices(Collections.singletonList(screenConfig.getDeviceCode()))
                .args(args)
                .build();
        String prarmJsonString = this.getPrarmJsonString(clear);
        Map<String, String> headers = new HashMap<>();
        headers.put(HttpHeaders.AUTHORIZATION, screenConfig.getAuthInfo());
        String string = HttpClientUtils.doPostJson(basePreFix + ThirdConstant.CLEAR_PROGRAM_URL, headers, prarmJsonString);
        HuiDuResponse huiDuResponse = JsonUtils.deserialize(string, HuiDuResponse.class);
        if (huiDuResponse.getCode() != 200) {
            throw new ServiceException("清空节目失败:" + huiDuResponse.getMessage());
        }
    }

    @Override
    public void sendProgram(BookRoom bookRoom, RoomScreenConfig roomScreenConfig) {

    }

    @Override
    public boolean support(String manufacturerCode) {
        return ManufacturerCodeEnum.HUI_DU.name().equals(manufacturerCode);
    }
}
