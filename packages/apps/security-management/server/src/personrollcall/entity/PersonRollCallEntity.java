package com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 人员点到明细 实体类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("person_roll_call")
public class PersonRollCallEntity extends BasicEnhancedTenantEntity<Long> {

    private Long id;
    /**
     * 点名任务记录编号
     */
    private String recordCode;

    /**
     * 姓名
     */
    private String personName;

    /**
     * 编号
     */
    private String indexCode;

    /**
     * 照片
     */
    private String facePhoto;

    /**
     * 抓拍照片
     */
    private String capturePhoto;

    /**
     * 点名设备编号
     */
    private String deviceCode;

    /**
     * 任务类型
     */
    private String taskType;

    /**
     * 人脸比对相似度
     */
    private Float similarity;

    /**
     * 状态：0-点到，1-特殊情况
     */
    private Integer status;

    /**
     * 特殊情况说明
     */
    private String remark;

    /**
     * 点到时间
     */
    private LocalDateTime rollcallTime;

    /**
     * 场所编号
     */
    private String areaIndexCode;

    /**
     * 点名对象所在监区编号
     */
    private String departmentIndexCode;

    /**
     * 点名对象所在监区名称
     */
    private String departmentName;

    /**
     * 场所名称（保留字段，当前版本无数据）
     */
    private String areaName;

    /**
     * 点名设备名称
     */
    private String deviceName;

    /**
     * 第三方任务主键
     */
    private String thirdRecordId;
}
