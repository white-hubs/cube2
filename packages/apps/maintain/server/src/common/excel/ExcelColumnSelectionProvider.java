package com.guhecloud.cube.srv.gen.maintain.server.common.excel;

import java.util.List;

/**
 * Excel列可选项提供者接口
 */
@FunctionalInterface
public interface ExcelColumnSelectionProvider {

    /**
     * 获取所有可选值
     * <p>
     * 对于非级联选择，返回的可选值对象包含value和name即可。
     * <p>
     * 对于级联选择，要返回全部的可选值对象，每个对象要包含value, name, refValue, refName。
     *
     * @return 所有可选值列表
     */
    List<ExcelColumnSelectionItem> selectionItems();

}
