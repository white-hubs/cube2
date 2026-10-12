package com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v6700.facade.event.handler;

import com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v6700.facade.event.LoginEvent;
import com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v6700.facade.request.LoginRequestAbstract;
import com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v6700.facade.response.LoginResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoginEventHandler extends AbstractSynchronizedHandler<LoginEvent, LoginRequestAbstract> {

    @EventListener
    public void login(LoginEvent loginEvent) {
        LoginResponse loginResponse = this.handle(loginEvent);
        loginEvent.accept(loginResponse.getJsessionid());
    }

    @Override
    public LoginRequestAbstract buildRequest(LoginEvent t) {
        return new LoginRequestAbstract(t.getUsername(), t.getPassword());
    }

    @Override
    public LoginResponse requesting(LoginRequestAbstract loginRequest) {
        ResponseEntity<LoginResponse> responseResponseEntity = requester().exchange(
                loginRequest.getUrl(),
                HttpMethod.POST,
                loginRequest.getHttpEntity(loginRequest),
                new ParameterizedTypeReference<LoginResponse>() {
                });
        return responseResponseEntity.getBody();
    }
}
