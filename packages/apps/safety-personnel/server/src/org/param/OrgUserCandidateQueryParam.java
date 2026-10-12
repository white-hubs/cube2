package com.guhecloud.cube.srv.gen.safety.personnel.server.org.param;

import com.guhecloud.platform.base.model.Paging;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 组织配置选人弹窗查询参数。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OrgUserCandidateQueryParam extends Paging {

    /**
     * 用户关键字；按OUAA用户姓名、账号或手机号等既有口径查询，可为空。
     */
    @Size(max = 100, message = "关键字不能超过100个字符")
    private String keyword;

    /**
     * OUAA组织机构ID；可为空，为空时不按部门过滤。
     */
    @Min(value = 1, message = "组织机构ID不能小于1")
    private Long organizationId;

    /**
     * 是否只查询启用用户；可为空，为空时按OUAA默认口径查询。
     */
    private Boolean enabled;
}
