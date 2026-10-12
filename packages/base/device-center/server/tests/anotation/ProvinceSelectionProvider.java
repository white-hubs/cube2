package com.guhecloud.cube.base.device.center.anotation;

import com.guhecloud.cube.base.device.center.core.excel.ExcelColumnSelectionItem;
import com.guhecloud.cube.base.device.center.core.excel.ExcelColumnSelectionProvider;

import java.util.ArrayList;
import java.util.List;

public class ProvinceSelectionProvider implements ExcelColumnSelectionProvider {
    @Override
    public List<ExcelColumnSelectionItem> selectionItems() {
        List<ExcelColumnSelectionItem> items = new ArrayList<>();
        items.add(new ExcelColumnSelectionItem("110000", "北京市"));
        items.add(new ExcelColumnSelectionItem("120000", "天津市"));
        items.add(new ExcelColumnSelectionItem("130000", "河北省"));
        items.add(new ExcelColumnSelectionItem("140000", "山西省"));
        return items;
    }
}
