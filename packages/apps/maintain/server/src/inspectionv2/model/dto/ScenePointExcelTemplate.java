package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto;

import com.guhecloud.cube.srv.gen.maintain.server.common.excel.ExcelColumn;
import com.guhecloud.cube.srv.gen.maintain.server.common.excel.ExcelModelBase;
import com.guhecloud.cube.srv.gen.maintain.server.common.excel.ExcelSheet;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@ExcelSheet(name = "批量导入模板")
@Data
public class ScenePointExcelTemplate extends ExcelModelBase {

    public static final String DEPARTMENT_TYPE_KEY = "departmentType";
    public static final String AREA_TYPE_KEY = "areaType";
    public static final String POINT_TYPE_KEY = "pintType";

    /**
     * 现场点编号
     */
    @ExcelColumn(name = "编号", width = 30, required = true)
    private String codeNum;

    /**
     * 现场点名称
     */
    @ExcelColumn(name = "现场点名称", width = 30, required = true)
    private String name;

    /**
     * 现场点类型
     */
    @ExcelColumn(name = "现场点类型", width = 20, selectionType = POINT_TYPE_KEY, required = true)
    private String typeId;

    /**
     * 部门
     */
    @ExcelColumn(name = "管理部门", width = 20, selectionType = DEPARTMENT_TYPE_KEY, required = true)
    private String depId;

    /**
     * 档位区域
     */
    @ExcelColumn(name = "所属区域", width = 20, selectionType = AREA_TYPE_KEY, required = true)
    private String areaId;

    /**
     * 具体位置
     */
    @ExcelColumn(name = "具体地址", width = 30)
    private String location;

    public String toSimpleString() {
        return "ExcelTemplate{" +
               "code='" + codeNum + '\'' +
               ", name='" + name + '\'' +
               '}';
    }
}
