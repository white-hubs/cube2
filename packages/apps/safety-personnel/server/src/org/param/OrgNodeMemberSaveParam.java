package com.guhecloud.cube.srv.gen.safety.personnel.server.org.param;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 组织节点成员保存参数。
 */
@Data
public class OrgNodeMemberSaveParam {

    /**
     * 绑定后的OUAA用户ID列表；允许为空列表，表示清空当前节点成员。
     */
    @NotNull(message = "用户ID列表不能为空")
    private List<Long> userIds;
}
