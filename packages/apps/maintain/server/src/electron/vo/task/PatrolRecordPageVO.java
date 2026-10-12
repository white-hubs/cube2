package com.guhecloud.cube.srv.gen.maintain.server.electron.vo.task;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * @author wql
 * @describe 巡更记录列表
 * @date 2023/5/29 18:11
 */
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatrolRecordPageVO {
    /**
     * 巡更任务id
     */
    @ExcelIgnore
    private Long id;
    /**
     * 巡更任务名称
     */
    @ExcelProperty(value = "任务名称")
    private String taskName;
    /**
     * 巡更人员姓名
     */
    @ExcelProperty(value = "巡更人员")
    private String userName;
    /**
     * 巡更人员部门
     */
    @ExcelProperty(value = "所属部门")
    private String userDepName;
    /**
     * 任务完成时间
     */
    @ExcelProperty(value = "任务完成时间")
    private LocalDateTime completeDate;
    /**
     * 巡更路线名称
     */
    @ExcelProperty(value = "巡更路线")
    private String routeName;
    /**
     * 巡更点数
     */
    @ExcelProperty(value = "巡更点数")
    private Integer patrolLocationNum;
    /**
     * 已巡点数
     */
    @ExcelProperty(value = "已巡")
    private Integer completedNum;
    /**
     * 漏巡点数
     */
    @ExcelProperty(value = "漏巡")
    private Integer undoneNum;
}
