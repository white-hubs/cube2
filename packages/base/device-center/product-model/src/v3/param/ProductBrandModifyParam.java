package com.guhecloud.cube.base.iot.product.model.v3.param;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * 产品品牌修改参数
 * 专用于品牌管理的修改参数
 */
@Data
public class ProductBrandModifyParam {

    /**
     * 字典项ID(修改时必填)
     */
    private Long id;

    /**
     * 父级品牌ID(顶级品牌填0)
     */
    private Long parentId = 0L;

    /**
     * 排序号
     */
    private Integer sort = 0;

    /**
     * 品牌名称
     */
    @NotBlank(message = "品牌名称不能为空")
    @Size(max = 100, message = "品牌名称长度不能超过100")
    private String itemLabel;

    /**
     * 品牌图标ID
     * <p>存储品牌图标图片在文件服务中的文件ID,通过文件服务接口上传后获得</p>
     * <p>调用链: 前端上传 -> 文件服务 -> 返回fileId -> 保存到品牌(存储在itemValue字段)</p>
     */
    @Size(max = 200, message = "品牌图片URL长度不能超过200")
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
     * @param dictId 品牌字典ID
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
