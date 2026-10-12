package com.guhecloud.cube.srv.gen.safety.education.server.transfer.rockermq.consumer.group;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.entity.CultivateTask;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.entity.CultivateTaskUser;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.message.model.CultivateSignMessage;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.service.CultivateTaskUserService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.service.impl.CultivateTaskServiceImpl;
import com.guhecloud.cube.srv.gen.safety.education.server.transfer.constant.TransferTopicConstant;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 集团消费市场培训任务
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = TransferTopicConstant.GROUP_RECEIVE_TRAIN_GROUP,
                         topic = TransferTopicConstant.GROUP_RECEIVE_TRAIN_TOP
)
public class GroupReceiveTrainingTaskConsumer implements RocketMQListener<CultivateSignMessage> {

    private final CultivateTaskServiceImpl cultivateTaskService;

    private final CultivateTaskUserService cultivateTaskUserService;

    @Override
    public void onMessage(CultivateSignMessage cultivateSignMessage) {
        try {
            SessionContextHolder.clearSessionContext();
            //查找市场任务来源Id对应集团任务
            CultivateTask task = cultivateSignMessage.getTask();
            CultivateTask groupTask = cultivateTaskService.getOne(Wrappers.<CultivateTask>lambdaQuery()
                                                                          .eq(CultivateTask::getSourceKey, task.getSourceKey())
                                                                          .eq(CultivateTask::getMarketTenant, task.getMarketTenant())
                                                                          .ne(CultivateTask::getTenant, task.getTenant()));
            if (Objects.nonNull(groupTask)) {
                SessionContextHolder.requestTenant(groupTask.getTenant());
                //查找人员
                CultivateTaskUser user = cultivateTaskUserService.getOne(Wrappers.<CultivateTaskUser>lambdaQuery()
                                                                                 .eq(CultivateTaskUser::getUserIdentify, cultivateSignMessage
                                                                                         .getUser().getUserIdentify())
                                                                                 .eq(CultivateTaskUser::getCultivateTaskId, task.getId()));
                boolean isExecuteSave = false;
                if (Objects.isNull(user)) {
                    isExecuteSave = true;
                    user = ObjectUtils.copying(cultivateSignMessage.getUser(), CultivateTaskUser.class);
                    //新增
                    user.setCultivateTaskId(groupTask.getId());
                    user.setId(IdWorker.getId());
                    user.setTenant(groupTask.getTenant());
                }
                if (isExecuteSave) {
                    cultivateTaskUserService.save(user);
                } else {
                    cultivateTaskUserService.updateById(user);
                }
            }
        } catch (Exception e) {
            log.error("消费市场提交的签到记录失败,失败原因:", e);
        } finally {
            SessionContextHolder.clearSessionContext();
        }
    }
}
