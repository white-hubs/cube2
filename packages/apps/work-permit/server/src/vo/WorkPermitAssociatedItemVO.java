package com.guhecloud.cube.srv.gen.work.permit.server.vo;

import lombok.Data;

/**
 * 作业关联项。
 */
@Data
public class WorkPermitAssociatedItemVO {
    /**
     * 主键。
     */
    private Long id;
    /**
     * 关联项内容。
     */
    private String itemName;
    /**
     * 排序号。
     */
    private Integer sortNo;
}
