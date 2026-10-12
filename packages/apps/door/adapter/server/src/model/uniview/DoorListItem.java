package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.uniview;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

@Data
public class DoorListItem {
    @JSONField(name = "deviceIp")
    private Object deviceIp;

    @JSONField(name = "doorSensorType")
    private int doorSensorType;

    @JSONField(name = "doorEffectiveTimePeriod")
    private int doorEffectiveTimePeriod;

    @JSONField(name = "doorCombination")
    private Object doorCombination;

    @JSONField(name = "doorName")
    private String doorName;

    @JSONField(name = "lockDriverTime")
    private int lockDriverTime;

    @JSONField(name = "typeName")
    private Object typeName;

    @JSONField(name = "templateId")
    private Object templateId;

    @JSONField(name = "deviceName")
    private Object deviceName;

    @JSONField(name = "deviceDoorId")
    private int deviceDoorId;

    @JSONField(name = "areaName")
    private Object areaName;

    @JSONField(name = "opreate")
    private Object opreate;

    @JSONField(name = "doorSensorTimeout")
    private int doorSensorTimeout;

    @JSONField(name = "id")
    private Object id;

    @JSONField(name = "deviceType")
    private Object deviceType;

    @JSONField(name = "swipeInterval")
    private int swipeInterval;

    @JSONField(name = "deviceCode")
    private Object deviceCode;

    @JSONField(name = "closeDoor")
    private boolean closeDoor;

    @JSONField(name = "templateName")
    private Object templateName;

    @JSONField(name = "doorIndex")
    private int doorIndex;

    @JSONField(name = "doorStatus")
    private int doorStatus;

    @JSONField(name = "doorOpenTimePeriod")
    private int doorOpenTimePeriod;

    @JSONField(name = "personId")
    private Object personId;

    @JSONField(name = "effectiveTimeStatus")
    private Object effectiveTimeStatus;

    @JSONField(name = "opentype")
    private int opentype;

    @JSONField(name = "startingFloor")
    private int startingFloor;

    @JSONField(name = "status")
    private String status;
}
