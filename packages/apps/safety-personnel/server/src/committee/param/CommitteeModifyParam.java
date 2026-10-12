package com.guhecloud.cube.srv.gen.safety.personnel.server.committee.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 安委会新增或编辑参数。
 */
@Data
public class CommitteeModifyParam {

    /**
     * 安委会名称；必填，租户内唯一。
     */
    @NotBlank(message = "安委会名称不能为空")
    @Size(max = 64, message = "安委会名称不能超过64个字符")
    private String committeeName;

    /**
     * 负责单位/部门ID，来源OUAA组织选择器；必填。
     */
    @jakarta.validation.constraints.NotNull(message = "负责单位/部门不能为空")
    private Long deptId;

    /**
     * 联系电话；可为空。
     */
    @Size(max = 32, message = "联系电话不能超过32个字符")
    private String contactPhone;

    /**
     * 简介；可为空。
     */
    @Size(max = 500, message = "简介不能超过500个字符")
    private String introduction;

    /**
     * 成员OUAA用户ID列表；新增或编辑时必填，至少1人。
     */
    @NotEmpty(message = "成员不能为空")
    private List<Long> userIds;

    /**
     * 附件文件ID列表；为空表示无附件，新增或编辑时整体保存。
     */
    private List<Long> fileIds;
}
