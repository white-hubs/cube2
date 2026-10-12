package com.guhecloud.cube.deliver.project.merchant.service;

import com.guhecloud.cube.deliver.project.merchant.dto.DeliverPeriod;
import com.guhecloud.cube.deliver.project.merchant.dto.KeyValueDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.MerchantCountDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.OperatingPriceDTO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2023-09-15  11:41
 * @Description:
 * @Version: 1.0
 */
public interface VisualizationService {
    MerchantCountDTO count();

    /**
     * 安全生产值排名
     *
     * @author: lijie
     * @date: 2023/9/15 10:33
     * @param:
     * @return:
     **/
    Pagination<KeyValueDTO> safeProductionByRanking(DeliverPeriod period);

    /**
     * 安全生产值占比
     *
     * @author: lijie
     * @date: 2023/9/15 10:33
     * @param: void
     * @return: []
     **/
    List<KeyValueDTO> safeProductionByProportion(DeliverPeriod period);

    /**
     * 问卷调查统计
     *
     * @author: lijie
     * @date: 2023/9/15 10:33
     * @param: void
     * @return: []
     **/
    List<KeyValueDTO> getQuestionnaire(DeliverPeriod period);

    /**
     * 商户清单
     *
     * @author: lijie
     * @date: 2023/9/15 10:33
     * @param: void
     * @return: []
     **/
    List<KeyValueDTO> getwholesalerAll(DeliverPeriod period);

    /**
     * 诚信经营占比
     *
     * @author: lijie
     * @date: 2023/9/15 10:33
     * @param: void
     * @return: []
     **/
    List<KeyValueDTO> getOperatingProportion(DeliverPeriod period);

    /**
     * 诚信经营值统计
     *
     * @author: lijie
     * @date: 2023/9/15 10:33
     * @param: void
     * @return: []
     **/
    List<OperatingPriceDTO> getOperatingPrice(DeliverPeriod period);

    /**
     * 商户合同统计
     *
     * @author: lijie
     * @date: 2023/9/15 10:33
     * @param: void
     * @return: []
     **/
    List<KeyValueDTO> getContractCount(DeliverPeriod period);
}
