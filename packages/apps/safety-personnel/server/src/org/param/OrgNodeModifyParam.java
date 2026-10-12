package com.guhecloud.cube.srv.gen.safety.personnel.server.org.param;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 组织节点新增或编辑参数。
 */
@Data
public class OrgNodeModifyParam {

    /**
     * 节点名称；必填，同一父节点下不可重复。
     */
    @NotBlank(message = "节点名称不能为空")
    @Size(max = 64, message = "节点名称不能超过64个字符")
    private String name;

    /**
     * 同级排序号；可为空，默认0。
     */
    @Min(value = 0, message = "排序号不能小于0")
    private Integer sortNo;
}
