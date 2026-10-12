package com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

@Data
public class ElectricityExcelVo implements Serializable {
    @Excel(name = "抄表日期", mergeVertical = true)
    private String collectTime;
    //配电设备
    @Excel(name = "高压进线编号", mergeVertical = true)
    private String powerMeterCode;
    //配电a相电流
    @Excel(name = "电表读数a", mergeVertical = true)
    private String powerMeterA;
    //配电b相电流
    @Excel(name = "电表读数b", mergeVertical = true)
    private String powerMeterB;
    //配电电流
    @Excel(name = "电量", mergeVertical = true)
    private String powerCurrentQuantity;
    //变压设备
    @Excel(name = "变压器编号")
    private String transformerMeterCode;
    //变压器a相电流
    @Excel(name = "电表读数a")
    private String transformerMeterA;
    //变压器b相电流
    @Excel(name = "电表读数b")
    private String transformerMeterB;
    //变压器配电
    @Excel(name = "电量")
    private String transformerCurrentQuantity;
    @Excel(name = "当天总耗电")
    private String todayTotal;
}
