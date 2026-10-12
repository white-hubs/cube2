package com.guhecloud.cube.srv.gen.safety.personnel.server.person.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 安全人员批量关联参数。
 */
@Data
public class PersonLinkParam {

    /**
     * OUAA用户ID列表；必填。
     */
    @NotEmpty(message = "用户ID列表不能为空")
    private List<Long> userIds;

    /**
     * 人员类型编码；必填，当前Tab类型。
     */
    @NotBlank(message = "人员类型不能为空")
    private String typeCode;
}
