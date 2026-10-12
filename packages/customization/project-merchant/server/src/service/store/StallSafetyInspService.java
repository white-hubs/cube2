package com.guhecloud.cube.deliver.project.merchant.service.store;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.deliver.project.merchant.dto.store.StallSafetyInspDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.store.StallSafetyInsp;

import java.util.List;

/**
 * <p>
 * 档位安全生产巡检扣分记录 服务类
 * </p>
 *
 * @author zwb
 * @since 2021-12-23
 */
public interface StallSafetyInspService extends IService<StallSafetyInsp> {
    /**
     * 新增档位上的巡检记录，并且扣分
     * @param dtos 巡检记录DTO
     */
    void save(List<StallSafetyInspDTO> dtos);

    /**
     * 删除巡检记录
     * @param id  ID
     */
    void remove(String id);
}
