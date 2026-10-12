package com.guhecloud.cube.srv.gen.maintain.server.common.excel;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class ExcelWriter<T> {

    public void writeData(List<T> data, Map<String, ExcelColumnSelectionProvider> map,
                          Class<? extends T> modelClasses, OutputStream outputStream,
                          boolean autoCloseStream,
                          Integer emptyMaxSize
                         ) throws IOException {
        ExcelTools.<T>exportSheet(data, map, modelClasses, outputStream, autoCloseStream, emptyMaxSize);
    }

    public byte[] writeHeaderToOutputStream(Map<String, ExcelColumnSelectionProvider> map,
                                            Class<? extends T> modelClasses,
                                            List<T> data
                                           ) {
        return ExcelTools.exportSheet(data, map, modelClasses);
    }
}
