package com.guhecloud.cube.srv.gen.park.management.server.commandgateway.command;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 类型数据
 */
@Getter
public class EnterpriseAppealParam {

    /**
     * 名称
     */
    private String name;

    /**
     * 诉求类型ID
     */
    private Long appealTypeId;

    /**
     * 诉求时间
     */
    private LocalDateTime time;

    /**
     * 反馈人员名
     */
    private String username;

    /**
     * 企业ID
     */
    private Long enterpriseId;

    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 描述
     */
    private String description;

    /**
     * 解决措施
     */
    private String measure;

    /**
     * 文件IDs
     */
    private List<Long> fileIds;

}
