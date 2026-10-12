package com.guhecloud.cube.biz.adapter.person.rollcall.server.rocketmq;

import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.biz.adapter.person.rollcall.server.application.hikspcc.service.HikSpccRollCallService;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemAdapterConfig;
import com.guhecloud.subsystem.config.util.SubsystemUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "subsystem_adapter_config_group",
                         topic = "DICTIONARY:personRollCallServer:*", selectorExpression = SubsystemUtils.SUBSYSTEM_ADAPTER_CONFIG_KEY
)
public class PersonRollCallAdapterListener implements RocketMQListener<String> {

    private final HikSpccRollCallService hspcRollCallService;

    /**
     * topic的组成 DICTIONARY:服务名:厂商名
     *
     * @param message 接受的信息
     */
    @Override
    public void onMessage(String message) {
        JSONObject msg = JSONObject.parseObject(message);
        SubsystemAdapterConfig subscribeInitMessage = JsonUtils.deserialize(msg.get("variable"), SubsystemAdapterConfig.class);
        //更新配置后，重启各个厂商对mq的连接、订阅
        String manufacturer = subscribeInitMessage.getManufacturer();
        switch (manufacturer) {
            case "hikspcc":
                hspcRollCallService.reConnect(subscribeInitMessage);
                break;
            default:
                log.error("PersonRollCall服务未知厂商：{}", manufacturer);
                break;
        }
    }
}
