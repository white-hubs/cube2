package com.guhecloud.cube.srv.gen.safety.management.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * @author wql
 * @describe 预案文件
 * @date 2023/10/8 17:15
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@TableName(value = "safety_emergency_file")
public class SafetyEmergencyFilePO extends BasicEnhancedTenantEntity<Long> {
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 级别编号
     */
    private String levelCode;

    /**
     * 预案事件分类编号
     */
    private String emergencyEventTypeCode;

    /**
     * 预案事件分类名称
     */
    private String emergencyEventTypeName;

    /**
     * 到期状态
     */
    private String expirationStatus;

    /**
     * 到期日期
     */
    private LocalDateTime expirationDate;

    /**
     * 备注
     */
    private String remark;

    /**
     * 文档附件
     */
    private String documentFileId;

    /**
     * 文档附件名称
     */
    private String documentFileName;

    /**
     * 文档附件路径
     */
    private String documentFilePath;

    /**
     * 流程附件
     */
    private String processFileId;

    /**
     * 流程附件名称
     */
    private String processFileName;

    /**
     * 流程附件路径
     */
    private String processFilePath;

    /**
     * 市场编号
     */
    private Integer tenant;

    /**
     * 市场编号
     */
    private Integer marketTenant;

    /**
     * 市场名称
     */
    private String marketName;
}
