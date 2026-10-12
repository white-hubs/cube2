package com.guhecloud.cube.biz.collection.analysis.gm.model.dto;

import com.guhecloud.cube.biz.collection.analysis.gm.model.result.KeyValueResult;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MerchantStatDTO {
    /**
     * 数量
     */
    private Integer total;
    /**
     * 性别比例集合
     */
    private List<KeyValueResult> sexList;
    /**
     * 年龄比例集合
     */
    private List<KeyValueResult> ageList;
    /**
     * 柱状图一(批发商：上游来源，采购商：籍贯)
     */
    private List<KeyValueResult> barChartOneList;
    /**
     * 柱状图二(批发商：上游来源，采购商：籍贯)
     */
    private List<KeyValueResult> barChartTwoList;
    /**
     * 饼图(批发商：经营规模，采购商：采购车类型)
     */
    private List<KeyValueResult> pieChartList;

    public MerchantStatDTO empty() {
        this.total = 0;
        this.sexList = new ArrayList<>();
        this.barChartOneList = new ArrayList<>();
        this.barChartTwoList = new ArrayList<>();
        this.pieChartList = new ArrayList<>();
        return this;
    }
}
