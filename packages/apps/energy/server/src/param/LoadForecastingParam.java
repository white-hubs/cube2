package com.guhecloud.cube.srv.gen.energymanagement.server.param;

import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.TreeIds;
import lombok.Data;

import java.util.List;

/**
 * @author wql
 * @describe 负荷预测请求参数
 * @date 2023/4/10 19:47
 */
@Data
public class LoadForecastingParam {
    private String filterFlag;
    private String timeFlag;
    private List<String> ids;
    private List<TreeIds> treeIds;
    private String instrumentType;
    private String period;
}
