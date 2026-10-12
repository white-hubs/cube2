package com.guhecloud.cube.srv.gen.safety.education.server.transfer.rockermq.consumer.group;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.ExamTaskService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.ExamTaskUserService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.aggregates.UserExamTaskRecord;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.UserExamStatusEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamTaskPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamTaskUserPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ExamTaskUserInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.infrastructure.mq.rocketmq.message.UserExamSubmittedMessage;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.infrastructure.repository.ExamTaskUserRecordRepository;
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
 * 集团消费市场考试任务
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = TransferTopicConstant.GROUP_RECEIVE_EXAM_GROUP,
                         topic = TransferTopicConstant.GROUP_RECEIVE_EXAM_TOP
)
public class GroupReceiveExamTaskConsumer implements RocketMQListener<UserExamSubmittedMessage> {

    private final ExamTaskUserService examTaskUserService;

    private final ExamTaskService examTaskService;

    private final ExamTaskUserRecordRepository examTaskUserRecordRepository;

    @Override
    public void onMessage(UserExamSubmittedMessage userExamSubmittedMessage) {
        SessionContextHolder.clearSessionContext();
        try {
            //根据市场的sourceKey获取集团的任务id
            ExamTaskPO examTaskPO = this.examTaskService.getOne(Wrappers.<ExamTaskPO>lambdaQuery()
                                                                        .eq(ExamTaskPO::getSourceKey, userExamSubmittedMessage.getSourceKey())
                                                                        .eq(ExamTaskPO::getMarketTenant, userExamSubmittedMessage.getTenant())
                                                                        .ne(ExamTaskPO::getTenant, userExamSubmittedMessage.getTenant()));
            if (ObjectUtils.isNotNull(examTaskPO)) {
                SessionContextHolder.requestTenant(examTaskPO.getTenant());
                //记录考试任务
                UserExamTaskRecord userExamTaskRecord = userExamSubmittedMessage.getUserExamTaskRecord();
                userExamTaskRecord.setId(IdWorker.getId());
                userExamTaskRecord.setExamTaskId(examTaskPO.getId());
                this.examTaskUserRecordRepository.save(userExamTaskRecord);
                //判断是否第一次考试：第一次新增考试任人员记录、非第一次原有考试人员记录修改
                ExamTaskUserPO examTaskUserPO = this.examTaskUserService.getOne(Wrappers.<ExamTaskUserPO>lambdaQuery()
                                                                                        .eq(ExamTaskUserPO::getExamTaskId, userExamTaskRecord.getExamTaskId())
                                                                                        .eq(ExamTaskUserPO::getUserIdentify, userExamTaskRecord
                                                                                                .getExamTaskUserInfo().getIdentify()));
                boolean isExecuteSave = true;
                if (Objects.nonNull(examTaskUserPO)) {
                    isExecuteSave = false;
                    examTaskUserPO.setExamNumber(examTaskUserPO.getExamNumber() + 1);
                    if (userExamTaskRecord.getScore() > examTaskUserPO.getScore()) {
                        examTaskUserPO.setUserExamTaskRecordId(userExamTaskRecord.getId());
                        examTaskUserPO.setPassed(userExamTaskRecord.getPassed());
                        examTaskUserPO.setScore(userExamTaskRecord.getScore());
                    }
                } else {
                    ExamTaskUserInfo examTaskUserInfo = userExamTaskRecord.getExamTaskUserInfo();
                    examTaskUserPO = ExamTaskUserPO.builder()
                            .userIdentify(examTaskUserInfo.getIdentify())
                            .examTaskId(examTaskPO.getId())
                            .userId(examTaskUserInfo.getId())
                            .userName(examTaskUserInfo.getName())
                            .userPhone(examTaskUserInfo.getPhone())
                            .userDepartmentId(examTaskUserInfo.getDepartmentId())
                            .userDepartmentName(examTaskUserInfo.getDepartmentName())
                            .userPostId(examTaskUserInfo.getPostId())
                            .userPostName(examTaskUserInfo.getPostName())
                            .examNumber(1)
                            .userExamTaskRecordId(userExamTaskRecord.getId())
                            .examStatus(UserExamStatusEnum.EXAMINED)
                            .passed(userExamTaskRecord.getPassed())
                            .score(userExamTaskRecord.getScore())
                            .build();
                }
                if (isExecuteSave) {
                    this.examTaskUserService.save(examTaskUserPO);
                } else {
                    this.examTaskUserService.updateById(examTaskUserPO);
                }
            }
        } catch (Exception e) {
            log.error("消费市场提交的考试任务记录失败,失败原因:", e);
        } finally {
            SessionContextHolder.clearSessionContext();
        }
    }
}
