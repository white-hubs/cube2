package com.guhecloud.cube.srv.gen.safety.education.server.transfer.rockermq.consumer.group;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.ExamTaskService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.ExamTaskUserService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamTaskPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamTaskUserPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.infrastructure.mq.rocketmq.message.UserExamMissMessage;
import com.guhecloud.cube.srv.gen.safety.education.server.transfer.constant.TransferTopicConstant;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 集团消费市场考试任务(缺考)
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = TransferTopicConstant.GROUP_RECEIVE_EXAM_MISS_GROUP,
                         topic = TransferTopicConstant.GROUP_RECEIVE_EXAM_MISS_TOP
)
public class GroupReceiveExamTaskMissConsumer implements RocketMQListener<UserExamMissMessage> {

    private final ExamTaskUserService examTaskUserService;

    private final ExamTaskService examTaskService;

    @Override
    public void onMessage(UserExamMissMessage userExamMissMessage) {
        SessionContextHolder.clearSessionContext();
        try {
            //根据市场的sourceKey获取集团的任务id
            ExamTaskPO examTaskPO = this.examTaskService.getOne(Wrappers.<ExamTaskPO>lambdaQuery()
                                                                        .eq(ExamTaskPO::getSourceKey, userExamMissMessage.getSourceKey())
                                                                        .eq(ExamTaskPO::getMarketTenant, userExamMissMessage.getTenant())
                                                                        .ne(ExamTaskPO::getTenant, userExamMissMessage.getTenant()));
            if (CollectionUtils.isEmpty(userExamMissMessage.getUsers())) {
                return;
            }
            if (ObjectUtils.isNotNull(examTaskPO)) {
                SessionContextHolder.requestTenant(examTaskPO.getTenant());
                List<String> examUserList = userExamMissMessage.getUsers()
                        .stream().map(ExamTaskUserPO::getUserIdentify).collect(Collectors.toList());
                //是否存在用户
                List<ExamTaskUserPO> examTaskUserPOS = examTaskUserService.list(Wrappers.<ExamTaskUserPO>lambdaQuery()
                                                                                        .eq(ExamTaskUserPO::getExamTaskId, examTaskPO.getId())
                                                                                        .in(ExamTaskUserPO::getUserIdentify, examUserList));
                List<String> existUser = new ArrayList<>();
                if (!CollectionUtils.isEmpty(examTaskUserPOS)) {
                    existUser = examTaskUserPOS.stream().map(ExamTaskUserPO::getUserIdentify)
                            .toList();
                }
                Iterator<ExamTaskUserPO> iterator = userExamMissMessage.getUsers().iterator();

                while (iterator.hasNext()) {
                    ExamTaskUserPO next = iterator.next();
                    //存在就不再更新
                    if (existUser.contains(next.getUserIdentify())) {
                        iterator.remove();
                    }
                }
                //缺考的
                if (!CollectionUtils.isEmpty(userExamMissMessage.getUsers())) {
                    userExamMissMessage.getUsers().forEach(t -> {
                        t.setExamTaskId(examTaskPO.getId());
                        t.setId(IdWorker.getId());
                        t.setTenant(examTaskPO.getTenant());
                    });
                    examTaskUserService.saveBatch(userExamMissMessage.getUsers());
                }
            }
        } catch (Exception e) {
            log.error("消费市场缺考的任务记录失败,失败原因:", e);
        } finally {
            SessionContextHolder.clearSessionContext();
        }
    }
}
