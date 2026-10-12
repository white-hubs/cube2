package com.guhecloud.cube.biz.ouaa.server.service;

import com.guhecloud.cube.biz.ouaa.server.param.AppointmentQueryParam;
import com.guhecloud.cube.biz.ouaa.server.param.OrganizationQueryParam;
import com.guhecloud.cube.biz.ouaa.server.param.PosistionQueryParam;
import com.guhecloud.cube.biz.ouaa.server.param.RoleQueryParam;
import com.guhecloud.cube.biz.ouaa.server.param.UserQueryParam;
import lombok.NonNull;

public interface EnterpriseExportService {
    /**
     * 导出用户
     */
    void exportStaff(@NonNull UserQueryParam param);

    /**
     * 部门导出
     */
    void exportOrganizations(@NonNull OrganizationQueryParam param);

    void exportPositions(@NonNull PosistionQueryParam param);

    void exportAppointment(@NonNull AppointmentQueryParam param);

    void exportRoles(@NonNull RoleQueryParam param);
}
