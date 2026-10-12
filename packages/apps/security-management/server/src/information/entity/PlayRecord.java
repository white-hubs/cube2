package com.guhecloud.cube.srv.gen.securitymanagement.server.information.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PlayRecord extends BasicEnhancedTenantEntity<Long> {
    private Long id;

    //第三方记录ID
    private String recordId;
    //终端ID（也是设备ID）
    private String clientId;
    private String clientName;
    //节目内容
    private String itemName;
    //开始时间
    private LocalDateTime startTime;
    //结束时间
    private LocalDateTime stopTime;
    //播放总时长
    private int totalTime;
    //播放次数
    private int totalCircle;
    //区域ID
    private String areaId;
    private String areaName;
}
