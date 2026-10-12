package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj;

import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * 考试任务负责人信息
 *
 * @author lanchun
 * @date 2023/4/19 15:23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamTaskChargeUserInfo {

    /**
     * 负责人主键
     */
    private Long id;

    /**
     * 负责人名称
     */
    private String name;

    public String getName() {
        if (Objects.nonNull(id)) {
            UserClient userClient = ApplicationContextHolder.getBean(UserClient.class);
            UserDTO user = userClient.getUser(id);
            if (Objects.nonNull(user)) {
                return user.getName();
            }
        }
        return null;
    }
}
