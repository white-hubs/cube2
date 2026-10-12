package com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeetingVO {
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

    /**
     * 会见人集合
     */
    private List<MeetingApplyPersonVO> meetingPersons;

}
