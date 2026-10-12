package com.guhecloud.cube.sn.meattrade.plugin.dbc.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class GetPendingTalliesRequest extends AbstractDbcRequest {

    /**
     * 批次号
     */
    private String tallyId;

    @Override
    public String getUrl() {
        return getUrl("/api/v2/getPendingTallies").append("?tallyId={tallyId}").toString();
    }
}
