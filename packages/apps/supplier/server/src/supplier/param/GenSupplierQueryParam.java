package com.guhecloud.cube.srv.gen.supplier.server.supplier.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author lzy
 * @date 2021/12/3
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GenSupplierQueryParam extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;

    /**
     * 批发商主键集合
     */
    private Collection<? extends Serializable> ids;

    /**
     * 用户主键集合
     */
    private Collection<? extends Serializable> userIds;

    /**
     * 市场租户编号
     */
    private Integer tenant;
}
