package com.guhecloud.cube.srv.gen.adapter.information.server.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PLayRecordVo {
    @JsonProperty("playlogid")
    private String playLogId;
    @JsonProperty("logtime")
    private LocalDateTime logTime;
    //设备ID或是终端ID
    private String clientId;
    //公司ID
    @JsonProperty("companyid")
    private String companyId;
    //终端名称
    @JsonProperty("clientname")
    private String clientName;
    //节目内容
    @JsonProperty("itemname")
    private String itemName;
    @JsonProperty("window")
    private String window;
    //开始时间
    @JsonProperty("starttime")
    private LocalDateTime startTime;
    //结束时间
    @JsonProperty("stoptime")
    private LocalDateTime stopTime;
    //播放总时长
    @JsonProperty("totaltime")
    private int totalTime;
    //播放次数
    @JsonProperty("totalcircle")
    private int totalCircle;
    //播放记录ID
    private String recordId;
    //记录生成时间
    private LocalDateTime recordTime;
}
