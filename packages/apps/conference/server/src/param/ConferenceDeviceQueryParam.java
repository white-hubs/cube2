package com.guhecloud.cube.srv.gen.conferencemanage.server.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

@Data
public class ConferenceDeviceQueryParam extends Paging {
    private String name;
    private String deviceModel;
}
