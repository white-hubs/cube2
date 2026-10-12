package com.guhecloud.cube.srv.gen.maintain.server.electron.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author wql
 * @describe 巡更统计查询参数
 * @date 2023/5/30 15:05
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticParam extends Paging {
    /**
     * 巡更时间
     */
    private Period period;
    /**
     * 巡更区域
     */
    private String areaId;
    /**
     * 部门id
     */
    private Long depId;
    /**
     * 关键字
     */
    private String keyword;

    private Integer resultSize;
    private String exceptionType;

    /**
     * 巡更结果
     */
    private String result;

    /**
     * 权限任务Id
     */

    private List<Long> taskIds;
}
