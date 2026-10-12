package com.guhecloud.cube.srv.gen.safety.personnel.server.cert.param;

import com.guhecloud.platform.base.model.Paging;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 人员证书分页查询参数。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EmployeeCertQueryParam extends Paging {

    /**
     * OUAA用户ID；可为空。
     */
    private Long userId;

    /**
     * 证书分类ID；可为空。
     */
    private Long categoryId;

    /**
     * 证书类型ID；可为空。
     */
    private Long typeId;

    /**
     * 关键字；按员工姓名、证书名称、证书编号模糊查询，可为空。
     */
    @Size(max = 100, message = "关键字不能超过100个字符")
    private String keyword;

    /**
     * 证书业务状态，见 {@link com.guhecloud.cube.srv.gen.safety.personnel.server.common.enumeration.CertStatusEnum}；可为空。
     */
    private String certStatus;

    /**
     * 复审状态；可为空。
     */
    private String reviewStatus;

    /**
     * 到期展示状态，见 {@link com.guhecloud.cube.srv.gen.safety.personnel.server.common.enumeration.CertExpireStatusEnum}；可为空。
     */
    private String expireStatus;
}
