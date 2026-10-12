package com.guhecloud.cube.srv.gen.attendance.management.server.leavepost.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.attendance.management.server.overtime.annotation.FormField;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author appet
 * @date 2022/12/12
 * @description 离岗明细表
 */
@Data
@TableName("leave_post_situation")
public class LeavePostSituationPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;
    @FormField(label = "用户ID")
    private Long applicantId;//申请人ID

    private String applicantName;//申请人姓名
    @FormField(label = "部门ID")
    private Long applicantDeptId;//部门ID

    private Long attendanceGroupId;//考情组ID

    private LocalDateTime applyTime;//申请时间

    private LocalDateTime applyDoneTime;//申请处理完成时间

    @FormField(label = "联系电话")
    private String phone;
    @FormField(label = "职位ID")
    private String postIds;//职位ID

    private String postName;//职位名称

    @FormField(label = "离岗时间")
    private LocalDateTime startTime;//开始时间
    @FormField(label = "返岗时间")
    private LocalDateTime endTime;//结束时间

    @FormField(label = "外出地点")
    private String reason;//补卡理由
}