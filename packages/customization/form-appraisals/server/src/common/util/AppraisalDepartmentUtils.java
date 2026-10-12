package com.guhecloud.cube.srv.gen.appraisals.server.common.util;

import com.guhecloud.platform.base.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

/**
 * 责任部门多选字段转换工具。
 */
public final class AppraisalDepartmentUtils {
    private static final String SEPARATOR = ",";

    private AppraisalDepartmentUtils() {
    }

    public static String joinDepartmentIds(List<Long> departmentIds) {
        if (departmentIds == null || departmentIds.isEmpty()) {
            return "";
        }
        LinkedHashSet<String> uniqueIds = new LinkedHashSet<>();
        for (Long departmentId : departmentIds) {
            if (departmentId != null) {
                uniqueIds.add(String.valueOf(departmentId));
            }
        }
        return String.join(SEPARATOR, uniqueIds);
    }

    public static List<Long> parseDepartmentIds(String departmentIds) {
        if (StringUtils.isBlank(departmentIds)) {
            return List.of();
        }
        LinkedHashSet<Long> ids = new LinkedHashSet<>();
        for (String part : departmentIds.split(SEPARATOR)) {
            if (StringUtils.isNotBlank(part)) {
                ids.add(Long.parseLong(part.trim()));
            }
        }
        return new ArrayList<>(ids);
    }

    public static String joinDepartmentNames(List<String> departmentNames) {
        if (departmentNames == null || departmentNames.isEmpty()) {
            return "";
        }
        LinkedHashSet<String> uniqueNames = new LinkedHashSet<>();
        for (String departmentName : departmentNames) {
            if (StringUtils.isNotBlank(departmentName)) {
                uniqueNames.add(departmentName.trim());
            }
        }
        return String.join(SEPARATOR, uniqueNames);
    }

    public static List<String> parseDepartmentNames(String departmentNames) {
        if (StringUtils.isBlank(departmentNames)) {
            return List.of();
        }
        LinkedHashSet<String> names = new LinkedHashSet<>();
        for (String part : departmentNames.split(SEPARATOR)) {
            if (StringUtils.isNotBlank(part)) {
                names.add(part.trim());
            }
        }
        return names.stream().filter(Objects::nonNull).toList();
    }
}
