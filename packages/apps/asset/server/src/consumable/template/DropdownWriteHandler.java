package com.guhecloud.cube.srv.gen.property.management.server.consumable.template;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.util.List;

public class DropdownWriteHandler implements SheetWriteHandler {

    private final List<String> storeNames;
    private int storeColumnIndex;
    private final List<String> categoryNames;
    private int categoryColumnIndex;

    public DropdownWriteHandler(List<String> storeNames, int storeColumnIndex, List<String> categoryNames, int categoryColumnIndex) {
        this.storeNames = storeNames;
        this.categoryNames = categoryNames;
        this.storeColumnIndex = storeColumnIndex;
        this.categoryColumnIndex = categoryColumnIndex;
    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder,
                                 WriteSheetHolder writeSheetHolder
                                ) {

        Sheet sheet = writeSheetHolder.getSheet();
        Workbook workbook = writeWorkbookHolder.getWorkbook();

        DataValidationHelper helper = sheet.getDataValidationHelper();

        // 仓库下拉：假设你放在第 14 列（自己改）
        addDropdown(helper, sheet, storeNames, 1, 5000, storeColumnIndex, storeColumnIndex);

        // 分类下拉：假设在第 15 列
        addDropdown(helper, sheet, categoryNames, 1, 5000, categoryColumnIndex, categoryColumnIndex);
    }

    private void addDropdown(DataValidationHelper helper, Sheet sheet, List<String> list,
                             int firstRow, int lastRow, int firstCol, int lastCol
                            ) {

        String[] arr = list.toArray(new String[0]);
        DataValidationConstraint constraint = helper.createExplicitListConstraint(arr);

        CellRangeAddressList rangeList =
                new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);

        DataValidation validation = helper.createValidation(constraint, rangeList);

        validation.setShowErrorBox(true);
        sheet.addValidationData(validation);
    }
}