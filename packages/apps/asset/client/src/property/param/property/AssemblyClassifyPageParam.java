package com.guhecloud.cube.property.management.client.property.param.property;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;

@Data
public class AssemblyClassifyPageParam extends Paging {
    /**
     * 分类类型
     * ("assetType", "资产分类"),
     * ("assetSource", "资产来源"),
     * ("reasonForScrapping", "报废原因")
     */
    @NotEmpty(message = "类型不能为空")
    private String type;
    /**
     * 名称
     */
    private String name;
}
