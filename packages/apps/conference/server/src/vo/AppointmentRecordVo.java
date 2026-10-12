package com.guhecloud.cube.srv.gen.conferencemanage.server.vo;

import com.guhecloud.cube.srv.gen.conferencemanage.server.enumeration.BookRoomEnum;
import com.guhecloud.cube.srv.gen.conferencemanage.server.enumeration.OperateEnum;
import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentRecordVo extends Paging {
    private Long conferenceId;
    private String userName;
    //用户id
    private String conferenceName;
    //操作
    private OperateEnum operate;
    //部门
    private String departmentName;
    //时段
    private String timeInterval;
    //会议设备
    private String conferenceDevice;
    private LocalDateTime createTime;
    private LocalDateTime appointmentDate;
    //会议主题
    private String conferenceTheme;
    //会议类型
    private String conferenceType;
    //人数
    private int bookNum;
    //预约状态
    private BookRoomEnum bookStatus;
    //预约人
    private Long subscribePerson;
    //部门
    private String deptName;
    //会议物品
    private String goodsIds;
    //会议物品名称
    private String goodsNames;
    //取消原因
    private String cancelReason;
    //流程实例id
    private String processInstanceId;
}
