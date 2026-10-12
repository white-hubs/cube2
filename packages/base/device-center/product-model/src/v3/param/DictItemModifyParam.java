package com.guhecloud.cube.base.iot.product.model.v3.param;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * 字典项修改参数
 * 用于新增和修改字典项
 * 注意: itemCode由系统内部维护,不对外暴露
 */
@Data
public class DictItemModifyParam {

    /**
     * 字典项ID(修改时必填)
     */
    private Long id;

    /**
     * 字典类型ID(必填)
     */
    @NotNull(message = "字典类型不能为空")
    private Long dictId;

    /**
     * 父级ID(顶级节点填0)
     */
    private Long parentId = 0L;

    /**
     * 排序号
     */
    private Integer sort = 0;

    /**
     * 字典标签(显示名称)
     */
    @NotBlank(message = "字典标签不能为空")
    @Size(max = 100, message = "字典标签长度不能超过100")
    private String itemLabel;

    /**
     * 字典键值
     */
    @Size(max = 200, message = "字典键值长度不能超过200")
    private String itemValue;

    /**
     * 状态: 0正常 1停用
     */
    private Integer status = 0;

    /**
     * 备注
     */
    @Size(max = 500, message = "备注长度不能超过500")
    private String remark;
}
