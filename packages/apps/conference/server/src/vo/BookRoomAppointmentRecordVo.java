package com.guhecloud.cube.srv.gen.conferencemanage.server.vo;

import com.guhecloud.cube.srv.gen.conferencemanage.server.enumeration.BookRoomEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 会议室预约信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookRoomAppointmentRecordVo {
    /**
     * 预约id
     */
    private Long bookId;
    /**
     * 预约人
     */
    private Long userId;
    private String userName;
    /**
     * 会议室
     */
    private Long conferenceId;
    /**
     * 部门
     */
    private Long deptId;
    private String deptName;
    /**
     * 时段
     */
    private String timeInterval;
    /**
     * 预约/使用状态
     */
    private String status;

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

    //排序字段
    private LocalDateTime sortTime;
}
