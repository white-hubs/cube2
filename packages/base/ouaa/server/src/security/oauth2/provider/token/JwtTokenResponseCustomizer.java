package com.guhecloud.cube.biz.ouaa.server.security.oauth2.provider.token;

import com.google.common.collect.Lists;
import com.guhecloud.cube.biz.ouaa.server.entity.Client;
import com.guhecloud.cube.biz.ouaa.server.entity.User;
import com.guhecloud.cube.biz.ouaa.server.entity.UserTenant;
import com.guhecloud.cube.biz.ouaa.server.security.authentication.password.UserAuthenticationToken;
import com.guhecloud.cube.biz.ouaa.server.service.ClientService;
import com.guhecloud.cube.biz.ouaa.server.service.UserService;
import com.guhecloud.cube.biz.ouaa.server.service.UserTenantService;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.security.util.TokenUtils;
import jakarta.annotation.Resource;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.guhecloud.cube.biz.ouaa.metadata.constant.OAuth2TokenParameterNames.PARAM_TENANT_KEY;
import static com.guhecloud.cube.biz.ouaa.metadata.constant.OAuth2TokenParameterNames.PARAM_TERMINAL_KEY;
import static com.guhecloud.cube.biz.ouaa.metadata.constant.OAuth2TokenParameterNames.PARAM_USERNAME_KEY;

@Component
public class JwtTokenResponseCustomizer implements JwtTokenCustomizer {
    @Resource
    private UserService userService;

    @Resource
    private UserTenantService userTenantService;

    @Resource
    private ClientService clientService;

    private void handleClientOnlyMode(JwtEncodingContext context) {
        String clientId = context.getRegisteredClient().getClientId();
        Client client = clientService.getByCode(clientId);
        List<String> tenants = Lists.newArrayList(client.getTenant().split(","));
        context.getClaims().claim(TokenUtils.JWT_CLAIM_ID, client.getId());
        context.getClaims().claim(TokenUtils.JWT_CLAIM_CLIENT_ID, client.getId());
        context.getClaims().claim(TokenUtils.JWT_CLAIM_ADMIN, false);
        context.getClaims().claim(TokenUtils.JWT_CLAIM_TENANTS, tenants);
    }

    private void handleOtherMode(JwtEncodingContext context) {
        var authentication = (UserAuthenticationToken) context.getPrincipal();
        Map<String, Object> tokenParameters = authentication.getAdditionalParameters();
        if (tokenParameters == null || tokenParameters.isEmpty()) {
            return;
        }
        String username = tokenParameters.get(PARAM_USERNAME_KEY).toString();
        User user = this.userService.lookupUser(username);
        if (ObjectUtils.isEmpty(user)) {
            throw new IllegalArgumentException("User not found: " + username);
        }
        List<UserTenant> userTenants = userTenantService.listUserTenants(user.getId());
        Object tenant = tokenParameters.get(PARAM_TENANT_KEY);
        Object terminal = tokenParameters.get(PARAM_TERMINAL_KEY);
        List<String> tenants = ObjectUtils.isNotEmpty(tenant) ? Lists.newArrayList(tenant.toString()) : userTenants.stream()
                .map(userTenant -> String.valueOf(userTenant.getTenant())).collect(Collectors.toList());
        List<String> administratedTenants = userTenants.stream().filter(UserTenant::getAdmin)
                .map(userTenant -> String.valueOf(userTenant.getTenant())).collect(Collectors.toList());
        // TODO-migrate: 返回的敏感信息后续需要清理
        context.getClaims().claim(TokenUtils.JWT_CLAIM_ID, user.getId().toString());
        context.getClaims().claim(TokenUtils.JWT_CLAIM_USER_NAME, username);
        context.getClaims().claim(TokenUtils.JWT_CLAIM_NAME, user.getName());
        context.getClaims().claim(TokenUtils.JWT_CLAIM_ADMIN, user.getAdmin());
        context.getClaims().claim(TokenUtils.JWT_CLAIM_TENANTS, tenants);
        context.getClaims().claim(TokenUtils.JWT_CLAIM_ADMINISTRATED_TENANTS, administratedTenants);
        if (ObjectUtils.isNotEmpty(terminal)) {
            // 如果终端不为空，则将终端信息放入token中，有些场景前端无法确定终端，会在跳转的时候存储在后端，然后前端再从token中获取
            context.getClaims().claim(TokenUtils.JWT_CLAIM_TERMINAL, terminal);
        }
    }

    @Override
    public void customize(JwtEncodingContext context) {
        if (context.getAuthorizationGrantType() == AuthorizationGrantType.CLIENT_CREDENTIALS) {
            handleClientOnlyMode(context);
        } else {
            handleOtherMode(context);
        }
    }
}
