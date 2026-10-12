package com.guhecloud.cube.biz.meattrade.server.third.api;

import com.guhecloud.cube.biz.meattrade.server.pork.param.StatQueryParam;
import com.guhecloud.cube.biz.meattrade.server.third.request.statistics.FarmSupplyRankingStatRequest;
import com.guhecloud.cube.biz.meattrade.server.third.request.statistics.SellersComparisonTopStatRequest;
import com.guhecloud.cube.biz.meattrade.server.third.request.statistics.SellersSupplySumStatRequest;
import com.guhecloud.cube.biz.meattrade.server.third.request.statistics.TestingRankingStatRequest;
import com.guhecloud.cube.biz.meattrade.server.third.request.statistics.TransactionActualAveragePricesStatRequest;
import com.guhecloud.cube.biz.meattrade.server.third.request.statistics.TransactionAmountStatRequest;
import com.guhecloud.cube.biz.meattrade.server.third.request.statistics.TransactionAveragePricesStatRequest;
import com.guhecloud.cube.biz.meattrade.server.third.request.statistics.TransactionCommissionStatRequest;
import com.guhecloud.cube.biz.meattrade.server.third.request.statistics.TransactionSliceStatRequest;
import com.guhecloud.cube.biz.meattrade.server.third.request.statistics.TransactionStatRequest;
import com.guhecloud.cube.biz.meattrade.server.third.request.statistics.TransactionWeightStatRequest;
import com.guhecloud.cube.biz.meattrade.server.third.response.statistics.MeatFarmSupplyRankingStatResponse;
import com.guhecloud.cube.biz.meattrade.server.third.response.statistics.MeatTestingRankingStatResponse;
import com.guhecloud.cube.biz.meattrade.server.third.response.statistics.SellerSupplySumStatResponse;
import com.guhecloud.cube.biz.meattrade.server.third.response.statistics.SellersComparisonTopStatResponse;
import com.guhecloud.cube.biz.meattrade.server.third.response.statistics.SummaryAmountStatResponse;
import com.guhecloud.cube.biz.meattrade.server.third.response.statistics.SummaryAveragePriceStatResponse;
import com.guhecloud.cube.biz.meattrade.server.third.response.statistics.SummaryCommissionStatResponse;
import com.guhecloud.cube.biz.meattrade.server.third.response.statistics.SummarySlicesStatResponse;
import com.guhecloud.cube.biz.meattrade.server.third.response.statistics.SummaryWeightStatResponse;
import com.guhecloud.cube.biz.meattrade.server.third.response.statistics.TransactionComparisonStatResponse;
import com.guhecloud.platform.base.model.Pagination;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ThirdMeatTradeStatApiService {

    /**
     * 白条来货数据统计
     *
     * @param param 参数信息
     *
     * @return 白条来货数据统计
     */
    public Pagination<MeatFarmSupplyRankingStatResponse> getFarmSupplyRankingStat(StatQueryParam param) {
        FarmSupplyRankingStatRequest request = new FarmSupplyRankingStatRequest();
        request.setType(param.getType().name());
        request.setPageNumber(param.getPageNumber());
        request.setPageSize(param.getPageSize());
        ResponseEntity<Pagination<MeatFarmSupplyRankingStatResponse>> response = request.requester().exchange(
                request.getUrl(),
                HttpMethod.GET,
                request.getHttpEntity(request),
                new ParameterizedTypeReference<Pagination<MeatFarmSupplyRankingStatResponse>>() {
                },
                request.getParamForGet());
        return response.getBody();
    }

    /**
     * 食安数据统计
     *
     * @param param 参数信息
     *
     * @return MeatTestingRankingStatResponse
     */
    public MeatTestingRankingStatResponse getMeatTestingRankingStat(StatQueryParam param) {
        TestingRankingStatRequest request = new TestingRankingStatRequest();
        request.setType(param.getType().name());
        request.setPageNumber(param.getPageNumber());
        request.setPageSize(param.getPageSize());
        ResponseEntity<MeatTestingRankingStatResponse> response = request.requester().exchange(
                request.getUrl(),
                HttpMethod.GET,
                request.getHttpEntity(request),
                new ParameterizedTypeReference<MeatTestingRankingStatResponse>() {},
                request.getParamForGet());
        return response.getBody();
    }

    /**
     * 白条交易大户统计
     *
     * @param param 参数信息
     *
     * @return SellersComparisonTopStatResponse
     */
    public Pagination<SellersComparisonTopStatResponse> getSellersComparisonTopStatPage(StatQueryParam param) {
        SellersComparisonTopStatRequest request = new SellersComparisonTopStatRequest();
        request.setType(param.getType().name());
        request.setPageNumber(param.getPageNumber());
        request.setPageSize(param.getPageSize());
        ResponseEntity<Pagination<SellersComparisonTopStatResponse>> response = request.requester().exchange(
                request.getUrl(),
                HttpMethod.GET,
                request.getHttpEntity(request),
                new ParameterizedTypeReference<Pagination<SellersComparisonTopStatResponse>>() {},
                request.getParamForGet());
        return response.getBody();
    }

    /**
     * 供货数据统计
     *
     * @param param 参数信息
     *
     * @return SellerSupplySumStatResponse
     */
    public SellerSupplySumStatResponse getSellersSupplySumStat(StatQueryParam param) {
        SellersSupplySumStatRequest request = new SellersSupplySumStatRequest();
        request.setType(param.getType().name());
        request.setPageNumber(param.getPageNumber());
        request.setPageSize(param.getPageSize());
        ResponseEntity<SellerSupplySumStatResponse> response = request.requester().exchange(
                request.getUrl(),
                HttpMethod.GET,
                request.getHttpEntity(request),
                new ParameterizedTypeReference<SellerSupplySumStatResponse>() {},
                request.getParamForGet());
        return response.getBody();
    }

    /**
     * 交易数据统计
     *
     * @param param 参数信息
     *
     * @return TransactionComparisonStatResponse
     */
    public TransactionComparisonStatResponse getTransactionComparisonStat(StatQueryParam param) {
        TransactionStatRequest request = new TransactionStatRequest();
        request.setType(param.getType().name());
        request.setPageNumber(param.getPageNumber());
        request.setPageSize(param.getPageSize());
        ResponseEntity<TransactionComparisonStatResponse> response = request.requester().exchange(
                request.getUrl(),
                HttpMethod.GET,
                request.getHttpEntity(request),
                new ParameterizedTypeReference<TransactionComparisonStatResponse>() {},
                request.getParamForGet());
        return response.getBody();
    }

    /**
     * 交易金额详细信息统计
     *
     * @param param 参数信息
     *
     * @return SummaryAmountStatResponse
     */
    public SummaryAmountStatResponse getStatSummaryAmount(StatQueryParam param) {
        TransactionAmountStatRequest request = new TransactionAmountStatRequest();
        request.setType(param.getType().name());
        request.setPageNumber(param.getPageNumber());
        request.setPageSize(param.getPageSize());
        ResponseEntity<SummaryAmountStatResponse> response = request.requester().exchange(
                request.getUrl(),
                HttpMethod.GET,
                request.getHttpEntity(request),
                new ParameterizedTypeReference<SummaryAmountStatResponse>() {},
                request.getParamForGet());
        return response.getBody();
    }

    /**
     * 交易佣金详细信息统计
     *
     * @param param 参数信息
     *
     * @return SummaryCommissionStatResponse
     */
    public SummaryCommissionStatResponse getStatSummaryCommission(StatQueryParam param) {
        TransactionCommissionStatRequest request = new TransactionCommissionStatRequest();
        request.setType(param.getType().name());
        request.setPageNumber(param.getPageNumber());
        request.setPageSize(param.getPageSize());
        ResponseEntity<SummaryCommissionStatResponse> response = request.requester().exchange(
                request.getUrl(),
                HttpMethod.GET,
                request.getHttpEntity(request),
                new ParameterizedTypeReference<SummaryCommissionStatResponse>() {},
                request.getParamForGet());
        return response.getBody();
    }

    /**
     * 交易均价详细信息
     *
     * @param param 参数信息
     *
     * @return SummaryAveragePriceStatResponse
     */
    public SummaryAveragePriceStatResponse getStatSummaryAveragePrice(StatQueryParam param) {
        TransactionAveragePricesStatRequest request = new TransactionAveragePricesStatRequest();
        request.setType(param.getType().name());
        request.setPageNumber(param.getPageNumber());
        request.setPageSize(param.getPageSize());
        ResponseEntity<SummaryAveragePriceStatResponse> response = request.requester().exchange(
                request.getUrl(),
                HttpMethod.GET,
                request.getHttpEntity(request),
                new ParameterizedTypeReference<SummaryAveragePriceStatResponse>() {},
                request.getParamForGet());
        return response.getBody();
    }

    /**
     * 交易佣金详细信息统计
     *
     * @param param 参数信息
     *
     * @return SummaryAveragePriceStatResponse
     */
    public SummaryAveragePriceStatResponse getStatSummaryActualAveragePrice(StatQueryParam param) {
        TransactionActualAveragePricesStatRequest request = new TransactionActualAveragePricesStatRequest();
        request.setType(param.getType().name());
        request.setPageNumber(param.getPageNumber());
        request.setPageSize(param.getPageSize());
        ResponseEntity<SummaryAveragePriceStatResponse> response = request.requester().exchange(
                request.getUrl(),
                HttpMethod.GET,
                request.getHttpEntity(request),
                new ParameterizedTypeReference<SummaryAveragePriceStatResponse>() {},
                request.getParamForGet());
        return response.getBody();
    }

    /**
     * 交易重量详细信息
     *
     * @param param 参数信息
     *
     * @return SummaryWeightStatResponse
     */
    public SummaryWeightStatResponse getStatSummaryWeightDetail(StatQueryParam param) {
        TransactionWeightStatRequest request = new TransactionWeightStatRequest();
        request.setType(param.getType().name());
        request.setPageNumber(param.getPageNumber());
        request.setPageSize(param.getPageSize());
        ResponseEntity<SummaryWeightStatResponse> response = request.requester().exchange(
                request.getUrl(),
                HttpMethod.GET,
                request.getHttpEntity(request),
                new ParameterizedTypeReference<SummaryWeightStatResponse>() {},
                request.getParamForGet());
        return response.getBody();
    }

    /**
     * 交易片数详细信息
     *
     * @param param 参数信息
     *
     * @return SummarySlicesStatResponse
     */
    public SummarySlicesStatResponse getStatSummarySlices(StatQueryParam param) {
        TransactionSliceStatRequest request = new TransactionSliceStatRequest();
        request.setType(param.getType().name());
        request.setPageNumber(param.getPageNumber());
        request.setPageSize(param.getPageSize());
        ResponseEntity<SummarySlicesStatResponse> response = request.requester().exchange(
                request.getUrl(),
                HttpMethod.GET,
                request.getHttpEntity(request),
                new ParameterizedTypeReference<SummarySlicesStatResponse>() {},
                request.getParamForGet());
        return response.getBody();
    }
}
