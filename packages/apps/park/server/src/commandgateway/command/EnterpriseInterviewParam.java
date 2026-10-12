package com.guhecloud.cube.srv.gen.park.management.server.commandgateway.command;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 类型数据
 */
@Getter
public class EnterpriseInterviewParam {

    /**
     * 企业ID
     */
    private Long enterpriseId;

    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 走访时间
     */
    private LocalDateTime time;

    /**
     * 走访类型ID
     */
    private Long interviewTypeId;

    /**
     * 走访人员
     */
    private String person;

    /**
     * 企业接待人员
     */
    private String receptionPerson;

    /**
     * 走访记录
     */
    private String recordContent;

    /**
     * 关联诉求ID
     */
    private String appealIds;

    /**
     * 文件IDs
     */
    private List<Long> fileIds;

}
