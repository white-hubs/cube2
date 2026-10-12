package com.guhecloud.cube.iot.mgt.comm.excel;


import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class ExcelWriter<T> {

    public void writeData(List<T> data, Map<String, ExcelColumnSelectionProvider> map,
                          Class<? extends T> modelClasses, OutputStream outputStream,
                          boolean autoCloseStream) throws IOException {
        ExcelTools.<T>exportSheet(data, map, modelClasses, outputStream, autoCloseStream);
    }

    public byte[] writeHeaderToOutputStream(Map<String, ExcelColumnSelectionProvider> map,
                                            Class<? extends T> modelClasses,
                                            List<T> data) {
        return ExcelTools.exportSheet(data, map, modelClasses);
    }
}
