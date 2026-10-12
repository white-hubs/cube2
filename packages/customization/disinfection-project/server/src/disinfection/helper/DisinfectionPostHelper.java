package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.helper;

import com.guhecloud.cube.biz.ouaa.client.AppointmentClient;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.AppointmentDetailDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.queryrier.UserQuery;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.MapUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class DisinfectionPostHelper {
    private final AppointmentClient appointmentClient;
    private final UserClient userClient;

    public Map<Long, String> getPostNameMap(List<Long> postIds) {
        if (ListUtils.isEmpty(postIds)) {
            return Collections.emptyMap();
        }
        try {
            List<AppointmentDetailDTO> appointments = appointmentClient.getAppointmentByIds(postIds.stream().map(String::valueOf)
                                                                                                    .collect(Collectors.joining(",")));
            return MapUtils.listToMap(appointments, AppointmentDetailDTO::getId, AppointmentDetailDTO::getName);

        } catch (Exception e) {
            log.error("查询岗位信息异常", e);
            return Collections.emptyMap();
        }
    }

    public String getPostName(Long postId) {
        return getPostNameMap(Collections.singletonList(postId)).getOrDefault(postId, "");
    }

    public Map<Long, Boolean> isExistUsersByPostIds(List<Long> postIds) {
        if (ListUtils.isEmpty(postIds)) {
            return Collections.emptyMap();
        }
        UserQuery userQuery = UserQuery.builder().positionIds(new HashSet<>(postIds)).build();
        List<UserDTO> userDTOS = userClient.postList(userQuery);
        Map<Long, Boolean> result = postIds.stream().collect(Collectors.toMap(postId -> postId, postId -> false));
        if (ListUtils.nonEmpty(userDTOS)) {
            userDTOS.forEach(userDTO -> {
                List<UserDTO.Appointment> appointments = userDTO.getAppointments();
                if (ListUtils.nonEmpty(appointments)) {
                    appointments.forEach(appointment -> {
                        Long id = appointment.getId();
                        result.put(id, true);
                    });
                }
            });
        }
        return result;
    }
}
