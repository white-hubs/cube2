package com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 会见记录 实体类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("meeting")
public class MeetingEntity extends BasicEnhancedTenantEntity<Long> {
    /**
     * 主键
     */
    private Long id;

    /**
     * 会见记录号
     */
    private String meetingCode;

    /**
     * 会见类型，哪种类型比如：亲友会见、法律咨询、工作事宜等等，以三方返回为准
     */
    private String meetingType;

    /**
     * 会见类型名称
     */
    private String meetingTypeName;

    /**
     * 会见事由，会见的原因，以三方返回为准
     */
    private String meetingReason;

    /**
     * 会见开始时间，实际见面的时间，以三方返回为准 yyyy-MM-dd HH:mm:ss
     */
    private LocalDateTime meetingStartTime;

    /**
     * 会见结束时间，实际见面的时间，以三方返回为准 yyyy-MM-dd HH:mm:ss
     */
    private LocalDateTime meetingEndTime;

    /**
     * 会见过程录音 url
     */
    private String meetingProcessAudio;

    /**
     * 第三方记录id
     */
    private String thirdRecordId;

}
