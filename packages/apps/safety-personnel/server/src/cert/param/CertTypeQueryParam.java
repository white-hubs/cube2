package com.guhecloud.cube.srv.gen.safety.personnel.server.cert.param;

import com.guhecloud.platform.base.model.Paging;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 证书类型分页查询参数。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CertTypeQueryParam extends Paging {

    /**
     * 证书分类ID；可为空。
     */
    private Long categoryId;

    /**
     * 关键字；按证书类型名称模糊查询，可为空。
     */
    @Size(max = 100, message = "关键字不能超过100个字符")
    private String keyword;
}
