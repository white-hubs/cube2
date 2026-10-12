package com.guhecloud.cube.srv.prepaid.server.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotNull;

/**
 * 抄表记录分页入参对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CollectRecordsPageParam extends Paging {

    /**
     * 电表编号
     */
    @NotNull(message = "电表编号不能为空!")
    private String meterId;
}
