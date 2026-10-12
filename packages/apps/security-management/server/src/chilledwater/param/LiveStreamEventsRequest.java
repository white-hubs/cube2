package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Data
public class LiveStreamEventsRequest {
    @NotNull
    private Long deviceId;
    @NotEmpty
    private List<GetDeviceAttributeResponse> updates;
}
