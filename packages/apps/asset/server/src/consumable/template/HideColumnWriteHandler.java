package com.guhecloud.cube.srv.gen.property.management.server.consumable.template;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import org.apache.poi.ss.usermodel.Sheet;

public class HideColumnWriteHandler implements SheetWriteHandler {

    private final int[] hiddenColumns;

    public HideColumnWriteHandler(int... hiddenColumns) {
        this.hiddenColumns = hiddenColumns;
    }

    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {}

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        Sheet sheet = writeSheetHolder.getSheet();
        for (int col : hiddenColumns) {
            sheet.setColumnHidden(col, true);
        }
    }
}