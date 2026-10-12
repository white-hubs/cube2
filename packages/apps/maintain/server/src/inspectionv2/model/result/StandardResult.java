package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 巡检标准
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StandardResult {

    private Long id;

    private Long standardId;

    private String standardName;
    /**
     * 巡检对象类型
     */
    private String targetCategory;
    /**
     * 接收人
     */
    private String receiveUserNames;
    /**
     * 巡检项
     */
    private List<ItemResult> itemList;
}
