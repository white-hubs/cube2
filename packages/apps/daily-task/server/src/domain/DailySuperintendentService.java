package com.guhecloud.cube.biz.daily.task.domain;

import com.guhecloud.cube.biz.daily.task.commons.dto.DailySuperintendentDTO;
import com.guhecloud.cube.biz.daily.task.domain.po.base.DailyBaseSuperintendent;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DailySuperintendentService {

    <T extends DailyBaseSuperintendent> List<DailySuperintendentDTO> handlerSuperintendent(List<T> poList);

    Map<String, UserDTO> getUserNameByAccounts(Set<String> accountList);

    List<UserDTO> filterAttendanceUser(List<UserDTO> userDTOS);
}
