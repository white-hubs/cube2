package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.infrastructure.mq.rocketmq.message;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamTaskUserPO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用户缺考消息发送
 *
 * @author lanchun
 * @date 2024/4/11 14:08
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserExamMissMessage {

    public static final String TOPIC = "USER_EXAM_MISS";

    /**
     * 任务主键
     */
    private Long id;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 来源编号
     */
    private String sourceCode;

    /**
     * 来源名称
     */
    private String sourceName;

    /**
     * 来源主键
     */
    private String sourceKey;

    /**
     * 租户编号
     */
    private Integer tenant;

    /**
     * 缺考的用户信息
     */
    private List<ExamTaskUserPO> users;
}
