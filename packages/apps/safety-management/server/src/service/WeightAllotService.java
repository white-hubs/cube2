package com.guhecloud.cube.srv.gen.safety.management.service;

import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.WeightAllotQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.WeightAllotVO;
import com.guhecloud.platform.base.model.Pagination;

/**
 * @author wql
 * @describe 权重分配
 * @date 2023/10/12 15:56
 */
public interface WeightAllotService {
    void saveWeightAllot(WeightAllotVO weightAllotVO);

    void deleteWeightAllot(Long id);

    Pagination<WeightAllotVO> weightAllotList(WeightAllotQuery query);

    WeightAllotVO weightAllotDetail(Long id);

    int getWeightAllotByItem(WeightAllotQuery query);
}
