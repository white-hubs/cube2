package com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.vo;

import lombok.Data;

@Data
public class MeetingApplyPersonVO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 会见申请记录id or 会见记录id
     */
    private Long businessKey;

    /**
     * 会见申请记录号 or 会见记录号
     */
    private String businessCode;

    /**
     * 关联业务code 1:会见申请记录、2:会见记录
     */
    private Integer businessType;

    /**
     * 第三方会见人id
     */
    private String meetingPersonId;

    /**
     * 第三方会见人编码
     */
    private String meetingPersonCode;

    /**
     * 会见人类型 0 会见 1 随行 2 被见
     */
    private String meetingPersonType;

    /**
     * 会见人类型名称
     */
    private String meetingPersonTypeName;

    /**
     * 会见人名称
     */
    private String meetingPersonName;

    /**
     * 会见人证件类型
     */
    private String meetingPersonDocumentType;

    /**
     * 会见人证件类型名称
     */
    private String meetingPersonDocumentTypeName;

    /**
     * 会见人证件号码
     */
    private String meetingPersonDocumentNumber;
}
