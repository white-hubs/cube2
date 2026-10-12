package com.guhecloud.cube.srv.gen.safety.personnel.server.person.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 安全人员台账导出行。
 */
@Data
public class PersonExportRowVO {

    /**
     * 姓名。
     */
    @ExcelProperty("姓名")
    @ColumnWidth(16)
    private String userName;

    /**
     * 手机号。
     */
    @ExcelProperty("手机号")
    @ColumnWidth(18)
    private String phone;

    /**
     * 所属单位。
     */
    @ExcelProperty("单位")
    @ColumnWidth(24)
    private String companyName;

    /**
     * 所属部门。
     */
    @ExcelProperty("部门")
    @ColumnWidth(24)
    private String deptName;

    /**
     * 岗位名称。
     */
    @ExcelProperty("岗位")
    @ColumnWidth(18)
    private String postName;

    /**
     * 人员状态文案，来源 {@link com.guhecloud.cube.srv.gen.safety.personnel.server.common.enumeration.PersonStatusEnum}。
     */
    @ExcelProperty("状态")
    @ColumnWidth(12)
    private String statusLabel;

    /**
     * 人员类型文案，多个以逗号分隔；编码见 {@link com.guhecloud.cube.srv.gen.safety.personnel.server.common.enumeration.PersonTypeCodeEnum}。
     */
    @ExcelProperty("人员类型")
    @ColumnWidth(28)
    private String typeLabels;

    /**
     * 纳入安全人员台账时间。
     */
    @ExcelProperty("纳入时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ColumnWidth(22)
    private LocalDateTime linkedAt;
}
