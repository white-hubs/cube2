package com.guhecloud.cube.srv.gen.business.info.sync.server.controller.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/1/13 15:31
 * @Description:
 */
@Data
public class ExcelVO {
    @ExcelProperty(value = "*证件号码")
    private String id;
    @ExcelProperty(value = "*人员名称")
    private String name;
    @ExcelProperty(value = "*卡号")
    private String serialNumber;
    @ExcelProperty(value = "*卡介质")
    private String type;
    /**
     * 有效期
     */
    @ExcelProperty(value = "*有效结束时间")
    private LocalDateTime validityDate;
}
