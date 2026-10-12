package com.guhecloud.cube.biz.adapter.person.rollcall.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 人员清点记录VO
 *
 * @author xiaofeng
 * @date 2024/11/05 17:36
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonRollCallRecordVO {

    /**
     * 任务记录编号
     */
    private String recordCode;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 场所编号（保留字段，当前版本无数据）
     */
    private String areaIndexCode;

    /**
     * 场所名称（保留字段，当前版本无数据）
     */
    private String areaName;

    /**
     * 点名类别
     */
    private String category;

    /**
     * 点名类型
     */
    private String type;

    /**
     * 点名任务执行状态 0已结束1执行中2待执行
     */
    private Integer status;

    /**
     * 点名开始时间IOS8601格式yyyy-MMdd'T'HH:mm:ss.SSSzzz
     */
    private String startTime;

    /**
     * 点名结束时间IOS8601格式yyyy-MMdd'T'HH:mm:ss.SSSzzz
     */
    private String endTime;

    /**
     * 已点到人数
     */
    private Integer rollcallNum;

    /**
     * 未点到人数
     */
    private Integer unrollcallNum;

    /**
     * 特殊情况人数
     */
    private Integer specialNum;

    /**
     * 点名时长
     */
    private Integer consumingTime;

    /**
     * 进出方向
     */
    private String direction;

    /**
     * 任务备注
     */
    private String remark;

}
