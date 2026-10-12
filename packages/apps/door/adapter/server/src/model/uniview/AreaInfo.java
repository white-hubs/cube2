package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.uniview;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

/**
 * 区域信息
 */
@Data
public class AreaInfo {
    @JSONField(name = "Outs")
    private int outs;

    @JSONField(name = "Levelorder")
    private Object levelorder;

    @JSONField(name = "Respath")
    private String respath;

    @JSONField(name = "DeviceId")
    private Object deviceId;

    @JSONField(name = "Count")
    private int count;

    @JSONField(name = "Inorout")
    private Object inorout;

    @JSONField(name = "ParentName")
    private String parentName;

    @JSONField(name = "Code")
    private String code;

    @JSONField(name = "Name")
    private String name;

    @JSONField(name = "Ins")
    private int ins;

    @JSONField(name = "Parentcode")
    private String parentcode;

    @JSONField(name = "Owned")
    private boolean owned;

    @JSONField(name = "OutLandCode")
    private Object outLandCode;

    @JSONField(name = "Version")
    private Object version;

    @JSONField(name = "UpdateTime")
    private Object updateTime;

    @JSONField(name = "Restype")
    private int restype;

    @JSONField(name = "Level")
    private int level;

    @JSONField(name = "Belongin")
    private int belongin;

    @JSONField(name = "Id")
    private int id;

    @JSONField(name = "OnlineCount")
    private int onlineCount;

    @JSONField(name = "Areatype")
    private int areatype;

    @JSONField(name = "Stay")
    private int stay;
}
