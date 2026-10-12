package com.guhecloud.cube.srv.gen.safety.personnel.server.cert.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

/**
 * 员工证书管理导出行。
 */
@Data
public class EmployeeCertExportRowVO {

    /**
     * 证书类型名称。
     */
    @ExcelProperty("证书类型")
    @ColumnWidth(20)
    private String typeName;

    /**
     * 员工姓名。
     */
    @ExcelProperty("员工")
    @ColumnWidth(16)
    private String userName;

    /**
     * 员工手机号。
     */
    @ExcelProperty("手机号")
    @ColumnWidth(18)
    private String phone;

    /**
     * 证书名称。
     */
    @ExcelProperty("证书名称")
    @ColumnWidth(24)
    private String certName;

    /**
     * 复审状态。
     */
    @ExcelProperty("复审状态")
    @ColumnWidth(14)
    private String reviewStatus;

    /**
     * 到期状态文案。
     */
    @ExcelProperty("到期状态")
    @ColumnWidth(12)
    private String expireStatusLabel;

    /**
     * 剩余到期时间文案。
     */
    @ExcelProperty("剩余到期")
    @ColumnWidth(14)
    private String remainingDaysLabel;
}
