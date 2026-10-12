package com.guhecloud.cube.deliver.project.merchant.controller;

import com.guhecloud.cube.deliver.project.merchant.dto.DeliverPeriod;
import com.guhecloud.cube.deliver.project.merchant.dto.KeyValueDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.MerchantCountDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.OperatingPriceDTO;
import com.guhecloud.cube.deliver.project.merchant.service.VisualizationService;

import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 商户管理可视化
 *
 * @Author: lijie
 * @CreateTime: 2023-09-15  10:06
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VisualizationController {
    private final VisualizationService visualizationService;
    /**
     * 商户/档位统计
     *
     * @author: lijie
     * @date: 2023/9/15 10:33
     * @param:
     * @return:
     **/
    @GetMapping("/visualization/count")
    public MerchantCountDTO count() {
        return visualizationService.count();
    }

    /**
     * 安全生产值排名
     *
     * @author: lijie
     * @date: 2023/9/15 10:33
     * @param:
     * @return:
     **/
    @GetMapping("/visualization/safeProduction/ranking")
    public Pagination<KeyValueDTO> safeProductionByRanking(DeliverPeriod period) {
        return visualizationService.safeProductionByRanking(period);
    }

    /**
     * 安全生产值占比
     *
     * @author: lijie
     * @date: 2023/9/15 10:33
     * @param: void
     * @return: []
     **/
    @GetMapping("/visualization/safeProduction/proportion")
    public List<KeyValueDTO> safeProductionByProportion(DeliverPeriod period) {
        return visualizationService.safeProductionByProportion(period);
    }

    /**
     * 问卷调查统计
     *
     * @author: lijie
     * @date: 2023/9/15 10:33
     * @param: void
     * @return: []
     **/
    @GetMapping("/visualization/questionnaire")
    public List<KeyValueDTO> getQuestionnaire(DeliverPeriod period) {
        return visualizationService.getQuestionnaire(period);
    }

    /**
     * 商户清单
     *
     * @author: lijie
     * @date: 2023/9/15 10:33
     * @param: void
     * @return: []
     **/
    @GetMapping("/visualization/wholesalerAll")
    public List<KeyValueDTO> getwholesalerAll(DeliverPeriod period) {
        return visualizationService.getwholesalerAll(period);
    }

    /**
     * 诚信经营占比
     *
     * @author: lijie
     * @date: 2023/9/15 10:33
     * @param: void
     * @return: []
     **/
    @GetMapping("/visualization/operating/proportion")
    public List<KeyValueDTO> getOperatingProportion(DeliverPeriod period) {
        return visualizationService.getOperatingProportion(period);
    }

    /**
     * 诚信经营值统计
     *
     * @author: lijie
     * @date: 2023/9/15 10:33
     * @param: void
     * @return: []
     **/
    @GetMapping("/visualization/operating/price")
    public List<OperatingPriceDTO> getOperatingPrice() {
        return visualizationService.getOperatingPrice(null);
    }

    /**
     * 商户合同统计
     *
     * @author: lijie
     * @date: 2023/9/15 10:33
     * @param: void
     * @return: []
     **/
    @GetMapping("/visualization/contract/count")
    public List<KeyValueDTO> getContractCount(DeliverPeriod period) {
        return visualizationService.getContractCount(period);
    }
}
