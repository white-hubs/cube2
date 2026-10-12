package com.guhecloud.subsystem.requester.sdk.vo.information;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PLayRecordVo {
    //播放记录ID
    private String recordId;
    //记录生成时间
    private LocalDateTime recordTime;
    //公司ID
    private String companyId;
    //设备ID或是终端ID
    private String clientId;
    private String clientName;
    //区域ID
    private String areaId;
    private String areaName;
    //节目内容或是名称
    private String itemName;

    private String window;
    //开始时间
    private LocalDateTime startTime;
    //结束时间
    private LocalDateTime stopTime;
    //播放时长
    private int totalTime;
    //播放次数
    private int totalCircle;
}
