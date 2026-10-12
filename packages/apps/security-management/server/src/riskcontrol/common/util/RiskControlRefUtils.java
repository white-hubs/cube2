package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.util;

import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskDepartmentRefVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskUserRefVO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class RiskControlRefUtils {
    private RiskControlRefUtils() {
    }

    public static void addId(Set<String> ids, String value) {
        if (!isBlank(value)) {
            ids.add(normalize(value));
        }
    }

    public static void addIds(Set<String> ids, Collection<String> values) {
        if (values == null) {
            return;
        }
        values.forEach(value -> addId(ids, value));
    }

    public static RiskUserRefVO toUser(String userId, Map<String, String> userNames) {
        if (isBlank(userId)) {
            return null;
        }
        String normalizedUserId = normalize(userId);
        RiskUserRefVO user = new RiskUserRefVO();
        user.setUserId(normalizedUserId);
        user.setUserName(userNames.get(normalizedUserId));
        return user;
    }

    public static List<RiskUserRefVO> toUsers(List<String> userIds, Map<String, String> userNames) {
        if (userIds == null) {
            return null;
        }
        return userIds.stream()
                .filter(userId -> !isBlank(userId))
                .map(userId -> toUser(userId, userNames))
                .toList();
    }

    public static RiskDepartmentRefVO toDepartment(String departmentId, Map<String, String> departmentNames) {
        if (isBlank(departmentId)) {
            return null;
        }
        String normalizedDepartmentId = normalize(departmentId);
        RiskDepartmentRefVO department = new RiskDepartmentRefVO();
        department.setDepartmentId(normalizedDepartmentId);
        department.setDepartmentName(departmentNames.get(normalizedDepartmentId));
        return department;
    }

    private static String normalize(String value) {
        return value.trim();
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
