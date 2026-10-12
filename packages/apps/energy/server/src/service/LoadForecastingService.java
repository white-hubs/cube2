package com.guhecloud.cube.srv.gen.energymanagement.server.service;

import com.guhecloud.cube.srv.gen.energymanagement.server.param.LoadForecastingParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.LoadForecastingVO;

import java.util.List;

/**
 * @author wql
 * @describe 负荷预测
 * @date 2023/4/10 18:05
 */
public interface LoadForecastingService {
    /**
     * @Author wql
     * @Description 查询负荷预测
     * @Date 18:39 2023/4/10
     **/
    List<LoadForecastingVO> getLoadForecasting(LoadForecastingParam param);
}
