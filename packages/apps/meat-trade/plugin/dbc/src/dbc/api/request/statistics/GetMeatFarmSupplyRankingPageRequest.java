package com.guhecloud.cube.sn.meattrade.plugin.dbc.api.request.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class GetMeatFarmSupplyRankingPageRequest extends AbstractDbcBiRequest {

    /**
     * 天数 查询近n天的数据和环比
     */
    private Integer day;

    /**
     * 页码
     */
    private Integer pageNum;

    /**
     * 页大小
     */
    private Integer pageSize;

    @Override
    public String getUrl() {
        return getUrl("/api/biData/getMeatFarmSupplyRanking").toString();
    }
}
