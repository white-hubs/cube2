package com.guhecloud.cube.sn.meattrade.plugin.dbc.api;

import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.request.statistics.GetMeatFarmSupplyRankingPageRequest;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.request.statistics.GetMeatTestingPageRequest;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.request.statistics.GetMeatTestingRankingRequest;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.request.statistics.GetSellerSupplyStatsSumRequest;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.request.statistics.GetTopSellersComparisonPageRequest;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.request.statistics.GetTransactionComparisonPageRequest;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.request.statistics.GetTransactionComparisonRequest;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.response.BasePageResponse;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.response.BaseResponse;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.response.statistics.GetMeatFarmSupplyRankingPageResponse;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.response.statistics.GetMeatTestingPageResponse;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.response.statistics.GetMeatTestingRankingResponse;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.response.statistics.GetSellerSupplyStatsSumResponse;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.response.statistics.GetTopSellersComparisonPageResponse;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.response.statistics.GetTransactionComparisonPageResponse;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.response.statistics.GetTransactionComparisonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class DbcMeatTradeStatisticsApiService {

    /**
     * 获取交易量统计信息
     *
     * @param request
     *
     * @return
     */
    public GetTransactionComparisonResponse getTransactionComparisonRequest(GetTransactionComparisonRequest request) {
        ResponseEntity<BaseResponse<GetTransactionComparisonResponse>> response = request.requester().exchange(
                request.getUrl(),
                HttpMethod.POST,
                request.getHttpEntity(request),
                new ParameterizedTypeReference<BaseResponse<GetTransactionComparisonResponse>>() {
                },
                request.getParamForGet());
        log.debug("获取到大白菜滑轨交易量统计数据响应信息{}", response);
        Assert.isTrue(Objects.nonNull(response.getBody()), "获取大白菜滑轨交易量统计数据为空");
        return response.getBody().getData();
    }

    /**
     * 获取交易量分页详情统计信息
     *
     * @param request
     *
     * @return
     */
    public BasePageResponse<GetTransactionComparisonPageResponse> getTransactionComparisonPage(GetTransactionComparisonPageRequest request
                                                                                              ) {
        ResponseEntity<BaseResponse<BasePageResponse<GetTransactionComparisonPageResponse>>> response = request.requester().exchange(
                request.getUrl(),
                HttpMethod.POST,
                request.getHttpEntity(request),
                new ParameterizedTypeReference<BaseResponse<BasePageResponse<GetTransactionComparisonPageResponse>>>() {
                },
                request.getParamForGet());
        log.debug("获取到大白菜滑轨交易量分页详情统计数据响应信息{}", response);
        Assert.isTrue(Objects.nonNull(response.getBody()), "获取大白菜滑轨交易量分页详情统计数据为空");
        return response.getBody().getData();
    }

    /**
     * 获取销售者供货量统计信息
     *
     * @param request
     *
     * @return
     */
    public GetSellerSupplyStatsSumResponse getSellerSupplyStatsSum(GetSellerSupplyStatsSumRequest request) {
        ResponseEntity<BaseResponse<GetSellerSupplyStatsSumResponse>> response = request.requester().exchange(
                request.getUrl(),
                HttpMethod.POST,
                request.getHttpEntity(request),
                new ParameterizedTypeReference<BaseResponse<GetSellerSupplyStatsSumResponse>>() {
                },
                request.getParamForGet());
        log.debug("获取到大白菜滑轨销售者供货量统计数据响应信息{}", response);
        Assert.isTrue(Objects.nonNull(response.getBody()), "获取大白菜滑轨销售者供货量统计数据为空");
        return response.getBody().getData();
    }

    /**
     * 获取白条交易大户排行分页信息
     *
     * @param request
     *
     * @return
     */
    public BasePageResponse<GetTopSellersComparisonPageResponse> getTopSellersComparisonPageResponseBasePage(
            GetTopSellersComparisonPageRequest request
                                                                                                            ) {
        ResponseEntity<BaseResponse<BasePageResponse<GetTopSellersComparisonPageResponse>>> response = request.requester().exchange(
                request.getUrl(),
                HttpMethod.POST,
                request.getHttpEntity(request),
                new ParameterizedTypeReference<BaseResponse<BasePageResponse<GetTopSellersComparisonPageResponse>>>() {
                },
                request.getParamForGet());
        log.debug("获取到大白菜滑轨白条交易大户排行分页数据响应信息{}", response);
        Assert.isTrue(Objects.nonNull(response.getBody()), "获取大白菜滑轨白条交易大户排行分页数据为空");
        return response.getBody().getData();
    }

    /**
     * 获取屠宰场供货排行分页信息
     *
     * @param request
     *
     * @return
     */
    public BasePageResponse<GetMeatFarmSupplyRankingPageResponse> getMeatFarmSupplyRankingPageResponseBasePage(
            GetMeatFarmSupplyRankingPageRequest request
                                                                                                              ) {
        ResponseEntity<BaseResponse<BasePageResponse<GetMeatFarmSupplyRankingPageResponse>>> response = request.requester().exchange(
                request.getUrl(),
                HttpMethod.POST,
                request.getHttpEntity(request),
                new ParameterizedTypeReference<BaseResponse<BasePageResponse<GetMeatFarmSupplyRankingPageResponse>>>() {
                },
                request.getParamForGet());
        log.debug("获取到大白菜滑轨获取屠宰场供货排行分页数据响应信息{}", response);
        Assert.isTrue(Objects.nonNull(response.getBody()), "获取大白菜滑轨获取屠宰场供货排行分页数据为空");
        return response.getBody().getData();
    }

    /**
     * 获取检测数据
     *
     * @param request
     *
     * @return
     */
    public List<GetMeatTestingRankingResponse> getMeatTestingRankingList(GetMeatTestingRankingRequest request) {
        ResponseEntity<BaseResponse<List<GetMeatTestingRankingResponse>>> response = request.requester().exchange(
                request.getUrl(),
                HttpMethod.POST,
                request.getHttpEntity(request),
                new ParameterizedTypeReference<BaseResponse<List<GetMeatTestingRankingResponse>>>() {
                },
                request.getParamForGet());
        log.debug("获取到大白菜滑轨食安检测数据响应信息{}", response);
        Assert.isTrue(Objects.nonNull(response.getBody()), "获取大白菜滑轨食安检测数据为空");
        return response.getBody().getData();
    }

    /**
     * 获取检测数据列表
     *
     * @param request
     *
     * @return
     */
    public BasePageResponse<GetMeatTestingPageResponse> getMeatTestingRankingPage(GetMeatTestingPageRequest request) {
        ResponseEntity<BaseResponse<BasePageResponse<GetMeatTestingPageResponse>>> response = request.requester().exchange(
                request.getUrl(),
                HttpMethod.POST,
                request.getHttpEntity(request),
                new ParameterizedTypeReference<BaseResponse<BasePageResponse<GetMeatTestingPageResponse>>>() {
                },
                request.getParamForGet());
        log.debug("获取到大白菜滑轨食安检测数据列表响应信息{}", response);
        Assert.isTrue(Objects.nonNull(response.getBody()), "获取大白菜滑轨食安检测列表数据为空");
        return response.getBody().getData();
    }
}
