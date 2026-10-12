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
public class MeetingApplyVO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 申请记录编号
     */
    private String applyCode;

    /**
     * 申请时间，填写记录的时间，以三方返回为准
     */
    private LocalDateTime applyTime;

    /**
     * 申请人id
     */
    private String applyPersonId;

    /**
     * 申请人名称，填写记录的人，以三方返回为准
     */
    private String applyPersonName;

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
     * 审批结果，申请是通过，还是不通过，不通过原因是什么，以三方返回为准
     */
    private String approvalResult;

    /**
     * 审批结果说明
     */
    private String approvalResultReason;

    /**
     * 第三方记录id 申请记录号
     */
    private String thirdRecordId;

    /**
     * 会见人集合
     */
    private List<MeetingApplyPersonVO> meetingPersons;

}
