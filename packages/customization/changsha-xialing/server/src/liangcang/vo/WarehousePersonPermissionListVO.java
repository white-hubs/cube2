package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 某粮仓已绑定的人员 id 列表。
 */
@Data
public class WarehousePersonPermissionListVO {
    private List<Long> personIds = new ArrayList<>();
}
