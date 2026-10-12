package com.guhecloud.cube.srv.gen.attendance.management.server.changeclasses.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.attendance.management.server.overtime.annotation.FormField;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author appet
 * @date 2022/11/21
 * @description 换班明细表
 */
@Data
@TableName("classes_change_situation")
public class ClassesChangeSituationPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;
    @FormField(label = "用户ID")
    private Long applicantId;//申请人ID
    @FormField(label = "姓名")
    private String applicantName;//申请人姓名
    @FormField(label = "部门ID")
    private Long applicantDeptId;//部门ID

    private Long attendanceGroupId;//考情组ID

    private LocalDateTime applyTime;//申请时间

    private LocalDateTime applyDoneTime;//申请处理完成时间

    @FormField(label = "被换人ID")
    private Long changerId;//被换人ID
    private String changerName;//被换人姓名
    @FormField(label = "要换日期")
    private LocalDateTime fromDate;//要换日期
    @FormField(label = "被换日期")
    private LocalDateTime toDate;//被换日期
    @FormField(label = "要换班次")
    private Long fromClassesId;//要换班次ID
    @FormField(label = "被换班次")
    private Long toClassesId;//被换班次
    @FormField(label = "换班类型")
    private String workType;//换班类型DUTY:值班,ARRANGE:排班
    @FormField(label = "换班原因")
    private String reason;

}