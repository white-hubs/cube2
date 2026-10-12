package com.guhecloud.cube.srv.gen.adapter.access.control.server.service;

import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.jieshun.api.dto.request.DoorEventUploadRequest;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.jieshun.api.dto.request.DoorStatusUploadRequest;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.jieshun.api.dto.response.JieShunResponse;

public interface JieShunAccessService extends AccessService{
    JieShunResponse<Void> uploadDoorEvent(DoorEventUploadRequest request);

    JieShunResponse<Void> uploadDoorStatus(DoorStatusUploadRequest request);
}
