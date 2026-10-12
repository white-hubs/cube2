package com.guhecloud.cube.srv.gen.attendance.management.server.rule.rocketmq.comsumer;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.ouaa.client.message.TenantInitMessage;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.ClassesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.ClassesMarkEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.ClassesPO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RocketMQMessageListener(
        consumerGroup = TenantInitMessage.TOPIC + "_INIT_CLASSES",
        topic = TenantInitMessage.TOPIC,
        selectorType = SelectorType.TAG
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InitClassesConsumer implements RocketMQListener<TenantInitMessage> {

    private final ClassesMapper classesMapper;

    @Override
    public void onMessage(TenantInitMessage tenantInitMessage) {
        log.info("收到租户初始化消息{}--班次初始化开始", tenantInitMessage);
        SessionContextHolder.requestTenant(tenantInitMessage.getTenant());
        ClassesPO restClasses = classesMapper.selectOne(Wrappers.<ClassesPO>lambdaQuery()
                                                                .eq(ClassesPO::getMark, ClassesMarkEnum.All_DAY_REST.name()));
        if (null == restClasses) {
            ClassesPO po = new ClassesPO();
            po.setName("休息");
            po.setMark(ClassesMarkEnum.All_DAY_REST.name());
            po.setTenant(tenantInitMessage.getTenant());
            classesMapper.insert(po);
        }
        ClassesPO defaultClasses = classesMapper.selectOne(Wrappers.<ClassesPO>lambdaQuery()
                                                                   .eq(ClassesPO::getMark, ClassesMarkEnum.DEFAULT_WORK.name()));
        if (null == defaultClasses) {
            ClassesPO po = new ClassesPO();
            po.setName("默认班次");
            po.setMark(ClassesMarkEnum.DEFAULT_WORK.name());
            po.setStartWorkTime("09:00");
            po.setEndWorkTime("18:00");
            po.setTenant(tenantInitMessage.getTenant());
            classesMapper.insert(po);
        }
    }
}
