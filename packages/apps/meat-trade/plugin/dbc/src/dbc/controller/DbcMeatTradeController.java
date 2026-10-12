package com.guhecloud.cube.sn.meattrade.plugin.dbc.controller;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.sn.meattrade.plugin.common.entity.ThirdPartyGoodsMapping;
import com.guhecloud.cube.sn.meattrade.plugin.common.enumeration.PorkArrangePushDataStatusEnum;
import com.guhecloud.cube.sn.meattrade.plugin.common.service.PorkArrangePushDataService;
import com.guhecloud.cube.sn.meattrade.plugin.common.service.impl.SnThirdPartyGoodsMappingQueryService;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.DbcMeatTradeApiService;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.response.GetPendingTalliesResponse;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.response.TallQuarantineTicketFileResponse;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.repository.po.MerchantSpotCheckStatPO;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.repository.service.MerchantSpotCheckStatService;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.vo.MerchantSpotCheckStatisticsVO;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.vo.TallQuarantineTicketFileVO;
import com.guhecloud.platform.base.util.CollectionUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DbcMeatTradeController {

    private final DbcMeatTradeApiService dbcMeatTradeApiService;

    private final SnThirdPartyGoodsMappingQueryService thirdPartyGoodsMappingQueryService;

    private final PorkArrangePushDataService porkArrangePushDataService;

    private final MerchantSpotCheckStatService merchantSpotCheckStatService;

    /**
     * 大白菜获取报备批次信息
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/dbc/meat-trade/getPendingTallies")
    public List<GetPendingTalliesResponse> getPendingTallies(@RequestParam String param) {
        List<GetPendingTalliesResponse> pendingTallies = this.dbcMeatTradeApiService.getPendingTallies(param);
        extendPendingTallies(pendingTallies);
        return pendingTallies;
    }

    /**
     * 文件预览
     *
     * @param filePath 图片地址
     * @param response 输出
     */
    @GetMapping("/dbc/meat-trade/preview/file")
    public void previewFile(@RequestParam("filePath") String filePath, HttpServletResponse response) {
        this.dbcMeatTradeApiService.previewFile(filePath, response);
    }

    /**
     * 获取商户食安数据统计
     *
     * @return
     */
    @GetMapping("/dbc/meat-trade/merchant/spot-check/statistics")
    public MerchantSpotCheckStatisticsVO getMerchantSpotCheckStatistics(@RequestParam String merchantId) {
        MerchantSpotCheckStatPO merchantSpotCheckStat = merchantSpotCheckStatService.getMerchantSpotCheckStat(merchantId);
        if (Objects.isNull(merchantSpotCheckStat)) {
            return null;
        }
        return MerchantSpotCheckStatisticsVO.builder()
                .passQty(merchantSpotCheckStat.getPassQty())
                .totalQty(merchantSpotCheckStat.getTotalQty())
                .passRate(merchantSpotCheckStat.getPassRate())
                .build();
    }

    /**
     * 获取批次检疫证明文件列表
     *
     * @param tallyId
     *
     * @return
     */
    @GetMapping("/dbc/meat-trade/tally/files")
    public List<TallQuarantineTicketFileVO> getTallyQuarantineTicketFiles(@RequestParam String tallyId) {
        List<TallQuarantineTicketFileResponse> tallQuarantineTicketFiles = this.dbcMeatTradeApiService.getTallQuarantineTicketFiles(tallyId);
        if (CollectionUtils.isEmpty(tallQuarantineTicketFiles)) {
            return Collections.emptyList();
        }
        return tallQuarantineTicketFiles.stream()
                .map(response ->
                             TallQuarantineTicketFileVO.builder()
                                     .fileUrl("/api/sn-meattrade-plugin/dbc/meat-trade/preview/file?filePath=" + response.getFileUrl())
                                     .fileName(response.getFileName())
                                     .build()
                    ).collect(Collectors.toList());
    }

    /**
     * 大白菜理货数据推送状态重置
     *
     * @author lanchun
     * @date 2024/12/2 13:57
     */
    @PutMapping("/dbc/pork-arrange-push/status/reset")
    public void resetTallyPushStatus(@RequestBody Set<String> tallyIds) {
        this.porkArrangePushDataService.updatePushStatusByTallIds(tallyIds,
                                                                  PorkArrangePushDataStatusEnum.UN_PUSH,
                                                                  0);
    }

    /**
     * 模拟生成大白菜报备批次信息
     *
     * @return
     */
    @GetMapping("/mock/dbc/meat-trade/getPendingTallies")
    public List<GetPendingTalliesResponse> mockGetPendingTallies(@RequestParam String param) {
        GetPendingTalliesResponse pendingTalliesResponse = new GetPendingTalliesResponse();
        pendingTalliesResponse.setMerchantId(param);
        pendingTalliesResponse.setMerchantName(param);
        pendingTalliesResponse.setPhone(param);
        pendingTalliesResponse.setProductId("070312");
        GetPendingTalliesResponse.Tally tally = new GetPendingTalliesResponse.Tally();
        tally.setTallyId(IdWorker.getIdStr());
        tally.setReportedCount(100);
        tally.setReportedWeight(BigDecimal.valueOf(1100.00));
        tally.setGoodsCategoryId("1697137643902545922");
        tally.setGoodsCategoryName("猪白条");
        pendingTalliesResponse.setBatches(Collections.singletonList(tally));
        return Collections.singletonList(pendingTalliesResponse);
    }

    private void extendPendingTallies(List<GetPendingTalliesResponse> pendingTallies) {
        pendingTallies.forEach(pendingTally -> pendingTally.getBatches().forEach(tally -> {
            tally.setGoodsCategoryName(null);
            Optional<ThirdPartyGoodsMapping> thirdPartyGoodsMapping = thirdPartyGoodsMappingQueryService.convertThirdPartyGoodsToStander(tally.getGoodsCategoryId());
            if (!thirdPartyGoodsMapping.isPresent()) {
                tally.setGoodsCategoryId(null);
                log.warn("获取大白菜商品品类数据为空,商品品类为{}", tally.getGoodsCategoryId());
            } else {
                tally.setGoodsCategoryId(thirdPartyGoodsMapping.get().getGoodsCategoryId());
            }
        }));
    }
}
