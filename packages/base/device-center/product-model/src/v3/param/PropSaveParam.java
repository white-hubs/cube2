package com.guhecloud.cube.base.iot.product.model.v3.param;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 属性保存参数
 */
@Data
public class PropSaveParam {

    /**
     * 产品ID
     */
    @NotNull(message = "产品ID不能为空")
    private Long prodId;

    /**
     * 属性名称
     */
    @NotBlank(message = "属性名称不能为空")
    @Size(max = 100, message = "属性名称长度不能超过100")
    private String name;

    /**
     * 属性Code,产品下唯一
     */
    @NotBlank(message = "属性编码不能为空")
    @Size(max = 50, message = "属性编码长度不能超过50")
    private String propCode;

    /**
     * 数据类型: int/long/float/double/string/bool/date/enum/struct/array
     */
    @NotBlank(message = "数据类型不能为空")
    private String dataType;

    /**
     * 数据精度(小数位数)
     */
    private Integer precision;

    /**
     * 数据单位符号(如: ℃, %, m/s)
     */
    @Size(max = 20, message = "单位符号长度不能超过20")
    private String unitSymbol;

    /**
     * 读写类型: false只读 true读写
     */
    private int dataWrite;

    /**
     * 数据约束配置(JSON格式)
     */
    private String dataConfig;

    /**
     * 属性描述
     */
    @Size(max = 500, message = "属性描述长度不能超过500")
    private String remark;

    /**
     * 默认值
     */
    @Size(max = 255, message = "默认值长度不能超过255")
    private String defaultValue;

    /**
     * 排序号
     */
    private Integer sortOrder = 0;
}
