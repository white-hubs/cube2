package com.guhecloud.cube.sn.meattrade.plugin.dbc.api;

import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.request.GetPendingTalliesRequest;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.request.MerchantSpotCheckStatisticsRequest;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.request.PreviewFileRequest;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.request.SetTallyDataRequest;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.request.TallQuarantineTicketFileRequest;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.response.BaseResponse;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.response.GetPendingTalliesResponse;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.response.MerchantSpotCheckStatisticsResponse;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.response.SetTallyDataResponse;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.response.TallQuarantineTicketFileResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;

/**
 * 大白菜滑轨接口服务
 *
 * @author lanchun
 * @date 2024/9/29 16:19
 */
@Slf4j
@Service
public class DbcMeatTradeApiService {

    /**
     * 获取未完成的理货批次与数据
     *
     * @param tallyId 批次号
     *
     * @return 批次结果
     */
    public List<GetPendingTalliesResponse> getPendingTallies(String tallyId) {
        GetPendingTalliesRequest request = new GetPendingTalliesRequest();
        request.setTallyId(tallyId);
        ResponseEntity<BaseResponse<List<GetPendingTalliesResponse>>> response = request.requester().exchange(
                request.getUrl(),
                HttpMethod.GET,
                request.getHttpEntity(request),
                new ParameterizedTypeReference<BaseResponse<List<GetPendingTalliesResponse>>>() {
                },
                request.getParamForGet());
        log.debug("获取到大白菜滑轨未完成的理货批次与数据响应信息{}", response);
        Assert.isTrue(Objects.nonNull(response.getBody()), "获取大白菜滑轨未完成的理货批次与数据失败");
        return response.getBody().getData();
    }

    /**
     * 上报理货数据
     *
     * @return BaseResponse
     */
    public BaseResponse<SetTallyDataResponse> setTallyData(SetTallyDataRequest setTallyDataRequest) {
        ResponseEntity<BaseResponse<SetTallyDataResponse>> response = setTallyDataRequest.requester().exchange(
                setTallyDataRequest.getUrl(),
                HttpMethod.POST,
                setTallyDataRequest.getHttpEntity(setTallyDataRequest),
                new ParameterizedTypeReference<BaseResponse<SetTallyDataResponse>>() {
                });
        log.info("上报大白菜滑轨理货数据响应信息{}, request={}", response, setTallyDataRequest.getTallyId());
        if (Objects.isNull(response.getBody())) {
            log.error("上报大白菜理货数据失败, request={}", setTallyDataRequest.getTallyId());
        }
        if (1 != response.getBody().getCode()) {
            log.error("上报大白菜滑轨理货失败,响应数据:{}, request={}", response.getBody(), setTallyDataRequest.getTallyId());
        }
        return response.getBody();
    }

    /**
     * 批次食安抽检附件信息
     *
     * @param tallyId 批次id
     *
     * @return TallQuarantineTicketFileResponse
     */
    public List<TallQuarantineTicketFileResponse> getTallQuarantineTicketFiles(String tallyId) {
        TallQuarantineTicketFileRequest request = new TallQuarantineTicketFileRequest();
        request.setTallyId(tallyId);
        ResponseEntity<BaseResponse<List<TallQuarantineTicketFileResponse>>> response = request.requester().exchange(
                request.getUrl(),
                HttpMethod.GET,
                request.getHttpEntity(request),
                new ParameterizedTypeReference<BaseResponse<List<TallQuarantineTicketFileResponse>>>() {
                },
                request.getParamForGet());
        log.debug("获取到大白菜批次检疫证明数据响应信息{}", response);
        Assert.isTrue(Objects.nonNull(response.getBody()), "获取到大白菜批次检疫证明数据失败");
        return response.getBody().getData();
    }

    /**
     * 食安数据统计-按客户
     *
     * @param merchantId 商户id
     *
     * @return MerchantSpotCheckStatisticsResponse
     */
    public MerchantSpotCheckStatisticsResponse getMerchantSpotCheckStatistics(String merchantId) {
        MerchantSpotCheckStatisticsRequest request = new MerchantSpotCheckStatisticsRequest();
        request.setMerchantId(merchantId);
        ResponseEntity<BaseResponse<MerchantSpotCheckStatisticsResponse>> response = request.requester().exchange(
                request.getUrl(),
                HttpMethod.GET,
                request.getHttpEntity(request),
                new ParameterizedTypeReference<BaseResponse<MerchantSpotCheckStatisticsResponse>>() {
                },
                request.getParamForGet());
        log.debug("获取到大白菜食安数据统计-按客户数据响应信息{}", response);
        Assert.isTrue(Objects.nonNull(response.getBody()), "获取到大白菜食安数据统计-按客户数据失败");
        return response.getBody().getData();
    }

    /**
     * 文件预览
     *
     * @param filePath 文件地址
     * @param response 输出
     */
    public void previewFile(String filePath, HttpServletResponse response) {
        PreviewFileRequest request = new PreviewFileRequest();
        request.setFileUrl(filePath);
        ResponseEntity<Resource> responseBody = request.requester().exchange(
                request.getUrl(),
                HttpMethod.GET,
                request.getHttpEntity(request),
                Resource.class,
                request.getParamForGet());
        // 检查响应状态码
        if (responseBody.getStatusCode() == HttpStatus.OK) {
            // 获取资源并设置响应头
            Resource resource = responseBody.getBody();
            if (resource != null) {
                // 使用流将资源写入响应
                try (InputStream inputStream = resource.getInputStream();
                     OutputStream outputStream = response.getOutputStream()) {
                    byte[] buffer = new byte[1024]; // 使用合适的缓冲区大小
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    outputStream.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            // 处理非200的响应状态码
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
