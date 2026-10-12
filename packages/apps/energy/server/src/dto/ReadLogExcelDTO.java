package com.guhecloud.cube.srv.gen.energymanagement.server.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ReadLogExcelDTO {
    /**
     * 设备id
     */
    @ExcelProperty(value = "设备id", index = 0)
    private Long deviceId;
    /**
     * 设备编码
     */
    @ExcelProperty(value = "设备编码", index = 1)
    private String deviceCode;
    /**
     * 设备名称
     */
    @ExcelProperty(value = "设备名称", index = 2)
    private String placeName;
    /**
     * 上期抄表时间
     */
    @ExcelProperty(value = "上期抄表时间", index = 3)
    private LocalDateTime preTime;
    /**
     * 上次抄表数
     */
    @ExcelProperty(value = "上次抄表数", index = 4)
    private BigDecimal preReadValue;
    /**
     * 本期抄表时间
     */
    @ExcelProperty(value = "本期抄表时间", index = 5)
    private LocalDateTime curTime;
    /**
     * 抄表值
     */
    @ExcelProperty(value = "抄表值", index = 6)
    private BigDecimal readValue;
    /**
     * 用量
     */
    @ExcelProperty(value = "用量", index = 7)
    private BigDecimal usage;
}
