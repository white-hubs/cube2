package com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.PatrolTaskLocation;
import lombok.Data;

import java.util.List;

/**
 * @author wql
 * @describe 巡更人员统计
 * @date 2023/5/30 14:40
 */
@Data
public class PatrolRouteStatisticsVO {
    /**
     * 巡更路线
     */
    @ExcelProperty(value = "巡更路线")
    private String patrolRouteName;
    /**
     * 包含巡更点
     */
    @ExcelProperty(value = "包含巡更点")
    private Integer patrolLocationNumber;
    /**
     * 巡更点总数
     */
    @ExcelProperty(value = "应巡查的点位次数")
    private Integer totalNum;
    /**
     * 已巡更点数
     */
    @ExcelProperty(value = "实际巡查的点位次数")
    private Integer realNum = 0;
    /**
     * 漏巡点数
     */
    @ExcelProperty(value = "漏巡")
    private Integer omitNum = 0;
    /**
     * 巡更率
     */
    @ExcelProperty(value = "路线完成率")
    private String patrolRatio;
    @ExcelIgnore
    List<PatrolTaskLocation> taskLocations;
}
