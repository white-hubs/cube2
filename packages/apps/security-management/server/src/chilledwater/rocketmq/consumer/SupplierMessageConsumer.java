package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.rocketmq.consumer;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.entity.ColdWholesaler;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.rocketmq.message.SupplierMessage;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service.ColdWholesalerService;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = SupplierMessage.TOPIC + "_security",
                         topic = SupplierMessage.TOPIC, selectorType = SelectorType.TAG
)
public class SupplierMessageConsumer implements RocketMQListener<SupplierMessage> {

    private final ColdWholesalerService wholesalerService;

    @Override
    public void onMessage(SupplierMessage message) {
        ColdWholesaler one = wholesalerService.getOne(Wrappers.<ColdWholesaler>lambdaQuery()
                                                              .eq(ColdWholesaler::getWholesalerId, message.getId()));
        one.setWholesalerName(message.getName());
        one.setOtherContacts(message.getOtherContacts());
        one.setShiftMaster(message.getShiftMaster());
        one.setShiftMasterCard(message.getShiftMasterCard());
        one.setShiftMasterPhone(message.getShiftMasterPhone());
        one.setCode(message.getCode());
        EntityAuditor.populateAudit(one, message.getCreateBy(), StringUtils.EMPTY_STRING, ActorTypeEnum.SYSTEM, message.getUpdateTime());
        wholesalerService.updateById(one);
    }
}
