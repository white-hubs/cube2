package com.guhecloud.cube.srv.gen.safety.personnel.server.committee.param;

import com.guhecloud.platform.base.model.Paging;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 安委会分页查询参数。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CommitteeQueryParam extends Paging {

    /**
     * 关键字；按安委会名称模糊查询，可为空。
     */
    @Size(max = 100, message = "关键字不能超过100个字符")
    private String keyword;
}
