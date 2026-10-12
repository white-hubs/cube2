package com.guhecloud.cube.biz.mdata.server.stall.param;

import com.guhecloud.cube.biz.mdata.server.excel.ExcelColumn;
import com.guhecloud.cube.biz.mdata.server.excel.ExcelModelBase;
import com.guhecloud.cube.biz.mdata.server.excel.ExcelSheet;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@ExcelSheet(name = "批量导入模板")
@Data
public class ExcelTemplate extends ExcelModelBase {

    public static final String DEPARTMENT_TYPE_KEY = "departmentType";
    public static final String AREA_TYPE_KEY = "areaType";
    public static final String STALL_TYPE_KEY = "stallType";
    public static final String LEASE_TYPE_KEY = "leaseType";
    public static final String LEASED_TYPE_KEY = "leasedType";

    /**
     * 档位编号
     */
    @ExcelColumn(name = "档位编号", width = 30, required = true)
    private String code;

    /**
     * 可视化编码
     */
    @ExcelColumn(name = "档位模型编码", width = 30)
    private String visualizationCode;

    /**
     * 档位名称
     */
    @ExcelColumn(name = "档位名称", width = 30, required = true)
    private String name;

    /**
     * 档主名称
     */
    @ExcelColumn(name = "档主名称", width = 30)
    private String masterName;

    /**
     * 档主手机号
     */
    @ExcelColumn(name = "档主手机号", width = 30)
    private String masterPhone;

    /**
     * 档位类型
     */
    @ExcelColumn(name = "档位类型", width = 20, selectionType = STALL_TYPE_KEY, required = true)
    private String type;

    /**
     * 档位区域
     */
    @ExcelColumn(name = "档位区域", width = 20, selectionType = AREA_TYPE_KEY, required = true)
    private String area;

    /**
     * 具体位置
     */
    @ExcelColumn(name = "具体位置", width = 30, required = true)
    private String location;

    /**
     * 建筑面积
     */
    @ExcelColumn(name = "建筑面积", width = 20, required = true)
    private String builtUpArea;

    /**
     * 实用面积
     */
    @ExcelColumn(name = "实用面积", width = 20, required = true)
    private String practicalArea;

    /**
     * 出租属性
     */
    @ExcelColumn(name = "出租属性", width = 20, selectionType = LEASE_TYPE_KEY, required = true)
    private String leaseType;

    /**
     * 是否出租
     */
    @ExcelColumn(name = "是否出租", width = 20, selectionType = LEASED_TYPE_KEY, required = true)
    private String leased;

    /**
     * 部门
     */
    @ExcelColumn(name = "所属部门", width = 20, selectionType = DEPARTMENT_TYPE_KEY, required = true)
    private String dept;

    public String toSimpleString() {
        return "ExcelTemplate{" +
               "code='" + code + '\'' +
               ", visualizationCode='" + visualizationCode + '\'' +
               ", name='" + name + '\'' +
               '}';
    }
}
