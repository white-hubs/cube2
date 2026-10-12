package com.guhecloud.cube.iot.mgt.dcm.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@NoArgsConstructor
@AllArgsConstructor
public class ProdProperty {

    /** 属性Code,产品下唯一 */
    private String code;

    /** 属性名称 */
    private String name;

    /** 数据类型 */
    private String dataType;

    /** 数据精度 */
    private Integer precision;

    /** 数据单位 */
    @Builder.Default
    private String unit = "";

    /** 读写类型:0只读:1读写 */
    @Builder.Default
    private int dataWrite = 0;

    @Builder.Default
    private String dataConfig = "";

    /** 属性描述 */
    @Builder.Default
    private String remark = "";
}
