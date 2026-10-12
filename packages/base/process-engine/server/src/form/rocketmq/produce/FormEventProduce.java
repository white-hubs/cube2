package com.guhecloud.cube.base.flow.form.rocketmq.produce;

import com.guhecloud.cube.base.flow.context.dao.FormFlowMapper;
import com.guhecloud.cube.base.flow.context.enumeration.FormFlowTypeEnum;
import com.guhecloud.cube.base.flow.context.po.FormFlowPO;
import com.guhecloud.cube.base.flow.form.dao.FormInstanceMapper;
import com.guhecloud.cube.base.flow.form.po.FormInstancePO;
import com.guhecloud.cube.base.flow.form.rocketmq.message.FormDataToBusinessMessage;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FormEventProduce {

    private final FormInstanceMapper formInstanceMapper;
    private final FormFlowMapper formFlowMapper;

    //TODO 如果是纯表单业务操作，此处应该发送消息通知业务方更新数据；如果是流程表单，目前是流程调用解析方式，由流程方通知
    public void sendFormDataToBusinessEvent(Long formInstanceId, String businessObject) {
        if (null == formInstanceId || StringUtils.isEmpty(businessObject)) {
            return;
        }
        FormInstancePO formInstancePO = formInstanceMapper.selectById(formInstanceId);
        if (formInstancePO == null) {
            return;
        }
        FormFlowPO formFlowPO = formFlowMapper.selectById(formInstancePO.getFormFlowId());
        if (formFlowPO == null || !FormFlowTypeEnum.FORM.name().equals(formFlowPO.getType())) {
            return;
        }
        FormDataToBusinessMessage message = new FormDataToBusinessMessage();
        message.setBusinessType(formInstancePO.getBusinessType());
        message.setBusinessKey(formInstancePO.getBusinessKey());
        message.setBusinessObject(businessObject);
        message.setTimespl(LocalDateTime.now());
        RocketMQContextHolder.send(FormDataToBusinessMessage.TOPIC, message);
    }

}
