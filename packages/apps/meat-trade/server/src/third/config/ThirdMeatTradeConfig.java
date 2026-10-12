package com.guhecloud.cube.biz.meattrade.server.third.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.guhecloud.cube.base.dictionary.adapter.DataDictionaryAdapter;
import com.guhecloud.cube.base.dictionary.dto.DataDictionaryDTO;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramParam;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.List;

/**
 * 三方滑轨信息
 *
 * @author lanchun
 * @date 2025/1/23 9:40
 */
@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThirdMeatTradeConfig {

    /**
     * 基础地址
     */
    private String serverApiAddr;

    /**
     * 待理货批次接口
     */
    private String pendingPorkReportApiUrl;

    /**
     * 商户食安数据统计api地址
     */
    private String merchantSpotCheckStatApiUrl;

    /**
     * 检疫证明文件api地址
     */
    private String quarantineCertificateFileApiUrl;

    /**
     * 白条来货数据统计api地址
     */
    private String statFarmSupplyRankingApiUrl;

    /**
     * 食安数据统计api地址
     */
    private String statTestingRankingApiUrl;

    /**
     * 白条交易大户统计api地址
     */
    private String statSellersComparisonTopApiUrl;

    /**
     * 供货数据统计api地址
     */
    private String statSellersSupplySumApiUrl;

    /**
     * 交易数据统计api地址
     */
    private String statTransactionComparisonApiUrl;

    /**
     * 交易金额详细信息统计api地址
     */
    private String statTransactionComparisonAmountApiUrl;

    /**
     * 交易佣金详细信息统计api地址
     */
    private String statTransactionComparisonCommissionApiUrl;

    /**
     * 交易均价详细信息统计api地址
     */
    private String statTransactionComparisonAveragePricesApiUrl;

    /**
     * 实际均价详细信息统计api地址
     */
    private String statTransactionComparisonActualAveragePricesApiUrl;

    /**
     * 交易重量详细信息统计api地址
     */
    private String statTransactionComparisonWeightApiUrl;

    /**
     * 交易片数详细信息统计api地址
     */
    private String statTransactionComparisonSliceApiUrl;

    private static final String SUB_SYSTEM_CODE = "THIRD_SERVICE";

    private static final String KEY = "THIRD_SERVICE::meattrade_plugin_service";

    @JsonIgnore
    public static ThirdMeatTradeConfig getRequestConfig() {
        DataDictionaryAdapter dataDictionaryAdapter = ApplicationContextHolder.getBean(DataDictionaryAdapter.class);
        DataDictionaryaramParam dataDictionaryaramParam = new DataDictionaryaramParam();
        dataDictionaryaramParam.setItemCode(SUB_SYSTEM_CODE);
        dataDictionaryaramParam.setCode(KEY);
        List<DataDictionaryDTO> dataDictionaryByItem = dataDictionaryAdapter.getDataDictionaryByItem(dataDictionaryaramParam);
        if (CollectionUtils.isEmpty(dataDictionaryByItem)) {
            log.warn("third_meattrade_plugin_config is null");
            return null;
        }
        return JsonUtils.deserialize(dataDictionaryByItem.get(0)
                                             .getVariable(), com.guhecloud.cube.biz.meattrade.server.third.config.ThirdMeatTradeConfig.class);
    }
}
