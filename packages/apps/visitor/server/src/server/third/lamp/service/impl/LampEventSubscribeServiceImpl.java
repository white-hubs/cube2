package com.guhecloud.cube.srv.gen.visitor.management.server.third.lamp.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.srv.gen.visitor.management.server.po.SystemLoginInfoPO;
import com.guhecloud.cube.srv.gen.visitor.management.server.third.lamp.service.LampEventSubscribeService;
import com.guhecloud.cube.srv.gen.visitor.management.server.third.lamp.service.LampLoginService;
import com.guhecloud.cube.srv.gen.visitor.management.server.third.lamp.utils.LampVisitorHttpUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 事件订阅
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LampEventSubscribeServiceImpl implements LampEventSubscribeService {
    private final LampLoginService loginService;
    /**
     * 事件订阅url
     */
    private static final String SUBSCRIBE_URL = "/evo-apigw/evo-event/1.0.0/subscribe/mqinfo";
    /**
     * 订阅的类型
     */
    private static final List<String> SUBSCRIBE_TYPES = Arrays.asList("visitor.status", "visitor.record");

    /**
     * 访客业务事件订阅
     */
    @Override
    public void subscribeVisitorBusinessEvent() {
        try {
            SystemLoginInfoPO loginInfo = loginService.loginToken();
            String subscribeParam = this.getSubscribeTypes(loginInfo);

            Object obj = LampVisitorHttpUtils.sendThirdPost(loginInfo.getBaseUrl() + SUBSCRIBE_URL, subscribeParam, loginInfo);
            if (obj == null) {
                log.error("访客管理订阅状态失败！");
            }
        } catch (Exception e) {
            log.error("访客状态订阅失败：" + e);
        }
    }

    private String getSubscribeTypes(SystemLoginInfoPO loginInfo) {
        if (loginInfo == null) {
            loginInfo = new SystemLoginInfoPO();
        }
        JSONObject eventParam = new JSONObject();
        JSONObject param = new JSONObject();
        // 监听组
        JSONArray monitors = new JSONArray();
        JSONObject defaultMonitor = new JSONObject();
        defaultMonitor.put("monitor", loginInfo.getCallbackBaseUrl() + "/public/visitor/visitorBusinessEventCallback");
        defaultMonitor.put("monitorType", "url");
        JSONArray events = new JSONArray();
        JSONObject businessEvent = new JSONObject();
        businessEvent.put("category", "business");
        businessEvent.put("subscribeAll", "1");
        businessEvent.put("domainSubscribe", "2");
        JSONArray authorities = new JSONArray();
        JSONObject authority = new JSONObject();
        JSONArray types = new JSONArray();
        types.addAll(SUBSCRIBE_TYPES);
        authority.put("types", types);
        authorities.add(authority);
        businessEvent.put("authorities", authorities);
        events.add(businessEvent);
        defaultMonitor.put("events", events);
        monitors.add(defaultMonitor);

        // 订阅者信息
        JSONObject subsystem = new JSONObject();
        // 系统类型,0代表子系统 固定值：0
        subsystem.put("subsystemType", "0");
        // 订阅者名称,唯一,建议与magic一样，也可按照ip_port_业务模块，例如10.35.111.10_8010_alarm
        // name存在则覆盖之前的订阅内容；name不存在则新增
        String ipPort = loginInfo.getRemark().replace(":", "_");
        subsystem.put("name", ipPort + "_visitor");
        // 订阅者ip端口,子系统填ip_port， ip、port为字段monitor中的ip与端口
        subsystem.put("magic", ipPort);

        param.put("subsystem", subsystem);
        param.put("monitors", monitors);
        eventParam.put("param", param);

        return eventParam.toJSONString();
    }

}
