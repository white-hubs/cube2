package com.guhecloud.cube.iot.mgt.transfer.dto.param;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 产品导出参数
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductExportParam {

    /**
     * 导出的产品ID
     */
    @NotEmpty(message = "产品ID不能为空")
    private Long productId;

    /**
     * 导出格式：json, excel
     */
    @Builder.Default
    private String format = "excel";

    /**
     * 是否包含属性
     */
    @Builder.Default
    private Boolean includeProperties = true;

    /**
     * 是否包含事件
     */
    @Builder.Default
    private Boolean includeEvents = true;

    /**
     * 是否包含命令
     */
    @Builder.Default
    private Boolean includeCommands = true;
}