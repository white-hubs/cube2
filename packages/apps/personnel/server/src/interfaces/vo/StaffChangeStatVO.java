package com.guhecloud.cube.srv.gen.personnel.management.server.interfaces.vo;

import com.guhecloud.cube.srv.gen.personnel.management.server.tool.excel.annotation.ExcelField;
import lombok.Data;

/**
 * @author bq
 * @date 2023/02/10
 * @description 人员异动统计
 */
@Data
public class StaffChangeStatVO {

    /**
     * 部门名称
     */
    @ExcelField(value = "部门")
    private String organizationName;

    /**
     * 招聘人数
     */
    @ExcelField(value = "招聘")
    private int recruitNum;

    /**
     * 入职人数
     */
    @ExcelField(value = "入职")
    private long inductionNum;

    /**
     * 转正人数
     */
    @ExcelField(value = "转正")
    private long regularWorkerNum;

    /**
     * 离职人数
     */
    @ExcelField(value = "离职")
    private long quitNum;

    /**
     * 当前人数
     */
    @ExcelField(value = "当前人数")
    private long currentNum;

}