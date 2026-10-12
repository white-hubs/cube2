package com.guhecloud.cube.srv.gen.maintain.server.fault.enumeration;

import com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo.ReportEnumVo;

import java.util.ArrayList;
import java.util.List;

public enum ReportEnum {
    REPORT_DAY("REPORT_DAY", "日报"),
    REPORT_WEEK("REPORT_WEEK", "周报"),
    REPORT_MONTH("REPORT_MONTH", "月报"),
    ;

    private String code;
    private String name;

    ReportEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static String getNameByCode(String code) {
        for (ReportEnum value : ReportEnum.values()) {
            if (code.equals(value.code)) {
                return value.name;
            }
        }
        return null;
    }

    public static List<ReportEnumVo> getEnumTypeVo() {
        ArrayList<ReportEnumVo> reportEnumVos = new ArrayList<>();
        for (ReportEnum reportEnum : ReportEnum.values()) {
            reportEnumVos.add(new ReportEnumVo(reportEnum.getCode(), reportEnum.getName()));
        }
        return reportEnumVos;
    }
}
