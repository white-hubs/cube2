package com.guhecloud.cube.base.device.center.core.vo;

import com.guhecloud.cube.base.device.center.core.excel.ExcelColumn;
import com.guhecloud.cube.base.device.center.core.excel.ExcelModelBase;
import com.guhecloud.cube.base.device.center.core.excel.ExcelSheet;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@ExcelSheet(name = "批量导入模板")
@Data
public class BatchImportExcelTemplate extends ExcelModelBase {
    public static final String DEPARTMENT_TYPE_KEY = "departmentType";
    public static final String AREA_TYPE_KEY = "areaType";
    public static final String MANAGER_TYPE_KEY = "managerType";

    @ExcelColumn(name = "设备ID", width = 40)
    private String iotDeviceId;
    @ExcelColumn(name = "设备编码", width = 30)
    private String iotDeviceCode;//设备编码
    @ExcelColumn(name = "设备名称", width = 30)
    private String deviceName;
    @ExcelColumn(name = "产品ID", width = 30)
    private String productId;
    @ExcelColumn(name = "产品名称", width = 30)
    private String productName;
    @ExcelColumn(name = "产品品类名称", width = 30)
    private String categoryNameConcat;

    @ExcelColumn(name = "维护部门", width = 30, selectionType = DEPARTMENT_TYPE_KEY)
    private String departmentId;

    @ExcelColumn(name = "区域", width = 20, selectionType = AREA_TYPE_KEY)
    private String areaId;

    @ExcelColumn(name = "维护人员", width = 20, selectionType = MANAGER_TYPE_KEY)
    private String managerIds;

    /**
     * 设备位置(经纬度)
     */
    @ExcelColumn(name = "设备位置", width = 30)
    private String lntAndLat;

    /**
     * 采购日期
     */
    @ExcelColumn(name = "采购日期", width = 20, dateFormat = "yyyy-MM-dd")
    private LocalDate purchaseDate;

    /**
     * 生产日期
     */
    @ExcelColumn(name = "生产日期", width = 20, dateFormat = "yyyy-MM-dd")
    private LocalDate productionDate;

    /**
     * 采购单价
     */
    @ExcelColumn(name = "采购单价", width = 20)
    private String unitPrice;

    /**
     * 使用寿命
     */
    @ExcelColumn(name = "使用寿命", width = 20)
    private String usefulLife;

    /**
     * 设备标签
     */
    @ExcelColumn(name = "设备标签", width = 120)
    private String customerTag;

    public String toSimpleString() {
        return "设备 {" +
               " 设备ID ='" + iotDeviceId + '\'' +
               ", 设备编码 ='" + iotDeviceCode + '\'' +
               ", 设备名称 ='" + deviceName + '\'' +
               '}';
    }
}
