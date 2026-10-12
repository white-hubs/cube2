package com.guhecloud.cube.srv.gen.property.management.server.property.service.depository;

import instance.event.ProcessStartedMessage;

/**
 * @Author: lijie
 * @CreateTime: 2023-11-28  11:24
 * @Description:
 * @Version: 1.0
 */
public interface PropertyRepairReportService {
    Boolean insert(ProcessStartedMessage message, Long propertyId);

    String selectRepairReport(Long propertyId, String processInstanceId);

    Boolean updateRepairReport(Long propertyId, String propertyStatus);

}
