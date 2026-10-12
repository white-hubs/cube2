package com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.po;

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
 * 广播记录数据模型
 *
 * @author lanchun
 * @date 2022/12/12 13:48
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("broadcast_play_record")
public class BroadcastPlayRecordPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 广播时间
     */
    private LocalDateTime broadcastTime;

    /**
     * 广播模板主键
     */
    private Long broadcastTemplateId;

    /**
     * 广播模板名称
     */
    private String broadcastTemplateName;

    /**
     * 广播模板原内容
     */
    private String broadcastTemplateContent;

    /**
     * 广播模板参数
     */
    private String broadcastTemplateContentParam;

    /**
     * 广播内容
     */
    private String broadcastPlayContent;

    /**
     * 广播次数
     */
    private Integer broadcastPlayNum;

    /**
     * 广播终端
     */
    private String broadcastPlayDevices;

    /**
     * 广播计划主键
     */
    private Long broadcastPlayPlanId;

    /**
     * 操作人
     */
    private String operatorBy;

    /**
     * 操作人名称
     */
    private String operatorByName;

    /**
     * 操作人部门
     */
    private Long operatorByDeptId;

    /**
     * 操作人部门名称
     */
    private String operatorByDeptName;

    /**
     * 播放结果
     */
    private String broadcastPlayStatus;

    /**
     * 播放异常描述
     */
    private String broadcastPlayErrorDesc;
}
