package com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.message.organization;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/12/18 10:02
 * @Description:
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/12/18 9:36
 * @Description: 组织关系操作消息
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OperationOrganizationMessage {
    public final static String TOPIC = "OPERATION_ORGANIZATION";
    /**
     * 操作类型
     */
    private String operationType;

    /**
     * 组织id
     */
    private Long id;

    /**
     * 组织名称
     */
    private String name;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 组织层级
     */
    private Integer level;

    /**
     * 上级组织主键
     */
    private Long parentId;

    /**
     * 组织机构路径id
     */
    private String orgPathId;

    /**
     * 组织机构路径名称
     */
    private String orgPathName;

    /**
     * 租户
     */
    private Integer tenant;

}

