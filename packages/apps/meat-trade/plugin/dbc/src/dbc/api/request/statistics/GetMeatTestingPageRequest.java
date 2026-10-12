package com.guhecloud.cube.sn.meattrade.plugin.dbc.api.request.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class GetMeatTestingPageRequest extends AbstractDbcBiRequest {

    /**
     * 开始时间
     */
    private String startDate;

    /**
     * 结束时间
     */
    private String endDate;

    /**
     * 页码
     */
    private Integer pageNum;

    /**
     * 页大小
     */
    private Integer pageSize;

    /**
     * 检测项目名称
     */
    private String meatTestingItemName;

    /**
     * 检测项目编号
     */
    private String meatTestingItemCode;

    @Override
    public String getUrl() {
        return getUrl("/api/biData/getMeatTestingDetail").toString();
    }
}
