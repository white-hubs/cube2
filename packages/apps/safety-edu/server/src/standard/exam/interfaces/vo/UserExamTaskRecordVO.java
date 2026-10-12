package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户考试记录返回参数
 *
 * @author lanchun
 * @date 2023/4/20 11:22
 */
@Data
public class UserExamTaskRecordVO {

    private Long id;

    /**
     * 提交时间
     */
    private LocalDateTime createTime;

    /**
     * 是否通过
     */
    private Boolean passed;

    /**
     * 的分数
     */
    private Float score;
}
