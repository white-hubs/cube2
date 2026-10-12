package com.guhecloud.cube.biz.meattrade.server.base.param;

import com.guhecloud.cube.biz.meattrade.server.base.enumeration.MeatGoodsFreshLabelEnum;
import lombok.Data;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * 商品品类重量设置参数
 *
 * @author lanchun
 * @date 2023/7/28 9:44
 */
@Data
public class GoodWeightConfigModifyParam {

    /**
     * 商品主键
     */
    @NotNull(message = "商品不能为空")
    private List<String> goodsCategoryIds;

    /**
     * 标准重量
     */
    @NotNull(message = "标准重量不能为空")
    @DecimalMin(value = "0.1", message = "重量不能小于0.1")
    private Double standardWeight;

    /**
     * 重量单位
     */
    @Size(max = 20, message = "重量单位不能大于20")
    private String weightUnit;

    /**
     * 冷热鲜标识
     */
    private MeatGoodsFreshLabelEnum freshLabel;

    /**
     * 备注信息
     */
    @Size(max = 200, message = "备注不能超过200个字")
    private String remark;
}
