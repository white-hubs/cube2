package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.command.IncreaseExamTaskUserRecordCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.command.IncreasePublicExamTaskUserRecordCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.validator.IncreaseExamTaskUserRecordValidator;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.validator.IncreasePublicExamTaskUserRecordValidator;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.aggregates.UserExamTaskRecord;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.event.UserExamTaskPublicSubmittedEvent;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.event.UserExamTaskSubmittedEvent;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.factory.ExamTaskUserRecordFactory;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.infrastructure.repository.ExamTaskUserRecordRepository;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.UserExamTaskRecordSubmittedVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserExamTaskRecordCommandService {

    private final IncreaseExamTaskUserRecordValidator increaseExamTaskUserRecordValidator;

    private final IncreasePublicExamTaskUserRecordValidator increasePublicExamTaskUserRecordValidator;

    private final ExamTaskUserRecordRepository examTaskUserRecordRepository;

    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * 用户考试记录新增
     *
     * @param increaseExamTaskUserRecordCommand
     */
    @Transactional(rollbackFor = Exception.class)
    public UserExamTaskRecordSubmittedVO increaseExamTaskUserRecord(IncreaseExamTaskUserRecordCommand increaseExamTaskUserRecordCommand) {
        this.increaseExamTaskUserRecordValidator.verify(increaseExamTaskUserRecordCommand);
        UserExamTaskRecord userExamTaskRecord = ExamTaskUserRecordFactory.newUserExamTaskRecord(increaseExamTaskUserRecordCommand);
        this.examTaskUserRecordRepository.save(userExamTaskRecord);
        //发送用户考试提交事件
        UserExamTaskSubmittedEvent userExamTaskSubmittedEvent = new UserExamTaskSubmittedEvent(this, userExamTaskRecord);
        this.applicationEventPublisher.publishEvent(userExamTaskSubmittedEvent);
        return userExamTaskSubmittedEvent.getUserExamTaskRecordSubmittedVO();
    }

    /**
     * 外部用户考试记录新增
     *
     * @param increasePublicExamTaskUserRecordCommand
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public UserExamTaskRecordSubmittedVO increasePublicExamTaskUserRecord(
            IncreasePublicExamTaskUserRecordCommand increasePublicExamTaskUserRecordCommand
                                                                         ) {
        this.increasePublicExamTaskUserRecordValidator.verify(increasePublicExamTaskUserRecordCommand);
        UserExamTaskRecord userExamTaskRecord = ExamTaskUserRecordFactory.newPublicUserExamTaskRecord(increasePublicExamTaskUserRecordCommand);
        SessionContextHolder.requestTenant(userExamTaskRecord.getTenant());
        this.examTaskUserRecordRepository.save(userExamTaskRecord);
        //发送用户考试提交事件
        UserExamTaskPublicSubmittedEvent userExamTaskSubmittedEvent = new UserExamTaskPublicSubmittedEvent(this, userExamTaskRecord);
        this.applicationEventPublisher.publishEvent(userExamTaskSubmittedEvent);
        return userExamTaskSubmittedEvent.getUserExamTaskRecordSubmittedVO();
    }
}
