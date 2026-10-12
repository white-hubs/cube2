package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 巡检记录分页列表导出数据
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InspectRecordPageExportData {
    /**
     * 总数
     */
    private Integer total;
    /**
     * 正常数
     */
    private Integer normal;
    /**
     * 异常数
     */
    private Integer abnormal;
    /**
     * 待修复数
     */
    private Integer waitRepair;
    /**
     * 已完成数
     */
    private Integer over;
    /**
     * 取消修复数
     */
    private Integer cancelRepair;
    private List<InspectRecordVO> inspectRecordList;

    public static InspectRecordPageExportData getEmptyData() {
        return InspectRecordPageExportData.builder().total(0).normal(0).abnormal(0).waitRepair(0).cancelRepair(0).over(0)
                .inspectRecordList(new ArrayList<>()).build();
    }
}
