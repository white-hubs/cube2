package com.guhecloud.cube.biz.door.server.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventRecord extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    //事件名称
    private String eventName;
    //事件id
    private String eventId;
    //事件时间
    private LocalDateTime eventTime;
    //人员Id
    private String personId;
    //人员名称
    private String personName;
    //门禁点编码
    private String doorIndexCode;
    //门禁名称
    private String doorName;
    //门禁类型
    private String eventType;
    //进出类型
    private String inAndOutType;
    //时间结果
    private String eventResult;
    //门禁方式
    private String eventWay;
    //卡号
    private String cardNo;
    //抓拍图片文件ID
    private Long pictureFileId;
}
