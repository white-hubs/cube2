package com.guhecloud.cube.deliver.project.merchant.service.store;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.deliver.project.merchant.dto.store.StallScoreDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.store.StallScoreQueryDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.Space;
import com.guhecloud.cube.deliver.project.merchant.entity.store.StallSafetyMark;

import java.util.List;

/**
 * <p>
 * 档位安全生产值 服务类
 * </p>
 *
 * @author zwb
 * @since 2021-12-24
 */
public interface StallSafetyMarkService extends IService<StallSafetyMark> {

    public static final int STORE_DEFAULT_MARK = 100;

    /**
     * 传入档位ID，分数，还有记录条数
     * 修改对应的安全分记录表中的内容
     * @param storeId 档位ID
     * @param mark 分数
     * @param size 本次巡检记录的条数
     */
    void minusScore(String storeId, int mark, int size);

    StallScoreDTO getStallScore(Space space);

    List<StallScoreDTO> getStallScoreAndEvaluation(StallScoreQueryDTO queryDTO);

}
