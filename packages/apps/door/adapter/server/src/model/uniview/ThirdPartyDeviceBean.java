package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.uniview;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * 门禁设备信息
 */
@Data
public class ThirdPartyDeviceBean {
    @JSONField(name = "deviceIp")
    private String deviceIp;

    @JSONField(name = "deviceSubtype")
    private Object deviceSubtype;

    @JSONField(name = "latitude")
    private Object latitude;

    @JSONField(name = "addMark")
    private int addMark;

    @JSONField(name = "typeName")
    private String typeName;

    @JSONField(name = "udpPort")
    private Object udpPort;

    @JSONField(name = "serverPort")
    private Object serverPort;

    @JSONField(name = "deviceName")
    private String deviceName;

    @JSONField(name = "deviceDoorStatus")
    private Object deviceDoorStatus;

    @JSONField(name = "mac")
    private Object mac;

    @JSONField(name = "daCode")
    private String daCode;

    @JSONField(name = "password")
    private Object password;

    @JSONField(name = "netmask")
    private Object netmask;

    @JSONField(name = "areaName")
    private String areaName;

    @JSONField(name = "id")
    private int id;

    @JSONField(name = "longitude")
    private Object longitude;

    @JSONField(name = "deviceType")
    private int deviceType;

    @JSONField(name = "oldIp")
    private Object oldIp;

    @JSONField(name = "connectHandle")
    private int connectHandle;

    @JSONField(name = "lockCount")
    private Object lockCount;

    @JSONField(name = "deviceIdList")
    private Object deviceIdList;

    @JSONField(name = "swipeInterval")
    private int swipeInterval;

    @JSONField(name = "netMask")
    private String netMask;

    @JSONField(name = "deviceCode")
    private String deviceCode;

    @JSONField(name = "versionNumber")
    private Object versionNumber;

    @JSONField(name = "serialNo")
    private Object serialNo;

    @JSONField(name = "modify")
    private Object modify;

    @JSONField(name = "doorList")
    private List<DoorListItem> doorList;

    @JSONField(name = "areaCode")
    private String areaCode;

    @JSONField(name = "port")
    private Object port;

    @JSONField(name = "modelNumber")
    private Object modelNumber;

    @JSONField(name = "startingFloor")
    private int startingFloor;

    @Override
    public String toString() {
        return "ThirdPartyDeviceBean{" +
               "deviceIp='" + deviceIp + '\'' +
               ", deviceSubtype=" + deviceSubtype +
               ", latitude=" + latitude +
               ", addMark=" + addMark +
               ", typeName='" + typeName + '\'' +
               ", udpPort=" + udpPort +
               ", serverPort=" + serverPort +
               ", deviceName='" + deviceName + '\'' +
               ", deviceDoorStatus=" + deviceDoorStatus +
               ", mac=" + mac +
               ", daCode='" + daCode + '\'' +
               ", password=" + password +
               ", netmask=" + netmask +
               ", areaName='" + areaName + '\'' +
               ", id=" + id +
               ", longitude=" + longitude +
               ", deviceType=" + deviceType +
               ", oldIp=" + oldIp +
               ", connectHandle=" + connectHandle +
               ", lockCount=" + lockCount +
               ", deviceIdList=" + deviceIdList +
               ", swipeInterval=" + swipeInterval +
               ", netMask='" + netMask + '\'' +
               ", deviceCode='" + deviceCode + '\'' +
               ", versionNumber=" + versionNumber +
               ", serialNo=" + serialNo +
               ", modify=" + modify +
               ", areaCode='" + areaCode + '\'' +
               ", port=" + port +
               ", modelNumber=" + modelNumber +
               ", startingFloor=" + startingFloor +
               '}';
    }
}
