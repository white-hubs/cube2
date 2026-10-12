package com.guhecloud.cube.biz.adapter.person.rollcall.server.application.hikspcc.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.biz.adapter.person.rollcall.server.application.hikspcc.model.HikSpccRollCallConfig;
import com.guhecloud.cube.biz.adapter.person.rollcall.server.application.hikspcc.mqtt.HikSpccRollCallMqttClientProtocol;
import com.guhecloud.cube.biz.adapter.person.rollcall.server.application.hikspcc.param.HikSpccRequestParam;
import com.guhecloud.cube.biz.adapter.person.rollcall.server.application.hikspcc.service.HikSpccRollCallService;
import com.guhecloud.cube.biz.adapter.person.rollcall.server.application.hikspcc.vo.HikSpccRollCallRecordVo;
import com.guhecloud.cube.biz.adapter.person.rollcall.server.param.PersonRollCallRecordParam;
import com.guhecloud.cube.biz.adapter.person.rollcall.server.vo.PersonRollCallRecordVO;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemAdapterConfig;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author xiaofeng
 * @describe 海康 智慧监管综合平台
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HikSpccRollCallServiceImpl implements HikSpccRollCallService {

    //设置OpenAPI接口的上下文
    private final String ARTEMIS_PATH = "/artemis";

    private final String clientHttps = "https://";

    private final HikSpccRollCallMqttClientProtocol hikSpccRollCallMqttClientProtocol;
    private final SubsystemRedisUtils subsystemRedisUtils;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void reConnect(SubsystemAdapterConfig subscribeInitMessage) {
        log.info("海康智慧监管综合平台 人员清点 重连mqtt,param={}", subscribeInitMessage);

    }

    @Override
    public List<PersonRollCallRecordVO> getRollCallRecord(PersonRollCallRecordParam param) {
        List<HikSpccRollCallRecordVo> hikSpccRollCallRecordVos = new ArrayList<>();
        log.info("海康 智慧监管综合平台 查询点名历史记录，参数为：" + JsonUtils.serialize(param));
        String contentType = "application/json";
        Map<String, String> path = this.getAuth("/api/rollcall/v1/record/query");

        int pageNo = 1;
        int pageSize = 1000;
        int total = 0;

        // 发送第一次请求以获取总记录数
        JSONObject jsonBody = new JSONObject();
        if (StringUtils.isNotEmpty(param.getEndTime()) && StringUtils.isNotEmpty(param.getStartTime())) {
            String[] startAndEnd = formatDateToISO8601(param.getStartTime(), param.getEndTime());
            jsonBody.put("beginTime", startAndEnd[0]);
            jsonBody.put("endTime", startAndEnd[1]);
        }
        jsonBody.put("pageNo", pageNo);
        jsonBody.put("pageSize", pageSize);

        String body = jsonBody.toJSONString();
        log.info("海康 智慧监管综合平台 查询点名历史记录 采集参数：" + body);
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType, null);
        log.info("海康 智慧监管综合平台 查询点名历史记录，返回结果:" + result);

        JSONObject jsonObject = JSONObject.parseObject(result);
        String data = jsonObject.get("data").toString();
        JSONObject jsonObject1 = JSONObject.parseObject(data);
        total = (Integer) jsonObject1.get("total");

        // 计算需要请求的总页数
        int totalPages = (int) Math.ceil((double) total / pageSize);

        for (int i = 0; i < totalPages; i++) {
            jsonBody.put("pageNo", pageNo + i);
            body = jsonBody.toJSONString();
            log.info("海康 智慧监管综合平台 查询点名历史记录 采集参数：" + body);
            result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType, null);
            log.info("海康 智慧监管综合平台 查询点名历史记录，返回结果:" + result);

            jsonObject = JSONObject.parseObject(result);
            data = jsonObject.get("data").toString();
            jsonObject1 = JSONObject.parseObject(data);
            JSONArray jsonArray = jsonObject1.getJSONArray("list");
            List<HikSpccRollCallRecordVo> hikSpccRollCallRecordVoList = jsonArray.toJavaList(HikSpccRollCallRecordVo.class);
            if (!CollectionUtils.isEmpty(hikSpccRollCallRecordVoList)) {
                hikSpccRollCallRecordVos.addAll(hikSpccRollCallRecordVoList);
            }
        }

        return hikDoorEventConvert(hikSpccRollCallRecordVos);
    }

    private List<PersonRollCallRecordVO> hikDoorEventConvert(List<HikSpccRollCallRecordVo> dataList) {
        if (!CollectionUtils.isEmpty(dataList)) {
            return dataList.stream().map(r -> {
                return PersonRollCallRecordVO.builder()
                        .recordCode(r.getRecordCode())
                        .taskName(r.getTaskName())
                        .areaIndexCode(r.getAreaIndexCode())
                        .areaName(r.getAreaName())
                        .category(r.getCategory())
                        .direction(r.getDirection())
                        .startTime(r.getStartTime())
                        .endTime(r.getEndTime())
                        .consumingTime(r.getConsumingTime())
                        .type(r.getType())
                        .status(r.getStatus())
                        .rollcallNum(r.getRollcallNum())
                        .unrollcallNum(r.getUnRollcallNum())
                        .specialNum(r.getSpeciallNum())
                        .remark(r.getRemark())
                        .build();
            }).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    /**
     * @Description 平台访问
     * @Author xiaofeng
     */
    private void login() {
        String key = SubsystemTypeConstants.PERSON_ROLLCALL_SERVER + ":" + "hikspcc" + ":" +
                     SessionContextHolder.getRequester().getTenant().toString();
        SubsystemAdapterConfig subAdapterRefreshConfig = subsystemRedisUtils.getSubAdapterRefreshConfig(key);
        HikSpccRollCallConfig clientHttpVo = JsonUtils.deserialize(subAdapterRefreshConfig.getManufacturerJson(), HikSpccRollCallConfig.class);
        /**
         * STEP1：设置平台参数，根据实际情况,设置host appkey appsecret 三个参数.
         */
        ArtemisConfig.host = clientHttpVo.getHost(); // 平台的ip端口
        ArtemisConfig.appKey = clientHttpVo.getAppKey();  // 密钥appkey
        ArtemisConfig.appSecret = clientHttpVo.getAppSecret();// 密钥appSecret
    }

    /**
     * 获取认证信息
     *
     * @return
     */
    @SuppressWarnings("DoubleBraceInitialization")
    private Map<String, String> getAuth(String apiPath) {
        //初始化认证信息
        login();
        //构建jdk请求参数
        final String previewURLsApi = ARTEMIS_PATH + apiPath;
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put(clientHttps, previewURLsApi);//根据现场环境部署确认是http还是https
            }
        };
        return path;
    }

    /**
     * 格式化海康 智慧监管综合平台查询须要的时间格式
     *
     * @param start
     * @param end
     *
     * @return
     */
    private String[] formatDateToISO8601(String start, String end) {
        //如要表示北京时间2004年5月3日下午5点30分8秒，可以写成2004-05-03T17:30:08+08:00
        String[] startDate = start.split(" ");
        String finalStart = startDate[0] + "T" + startDate[1] + "+08:00";
        String[] endDate = end.split(" ");
        String finalEnd = endDate[0] + "T" + endDate[1] + "+08:00";
        log.info("海康 智慧监管综合平台 查询点名历史记录，处理时间格式为：" + finalStart + "," + finalEnd);
        return new String[]{ finalStart, finalEnd };
    }

    /**
     * 调海康 智慧监管综合平台接口
     *
     * @param url  请求地址
     * @param body 请求参数
     *
     * @return 结果
     */
    @SuppressWarnings("DoubleBraceInitialization")
    private String callHikInterface(String url, String body) {
        log.info("海康 智慧监管综合平台请求url:{},请求参数:{}", url, body);
        SubsystemAdapterConfig subAdapterRefreshConfig = this.getBasicConfig();
        HikSpccRequestParam hikParam = JsonUtils.deserialize(subAdapterRefreshConfig.getManufacturerJson(), HikSpccRequestParam.class);
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

    /**
     * 获取基础配置
     *
     * @return 基础配置
     */
    private SubsystemAdapterConfig getBasicConfig() {
        String key =
                SubsystemTypeConstants.PARKING_SERVER + ":" + "hikspcc" + ":" + SessionContextHolder.getRequester().getTenant().toString();
        return subsystemRedisUtils.getSubAdapterRefreshConfig(key);
    }
}
