package com.guhecloud.cube.base.device.center.anotation;

import com.guhecloud.cube.base.device.center.core.excel.ExcelColumnSelectionItem;
import com.guhecloud.cube.base.device.center.core.excel.ExcelColumnSelectionProvider;

import java.util.ArrayList;
import java.util.List;

public class JobTypeSelectionProvider implements ExcelColumnSelectionProvider {
    @Override
    public List<ExcelColumnSelectionItem> selectionItems() {
        List<ExcelColumnSelectionItem> items = new ArrayList<>();
        items.add(new ExcelColumnSelectionItem("01", "管理人员"));
        items.add(new ExcelColumnSelectionItem("02", "现场工人"));
        items.add(new ExcelColumnSelectionItem("03", "后勤人员"));
        items.add(new ExcelColumnSelectionItem("99", "其他人员"));
        return items;
    }
}
