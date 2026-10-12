package com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.function;

import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.mzt.logapi.service.IParseFunction;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wql
 * @describe 用户名称转换
 * @date 2023/6/13 16:46
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserNameParseFunction implements IParseFunction {
    private final UserClient userClient;

    @Override
    public boolean executeBefore() {
        return true;
    }

    @Override
    public String functionName() {
        return "USERNAME";
    }

    @Override
    public String apply(Object value) {
        if (ObjectUtils.isEmpty(value)) {
            return "";
        }
        UserDTO user = userClient.getUser((Long) value);
        return ObjectUtils.isEmpty(user) ? "" : user.getName();
    }
}
