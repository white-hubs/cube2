package com.guhecloud.cube.base.flow.webhooks.rocketmq;

import com.guhecloud.cube.base.flow.context.interfaces.vo.FormFlowDetailVO;
import com.guhecloud.cube.base.flow.context.querygateway.FormFlowBusinessQueryService;
import com.guhecloud.cube.base.flow.flowable.process.domain.ExtProcessInstance;
import com.guhecloud.cube.base.flow.flowable.process.domain.respository.ExtProcessInstanceRepository;
import com.guhecloud.cube.base.flow.form.enumeration.FormJsonEnum;
import com.guhecloud.cube.base.flow.form.util.HttpClientUtils;
import com.guhecloud.cube.base.flow.webhooks.vo.WebhookConfigVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = WebHookMessage.GROUP, topic = WebHookMessage.TOPIC)
@Aspect
public class WebHooksConsumer implements RocketMQListener<WebHookMessage> {
    private final ExtProcessInstanceRepository extProcessInstanceRepository;
    private final FormFlowBusinessQueryService formFlowBusinessQueryService;

    @Override
    public void onMessage(WebHookMessage message) {
        log.info(message.getCallBackNode() + "节点回调接口开始");
        try {
            SessionContextHolder.requestTenant(message.getTenant());
            if (StringUtils.isBlank(message.getProcessInstanceId())) {
                return;
            }
            Optional<ExtProcessInstance> processInstance = extProcessInstanceRepository.get(message.getProcessInstanceId());
            ExtProcessInstance extProcessInstance = processInstance.get();
            FormFlowDetailVO detailVO = formFlowBusinessQueryService.getByBusinessType(extProcessInstance.getBusinessId()
                                                                                               .getBusinessType());
            if (CollectionUtils.isEmpty(detailVO.getWebhookConfigVOList())) {
                return;
            }
            for (WebhookConfigVO webhookConfigVO : detailVO.getWebhookConfigVOList()) {
                Map<String, String> params = new HashMap<>();
                Map<String, String> authMap = new HashMap<>();
                params.put("processInstanceId", message.getProcessInstanceId());
                params.put("result", message.getResult());
                authMap.put(FormJsonEnum.HEADER_AUTHORIZATION.getValue(),
                            FormJsonEnum.HEADER_BEARER.getValue() + webhookConfigVO.getToken());
                Map<String, Object> jsonObject = HttpClientUtils.restPost(webhookConfigVO.getUrl(), params, authMap);
                if (Integer.parseInt(jsonObject.get("code").toString()) != 1) {
                    throw new Exception(jsonObject.get("message").toString());
                }
            }
        } catch (Exception exception) {
            log.info("webhook回调失败失败：{}", exception);
        }

    }
}
