package com.guhecloud.cube.srv.gen.safety.personnel.server.org.util;

import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * OUAA用户展示字段转换工具。
 */
public final class OuaaUserUtils {

    private OuaaUserUtils() {
    }

    /**
     * 提取OUAA用户部门名称。
     *
     * @param user OUAA用户信息
     *
     * @return 部门名称；无部门时为空。
     */
    public static String resolveDeptName(UserDTO user) {
        if (user.getOrganization() != null) {
            return user.getOrganization().getName();
        }
        if (user.getOrganizations() == null || user.getOrganizations().isEmpty()) {
            return null;
        }
        return user.getOrganizations().stream()
                .map(UserDTO.Organization::getName)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(","));
    }

    /**
     * 提取 OUAA 用户岗位名称（优先 {@link UserDTO#getAppointmentList()}，回退 {@link UserDTO#getAppointments()}，非 positions 职位）。
     *
     * @param user OUAA用户信息
     *
     * @return 岗位名称；无岗位时为空。
     */
    public static String resolvePostName(UserDTO user) {
        List<UserDTO.Appointment> appointments = user.getAppointmentList();
        if (appointments == null || appointments.isEmpty()) {
            appointments = user.getAppointments();
        }
        if (appointments == null || appointments.isEmpty()) {
            return null;
        }
        return appointments.stream()
                .map(UserDTO.Appointment::getName)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(","));
    }
}
