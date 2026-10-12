package com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.facade.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IscVideoSnapCaptureRequest extends BasicRequest {

    private String cameraIndexCode;

    @Override
    public Map<String, String> getUrl() {
        return getUrl("/api/video/v1/manualCapture").build();
    }
}
