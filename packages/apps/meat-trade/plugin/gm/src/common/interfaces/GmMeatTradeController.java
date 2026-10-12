package com.guhecloud.cube.gm.meattrade.plugin.common.interfaces;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.gm.meattrade.plugin.common.application.querygateway.ThirdPartyGoodsMappingQueryService;
import com.guhecloud.cube.gm.meattrade.plugin.common.domain.entity.ThirdPartyGoodsMapping;
import com.guhecloud.cube.gm.meattrade.plugin.common.interfaces.vo.GetPendingTalliesVO;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GmMeatTradeController {

    private final ThirdPartyGoodsMappingQueryService thirdPartyGoodsMappingQueryService;

    /**
     * 获取报备批次信息
     *
     * @param batchNo        批次号
     * @param enterpriseNo   企业编号
     * @param enterpriseName 企业名称
     * @param coldHotFresh   冷热鲜标识
     * @param goodsType      商品类型名称
     *
     * @return
     */
    @GetMapping("/gm/meat-trade/getPendingTallies")
    public List<GetPendingTalliesVO> getPendingTallies(@RequestParam String param) {
        try {
            Map<String, String> params = JsonUtils.deserialize(param, new TypeReference<>() {});
            var pendingTalliesVO = GetPendingTalliesVO.builder()
                    .merchantId(params.get("enterpriseNo"))
                    .merchantName(
                            StringUtils.isNotBlank(params.get("enterpriseName")) ? params.get("enterpriseName")
                                                                                 : params.get("enterpriseNo"))
                    .batches(buildTallies(params.get("batchNo"), params.get("coldHotFresh"), params.get("goodsType")))
                    .phone(params.get("enterpriseNo"))
                    .build();
            return Collections.singletonList(pendingTalliesVO);
        } catch (Exception e) {
            throw new ServiceException("二维码参数解析错误,无法获取报备批次信息");
        }
    }

    private List<GetPendingTalliesVO.Tally> buildTallies(String batchNo,
                                                         String coldHotFresh,
                                                         String goodsType
                                                        ) {
        Optional<ThirdPartyGoodsMapping> thirdPartyGoodOptional = thirdPartyGoodsMappingQueryService.convertThirdPartyGoodsToStander(coldHotFresh, goodsType);
        if (thirdPartyGoodOptional.isEmpty()) {
            log.warn("获取光明商品品类数据为空,冷热鲜标识为{},商品品类为{}", coldHotFresh, goodsType);
        }
        GetPendingTalliesVO.Tally tally = GetPendingTalliesVO.Tally.builder()
                .tallyId(batchNo)
                .build();
        thirdPartyGoodOptional.ifPresent(thirdPartyGood -> {
            tally.setGoodsCategoryId(thirdPartyGood.getGoodsCategoryId());
            tally.setFreshLabel(thirdPartyGood.getFreshLabel());
        });
        return Collections.singletonList(tally);
    }
}
