package com.guhecloud.cube.srv.gen.adapter.information.server.das.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.srv.gen.adapter.information.server.das.dto.DasProgramResponse;
import com.guhecloud.cube.srv.gen.adapter.information.server.das.dto.DasResponse;
import com.guhecloud.cube.srv.gen.adapter.information.server.das.enums.DasProgramTypeEnum;
import com.guhecloud.cube.srv.gen.adapter.information.server.das.qo.DasRequestQo;
import com.guhecloud.cube.srv.gen.adapter.information.server.das.qo.DasSendProgramParam;
import com.guhecloud.cube.srv.gen.adapter.information.server.das.service.DasInformationService;
import com.guhecloud.cube.srv.gen.adapter.information.server.param.ProgramScheduleParam;
import com.guhecloud.cube.srv.gen.adapter.information.server.param.SendProgramToTerminalParam;
import com.guhecloud.cube.srv.gen.adapter.information.server.vo.ProgramVo;
import com.guhecloud.cube.srv.gen.adapter.information.server.vo.SendProgramToTerminalVo;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemAdapterConfig;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DasInformationServiceImpl implements DasInformationService {

    private final String API_URL = "/API.aspx";

    private final SubsystemRedisUtils subsystemRedisUtils;

    /**
     * 节目列表(节目库)
     */
    @Override
    public List<ProgramVo> getProgramList(ProgramScheduleParam request) {
        log.info(">>>>>>>>>>>>das获取节目库>>>>>>>>>");
        // 1. 获取基础参数
        DasRequestQo dasRequestQo = this.getBasicConfig();

        // 2. 调用接口并返回
        return this.getResourceList(dasRequestQo).stream().map(dasProgramPageDTO -> ProgramVo.builder()
                        .id(String.valueOf(dasProgramPageDTO.getId()))
                        .name(dasProgramPageDTO.getName())
                        .categoryName(DasProgramTypeEnum.getDescByType(dasProgramPageDTO.getState()))
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * 发送节目到指定终端
     */
    @Override
    public List<SendProgramToTerminalVo> sendProgramToTerminal(SendProgramToTerminalParam param) {
        log.info(">>>>>>>>>>>>das发送节目>>>>>>>>>");
        DasRequestQo dasRequestQo = this.getBasicConfig();
        //发送节目
        return param.getTerminals().stream().map(terminal -> {
            //封装节目的内容
            DasSendProgramParam dasSendProgramParam = buildSendProgramToTerminalParam(param);
            dasSendProgramParam.setDeviceId(terminal.getTerminalId());

            DasResponse response = this.getStorePush(dasRequestQo, JSON.toJSONString(dasSendProgramParam));
            if ("0".equals(response.getErrorCode())) {
                return SendProgramToTerminalVo.builder().id(String.valueOf(IdWorker.getId()))
                        .terminalId(terminal.getTerminalName())
                        .sendResult("发送成功").sendStatus(0).build();
            } else {
                return SendProgramToTerminalVo.builder().id(String.valueOf(IdWorker.getId()))
                        .terminalId(terminal.getTerminalName())
                        .sendResult("发送失败").sendStatus(1).build();
            }
        }).collect(Collectors.toList());
    }

    /**
     * 获取基础配置
     *
     * @return 基础配置
     */
    private DasRequestQo getBasicConfig() {
        String key = SubsystemTypeConstants.INFORMATION_SERVER + ":" + "das" + ":" +
                     SessionContextHolder.getRequester().getTenant().toString();
        SubsystemAdapterConfig subAdapterRefreshConfig = subsystemRedisUtils.getSubAdapterRefreshConfig(key);
        return JsonUtils.deserialize(subAdapterRefreshConfig.getManufacturerJson(), DasRequestQo.class);
    }

    private DasSendProgramParam buildSendProgramToTerminalParam(SendProgramToTerminalParam param) {
        return DasSendProgramParam.builder()
                .programId(param.getThirdProgramId())
                .priority("1")
                .beginTime(DateUtils.formatDate(param.getBeginDate()))
                .endTime(DateUtils.formatDate(param.getEndDate()))
                .build();
    }

    /**
     * 获取节目列表分页
     *
     * @param dasRequestQo itc信息发布配置文件
     */
    public List<DasProgramResponse.DasProgramPageDTO> getResourceList(DasRequestQo dasRequestQo) {
        Map<String, String> pathParam = new HashMap<>();
        pathParam.put("apiId", "program");
        pathParam.put("command", "getprogram");
        String url = this.buildPathParamUrl(pathParam, dasRequestQo.getUrl() + API_URL);
        //2.获取节目详情中的图片url
        String body = HttpUtil.createPost(url)
                .execute()
                .body();
        DasProgramResponse dasProgramResponse = JSON.parseObject(body, DasProgramResponse.class);
        log.info("das信息发布节目列表分页 返回：{}", dasProgramResponse);
        //3.处理返回请求
        if (!"0".equals(dasProgramResponse.getErrorCode())) {
            log.error("das信息发布获取节目列表分页失败, response:{}", body);
            return null;
        } else {
            return dasProgramResponse.getData();
        }
    }

    /**
     * 发送节目
     *
     * @param dasRequestQo das信息发布配置文件
     * @param paramJson    入参body
     */
    public DasResponse getStorePush(DasRequestQo dasRequestQo, String paramJson) {
        Map<String, String> pathParam = new HashMap<>();
        pathParam.put("apiId", "program");
        pathParam.put("command", "programsend");
        pathParam.put("autoCheck", "true");
        String url = this.buildPathParamUrl(pathParam, dasRequestQo.getUrl() + API_URL);
        //2.获取节目详情中的图片url
        String body = HttpUtil.createPost(url)
                .body(paramJson)
                .execute()
                .body();
        DasResponse dasProgramResponse = JSON.parseObject(body, DasResponse.class);
        log.info("das信息发布节目列表分页 返回：{}", dasProgramResponse);
        //3.处理返回请求
        if (!"0".equals(dasProgramResponse.getErrorCode())) {
            log.error("das信息发布获取节目列表分页失败, response:{}", body);
            return null;
        } else {
            return dasProgramResponse;
        }
    }

    private String buildPathParamUrl(Map<String, String> pathParam, String url) {
        DasRequestQo dasRequestQo = this.getBasicConfig();

        url = url.concat("?accessToken=").concat(dasRequestQo.getToken());
        if (!pathParam.isEmpty()) {
            for (Map.Entry<String, String> entry : pathParam.entrySet()) {
                url = url.concat("&").concat(entry.getKey()).concat("=").concat(entry.getValue());
            }
        }
        return url;
    }
}
