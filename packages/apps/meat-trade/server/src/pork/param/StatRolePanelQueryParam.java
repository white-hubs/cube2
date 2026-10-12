package com.guhecloud.cube.biz.meattrade.server.pork.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * @author lzy
 * @date 2021/11/22
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StatRolePanelQueryParam extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;

    /**
     * 角色主键集合
     */
    private List<Long> roleIds;

    /**
     * 市场租户编号
     */
    private Integer tenant;

}
