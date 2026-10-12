package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.interfaces.convert;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.WriteCellData;

public class BalanceExportConvert implements Converter<String> {
    @Override
    public Class<String> supportJavaTypeKey() {
        return String.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public String convertToJavaData(ReadConverterContext<?> context) throws Exception {
        return Converter.super.convertToJavaData(context);
    }

    @Override
    public WriteCellData<?> convertToExcelData(WriteConverterContext<String> context) throws Exception {
        if ("NO_LIMITED".equals(context.getValue())) {
            return new WriteCellData<>("不限制");
        }
        if ("NOT_CONFIGURED".equals(context.getValue())) {
            return new WriteCellData<>("-");
        }
        return new WriteCellData<>(handle(context.getValue()));
    }

    public String handle(String balance) {
        if (balance.contains(".")) {
            return balance.substring(0, balance.indexOf(".") + 2);
        }
        return balance;
    }
}
