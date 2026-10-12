package com.guhecloud.cube.srv.gen.adapter.information.server.yozhiled.service.impl;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.cube.srv.gen.adapter.information.server.param.DeviceDTO;
import com.guhecloud.cube.srv.gen.adapter.information.server.param.ProgramScheduleParam;
import com.guhecloud.cube.srv.gen.adapter.information.server.param.SendProgramToTerminalParam;
import com.guhecloud.cube.srv.gen.adapter.information.server.vo.ProgramScheduleVo;
import com.guhecloud.cube.srv.gen.adapter.information.server.vo.ProgramVo;
import com.guhecloud.cube.srv.gen.adapter.information.server.vo.SendProgramToTerminalVo;
import com.guhecloud.cube.srv.gen.adapter.information.server.yozhiled.dto.SendProgramToTerminalResponse;
import com.guhecloud.cube.srv.gen.adapter.information.server.yozhiled.dto.YouZhiLedProgramResponse;
import com.guhecloud.cube.srv.gen.adapter.information.server.yozhiled.dto.YouZhiLedResponse;
import com.guhecloud.cube.srv.gen.adapter.information.server.yozhiled.enums.YouZhiLedCronEnum;
import com.guhecloud.cube.srv.gen.adapter.information.server.yozhiled.enums.YouZhiLedPriorityEnum;
import com.guhecloud.cube.srv.gen.adapter.information.server.yozhiled.enums.YouZhiLedSendProgramEnum;
import com.guhecloud.cube.srv.gen.adapter.information.server.yozhiled.enums.YouZhiLedStateEnum;
import com.guhecloud.cube.srv.gen.adapter.information.server.yozhiled.enums.YouZhiLedTypeEnum;
import com.guhecloud.cube.srv.gen.adapter.information.server.yozhiled.enums.YouZhiLedWeekCronEnum;
import com.guhecloud.cube.srv.gen.adapter.information.server.yozhiled.qo.YouZhiLedRequestQo;
import com.guhecloud.cube.srv.gen.adapter.information.server.yozhiled.qo.YouZhiLedSendProgramQo;
import com.guhecloud.cube.srv.gen.adapter.information.server.yozhiled.service.YouZhiLedInformationService;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemAdapterConfig;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.guhecloud.platform.base.util.ObjectUtils.getCopierBuilder;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class YouZhiLedInformationServiceImpl implements YouZhiLedInformationService {

    //服务名称
    private final String SERVICE_SEND = "send";
    private final String SERVICE_PROGRAM = "program";

    //获取指定终端的节目单列表
    public final static String METHOD_LIST = "list";
    //发送或者插播现有的节目
    public final static String METHOD_SEND_BY_PROGRAM_ID = "sendByProgramId";

    private final SubsystemRedisUtils subsystemRedisUtils;

    @Override
    public List<ProgramScheduleVo> getProgramInfoList(ProgramScheduleParam request) {
        log.info(">>>>>>>>>>>>深圳优质光电节目单列表>>>>>>>>>");
        MapperFacade facade = getCopierBuilder().getMapperFacade();
        List<ProgramScheduleVo> programScheduleVoList = new ArrayList<>();
        //1.拼接参数
        YouZhiLedRequestQo youZhiLedParam = this.getBasicConfig();
        String url = youZhiLedParam.getHttpFlag() + "://" + youZhiLedParam.getHost() + youZhiLedParam.getInterFace();
        //注：优质广电并未提供可通过终端id集合查询多个终端设备的接口，故这里是循环查询的。
        request.getClientIds().forEach(clientId -> {
            Map<String, Object> params = new HashMap<>();
            params.put("token", youZhiLedParam.getToken());
            params.put("service", SERVICE_SEND);
            params.put("method", METHOD_LIST);
            params.put("terminalId", clientId);
            //yyyy-MM-dd
            params.put("beginDate", request.getStartTime());
            params.put("endDate", request.getEndTime());
            //2.调用接口
            String body = HttpUtil.createPost(url + "?" + mapToParam(params))
                    .execute()
                    .body();
            YouZhiLedResponse yourZhiLedResponse = JSON.parseObject(body, YouZhiLedResponse.class);

            log.info("优质光电信息发布节目单列表 返回：{}", yourZhiLedResponse);
            //3.处理返回请求
            if (!ObjectUtils.isEmpty(yourZhiLedResponse) && !ObjectUtils.isEmpty(yourZhiLedResponse.getCode()) &&
                "0".equals(yourZhiLedResponse.getCode())) {
                if (!ObjectUtils.isEmpty(yourZhiLedResponse.getData())) {
                    List<YouZhiLedProgramResponse> data = JSON.parseArray(yourZhiLedResponse.getData().toString(),
                                                                          YouZhiLedProgramResponse.class);
                    List<ProgramScheduleVo> programScheduleVos = data.stream().map((source) -> {
                        ProgramScheduleVo programScheduleVo = facade.map(source, ProgramScheduleVo.class);
                        programScheduleVo.setClientId(clientId);
                        programScheduleVo.setPriority(Objects
                                                              .requireNonNull(YouZhiLedPriorityEnum.getByCode(programScheduleVo.getPriority()))
                                                              .getDesc());
                        programScheduleVo.setState(Objects
                                                           .requireNonNull(YouZhiLedStateEnum.getByCode(programScheduleVo.getState()))
                                                           .getDesc());
                        programScheduleVo.setType(Objects
                                                          .requireNonNull(YouZhiLedTypeEnum.getByCode(programScheduleVo.getType()))
                                                          .getDesc());
                        if (StringUtils.isNotBlank(source.getWeeks())) {
                            String weeks = Arrays.stream(source.getWeeks().split(","))
                                    .map(week -> Objects.requireNonNull(YouZhiLedWeekCronEnum
                                                                                .getByValue(Integer.parseInt(week))
                                                                                .getName()))
                                    .collect(Collectors.joining(","));
                            programScheduleVo.setWeeks(weeks);
                        }
                        return programScheduleVo;
                    }).toList();
                    programScheduleVoList.addAll(programScheduleVos);
                }
            }
        });
        return programScheduleVoList;
    }

    @Override
    public List<ProgramVo> getProgramList(ProgramScheduleParam request) {
        log.info(">>>>>>>>>>>>深圳优质光电节目列表>>>>>>>>>");
        //1.拼接参数
        YouZhiLedRequestQo youZhiLedParam = this.getBasicConfig();
        String url = youZhiLedParam.getHttpFlag() + "://" + youZhiLedParam.getHost() + youZhiLedParam.getInterFace();
        Map<String, Object> params = new HashMap<>();
        params.put("token", youZhiLedParam.getToken());
        params.put("service", SERVICE_PROGRAM);
        params.put("method", METHOD_LIST);
        //2.调用接口
        String body = HttpUtil.createPost(url + "?" + mapToParam(params))
                .execute()
                .body();
        YouZhiLedResponse yourZhiLedResponse = JSON.parseObject(body, YouZhiLedResponse.class);
        log.info("优质光电信息发布节目列表 返回：{}", yourZhiLedResponse);
        //3.处理返回请求
        if (!ObjectUtils.isEmpty(yourZhiLedResponse) && !ObjectUtils.isEmpty(yourZhiLedResponse.getCode()) &&
            "0".equals(yourZhiLedResponse.getCode())) {
            if (!ObjectUtils.isEmpty(yourZhiLedResponse.getData())) {
                return JSON.parseArray(yourZhiLedResponse.getData().toString(), ProgramVo.class);
            }
        }

        return Collections.emptyList();
    }

    @Override
    public List<SendProgramToTerminalVo> sendProgramToTerminal(SendProgramToTerminalParam param) {
        log.info(">>>>>>>>>>>>深圳优质光电发送节目>>>>>>>>>");
        ObjectMapper objectMapper = new ObjectMapper();
        //1.拼接参数
        YouZhiLedRequestQo youZhiLedParam = this.getBasicConfig();
        String url = youZhiLedParam.getHttpFlag() + "://" + youZhiLedParam.getHost() + youZhiLedParam.getInterFace();
        Map<String, Object> params = new HashMap<>();
        params.put("token", youZhiLedParam.getToken());
        params.put("service", SERVICE_PROGRAM);
        params.put("method", METHOD_SEND_BY_PROGRAM_ID);

        //拼接入参
        String jsonString = JSON.toJSONString(buildSendProgramToTerminalParam(param));
        //2.调用接口
        HttpResponse response = HttpUtil.createPost(url + "?" + mapToParam(params))
                .body(jsonString)
                .execute();
        YouZhiLedResponse yourZhiLedResponse = JSON.parseObject(response.body(), YouZhiLedResponse.class);
        log.info("优质光电信息发布节目列表 返回：{}", yourZhiLedResponse);
        //3.处理返回请求
        if (!ObjectUtils.isEmpty(yourZhiLedResponse) && !ObjectUtils.isEmpty(yourZhiLedResponse.getCode()) &&
            "0".equals(yourZhiLedResponse.getCode()) && !ObjectUtils.isEmpty(yourZhiLedResponse.getData())) {
            List<SendProgramToTerminalResponse> programToTerminalResponses = JSON.parseArray(yourZhiLedResponse.getData()
                                                                                                     .toString(), SendProgramToTerminalResponse.class);
            return programToTerminalResponses.stream().map(sendProgramToTerminalResponse -> {
                SendProgramToTerminalVo vo = new SendProgramToTerminalVo();
                vo.setId(StringUtils.isEmpty(sendProgramToTerminalResponse.getId()) ? "" : sendProgramToTerminalResponse.getId());
                vo.setTerminalId(StringUtils.isEmpty(sendProgramToTerminalResponse.getTerminalId()) ? ""
                                                                                                    : sendProgramToTerminalResponse.getTerminalId());
                Map<String, Object> map = new HashMap<>();
                map.put("sendResult", Objects.nonNull(sendProgramToTerminalResponse.getStatus()) ? Objects
                        .requireNonNull(YouZhiLedSendProgramEnum.getByCode(sendProgramToTerminalResponse.getStatus())).getDesc()
                                                                                                 : "");

                try {
                    vo.setSendResult(objectMapper.writeValueAsString(map));
                } catch (JsonProcessingException e) {
                    throw new ServiceException("优质光电信息发布 发送结果序列化失败");
                }
                return vo;
            }).collect(Collectors.toList());
        } else {
            return param.getTerminals().stream().map(terminal -> {
                SendProgramToTerminalVo vo = new SendProgramToTerminalVo();
                vo.setId("");
                vo.setTerminalId(terminal.getTerminalId());
                Map<String, Object> map = new HashMap<>();
                map.put("sendResult", YouZhiLedSendProgramEnum.OFFLINE.getDesc());
                try {
                    vo.setSendResult(objectMapper.writeValueAsString(map));
                } catch (JsonProcessingException e) {
                    throw new ServiceException("优质光电信息发布 发送结果序列化失败");
                }
                return vo;
            }).collect(Collectors.toList());
        }
    }

    /**
     * 获取基础配置
     *
     * @return 基础配置
     */
    private YouZhiLedRequestQo getBasicConfig() {
        String key = SubsystemTypeConstants.INFORMATION_SERVER + ":" + "youZhiLed" + ":" +
                     SessionContextHolder.getRequester().getTenant().toString();
        SubsystemAdapterConfig subAdapterRefreshConfig = subsystemRedisUtils.getSubAdapterRefreshConfig(key);
        return JsonUtils.deserialize(subAdapterRefreshConfig.getManufacturerJson(), YouZhiLedRequestQo.class);
    }

    /**
     * map转params数据
     *
     * @param params
     *
     * @return
     */
    private static String mapToParam(Map<String, Object> params) {
        StringBuilder sb = new StringBuilder();
        if (params != null) {
            for (Map.Entry<String, Object> e : params.entrySet()) {
                sb.append(e.getKey());
                sb.append("=");
                sb.append(e.getValue());
                sb.append("&");
            }
            //去掉最后一个&
            if (sb.length() > 0) {
                sb.substring(0, sb.length() - 1);
            }
        }
        return sb.toString();
    }

    private YouZhiLedSendProgramQo buildSendProgramToTerminalParam(SendProgramToTerminalParam param) {
        return YouZhiLedSendProgramQo.builder()
                .beginDate(DateUtils.formatDate(param.getBeginDate()))
                .beginTime(param.getBeginTime())
                .endDate(DateUtils.formatDate(param.getEndDate()))
                .endTime(param.getEndTime())
                .priority(param.getPriority())
                .programId(param.getThirdProgramId())
                .terminalIds(param.getTerminals().stream().map(DeviceDTO::getTerminalId)
                                     .collect(Collectors.toList()))
                .weeks(param.getExecutionCycle().stream()
                               .map(week -> Objects.requireNonNull(YouZhiLedCronEnum.getByName(week))
                                       .getValue())
                               .collect(Collectors.toList()))
                .build();
    }
}
