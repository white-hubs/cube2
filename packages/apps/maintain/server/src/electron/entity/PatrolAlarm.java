package com.guhecloud.cube.srv.gen.maintain.server.electron.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("patrol_alarm")
public class PatrolAlarm extends BasicEnhancedTenantEntity<String> {
    private String id;
    //类型
    private String alarmType;
    //地点
    private String alarmPoint;
    //内容
    private String alarmContent;
    //时间
    private String alarmTime;
    //处理结果
    private String alarmResult;
    //巡检记录ID
    private String patrolRecordId;

}
