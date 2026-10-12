package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 巡检记录
 * </p>
 *
 * @author zwb
 * @since 2021-03-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("fireinsp_record")
public class FireinspRecordPO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 计划ID
     */
    private String planId;

    /**
     * 计划名称
     */
    private String planName;

    /**
     * 巡检点ID
     */
    private String pointId;

    /**
     * 巡检点名称
     */
    private String pointName;

    /**
     * 点位置
     */
    private String pointAddress;

    /**
     * 区域ID1
     */
    private String areaId1;

    /**
     * 区域ID2
     */
    private String areaId2;

    /**
     * 区域ID3
     */
    private String areaId3;

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 档位ID
     */
    private String storeId;

    /**
     * 对象类型
     */
    private String targetType;

    /**
     * 对象ID
     */
    private String targetId;

    /**
     * 档位或设备名
     */
    private String targetName;

    /**
     * 对象子类类型
     */
    private String targetCategory;

    /**
     * 编号
     */
    private String code;

    /**
     * 巡检人ID
     */
    private String userId;

    /**
     * 巡检人名
     */
    private String username;

    /**
     * 巡检人昵称
     */
    private String nickname;

    /**
     * 部门ID
     */
    private String deptId;

    /**
     * 部门名
     */
    private String deptName;

    /**
     * 巡检时间
     */
    private LocalDateTime time;

    /**
     * 关联诉求ID
     */
    private String appealId;

    /**
     * gis经度
     */
    private String gisLongitude;

    /**
     * gis纬度
     */
    private String gisLatitude;

    /**
     * 结果
     */
    private String result;

    /**
     * 状态
     */
    private String status;

    /**
     * 巡检说明
     */
    private String remark;

    /**
     * json数据
     */
    private String data;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}
