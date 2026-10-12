package com.guhecloud.cube.srv.gen.work.permit.server.param;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 作业关联项保存参数。
 */
@Data
public class WorkPermitAssociatedItemParam {
    /**
     * 关联项内容。
     */
    @NotBlank(message = "关联项内容不能为空")
    private String itemName;
    /**
     * 排序号。
     */
    private Integer sortNo;
}
