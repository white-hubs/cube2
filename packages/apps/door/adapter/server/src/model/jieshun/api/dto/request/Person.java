package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.jieshun.api.dto.request;

import lombok.Data;

import java.util.List;

/**
 * author: ht
 * create: 2025-05-22 16:40
 */
@Data
public class Person {
    /**
     * personId 人员唯一标识 string 人员信息的唯一标识 是
     * personNo 人员编号 string(10) 编号 是
     * personName 人员姓名 string(20) 人员姓名 是
     * personGender 性别 int 值参考：数据字典 否
     * deptId 组织唯一标
     * 识
     * string 组织唯一标识 是
     * deptName 组织名称 string 组织层级用’|’分隔，多个
     * 组织用’;’分号分隔
     * 否
     * remark 备注 string(200) 备注信息 否
     * personPhoto 人员图片 string 人员图片 Url 否
     * certificateType 证件类型 string
     * 值参考：数据字典
     * 否
     * identityNo 证件号码 string(20) 证件号码 否
     * mobile 手机号码 string(20) 手机号码 否
     * tel1 电话 1 string(20) 电话 1 否
     * tel2 电话 2 string(20) 电话 2 否
     * Page 29 of 125
     * email 邮箱 string(200) 邮箱 否
     * roomNo 房号 string(200) 房牌号码 否
     * address 地址 string(200) 地址 否
     * tenementType 住户类型 int 值参考：数据字典 否
     * vehicleList 数组
     * plateNumber 车牌号 string 格式参考：数据字典 是
     * plateColor 车牌颜色 string 值参考：数据字典 否
     * vehicleBrand 车标 string 车标 否
     * vehicleColor 车身颜色 string 车身颜色 否
     * vehicleStatus 绑定状态 int 值参考：数据字典 否
     * remark 备注 string 否
     */
    private String personId;
    private String personNo;
    private String personName;
    private String personGender;
    private String deptId;
    private String deptName;
    private String remark;
    private String personPhoto;
    private String certificateType;
    private String identityNo;
    private String mobile;
    private String tel1;
    private String tel2;
    private String email;
    private String roomNo;
    private String address;
    private String tenementType;
    private List<Vehicle> vehicleList;

    @Data
    public static class Vehicle {
        private String plateNumber;
        private String plateColor;
        private String vehicleBrand;
        private String vehicleColor;
        private String vehicleStatus;
        private String remark;
    }

}
