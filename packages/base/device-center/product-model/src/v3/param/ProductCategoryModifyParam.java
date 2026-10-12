package com.guhecloud.cube.base.iot.product.model.v3.param;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 产品品类修改参数
 * 专用于品类管理的修改参数
 * <p>前端不需要理解字典类型(dictId),由后端固定设置为品类字典</p>
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductCategoryModifyParam {

    /**
     * 字典项ID(修改时必填)
     */
    private Long id;

    /**
     * 父级品类ID(顶级品类填0)
     */
    private Long parentId = 0L;

    /**
     * 排序号
     */
    private Integer sort = 0;

    /**
     * 品类名称
     */
    @NotBlank(message = "品类名称不能为空")
    @Size(max = 100, message = "品类名称长度不能超过100")
    private String itemLabel;

    /**
     * 品类值(可选)
     */
    @Size(max = 200, message = "品类值长度不能超过200")
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

    /**
     * 转换为通用字典修改参数
     *
     * @param dictId 品类字典ID
     * @return DictItemModifyParam
     */
    public DictItemModifyParam toCommonDictItemModifyParam(Long dictId) {
        DictItemModifyParam param = new DictItemModifyParam();
        param.setId(this.id);
        param.setDictId(dictId);
        param.setParentId(this.parentId);
        param.setSort(this.sort);
        param.setItemLabel(this.itemLabel);
        param.setItemValue(this.itemValue);
        param.setStatus(this.status);
        param.setRemark(this.remark);
        return param;
    }
}

