package com.guhecloud.cube.srv.gen.property.management.server.consumable.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * @author Appet
 * @Description 耗材盘点明细
 * @date 2023-03-16 10:08:36
 */
@Data
public class ConsumableCheckDetailParam extends Paging {

    //盘点单ID
    @NotNull(message = "盘点单ID不能为空")
    private Long checkId;
    //耗材ID
    private Long consumableId;
    //盘点人ID
    private Long checkerId;
    //盘点人姓名
    private String checkerName;
    //关键字：耗材名称或编码
    private String keyword;
    //耗材类型
    private Long categoryId;
    /**
     * 盘点状态
     */
    private String status;

}