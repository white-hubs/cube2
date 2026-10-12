package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.interfaces.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.UserExamStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 用户考试记录返回参数
 *
 * @author lanchun
 * @date 2024/4/11 15:23
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ContentRowHeight(21)
@HeadRowHeight(30)
@ColumnWidth(20)
@ExcelIgnoreUnannotated
public class ExamTaskUserRecordExportVO {

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
     * 开始时间
     */
    @ExcelProperty(value = { "开始考试时间" }, index = 4)
    private LocalDateTime userExamStartTime;

    /**
     * 结束时间
     */
    @ExcelProperty(value = { "试卷提交时间" }, index = 5)
    private LocalDateTime userExamEndTime;

    /**
     * 考试耗时(秒数)
     */
    private Integer duration;

    /**
     * 翻译后的考试时长
     */
    @ExcelProperty(value = { "考试时长" }, index = 6)
    private String translateDurationName;

    /**
     * 分数
     */
    @ExcelProperty(value = { "考试分数" }, index = 7)
    private Float score;

    /**
     * 考试状态
     */
    private String status;

    /**
     * 出勤状态名称
     */
    @ExcelProperty(value = { "出勤状态" }, index = 8)
    private String statusName;

    /**
     * 是否通过
     */
    private Boolean passed;

    /**
     * 是否通过翻译值
     */
    @ExcelProperty(value = { "考试状态" }, index = 9)
    private String passedName;

    /**
     * 任务名称
     */
    @ExcelProperty(value = { "考试任务" }, index = 10)
    private String taskName;

    /**
     * 来源主键
     */
    private String sourceKey;

    /**
     * 主题名称
     */
    @ExcelProperty(value = { "考试主题" }, index = 11)
    private String themeName;

    /**
     * 负责人名称
     */
    @ExcelProperty(value = { "负责人" }, index = 12)
    private String chargeUserName;

    /**
     * 总分
     */
    @ExcelProperty(value = { "总分" }, index = 13)
    private Float paperScore;

    /**
     * 及格分
     */
    @ExcelProperty(value = { "及格分" }, index = 14)
    private Float paperGradeScore;

    /**
     * 任务开始时间
     */
    @ExcelProperty(value = { "任务开始时间" }, index = 15)
    private LocalDateTime examStartTime;

    /**
     * 任务结束时间
     */
    @ExcelProperty(value = { "任务结束时间" }, index = 16)
    private LocalDateTime examEndTime;

    public String getTranslateDurationName() {
        if (Objects.nonNull(duration) && duration > 0) {
            long nd = 24L * 60L * 60L;
            long nh = 60L * 60L;
            long nm = 60;
            // 计算差多少小时
            long hour = duration % nd / nh;
            // 计算差多少分钟
            long min = duration % nd % nh / nm;
            return hour + "小时" + min + "分";
        }
        return null;
    }

    public String getStatusName() {
        if (Objects.nonNull(status)) {
            return UserExamStatusEnum.valueOf(status).getDescription();
        }
        return null;
    }

    public String getPassedName() {
        if (Objects.nonNull(passed) && Objects.equals(Boolean.TRUE, passed)) {
            return "及格";
        }
        return "不及格";
    }
}
