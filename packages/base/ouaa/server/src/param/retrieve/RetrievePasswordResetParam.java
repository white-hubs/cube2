package com.guhecloud.cube.biz.ouaa.server.param.retrieve;

public record RetrievePasswordResetParam(String resetSessionId, String newPassword, String confirmPassword) {

}

