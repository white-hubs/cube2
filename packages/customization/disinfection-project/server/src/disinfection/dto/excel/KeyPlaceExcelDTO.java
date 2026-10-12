package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.dto.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class KeyPlaceExcelDTO {

    /**
     * 场所名称
     */
    @ExcelProperty(value = "*场所名称", index = 0)
    private String placeName;

    /**
     * 场所分类名称
     */
    @ExcelProperty(value = "*场所分类", index = 1)
    private String placeTypeName;

    /**
     * 项目名称
     */
    @ExcelProperty(value = "*所属项目名称", index = 2)
    private String projectName;

    /**
     * 省
     */
    @ExcelProperty(value = "*省", index = 3)
    private String province;

    /**
     * 市
     */
    @ExcelProperty(value = "市", index = 4)
    private String city;

    /**
     * 区
     */
    @ExcelProperty(value = "区", index = 5)
    private String region;

    /**
     * 重点场所详细位置
     */
    @ExcelProperty(value = "重点场所详细位置", index = 6)
    private String placeDetail;
    /**
     * 经度
     */
    @ExcelProperty(value = "经度", index = 7)
    private BigDecimal longitude;

    /**
     * 纬度
     */
    @ExcelProperty(value = "纬度", index = 8)
    private BigDecimal latitude;

    /**
     * 场所地址
     */
    @ExcelProperty(value = "场所地址", index = 9)
    private String placeAddress;

    /**
     * 场所说明
     */
    @ExcelProperty(value = "备注", index = 10)
    private String placeDescription;

}
