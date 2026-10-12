package com.guhecloud.cube.customization.xialing.server.liangcang.param;

import lombok.Data;

/**
 * 调智慧仓 GET warehousePage 的入参，与三方「扁平 input」一致。
 */
@Data
public class LiangcangWarehousePageQueryParam {
    /** 嵌套查询条件，可空。 */
    private WarehousePageInput input;

    @Data
    public static class WarehousePageInput {
        /** 父级 id，默认可 0。 */
        private Integer parentId;
        /** 仓名等关键字。 */
        private String search;
        /** 每页条数。 */
        private Integer pageSize;
        /** 页码。 */
        private Integer pageNum;
    }
}
