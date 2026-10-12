package com.guhecloud.cube.srv.gen.loose.works.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * @author wql
 * @describe 跟踪记录
 * @date 2023/9/14 20:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class TrackRecordVO {
    /**
     * 记录id
     */
    private Long id;
    /**
     * 记录时间
     */
    private LocalDateTime recordTime;
    /**
     * 姓名
     */
    private String username;
    /**
     * 部门
     */
    private String depName;
    /**
     * 操作
     */
    private String operation;
    private String operationCode;
    /**
     * 反馈内容
     */
    private String feedbackContent;
    /**
     * 验收内容
     */
    private String checkAcceptContent;
    /**
     * 验收结果
     */
    private String acceptanceResult;
}
