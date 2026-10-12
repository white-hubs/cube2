package com.guhecloud.cube.srv.gen.safety.education.server.transfer.rockermq.consumer.group;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.entity.CultivateTask;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.entity.CultivateTaskUser;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.message.CultivateUnSignMessage;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.service.CultivateTaskUserService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.service.impl.CultivateTaskServiceImpl;
import com.guhecloud.cube.srv.gen.safety.education.server.transfer.constant.TransferTopicConstant;
import com.guhecloud.platform.base.context.SessionContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 集团消费市场培训任务（未签到）
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = TransferTopicConstant.GROUP_RECEIVE_TRAIN_UN_SIGN_GROUP,
                         topic = TransferTopicConstant.GROUP_RECEIVE_TRAIN_UN_SIGN_TOP
)
public class GroupReceiveTrainingTaskUnSignConsumer implements RocketMQListener<CultivateUnSignMessage> {

    private final CultivateTaskServiceImpl cultivateTaskService;

    private final CultivateTaskUserService cultivateTaskUserService;

    @Override
    public void onMessage(CultivateUnSignMessage cultivateUnSignMessage) {
        try {
            SessionContextHolder.clearSessionContext();
            //查找市场任务来源Id对应集团任务
            CultivateTask task = cultivateUnSignMessage.getTask();
            CultivateTask groupTask = cultivateTaskService.getOne(Wrappers.<CultivateTask>lambdaQuery()
                                                                          .eq(CultivateTask::getSourceKey, task.getSourceKey())
                                                                          .eq(CultivateTask::getMarketTenant, task.getTenant())
                                                                          .ne(CultivateTask::getTenant, task.getTenant()));
            if (CollectionUtils.isEmpty(cultivateUnSignMessage.getUsers())) {
                return;
            }
            if (Objects.nonNull(groupTask)) {
                SessionContextHolder.requestTenant(groupTask.getTenant());
                List<String> cultivateTaskUserList = cultivateUnSignMessage.getUsers()
                        .stream().map(CultivateTaskUser::getUserIdentify)
                        .collect(Collectors.toList());
                //是否存在用户
                List<CultivateTaskUser> examTaskUserPOS = cultivateTaskUserService.list(Wrappers.<CultivateTaskUser>lambdaQuery()
                                                                                                .eq(CultivateTaskUser::getCultivateTaskId, groupTask.getId())
                                                                                                .in(CultivateTaskUser::getUserIdentify, cultivateTaskUserList));
                List<String> existUser = new ArrayList<>();
                if (!CollectionUtils.isEmpty(examTaskUserPOS)) {
                    existUser = examTaskUserPOS.stream().map(CultivateTaskUser::getUserIdentify)
                            .collect(Collectors.toList());
                }
                Iterator<CultivateTaskUser> iterator = cultivateUnSignMessage.getUsers().iterator();

                while (iterator.hasNext()) {
                    CultivateTaskUser next = iterator.next();
                    //存在就不再更新
                    if (existUser.contains(next.getUserIdentify())) {
                        iterator.remove();
                    }
                }
                if (!CollectionUtils.isEmpty(cultivateUnSignMessage.getUsers())) {
                    cultivateUnSignMessage.getUsers().forEach(t -> {
                        t.setCultivateTaskId(groupTask.getId());
                        t.setId(IdWorker.getId());
                        t.setTenant(groupTask.getTenant());
                    });
                    cultivateTaskUserService.saveBatch(cultivateUnSignMessage.getUsers());
                }
            }
        } catch (Exception e) {
            log.error("消费市场提交的缺签任务记录失败,失败原因:", e);
        } finally {
            SessionContextHolder.clearSessionContext();
        }
    }
}
