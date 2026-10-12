package com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.function;

import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.AttConstants;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.mzt.logapi.service.IParseFunction;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 班次建议应用范围转换
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ClassesDeptIdsParseFunction implements IParseFunction {
    private final OrganizationClient organizationClient;

    @Override
    public boolean executeBefore() {
        return true;
    }

    @Override
    public String functionName() {
        return "CLASS_DEPT_IDS";
    }

    @Override
    public String apply(Object value) {
        if (ObjectUtils.isEmpty(value)) {
            return "";
        }
        String deptStr = String.valueOf(value);
        if (deptStr.length() <= AttConstants.oneInt) {
            try {
                long deptId = Long.parseLong(deptStr);
                if (deptId <= AttConstants.zeroInt) {
                    return "";
                }
                OrganizationDTO organizationDTO = organizationClient.getOrganizationsById(deptId);
                if (organizationDTO == null) {
                    return "";
                }
                return organizationDTO.getName();
            } catch (Exception ignored) {
            }
        }
        String[] deptIds = deptStr.split(",");
        List<Long> deptIdList = Arrays.stream(deptIds).map(Long::parseLong).collect(Collectors.toList());
        if (deptIdList.isEmpty()) {
            return "";
        }
        List<OrganizationDTO> dtoList = organizationClient.getOrganizationsByIds(deptIdList);
        if (dtoList == null || dtoList.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (OrganizationDTO organizationDTO : dtoList) {
            sb.append(organizationDTO.getName()).append(",");
        }
        if (sb.length() > AttConstants.oneInt) {
            return sb.substring(AttConstants.zeroInt, sb.length() - AttConstants.oneInt);
        }
        return "";
    }
}
