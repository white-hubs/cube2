package com.guhecloud.cube.srv.gen.safety.personnel.server.cert.param;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 证书类型新增或编辑参数。
 */
@Data
public class CertTypeModifyParam {

    /**
     * 证书分类ID；必填。
     */
    @NotNull(message = "证书分类ID不能为空")
    private Long categoryId;

    /**
     * 证书类型名称；必填，同一分类下不可重复。
     */
    @NotBlank(message = "证书类型名称不能为空")
    @Size(max = 64, message = "证书类型名称不能超过64个字符")
    private String typeName;

    /**
     * 定义说明；可为空。
     */
    @Size(max = 500, message = "定义说明不能超过500个字符")
    private String definition;

    /**
     * 有效期数值；可为空。
     */
    @Min(value = 1, message = "有效期数值不能小于1")
    private Integer validPeriod;

    /**
     * 有效期单位，DAY/MONTH/YEAR；可为空。
     */
    private String validPeriodUnit;

    /**
     * 到期提前提醒天数；可为空。
     */
    @Min(value = 0, message = "到期提前提醒天数不能小于0")
    private Integer warnDays;

    /**
     * 是否需要复审；可为空，默认false。
     */
    private Boolean needReview;

    /**
     * 复审周期数值；可为空。
     */
    @Min(value = 1, message = "复审周期数值不能小于1")
    private Integer reviewPeriod;

    /**
     * 复审周期单位，DAY/MONTH/YEAR；可为空。
     */
    private String reviewPeriodUnit;
}
