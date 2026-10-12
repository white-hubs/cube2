package com.guhecloud.cube.srv.gen.personnel.management.server.archive.appplication.event.consumer;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.ouaa.client.message.DelTenantUserMessage;
import com.guhecloud.cube.srv.gen.personnel.management.server.commandgateway.StaffCommandService;
import com.guhecloud.cube.srv.gen.personnel.management.server.po.StaffPO;
import com.guhecloud.cube.srv.gen.personnel.management.server.querygateway.StaffQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RocketMQMessageListener(
        consumerGroup = DelTenantUserMessage.TOPIC + "_PERSONNEL",
        topic = DelTenantUserMessage.TOPIC
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DelTenantUserConsumer implements RocketMQListener<DelTenantUserMessage> {

    private final StaffCommandService staffCommandService;
    private final StaffQueryService staffQueryService;

    @Override
    public void onMessage(DelTenantUserMessage delTenantUserMessage) {
        log.info("收到删除租户用户消息,消息体:{}", delTenantUserMessage);
        StaffPO staffPO = staffQueryService.getOne(Wrappers.<StaffPO>lambdaQuery().eq(StaffPO::getUserId, delTenantUserMessage.getId()));
        staffCommandService.remove(staffPO.getId());
    }
}
