package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param;

import lombok.Getter;

import java.util.List;

@Getter
public class InspectItemBatchCommandModel {

    private String alias;
    private String referType;
    /**
     * 是否为结果巡检项
     */
    private Boolean isResultItem;
    List<InspectItemCommandModel> items;

}
