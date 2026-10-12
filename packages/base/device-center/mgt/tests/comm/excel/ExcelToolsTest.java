package com.guhecloud.cube.iot.mgt.comm.excel;

import com.guhecloud.cube.iot.mgt.device.param.DeviceCreateParamNew;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ExcelToolsTest {

    @Test
    public void importSheetSkipsBlankRowsCreatedByTemplateFormatting() throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("设备导入模板");
        sheet.createRow(0);
        writeDevice(sheet.createRow(1), "一号楼监控", "1F-JK01");
        Row blankTemplateRow = sheet.createRow(2);
        blankTemplateRow.createCell(0).setCellValue("");
        blankTemplateRow.createCell(1).setCellValue("");
        writeDevice(sheet.createRow(3), "二号楼监控", "2F-JK01");
        BlockingDeque<DeviceCreateParamNew> queue = new LinkedBlockingDeque<>();

        List<String> errors = ExcelTools.importSheet(workbook, sheet, DeviceCreateParamNew.class, queue,
                new AtomicBoolean(false), null, null);

        assertTrue(errors.isEmpty());
        assertEquals(2, queue.size());
    }

    private static void writeDevice(Row row, String deviceName, String deviceNumber) {
        row.createCell(0).setCellValue(deviceName);
        row.createCell(1).setCellValue(deviceNumber);
    }
}
