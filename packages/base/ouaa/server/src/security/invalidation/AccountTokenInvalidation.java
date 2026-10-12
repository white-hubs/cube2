package com.guhecloud.cube.biz.ouaa.server.security.invalidation;

import com.guhecloud.cube.biz.ouaa.server.entity.Terminal;
import com.guhecloud.cube.biz.ouaa.server.service.TerminalService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class AccountTokenInvalidation implements TokenInvalidation {

    private final TokenInvalidation delegate;

    private final TerminalService terminalService;

    public AccountTokenInvalidation(@NonNull RedisTokenInvalidation redisTokenInvalidation, @NonNull TerminalService terminalService) {
        this.delegate = redisTokenInvalidation;
        this.terminalService = terminalService;
    }

    public void removeToken(String account) {
        List<Terminal> terminals = this.terminalService.list();
        terminals.forEach(terminal -> this.removeToken(account, terminal.getCode()));
    }

    @Override
    public void removeToken(String account, String terminal) {
        this.delegate.removeToken(account, terminal);
    }
}
