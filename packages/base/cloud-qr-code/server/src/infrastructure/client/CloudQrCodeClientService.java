package com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.client;

import com.guhecloud.cube.biz.ouaa.client.TenantClient;
import com.guhecloud.cube.biz.ouaa.client.dto.ClientDTO;
import com.guhecloud.cube.biz.ouaa.client.param.ClientQueryParam;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 云二维码客户端服务
 * 封装与租户客户端相关的远程调用
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CloudQrCodeClientService {

    private final TenantClient tenantClient;

    /**
     * 批量获取客户端名称映射
     *
     * @param clientIds 客户端编号集合
     *
     * @return code -> name 映射关系
     */
    public Map<String, String> getClientNameMap(Collection<String> clientIds) {
        if (CollectionUtils.isEmpty(clientIds)) {
            return Collections.emptyMap();
        }

        // 构建查询参数，查询所有客户端（不限制租户）
        ClientQueryParam queryParam = new ClientQueryParam();
        queryParam.setPageNumber(1);
        queryParam.setPageSize(1000); // 设置较大的分页数，确保获取所有数据

        // 调用租户客户端接口获取数据
        Pagination<ClientDTO> clientPage = tenantClient.getTenantClientPage(queryParam);

        if (clientPage == null || CollectionUtils.isEmpty(clientPage.getRecords())) {
            return Collections.emptyMap();
        }

        // 过滤出需要的客户端，并构建 id -> name 映射
        return clientPage.getRecords().stream()
                .filter(client -> clientIds.contains(client.getId().toString()))
                .collect(Collectors.toMap(
                        x -> x.getId().toString(),
                        ClientDTO::getName));
    }

    /**
     * 获取单个客户端名称
     *
     * @param clientId 客户端id
     *
     * @return 客户端名称，不存在返回null
     */
    public String getClientName(String clientId) {
        if (clientId == null) {
            return null;
        }
        Map<String, String> map = getClientNameMap(Collections.singleton(clientId));
        return map.get(clientId);
    }
}
