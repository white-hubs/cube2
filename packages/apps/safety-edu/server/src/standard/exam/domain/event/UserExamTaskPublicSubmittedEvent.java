package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.event;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.aggregates.UserExamTaskRecord;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.UserExamTaskRecordSubmittedVO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 外部用户考试提交事件
 *
 * @author lanchun
 * @date 2024/4/10 19:55
 */
@Getter
public class UserExamTaskPublicSubmittedEvent extends ApplicationEvent implements Supplier<UserExamTaskRecordSubmittedVO>, Consumer<UserExamTaskRecordSubmittedVO> {

    private final UserExamTaskRecord userExamTaskRecord;

    private UserExamTaskRecordSubmittedVO userExamTaskRecordSubmittedVO;

    public UserExamTaskPublicSubmittedEvent(Object source, UserExamTaskRecord userExamTaskRecord) {
        super(source);
        this.userExamTaskRecord = userExamTaskRecord;
    }

    @Override
    public void accept(UserExamTaskRecordSubmittedVO userExamTaskRecordSubmittedVO) {
        this.userExamTaskRecordSubmittedVO = userExamTaskRecordSubmittedVO;
    }

    @Override
    public UserExamTaskRecordSubmittedVO get() {
        return this.userExamTaskRecordSubmittedVO;
    }
}
