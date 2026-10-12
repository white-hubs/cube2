package com.guhecloud.cube.srv.gen.attendance.management.server.rule.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author bq
 * @date 2022/11/14
 * @description 考勤组
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("attendance_group")
public class AttendanceGroupPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 考勤类型（排班制、值班制）
     */
    private String type;

    /**
     * 适用范围(所有人、部分人)
     */
    private String ranges;

    /**
     * 班次类型（固定班制、排班制）
     */
    private String classesType;

    /**
     * 法定节假日自动排休
     */
    private Boolean autoRest;

    /**
     * 包含班次
     */
    private String classesIds;

    /**
     * 是否需要打卡
     */
    private Boolean isPunchCard;

    /**
     * 是否允许补卡
     */
    private Boolean isSupplementCard;

    /**
     * 是否允许加班
     */
    private Boolean isWorkOvertime;

    /**
     * 打卡方式
     */
    private String punchCardWayStr;

    public AttendanceGroupPO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public AttendanceGroupPO(Long id) {
        this.id = id;
    }
}