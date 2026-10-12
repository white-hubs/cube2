package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.enumeration.RuleIssuedIotStatusEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 规则下发到iot状态
 *
 * @author lanchun
 * @date 2024/9/14 15:10
 */
@TableName("medical_rule_issued_iot")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class MedicalRuleIssuedIot extends BasicEnhancedEntity<Long> {

    @TableId
    private Long id;

    /**
     * 下发状态
     */
    private RuleIssuedIotStatusEnum issuedStatus;

    /**
     * 最后下发时间
     */
    private LocalDateTime lastIssuedTime;

    /**
     * 租户编号
     */
    private Integer tenant;
}
