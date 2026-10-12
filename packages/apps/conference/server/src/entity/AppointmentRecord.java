package com.guhecloud.cube.srv.gen.conferencemanage.server.entity;

import com.guhecloud.cube.srv.gen.conferencemanage.server.enumeration.OperateEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentRecord extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    //用户
    private String userName;
    //用户id
    private Long userId;
    //会仪室
    private String conferenceName;
    //操作
    private OperateEnum operate;
    //部门
    private String departmentName;
    //时段
    private String timeInterval;
    //会议设备
    private String conferenceDevice;
    //预约日期
    private LocalDateTime appointmentDate;
    //会议物品
    private String goodsNames;
    //取消原因
    private String cancelReason;
    //流程id
    private String processInsId;
}
