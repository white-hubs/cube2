package com.guhecloud.cube.base.alarm.server.flow.application.process.model.result;

import com.guhecloud.platform.base.util.StringUtils;

public record ProcessResult(boolean shouldContinue, String message) {

    public static ProcessResult continued() {
        return new ProcessResult(true, StringUtils.EMPTY_STRING);
    }

    public static ProcessResult stop(String message) {
        return new ProcessResult(false, message);
    }
}
