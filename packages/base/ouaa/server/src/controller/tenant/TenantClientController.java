package com.guhecloud.cube.biz.ouaa.server.controller.tenant;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.guhecloud.cube.biz.ouaa.server.entity.Client;
import com.guhecloud.cube.biz.ouaa.server.param.ClientModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.ClientQueryParam;
import com.guhecloud.cube.biz.ouaa.server.param.TenantClientModifyParam;
import com.guhecloud.cube.biz.ouaa.server.service.ClientService;
import com.guhecloud.cube.biz.ouaa.server.vo.ClientVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TenantClientController {

    private final ClientService clientService;

    /**
     * 新增租户客户端配置
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/tenant-client")
    public String addTenantClient(@RequestBody @Valid TenantClientModifyParam param) {
        String clientId = IdWorker.get32UUID();
        ClientModifyParam clientModifyParam = ClientModifyParam.builder()
                .code(clientId)
                .name(param.getName())
                .password(IdWorker.get32UUID())
                .scopes(param.getScopes())
                .enabled(param.getEnabled())
                .tenants(Collections.singletonList(SessionContextHolder.getRequester().getTenant().toString()))
                .grantTypes(param.getGrantTypes())
                .accessTokenTimeout(param.getAccessTokenTimeout())
                .refreshTokenTimeout(param.getRefreshTokenTimeout())
                .build();
        this.clientService.save(clientModifyParam);
        return clientId;
    }

    /**
     * 新增租户客户端配置
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/inner/tenant-client")
    public ClientVO innerAddTenantClient(@RequestBody @Valid TenantClientModifyParam param) {
        String code = addTenantClient(param);
        Client client = this.clientService.getTenantClientByCode(SessionContextHolder.getRequester().getTenant(), code);
        return ObjectUtils.copying(client, ClientVO.class);
    }

    /**
     * 修改租户客户端配置
     *
     * @param code
     * @param param
     */
    @PutMapping("/tenant-client/{code}")
    public void updateTenantClient(@PathVariable("code") String code, @RequestBody @Valid TenantClientModifyParam param) {
        Client client = this.clientService.getTenantClientByCode(SessionContextHolder.getRequester().getTenant(), code);
        if (Objects.nonNull(client)) {
            ClientModifyParam clientModifyParam = ClientModifyParam.builder()
                    .code(code)
                    .name(param.getName())
                    .scopes(param.getScopes())
                    .enabled(param.getEnabled())
                    .tenants(Collections.singletonList(SessionContextHolder.getRequester().getTenant().toString()))
                    .grantTypes(param.getGrantTypes())
                    .accessTokenTimeout(param.getAccessTokenTimeout())
                    .refreshTokenTimeout(param.getRefreshTokenTimeout())
                    .build();
            this.clientService.update(clientModifyParam);
        }
    }

    /**
     * 修改客户端状态
     *
     * @param code
     * @param enabled
     */
    @PutMapping("/tenant-client/{code}/enable/{enabled}")
    public void updateTenantClientEnable(@PathVariable("code") String code, @PathVariable("enabled") Boolean enabled) {
        Client client = this.clientService.getTenantClientByCode(SessionContextHolder.getRequester().getTenant(), code);
        if (Objects.nonNull(client)) {
            this.clientService.updateEnable(code, enabled);
        }
    }

    /**
     * 获取租户客户端配置
     *
     * @return ClientVO
     */
    @GetMapping("/tenant-client/{code}")
    public ClientVO getTenantClient(@PathVariable String code) {
        Client client = this.clientService.getTenantClientByCode(SessionContextHolder.getRequester().getTenant(), code);
        return ObjectUtils.copying(client, ClientVO.class);
    }

    /**
     * 获取租户客户端配置列表
     *
     * @return
     */
    @GetMapping("/tenant-client/page")
    public Pagination<ClientVO> getTenantClientPage(@Valid ClientQueryParam query) {
        Page<Client> clientPage = this.clientService.getTenantClientPage(SessionContextHolder.getRequester().getTenant(), query);
        return PagingUtils.convert(clientPage, ClientVO.class);
    }

    /**
     * 租户客户端授权配置
     *
     * @param clientCode
     * @param scopeCode
     */
    @PostMapping("/tenant-client/{clientCode}/{scopeCode}")
    public void addClientScope(@PathVariable("clientCode") String clientCode, @PathVariable("scopeCode") String scopeCode) {
        Client client = this.clientService.getTenantClientByCode(SessionContextHolder.getRequester().getTenant(), clientCode);
        if (StringUtils.isNotEmpty(scopeCode)) {
            ArrayList<String> scopes = Lists.newArrayList(client.getScopes().split(","));
            scopes.add(scopeCode);
            client.setScopes(String.join(",", scopes));
        } else {
            client.setScopes(scopeCode);
        }
        this.clientService.updateById(client);
    }

    /**
     * 移除租户客户端授权配置
     *
     * @param clientCode
     * @param scopeCode
     */
    @DeleteMapping("/tenant-client/{clientCode}/{scopeCode}")
    public void removeClientScope(@PathVariable("clientCode") String clientCode, @PathVariable("scopeCode") String scopeCode) {
        Client client = this.clientService.getTenantClientByCode(SessionContextHolder.getRequester().getTenant(), clientCode);
        if (StringUtils.isNotEmpty(scopeCode)) {
            ArrayList<String> scopes = Lists.newArrayList(client.getScopes().split(","));
            scopes.remove(scopeCode);
            client.setScopes(String.join(",", scopes));
            this.clientService.updateById(client);
        }
    }

    /**
     * 移除租户客户端配置
     *
     * @param clientCode 客户端编号
     */
    @DeleteMapping("/tenant-client/{clientCode}")
    public void removeClient(@PathVariable("clientCode") String clientCode) {
        this.clientService.removeTenantClient(SessionContextHolder.getRequester().getTenant(), clientCode);
    }
}
