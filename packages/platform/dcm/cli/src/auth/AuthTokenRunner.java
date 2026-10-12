package com.guhecloud.platform.dcm.auth;

import com.guhecloud.platform.dcm.auth.dto.AuthTokenResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Timer;
import java.util.TimerTask;

@Slf4j
public class AuthTokenRunner {
    private static final long RETRY_FETCH_DELAY = 10 * 1000L;
    private final AuthTokenProperties authTokenProperties;

    private final OuaaTokenStorage tokenStorage;

    private final Timer timer;

    private final RestTemplate restTemplate;

    private volatile boolean firstFetchToken = true;

    public AuthTokenRunner(AuthTokenProperties authTokenProperties, OuaaTokenStorage tokenStorage) {
        this.authTokenProperties = authTokenProperties;
        this.tokenStorage = tokenStorage;
        this.timer = new Timer("authTokenRefreshTimer", true);
        this.restTemplate = new RestTemplate();
        log.info("AuthTokenRunner inited with {}", authTokenProperties);
        System.out.println("AuthTokenRunner inited with token url: " + authTokenProperties.getUrl());
    }

    class RefreshTimerTask extends TimerTask {

        RefreshTimerTask() {
        }

        @Override
        public void run() {
            fetchToken();
        }

        private void fetchToken() {
            HttpEntity<MultiValueMap<String, String>> requestEntity = buildAuthTokenRequestEntity();
            try {
                log.debug("AuthTokenRunner: fetch token with request: {}", requestEntity);
                ResponseEntity<AuthTokenResultDTO> result =
                        restTemplate.postForEntity(authTokenProperties.getUrl(), requestEntity, AuthTokenResultDTO.class);
                if (HttpStatus.OK.equals(result.getStatusCode())) {
                    tokenStorage.setToken(result.getBody().getAccessToken());
                    timer.schedule(new RefreshTimerTask(), authTokenProperties.getRefreshInterval() * 1000L);
                    firstFetchToken = false;
                    log.debug("AuthTokenRunner: token got as : {}", tokenStorage.getToken());
                    return;
                } else {
                    log.warn("Fetched token failed, StatusCode: {} ", result.getStatusCode());
                }
            } catch (RuntimeException e) {
                log.warn("Exception Raised in fetch token: {}", e.toString());
            } catch (Throwable t) {
                log.error("Unexpected behavior:", t);
            }
            timer.schedule(new RefreshTimerTask(), RETRY_FETCH_DELAY);
        }

        private HttpEntity<MultiValueMap<String, String>> buildAuthTokenRequestEntity() {
            MultiValueMap<String, String> bodyBuilder = new LinkedMultiValueMap<>();
            bodyBuilder.add("grant_type", "client_credentials");
            bodyBuilder.add("client_id", authTokenProperties.getClientId());
            bodyBuilder.add("client_secret", authTokenProperties.getClientSecret());
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add("tenant", authTokenProperties.getTenant().toString());
            headers.add("terminal", authTokenProperties.getTerminal());
            headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(bodyBuilder, headers);
            return requestEntity;
        }
    }

    public void start() {
        timer.schedule(new RefreshTimerTask(), 0);
    }
}
