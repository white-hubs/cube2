package com.guhecloud.platform.dcm.auth;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OuaaTokenStorage {
    private String token;

    public synchronized void setToken(String token) {
        this.token = token;
        log.info("OuaaTokenStorage: token set  [{}]", token);
        this.notifyAll();
    }

    public String getToken() {
        return token;
    }

    public synchronized void clear() {
        this.token = null;
        notifyAll();
    }

    public synchronized String waitToken(long waitMillis) {
        if (token != null || waitMillis <= 0) {
            return token;
        } else {
            try {
                wait(waitMillis);
                return token;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
