package com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 考勤操作日志
 * @date 2023/6/12 14:55
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName("attendance_operation_log")
public class AttendanceOperationLog extends BasicEnhancedTenantEntity<Long> {
    /**
     * 主键
     */
    private Long id;
    /**
     * 操作对象id
     */
    private Long objId;
    /**
     * 操作人id
     */
    private Long userId;
    /**
     * 操作人名称
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
