package com.guhecloud.cube.srv.gen.adapter.information.server.huifeng.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.srv.gen.adapter.information.server.huifeng.qo.InformationParam;
import com.guhecloud.cube.srv.gen.adapter.information.server.huifeng.service.InformationService;
import com.guhecloud.cube.srv.gen.adapter.information.server.vo.ClientHttpVo;
import com.guhecloud.cube.srv.gen.adapter.information.server.vo.PLayRecordVo;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemAdapterConfig;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InformationServiceImpl implements InformationService {

    private final SubsystemRedisUtils subsystemRedisUtils;

    private final RestTemplate restTemplate;

    @Override
    public List<PLayRecordVo> getPayLogs(InformationParam informationParam) {
        String key = SubsystemTypeConstants.INFORMATION_SERVER + ":" + "information" + ":" +
                     SessionContextHolder.getRequester().getTenant().toString();
        SubsystemAdapterConfig subAdapterRefreshConfig = subsystemRedisUtils.getSubAdapterRefreshConfig(key);
        ClientHttpVo clientHttpVo = JsonUtils.deserialize(subAdapterRefreshConfig.getManufacturerJson(), ClientHttpVo.class);
        String login = login(clientHttpVo);
        String url = clientHttpVo.getHost() + "/module/wiseapi/ClientAPI.ashx";
        //获取请求
        HttpHeaders headers = new HttpHeaders();
        headers.add("Ticket", login);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(null, headers);
        Map<String, Object> param = new HashMap<>();
        param.put("act", "getPlayLog");
        param.put("companyid", clientHttpVo.getCompanyId());
        param.put("starttime", informationParam.getStartTime());
        param.put("endtime", informationParam.getEndTime());
        ResponseEntity<JSONObject> exchange = restTemplate.exchange(url, HttpMethod.GET, httpEntity, JSONObject.class, param);
        JSONObject body = exchange.getBody();
        JSONObject data = body.getJSONObject("data");
        JSONArray records = data.getJSONArray("records");
        List<PLayRecordVo> copying = ObjectUtils.copying(records, PLayRecordVo.class);
        return copying;
    }

    /**
     * @Description 登录
     * @Author lrj
     */
    private String login(ClientHttpVo clientHttpVo) {
        String url = clientHttpVo.getHost() + "/module/wiseapi/gettoken.ashx";
        //获取请求
        Map<String, Object> param = new HashMap<>();
        param.put("companyid", clientHttpVo.getCompanyId());
        param.put("username", clientHttpVo.getAccount());
        param.put("password", clientHttpVo.getPwd());
        JSONObject jsonObject = restTemplate.getForObject(url, JSONObject.class, param);
        String token = jsonObject.getJSONObject("data").toString();
        return token;
    }
}
