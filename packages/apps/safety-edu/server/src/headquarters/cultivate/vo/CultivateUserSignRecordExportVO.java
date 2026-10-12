package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.enumeration.TrainModeEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.enumeration.TrainSignEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.guhecloud.platform.base.util.StringUtils;

import java.time.LocalDateTime;

/**
 * 用户培训签到记录
 *
 * @author lanchun
 * @date 2024/4/12 9:27
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ContentRowHeight(21)
@HeadRowHeight(30)
@ColumnWidth(20)
@ExcelIgnoreUnannotated
public class CultivateUserSignRecordExportVO {

    private Long id;

    /**
     * 用户名称
     */
    @ExcelProperty(value = { "人员姓名" }, index = 0)
    private String name;

    /**
     * 租户名称
     */
    @ExcelProperty(value = { "所属市场" }, index = 1)
    private String marketName;

    /**
     * 部门名称
     */
    @ExcelProperty(value = { "所在部门" }, index = 2)
    private String departmentName;

    /**
     * 岗位名称
     */
    @ExcelProperty(value = { "岗位" }, index = 3)
    private String postName;

    /**
     * 租户编号
     */
    private Integer marketTenant;

    /**
     * 签到时间
     */
    @ExcelProperty(value = { "人员打卡时间" }, index = 4)
    private LocalDateTime signDate;

    /**
     * 签到状态
     */
    private String status;

    @ExcelProperty(value = { "学习状态" }, index = 5)
    private String statusName;

    /**
     * 是否迟到
     */
    private Boolean belated;

    /**
     * 任务名称
     */
    @ExcelProperty(value = { "培训任务" }, index = 6)
    private String taskName;

    /**
     * 主题名称
     */
    @ExcelProperty(value = { "培训主题" }, index = 7)
    private String themeName;

    /**
     * 培训形式(线上、线下)
     */
    private String cultivateMode;

    @ExcelProperty(value = { "培训形式" }, index = 8)
    private String cultivateModeName;

    /**
     * 负责人名称
     */
    @ExcelProperty(value = { "负责人" }, index = 9)
    private String chargeUserName;

    /**
     * 来源主键
     */
    private String sourceKey;

    /**
     * 培训开始时间
     */
    @ExcelProperty(value = { "任务开始时间" }, index = 10)
    private LocalDateTime startTime;

    /**
     * 培训结束时间
     */
    @ExcelProperty(value = { "任务截止时间" }, index = 11)
    private LocalDateTime endTime;

    public String getStatusName() {
        if (StringUtils.isNotBlank(this.status)) {
            return TrainSignEnum.valueOf(status).getDesc();
        }
        return null;
    }

    public String getCultivateModeName() {
        if (StringUtils.isNotBlank(this.cultivateMode)) {
            return TrainModeEnum.valueOf(cultivateMode).getDesc();
        }
        return null;
    }
}
