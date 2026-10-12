package com.guhecloud.cube.srv.gen.property.management.server.consumable.filter;

import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.database.annotation.DataPermissionField;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: lijie
 * @Date: 2024/8/27 15:45
 * @Description:
 */
@Component
public class ConsumableCustomDeptFieldFilter implements DataPermissionField.FieldParamConvert {

    @Override
    public Object getFieldParamConvert(Object o) {
        if (Objects.isNull(o)) {
            return null;
        }
        //自定义部门过滤
        Set<Long> customDeptIds = new HashSet<>();
        if (o instanceof List) {
            ((List) o).stream().forEach(t -> {
                customDeptIds.add(Long.valueOf(t.toString()));
            });
        } else {
            customDeptIds.add(Long.valueOf(o.toString()));
        }
        if (!CollectionUtils.isEmpty(customDeptIds)) {
            UserClient userClient = ApplicationContextHolder.getBean(UserClient.class);
            List<Long> userIdList = userClient.listEnableUserByOrgAndPost(new ArrayList<>(customDeptIds), new ArrayList<>());
            if (CollectionUtils.isEmpty(userIdList)) {
                return null;
            }
            List<UserDTO> userDTOS = userClient.listUserByIds(userIdList);
            return userDTOS.stream().map(UserDTO::getMobile).collect(Collectors.toSet());
        }
        return null;
    }
}
