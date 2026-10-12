package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.infrastructure.mq.message;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskOperationEnum;
import lombok.Data;

/**
 * 总部考试任务发布消息
 *
 * @author lanchun
 * @date 2024/4/10 9:38
 */
@Data
public class HeadquartersExamTaskPublishedMessage {

    public static final String TOPIC = "HEADQUARTERS_EXAM_TASK";

    /**
     * 任务主键
     */
    private Long id;

    /**
     * 考试内容(json字符串)
     */
    private String examTaskContent;

    /**
     * 操作类型
     */
    private ExamTaskOperationEnum operationType;
}
