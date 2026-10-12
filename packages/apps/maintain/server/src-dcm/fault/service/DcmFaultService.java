package com.guhecloud.cube.srv.gen.maintain.dcm.fault.service;

import com.guhecloud.platform.dcm.entity.RequestedConfigItem;

import java.util.List;

public interface DcmFaultService {
    List<RequestedConfigItem> fetchRuntimeFaultTypeConfig(Long aLong);

    List<RequestedConfigItem> applyFaultTypeConfig(List<RequestedConfigItem> requestedConfigItems, Long aLong);
}
