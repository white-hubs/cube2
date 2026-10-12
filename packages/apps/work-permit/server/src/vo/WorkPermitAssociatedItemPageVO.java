package com.guhecloud.cube.srv.gen.work.permit.server.vo;

import lombok.Data;

/**
 * 作业关联项分页列表。
 */
@Data
public class WorkPermitAssociatedItemPageVO {
    /**
     * 主键。
     */
    private Long id;
    /**
     * 作业类型配置ID。
     */
    private Long workPermitTypeId;
    /**
     * 作业类型名称。
     */
    private String typeName;
    /**
     * 作业等级。
     */
    private String workPermitLevel;
    /**
     * 关联项内容。
     */
    private String itemName;
    /**
     * 排序号。
     */
    private Integer sortNo;
}
