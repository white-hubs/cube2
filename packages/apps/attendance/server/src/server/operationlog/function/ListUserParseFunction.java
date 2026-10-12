package com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.function;

import com.google.common.collect.Lists;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.mzt.logapi.service.IParseFunction;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wql
 * @describe 用户id列表转用户名
 * @date 2023/6/14 15:08
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ListUserParseFunction implements IParseFunction {
    private final UserClient userClient;

    @Override
    public boolean executeBefore() {
        return true;
    }

    @Override
    public String functionName() {
        return "LIST_USER";
    }

    @Override
    public String apply(Object value) {
        if (ObjectUtils.isEmpty(value)) {
            return "";
        }
        String userIds = (String) value;
        List<Long> collect = Lists.newArrayList(userIds.split(",")).stream().map(Long::valueOf).collect(Collectors.toList());
        List<UserDTO> userDTOS = userClient.listUserByIds(collect);
        if (ObjectUtils.isNotEmpty(userDTOS)) {
            List<String> names = userDTOS.stream().map(UserDTO::getName).collect(Collectors.toList());
            return String.join(",", names);
        }
        return "";
    }
}
