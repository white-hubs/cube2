package com.guhecloud.cube.biz.ouaa.server.security.oauth2.provider;

import com.guhecloud.cube.biz.ouaa.server.entity.Client;
import com.guhecloud.cube.biz.ouaa.server.service.ClientService;
import com.guhecloud.platform.base.util.ObjectUtils;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.Duration;
import java.util.Arrays;

/**
 * 客户端详情业务实现
 *
 * @author Woody
 * @date 2021/6/29
 * @see com.guhecloud.cube.biz.ouaa.server.config.AuthorizationServerConfigurer
 */
public class RegisteredClientRepositoryImpl implements RegisteredClientRepository {
    private final ClientService clientService;

    public RegisteredClientRepositoryImpl(ClientService clientService) {this.clientService = clientService;}

    @Override
    public void save(RegisteredClient registeredClient) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RegisteredClient findById(String id) {
        var client = this.clientService.getById(Long.valueOf(id));
        if (client == null || !Boolean.TRUE.equals(client.getEnabled())) {
            throw new RuntimeException("Invalid client");
        }
        return buildRegisteredClient(client);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        var client = this.clientService.getByCode(clientId);
        if (client == null || !Boolean.TRUE.equals(client.getEnabled())) {
            throw new RuntimeException("Invalid client");
        }
        return buildRegisteredClient(client);
    }

    private RegisteredClient buildRegisteredClient(Client client) {
        var tokenSettingBuilder = TokenSettings.builder()
                .accessTokenTimeToLive(Duration.ofSeconds(client.getAccessTokenTimeout()));
        if (ObjectUtils.isNotNull(client.getRefreshTokenTimeout())) {
            tokenSettingBuilder
                    .refreshTokenTimeToLive(Duration.ofSeconds(client.getRefreshTokenTimeout()));
        }
        return RegisteredClient.withId(client.getCode())
                .clientId(client.getCode())
                .scopes(scopes -> Arrays.stream(client.getScopes().split(",")).forEach(
                        scope -> scopes.add(scope.trim())))
                .authorizationGrantTypes(types -> Arrays.stream(client.getGrantTypes().split(",")).forEach(
                        type -> types.add(new AuthorizationGrantType(type.trim()))))
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .clientSecret(client.getSecret())
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .tokenSettings(tokenSettingBuilder.build())
                .build();
    }
}
