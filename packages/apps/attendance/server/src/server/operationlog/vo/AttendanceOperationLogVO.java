package com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author wql
 * @describe 考勤管理日志记录列表字段
 * @date 2023/6/12 15:17
 */
@Data
public class AttendanceOperationLogVO {
    /**
     * 操作时间
     */
    private LocalDateTime operationTime;
    /**
     * 操作人
     */
    private String userName;
    /**
     * 业务应用
     */
    private String businessType;
    /**
     * 操作类型
     */
    private String operationType;
    /**
     * 操作对象
     */
    private String operationObj;
    /**
     * 操作内容
     */
    private String operationContent;
}
