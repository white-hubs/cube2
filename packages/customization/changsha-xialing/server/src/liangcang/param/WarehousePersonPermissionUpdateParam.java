package com.guhecloud.cube.customization.xialing.server.liangcang.param;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 更新粮仓绑定人员
 * 请求体 JSON：整体替换某仓下的人员列表；personIds 可为空表示清空绑定。
 */
@Data
public class WarehousePersonPermissionUpdateParam {
    /** 智慧仓库粮仓 id。 */
    @NotNull
    private Long warehouseId;
    /** 绑定的人员 id 列表，不含重复；不传或空列表表示该仓下不再绑定任何人员。 */
    private List<Long> personIds = new ArrayList<>();
}
