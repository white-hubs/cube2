package com.guhecloud.cube.srv.gen.personnel.management.server.interfaces.vo;

import com.guhecloud.cube.srv.gen.personnel.management.server.enumeration.StaffStatusEnum;
import lombok.Data;
import com.guhecloud.platform.base.util.StringUtils;

import java.time.LocalDateTime;

/**
 * @author bq
 * @date 2023/02/09
 * @description 人员异动
 */
@Data
public class StaffChangeVO {

    private Long id;

    private Long userId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 入职日期
     */
    private LocalDateTime inductionDate;

    /**
     * 转正日期
     */
    private LocalDateTime regularWorkerDate;

    /**
     * 离职日期
     */
    private LocalDateTime quitDate;

    /**
     * 职位ID
     */
    private String appointmentId;

    /**
     * 职位名称
     */
    private String appointmentName;

    /**
     * 部门ID
     */
    private Long organizationId;

    /**
     * 部门名称
     */
    private String organizationName;

    /**
     * 员工类型
     */
    private String type;

    private String typeName;

    /**
     * 员工状态
     */
    private String status;

    private String statusName;

    /**
     * 轮岗部门
     */
    private String rotationOrganizationName;

    /**
     * 轮岗职位
     */
    private String rotationAppointmentName;

    /**
     * 轮岗开始时间
     */
    private String rotationBeginTime;

    /**
     * 轮岗结束时间
     */
    private String rotationEndTime;

    public void setStatus(String status) {
        this.status = status;
        this.statusName = StringUtils.isEmpty(status) ? "" : StaffStatusEnum.valueOf(status).getValue();
    }

}