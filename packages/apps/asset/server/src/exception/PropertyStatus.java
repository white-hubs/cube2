package com.guhecloud.cube.srv.gen.property.management.server.exception;

import com.guhecloud.platform.base.standard.exception.ErrorSpec;

public enum PropertyStatus implements ErrorSpec {
    PROPERTY_DATA("102001", "资产信息不完整,请检查数据"),
    PROPERTY_FLOW_CALLBACK("102002", "流程回调参数错误"),
    PROPERTY_FLOW_CALLBACK_BUSINESS("102003", "业务类型不存在"),
    CONSUMABLE_REDUCE_STOCK("102201", "出库数量不能大于库存"),
    CONSUMABLE_REDUCE_RESTRICT("102202", "出库数量不能大于限领数量"),
    CONSUMABLE_CHECK_RESTRICT("102203", "该仓库盘点中，无法出入库"),
    CONSUMABLE_NO_DETAILS("102204", "未查询到相关信息"),
    CONSUMABLE_CHECK_DONE("102205", "该耗材已被盘点"),
    CONSUMABLE_CHECK_TASK("102206", "当前仓库暂无耗材,无需盘点!"),
    CONSUMABLE_CHECK_NO("102207", "该仓库有未完成的盘点"),
    CONSUMABLE_ADD("102208", "耗材入库失败"),
    CONSUMABLE_LACK("102209", "耗材库存不足，无法出库！"),
    CONSUMABLE_RESTRICT_NO_DATA("102210", "数据错误，出库失败"),
    CONSUMABLE_CODE_CHECK("102211", "耗材编码重复，请修改编码");
    private final String code;
    private final String message;

    PropertyStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String errorCode() {
        return code;
    }

    @Override
    public String errorMessage() {
        return message;
    }

    @Override
    public void setErrorMessage(String message) {
    }
}
