package com.guhecloud.cube.srv.gen.maintain.server.common.excel;

import lombok.Data;

import java.io.Serializable;

/**
 * Excel模型某个列的可选项值
 */
@SuppressWarnings("serial")
@Data
public class ExcelColumnSelectionItem implements Serializable {

    /**
     * 实际值
     */
    private String value;
    /**
     * 显示名称
     */
    private String name;

    /**
     * 联动上级实际值，用于级联选择的场景
     */
    private String refValue;
    /**
     * 联动上级显示名称，用于级联选择的场景
     */
    private String refName;

    public ExcelColumnSelectionItem() {
    }

    public ExcelColumnSelectionItem(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public ExcelColumnSelectionItem(String value, String name, String refValue, String refName) {
        this.value = value;
        this.name = name;
        this.refValue = refValue;
        this.refName = refName;
    }

}
