package com.guhecloud.cube.biz.ouaa.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.server.dao.ClientMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.Client;
import com.guhecloud.cube.biz.ouaa.server.param.ClientModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.ClientQueryParam;
import com.guhecloud.cube.biz.ouaa.server.service.ClientService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * 客户端业务操作实现
 *
 * @author Woody
 * @date 2021/7/26
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ClientServiceImpl extends ServiceImpl<ClientMapper, Client> implements ClientService {

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull ClientModifyParam param) {
        Client client = ObjectUtils.copying(param, Client.class);
        client.setSecretPlain(param.getPassword());
        client.setSecret(passwordEncoder.encode(param.getPassword()));
        if (!CollectionUtils.isEmpty(param.getTenants())) {
            client.setTenant(String.join(",", param.getTenants()));
        }
        this.baseMapper.insert(client);
        return client.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@NonNull ClientModifyParam param) {
        Client client = this.getByCode(param.getCode());
        client.setName(param.getName());
        client.setRemark(param.getRemark());
        client.setEnabled(param.getEnabled());
        if (!CollectionUtils.isEmpty(param.getTenants())) {
            client.setTenant(String.join(",", param.getTenants()));
        }
        client.setScopes(param.getScopes());
        client.setGrantTypes(param.getGrantTypes());
        client.setAccessTokenTimeout(param.getAccessTokenTimeout());
        client.setRefreshTokenTimeout(param.getRefreshTokenTimeout());
        this.baseMapper.updateById(client);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEnable(@NonNull String code, @NonNull Boolean enabled) {
        Client client = this.getByCode(code);
        client.setEnabled(enabled);
        this.baseMapper.updateById(client);
    }

    @Override
    public Client getTenantClientByCode(@NonNull Integer tenant, @NonNull String code) {
        return this.baseMapper.selectOne(Wrappers.<Client>lambdaQuery()
                                                 .eq(Client::getTenant, tenant)
                                                 .eq(Client::getCode, code)
                                        );
    }

    @Override
    public Page<Client> getTenantClientPage(@NonNull Integer tenant, ClientQueryParam query) {
        return this.baseMapper.selectPage(PagingUtils.convert(query),
                                          Wrappers.<Client>lambdaQuery()
                                                  .eq(Client::getTenant, tenant)
                                                  .eq(Objects.nonNull(query.getEnabled()), Client::getTenant, query.getTenant())
                                                  .like(StringUtils.isNotBlank(query.getKeyword()), Client::getName, query.getKeyword())
                                                  .orderByDesc(Client::getCreateTime)
                                         );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@NonNull Long id, @NonNull ClientModifyParam param) {
        Client client = this.getById(id);
        Assert.isNotFound(client, "客户端不存在");
        if (StringUtils.isNotEmpty(param.getPassword())) {
            client.setSecret(passwordEncoder.encode(param.getPassword()));
        }
        ObjectUtils.copying(param, client);
        if (!CollectionUtils.isEmpty(param.getTenants())) {
            client.setTenant(String.join(",", param.getTenants()));
        }
        this.baseMapper.updateById(client);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeTenantClient(@NonNull Integer tenant, @NonNull String code) {
        Client clientByCode = this.getTenantClientByCode(tenant, code);
        if (Objects.nonNull(clientByCode)) {
            DatabaseContextHolder.softDelete(this, clientByCode);
        }
    }

    @Override
    public Client getById(@NonNull Long id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public Client getByCode(@NonNull String code) {
        // 获取客户端信息并将信息存入当前会话上下文，避免多次查询
        return StringUtils.isEmpty(code) ? null : SessionContextHolder.getContextObject(
                "client:" + code, k -> this.lambdaQuery().eq(Client::getCode, code).one());
    }

    @Override
    public Page<Client> listByPage(ClientQueryParam param) {
        return this.lambdaQuery()
                .eq(Objects.nonNull(param.getTenant()), Client::getTenant, param.getTenant())
                .eq(Objects.nonNull(param.getEnabled()), Client::getEnabled, param.getEnabled())
                .and(StringUtils.isNotEmpty(param.getKeyword()), q -> q
                        .like(Client::getCode, param.getKeyword())
                        .or()
                        .like(Client::getName, param.getKeyword())
                    ).orderByDesc(Client::getCreateTime).page(PagingUtils.convert(param));
    }
}
