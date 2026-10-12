package com.guhecloud.cube.srv.gen.loose.works.dto.request;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

/**
 * @author wql
 * @describe 统计面板查询参数
 * @date 2023/9/18 17:49
 */
@Data
public class ProjectStatisticsQuery {
    private Period period;
    private Long startUser;
    private String keyword;
}
