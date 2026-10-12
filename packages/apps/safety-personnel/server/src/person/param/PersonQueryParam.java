package com.guhecloud.cube.srv.gen.safety.personnel.server.person.param;

import com.guhecloud.platform.base.model.Paging;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 安全人员分页查询参数。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PersonQueryParam extends Paging {

    /**
     * 人员类型编码；ALL 表示全部（仅查询），其它见 {@link com.guhecloud.cube.srv.gen.safety.personnel.server.common.enumeration.PersonTypeCodeEnum}。
     */
    private String typeCode;

    /**
     * 关键字；按姓名、手机号、部门模糊查询，可为空。
     */
    @Size(max = 100, message = "关键字不能超过100个字符")
    private String keyword;

    /**
     * 人员状态，见 {@link com.guhecloud.cube.srv.gen.safety.personnel.server.common.enumeration.PersonStatusEnum}；可为空。
     */
    private String status;
}
