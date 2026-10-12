package com.guhecloud.cube.deliver.project.merchant.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("device")
public class Device {

    private String id;

    private String projectId;

    private String areaId1;

    private String areaId2;

    private String areaId3;

    private String typeId;

    private String model;

    private String iotDeviceId;

    private String iotDeviceType;

    private String bimModelId;

    private String code;

    private String name;

    private String pinyin;

    private String status;

    private Integer online;

    private LocalDateTime manualMaint;

    private Double price;

    private String installAddress;

    private LocalDateTime installDate;

    private LocalDateTime startDate;

    private LocalDateTime expireDate;

    private LocalDateTime productionDate;

    private Integer timeLimit;

    private Integer maintCycle;

    private LocalDateTime maintDate;

    private String token;

    private String manufacturer;

    private String specification;

    private String deptId;

    private String contactPerson;

    private String contactPhone;

    private String contactAddress;

    private String qrCode;

    private String gisLongitude;

    private String gisLatitude;

    private String account;

    private String password;

    private String ipAddr;

    private String port;

    private String videoboxId;

    private String financeCode;

    private String remark;

    private String nearbyCams;

    private Boolean visibility;

    private String monitoringObject;

    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Boolean deleted;

}
