package com.guhecloud.cube.biz.fusion.communicate.server.third.xunyi.api;

import com.guhecloud.cube.biz.fusion.communicate.server.third.xunyi.api.request.AllDeviceXunYiRequest;
import com.guhecloud.cube.biz.fusion.communicate.server.third.xunyi.api.request.CallRecordBySidXunYiRequest;
import com.guhecloud.cube.biz.fusion.communicate.server.third.xunyi.api.request.SaveXunYiAccountRequest;
import com.guhecloud.cube.biz.fusion.communicate.server.third.xunyi.api.response.XunYiAllDeviceResponse;
import com.guhecloud.cube.biz.fusion.communicate.server.third.xunyi.api.response.XunYiBaseResponseDTO;
import com.guhecloud.cube.biz.fusion.communicate.server.third.xunyi.api.response.XunYiFileResponseDTO;
import com.guhecloud.cube.biz.fusion.communicate.server.third.xunyi.api.response.XunYiPageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 讯一的API调用服务
 *
 * @author lanchun
 * @date 2024/7/30 15:08
 */
@Slf4j
@Service
public class XunYiApiService extends BaseHttp {

    /**
     * 根据记录ID获取文件信息
     *
     * @param id
     *
     * @returns
     */
    public XunYiFileResponseDTO getFileRecordById(String id) {
        CallRecordBySidXunYiRequest xunYiCallRecordBySidRequest = new CallRecordBySidXunYiRequest();
        xunYiCallRecordBySidRequest.setCid(id);
        ResponseEntity<XunYiFileResponseDTO> responseResponseEntity = requester().exchange(
                xunYiCallRecordBySidRequest.getUrl(),
                HttpMethod.POST,
                xunYiCallRecordBySidRequest.getHttpEntity(xunYiCallRecordBySidRequest),
                new ParameterizedTypeReference<XunYiFileResponseDTO>() {
                });
        log.debug("获取到的讯一文件响应信息{}", responseResponseEntity);
        return responseResponseEntity.getBody();
    }

    /**
     * 获取所有设备(账号)
     *
     * @return
     */
    public List<XunYiAllDeviceResponse> getAllDevices() {
        AllDeviceXunYiRequest xunYiAllDeviceRequest = new AllDeviceXunYiRequest();
        xunYiAllDeviceRequest.setCurPage(1);
        xunYiAllDeviceRequest.setPageRows(1000);
        ResponseEntity<XunYiPageResponse<XunYiAllDeviceResponse>> responseResponseEntity = requester().exchange(
                xunYiAllDeviceRequest.getUrl(),
                HttpMethod.GET,
                xunYiAllDeviceRequest.getHttpEntity(xunYiAllDeviceRequest),
                new ParameterizedTypeReference<XunYiPageResponse<XunYiAllDeviceResponse>>() {
                },
                xunYiAllDeviceRequest.getParamForGet());
        if (Objects.isNull(responseResponseEntity.getBody())) {
            return Collections.emptyList();
        }
        log.debug("获取到的讯一设备响应信息{}", responseResponseEntity);
        XunYiPageResponse<XunYiAllDeviceResponse> body = responseResponseEntity.getBody();
        if (Objects.nonNull(body) && Objects.nonNull(body.getPage())) {
            return body.getPage().getDataList();
        }
        return Collections.emptyList();

    }

    /**
     * 保存账号信息
     *
     * @param xunYiAccountRequest
     *
     * @return
     */
    public XunYiBaseResponseDTO saveEmployeeInfo(SaveXunYiAccountRequest xunYiAccountRequest) {
        ResponseEntity<XunYiBaseResponseDTO> responseResponseEntity = requester().exchange(
                xunYiAccountRequest.getUrl(),
                HttpMethod.POST,
                xunYiAccountRequest.getHttpEntity(xunYiAccountRequest.getAccount()),
                new ParameterizedTypeReference<XunYiBaseResponseDTO>() {
                });
        log.debug("更新讯一设备响应信息{}", responseResponseEntity);
        return responseResponseEntity.getBody();
    }
}
