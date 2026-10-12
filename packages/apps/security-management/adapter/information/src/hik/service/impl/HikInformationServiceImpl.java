package com.guhecloud.cube.srv.gen.adapter.information.server.hik.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.cube.srv.gen.adapter.information.server.hik.dto.HikDataResponse;
import com.guhecloud.cube.srv.gen.adapter.information.server.hik.dto.HikMaterialResponse;
import com.guhecloud.cube.srv.gen.adapter.information.server.hik.dto.HikSendProgramResponse;
import com.guhecloud.cube.srv.gen.adapter.information.server.hik.enums.HikInsertTypeEnum;
import com.guhecloud.cube.srv.gen.adapter.information.server.hik.enums.HikSendStatusEnum;
import com.guhecloud.cube.srv.gen.adapter.information.server.hik.enums.HikUniqueFieldTypeEnum;
import com.guhecloud.cube.srv.gen.adapter.information.server.hik.qo.HikInsertInfoDetailParam;
import com.guhecloud.cube.srv.gen.adapter.information.server.hik.qo.HikProgramInfoParam;
import com.guhecloud.cube.srv.gen.adapter.information.server.hik.qo.HikRequestQo;
import com.guhecloud.cube.srv.gen.adapter.information.server.hik.qo.HikSendProgramParam;
import com.guhecloud.cube.srv.gen.adapter.information.server.hik.service.HikInformationService;
import com.guhecloud.cube.srv.gen.adapter.information.server.param.DeviceDTO;
import com.guhecloud.cube.srv.gen.adapter.information.server.param.MaterialParam;
import com.guhecloud.cube.srv.gen.adapter.information.server.param.ProgramScheduleParam;
import com.guhecloud.cube.srv.gen.adapter.information.server.param.SendProgramToTerminalParam;
import com.guhecloud.cube.srv.gen.adapter.information.server.vo.MaterialVo;
import com.guhecloud.cube.srv.gen.adapter.information.server.vo.ProgramVo;
import com.guhecloud.cube.srv.gen.adapter.information.server.vo.SendProgramToTerminalVo;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemAdapterConfig;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SuppressWarnings("DoubleBraceInitialization")
public class HikInformationServiceImpl implements HikInformationService {

    //分页获取节目列表
    private final String PROGRAMS = "/api/xpublish-web/v1/programService/programs";

    //插播节目
    private final String INSERT_CONTENT = "/api/xpublish-web/v1/operationService/insertContent";

    private final SubsystemRedisUtils subsystemRedisUtils;

    @Override
    public List<ProgramVo> getProgramList(ProgramScheduleParam request) {
        log.info(">>>>>>>>>>>>海康信息发布节目单列表采集>>>>>>>>>");
        JSONObject jsonBody = new JSONObject();
        //1.调用海康服务
        String result = callHikInterface(PROGRAMS, jsonBody.toJSONString());
        log.info("海康信息发布 返回：{}", result);
        //2.处理返回请求
        if (StringUtils.isNotBlank(result)) {
            JSONObject responseBody = JSONObject.parseObject(result);
            if ("0".equals(responseBody.getString("code"))) {
                HikDataResponse data = responseBody.getObject(responseBody.getString("code"), HikDataResponse.class);
                if (!ObjectUtils.isEmpty(data) && !CollectionUtils.isEmpty(data.getList())) {
                    return data.getList().stream().map(programDto -> {
                        ProgramVo programVo = new ProgramVo();
                        programVo.setId(
                                ObjectUtils.isEmpty(programDto.getProgramId()) ? "" : programDto.getProgramId().toString());
                        programVo.setName(programDto.getProgramName());
                        programVo.setCategoryName(programDto.getProgramTypeName());
                        return programVo;
                    }).collect(Collectors.toList());
                }
            }
        }
        return Collections.emptyList();
    }

    @Override
    public List<ProgramVo> getMaterialsByProgramId(MaterialParam request) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<ProgramVo> programVos = new ArrayList<>();
        log.info(">>>>>>>>>>>>海康信息发布获取节目对应的素材>>>>>>>>>");
        request.getProgramIds().forEach(programId -> {
            ProgramVo vo = new ProgramVo();
            vo.setId(programId);

            Map<String, Object> params = new HashMap<>();
            params.put("programId", programId);
            //1.调用海康服务
            try {
                String result = callHikInterface(PROGRAMS, objectMapper.writeValueAsString(params));
                log.info("海康信息发布 返回：{}", result);
                //2.处理返回请求
                if (StringUtils.isNotBlank(result)) {
                    JSONObject responseBody = JSONObject.parseObject(result);
                    if ("0".equals(responseBody.getString("code"))) {
                        HikMaterialResponse data = responseBody.getObject(responseBody.getString("code"), HikMaterialResponse.class);
                        if (!ObjectUtils.isEmpty(data) && !CollectionUtils.isEmpty(data.getList())) {
                            vo.setMaterials(data.getList().stream().map(materialDto -> {
                                MaterialVo materialVo = new MaterialVo();
                                materialVo.setMaterialId(ObjectUtils.isEmpty(materialDto.getMaterialId()) ? "" : materialDto
                                        .getMaterialId().toString());
                                materialVo.setMaterialName(materialDto.getMaterialName());
                                materialVo.setAddress(materialDto.getAddress());
                                return materialVo;
                            }).collect(Collectors.toList()));
                            programVos.add(vo);
                        }
                    }
                }
            } catch (JsonProcessingException e) {
                log.error("海康信息发布获取节目对应的素材，入参序列化失败：{}", params);
                throw new ServiceException("海康信息发布获取节目对应的素材，入参序列化失败");
            }
        });
        return programVos;
    }

    @Override
    public List<SendProgramToTerminalVo> sendProgramToTerminal(SendProgramToTerminalParam param) {
        log.info(">>>>>>>>>>>>海康信息发布发送节目到指定终端>>>>>>>>>");
        ObjectMapper objectMapper = new ObjectMapper();
        HikSendProgramParam paramMap = buildSendProgramToTerminalParam(param);
        List<SendProgramToTerminalVo> sendProgramToTerminalVos = new ArrayList<>();
        //1、调用海康服务
        try {
            String result = callHikInterface(INSERT_CONTENT, objectMapper.writeValueAsString(paramMap));
            log.info("海康信息发布发送节目到指定终端 返回：{}", result);
            //2.处理返回请求
            if (StringUtils.isNotBlank(result)) {
                HikSendProgramResponse hikSendProgramResponse = objectMapper.readValue(result, HikSendProgramResponse.class);
                if ("0".equals(hikSendProgramResponse.getCode())) {
                    return param.getTerminals().stream().map(terminal -> {
                        String sendResult = HikSendStatusEnum.getDescByCode(hikSendProgramResponse.getType());
                        Map<String, Object> sendResultMap = new HashMap<>();
                        sendResultMap.put("sendResult", sendResult);

                        SendProgramToTerminalVo sendProgramToTerminalVo = new SendProgramToTerminalVo();
                        sendProgramToTerminalVo.setId(hikSendProgramResponse.getData());
                        try {
                            sendProgramToTerminalVo.setSendResult(objectMapper.writeValueAsString(sendResultMap));
                        } catch (JsonProcessingException e) {
                            throw new ServiceException("海康信息发布发送节目到指定终端 返回结果序列化失败");
                        }
                        sendProgramToTerminalVo.setTerminalId(terminal.getTerminalId());
                        return sendProgramToTerminalVo;
                    }).collect(Collectors.toList());
                }
            }
        } catch (JsonProcessingException e) {
            log.error("海康信息发布发送节目到指定终端，入参序列化失败：{}", paramMap);
            throw new ServiceException("海康信息发布发送节目到指定终端，入参序列化失败");
        }
        return sendProgramToTerminalVos;
    }

    /**
     * 获取基础配置
     *
     * @return 基础配置
     */
    private SubsystemAdapterConfig getBasicConfig() {
        String key = SubsystemTypeConstants.INFORMATION_SERVER + ":" + "hik" + ":" +
                     SessionContextHolder.getRequester().getTenant().toString();
        return subsystemRedisUtils.getSubAdapterRefreshConfig(key);
    }

    /**
     * 调海康接口
     *
     * @param url  请求地址
     * @param body 请求参数
     *
     * @return 结果
     */
    private String callHikInterface(String url, String body) {
        log.info("海康请求url:{},请求参数:{}", url, body);
        SubsystemAdapterConfig subAdapterRefreshConfig = this.getBasicConfig();
        HikRequestQo hikParam = JsonUtils.deserialize(subAdapterRefreshConfig.getManufacturerJson(), HikRequestQo.class);
        ArtemisConfig.host = hikParam.getHost();
        ArtemisConfig.appKey = hikParam.getAppKey();
        ArtemisConfig.appSecret = hikParam.getAppSecret();
        final String ARTEMIS_PATH = "/artemis";
        final String previewURLsApi = ARTEMIS_PATH + url;
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                //根据现场环境部署确认是http还是https
                put("https://", previewURLsApi);
            }
        };
        String contentType = MediaType.APPLICATION_JSON_VALUE;
        return ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType, null);
    }

    private HikSendProgramParam buildSendProgramToTerminalParam(SendProgramToTerminalParam param) {
        return HikSendProgramParam.builder()
                .terminalUniqueFieldType(HikUniqueFieldTypeEnum.ID_TYPE.getCode())
                .terminalUniqueFieldValues(param.getTerminals().stream().map(DeviceDTO::getTerminalId)
                                                   .collect(Collectors.toList()))
                .orgIndexCodes(new ArrayList<>())
                .intervalTime(0)
                .insertInfoDetail(HikInsertInfoDetailParam.builder()
                                          .insertType(HikInsertTypeEnum.PROGRAM.getCode())
                                          .info(HikProgramInfoParam.builder()
                                                        .type(HikUniqueFieldTypeEnum.ID_TYPE.getCode())
                                                        .uniqueField(param.getThirdProgramId())
                                                        .playMode("byTime")
                                                        .playDuration(120).build())
                                          .build())
                .build();
    }
}
