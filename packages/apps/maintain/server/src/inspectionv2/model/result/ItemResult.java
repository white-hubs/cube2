package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 巡检项返回对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemResult {

    private long itemId;

    private String itemName;

    private String itemContent;

    /**
     * 默认巡检项
     */
    private String defaultResult;

    /**
     * 巡检结果逗号分开
     */
    private String resultNames;

    /**
     * 适用对象类型名称
     */
    private String referTypeNames;
}
