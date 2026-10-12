package com.guhecloud.cube.biz.meattrade.server.third.api;

import com.guhecloud.cube.biz.meattrade.server.third.request.GetPendingTalliesRequest;
import com.guhecloud.cube.biz.meattrade.server.third.request.MerchantSpotCheckStatRequest;
import com.guhecloud.cube.biz.meattrade.server.third.request.TallyQuarantineTicketFileRequest;
import com.guhecloud.cube.biz.meattrade.server.third.response.GetPendingTalliesResponse;
import com.guhecloud.cube.biz.meattrade.server.third.response.MerchantSpotCheckStatResponse;
import com.guhecloud.cube.biz.meattrade.server.third.response.TallQuarantineTicketFileResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class ThirdMeatTradeApiService {

    /**
     * 获取第三方未完成的理货批次与数据
     *
     * @param param 第三方批次信息
     *
     * @return 批次结果
     */
    public List<GetPendingTalliesResponse> getPendingTallies(Map<String, String> param) {
        var request = new GetPendingTalliesRequest();
        request.setParam(param.get("param"));
        var response = request.requester().exchange(
                request.getUrl(),
                HttpMethod.GET,
                request.getHttpEntity(request),
                new ParameterizedTypeReference<List<GetPendingTalliesResponse>>() {
                },
                request.getParamForGet());
        log.debug("获取到第三方滑轨未完成的理货批次与数据响应信息{}", response);
        Assert.isTrue(Objects.nonNull(response.getBody()), "获取第三方滑轨未完成的理货批次与数据失败");
        return response.getBody();
    }

    /**
     * 获取第三方商户食安统计数据
     *
     * @param merchantId 商户id
     *
     * @return MerchantSpotCheckStatResponse
     */
    public MerchantSpotCheckStatResponse getMerchantSpotCheckStat(String merchantId) {
        var request = new MerchantSpotCheckStatRequest();
        request.setMerchantId(merchantId);
        var response = request.requester().exchange(
                request.getUrl(),
                HttpMethod.GET,
                request.getHttpEntity(request),
                new ParameterizedTypeReference<MerchantSpotCheckStatResponse>() {},
                request.getParamForGet());
        log.debug("获取到第三方按客户统计食安数据响应信息{}", response);
        Assert.isTrue(Objects.nonNull(response.getBody()), "获取到第三方按客户统计食安数据失败");
        return response.getBody();
    }

    /**
     * 获取第三方批次检疫证明文件
     *
     * @param tallyId 批次id
     *
     * @return TallQuarantineTicketFileResponse
     */
    public List<TallQuarantineTicketFileResponse> getTallQuarantineTicketFile(String tallyId) {
        var request = new TallyQuarantineTicketFileRequest();
        request.setTallyId(tallyId);
        var response = request.requester().exchange(
                request.getUrl(),
                HttpMethod.GET,
                request.getHttpEntity(request),
                new ParameterizedTypeReference<List<TallQuarantineTicketFileResponse>>() {},
                request.getParamForGet());
        log.debug("获取到第三方批次检疫证明数据响应信息{}", response);
        Assert.isTrue(Objects.nonNull(response.getBody()), "获取到第三方批次检疫证明数据失败");
        return response.getBody();
    }
}
