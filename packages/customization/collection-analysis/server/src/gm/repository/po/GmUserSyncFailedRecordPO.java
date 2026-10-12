package com.guhecloud.cube.biz.collection.analysis.gm.repository.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 光明人员同步失败记录
 */
@Data
@TableName("gm_user_sync_failed_record")
@EqualsAndHashCode(callSuper = true)
public class GmUserSyncFailedRecordPO extends BasicEnhancedTenantEntity<Long> {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 三方用户 id
     */
    private Long thirdUserId;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 三方用户名
     */
    private String userName;
    /**
     * 本次同步开始时间
     */
    private LocalDateTime updateTimeBegin;
    /**
     * 本次同步结束时间
     */
    private LocalDateTime updateTimeEnd;
    /**
     * 失败原因
     */
    private String errorMessage;
    /**
     * 异常堆栈
     */
    private String errorStack;
}
